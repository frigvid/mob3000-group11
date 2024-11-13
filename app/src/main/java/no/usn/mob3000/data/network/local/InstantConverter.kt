package no.usn.mob3000.data.network.local

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

/**
 * Support class for converting [Instant] to [Long] and vice versa.
 *
 * @author 258030
 * @created 2024-11-13
 */
class InstantConverter {
    @TypeConverter
    fun fromInstant(instant: Instant?): Long? {
        return instant?.toEpochMilliseconds()
    }

    @TypeConverter
    fun toInstant(timestamp: Long?): Instant? {
        return timestamp?.let { Instant.fromEpochMilliseconds(it) }
    }
}

