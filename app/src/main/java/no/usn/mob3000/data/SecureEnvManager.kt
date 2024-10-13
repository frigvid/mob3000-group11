package no.usn.mob3000.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import no.usn.mob3000.BuildConfig

/**
 * Manages secure storage and retrieval of environment variables in Android applications.
 *
 * This object provides functionality to securely encrypt and store sensitive information,
 * such as API keys and URLs, using Android's EncryptedSharedPreferences. It offers methods
 * to initialize, store, and retrieve these encrypted values throughout the application lifecycle.
 *
 * @author frigvid
 * @created 2024-10-07
 */
object SecureEnvManager {
    private const val ENCRYPTED_PREF_FILE = "encrypted_env_prefs"

    /**
     * Creates and returns an instance of EncryptedSharedPreferences.
     *
     * This function sets up a secure shared preferences file using AES256 GCM encryption
     * for the master key, AES256 SIV for key encryption, and AES256 GCM for value encryption.
     *
     * @param context The application context used to create the EncryptedSharedPreferences.
     * @return An instance of EncryptedSharedPreferences for secure data storage.
     * @author frigvid
     * @created 2024-10-07
     */
    private fun getEncryptedSharedPreferences(context: Context): EncryptedSharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_PREF_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    /**
     * Encrypts and stores a key-value pair in EncryptedSharedPreferences.
     *
     * This function takes a key and its corresponding value, encrypts the value,
     * and stores the encrypted key-value pair in EncryptedSharedPreferences.
     * If the value is null, no action is taken.
     *
     * @param context The application context used to access EncryptedSharedPreferences.
     * @param key The key under which the value will be stored.
     * @param value The value to be encrypted and stored. Can be null.
     * @author frigvid
     * @created 2024-10-07
     */
    private fun encryptAndStoreValue(context: Context, key: String, value: String?) {
        value?.let {
            getEncryptedSharedPreferences(context).edit().putString(key, it).apply()
        }
    }

    /**
     * Retrieves and decrypts a value from EncryptedSharedPreferences.
     *
     * This function takes a key, retrieves its corresponding encrypted value
     * from EncryptedSharedPreferences, decrypts it, and returns the decrypted value.
     * If the key is not found, null is returned.
     *
     * @param context The application context used to access EncryptedSharedPreferences.
     * @param key The key whose value is to be retrieved and decrypted.
     * @return The decrypted value associated with the key, or null if the key is not found.
     * @author frigvid
     * @created 2024-10-07
     */
    fun getDecryptedValue(context: Context, key: String): String? {
        return getEncryptedSharedPreferences(context).getString(key, null)
    }

    /**
     * Initializes and securely stores environment variables.
     *
     * This function should be called once when the application starts, typically in the
     * Application class's onCreate() method. It reads environment variables from BuildConfig
     * and securely stores them using EncryptedSharedPreferences.
     *
     * @param context The application context used to access EncryptedSharedPreferences.
     * @author frigvid
     * @created 2024-10-07
     */
    fun initializeEnvVariables(context: Context) {
        encryptAndStoreValue(context, "NEXT_PUBLIC_SUPABASE_URL", BuildConfig.NEXT_PUBLIC_SUPABASE_URL)
        encryptAndStoreValue(context, "NEXT_PUBLIC_SUPABASE_ANON_KEY", BuildConfig.NEXT_PUBLIC_SUPABASE_ANON_KEY)
    }
}