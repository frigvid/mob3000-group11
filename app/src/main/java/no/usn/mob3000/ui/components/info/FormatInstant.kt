package no.usn.mob3000.ui.components.info

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Formats a timestamp into a readable date string with a prefix indicating whether it's a creation or modification date.
 * Might be overkill to have this as its own class
 *
 * TODO: Consider moving this to an abstract util class
 *
 * @param timestamp The timestamp to format.
 * @param isCreated A boolean indicating whether the timestamp is a creation date or not.
 *
 * @author 258030
 * @created 2024-10-30
 */
@Composable
fun formatInstant(timestamp: Long?, isCreated: Boolean): String {
    if (timestamp == null) return ""

    val (prefixRes, patternRes) = if (isCreated) {
        R.string.info_item_details_created_date_prefix to R.string.ISO8601_yyyy_MM_dd_HH_mm
    } else {
        R.string.info_item_details_modified_date_prefix to R.string.ISO8601_yyyy_MM_dd_HH_mm
    }

    val formatter = SimpleDateFormat(stringResource(patternRes), Locale.getDefault())
    return "${stringResource(prefixRes)}: ${formatter.format(Date(timestamp))}"
}
