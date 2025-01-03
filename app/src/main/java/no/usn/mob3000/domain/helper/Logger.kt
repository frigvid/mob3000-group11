package no.usn.mob3000.domain.helper

import android.util.Log
import no.usn.mob3000.BuildConfig

/**
 * Wrapper for logging utility that provides some extra functionality in the form of tag inference.
 *
 * Additionally, `Logger` ensures that any logs are only printed to the output so long as the
 * `BuildConfig.DEBUG` is set to `true`. This means actually building the final APK will have
 * logging disabled, and thus should not impede regular users.
 *
 * TODO@frigvid: Create UnitTestLogger class that extends Logger, and overrides Logger with
                 Java's System.out.println().
 *
 * @author frigvid
 * @created 2024-11-01
 */
object Logger {
    fun d(
        message: String,
        tagType: TagType = TagType.DEFAULT,
        tag: String = inferTag(tagType)
    ) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun e(
        message: String,
        throwable: Throwable? = null,
        tagType: TagType = TagType.DEFAULT,
        tag: String = inferTag(tagType)
    ) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, throwable)
        }
    }

    fun i(
        message: String,
        tagType: TagType = TagType.DEFAULT,
        tag: String = inferTag(tagType)
    ) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    fun v(
        message: String,
        tagType: TagType = TagType.DEFAULT,
        tag: String = inferTag(tagType)
    ) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message)
        }
    }

    fun w(
        message: String,
        tagType: TagType = TagType.DEFAULT,
        tag: String = inferTag(tagType)
    ) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message)
        }
    }

    private fun inferTag(
        tagType: TagType
    ): String {
        val trace = Throwable().stackTrace
        val element = trace.getOrNull(2)

        if (element != null) {
            val className = element.className.substringAfterLast(".")
            val methodName = element.methodName
            val fileName = element.fileName?.substringBeforeLast(".")

            return when (tagType) {
                TagType.CLASSNAME_METHODNAME -> "$className.${methodName ?: "NoMethod"}"
                TagType.CLASSNAME -> className
                TagType.FILENAME -> fileName ?: "NoFile"
                TagType.DEFAULT -> {
                    when {
                        /* Prefer "ClassName.methodName" if it's within a class and function. */
                        className.isNotEmpty() && methodName != "<init>" -> "$className.$methodName"

                        /* Fall back to "ClassName" if method name is not available but class name is. */
                        className.isNotEmpty() -> className

                        /* Use file name if neither class nor method context is available. */
                        fileName != null -> fileName

                        else -> throw IllegalArgumentException("Logger tag cannot be inferred. Please specify a tag.")
                    }
                }
            }
        } else {
            throw IllegalArgumentException("Logger tag cannot be inferred. Please specify a tag.")
        }
    }
}

enum class TagType {
    CLASSNAME_METHODNAME,
    CLASSNAME,
    FILENAME,
    DEFAULT
}
