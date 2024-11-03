package no.usn.mob3000.ui.components.info

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R

@Composable
fun formatInstant(timestamp: Long?, isCreated: Boolean): String {
    if (timestamp == null) return ""

    val (prefixRes, patternRes) = if (isCreated) {
        R.string.documentation_details_created_date_prefix to R.string.documentation_details_created_date_pattern
    } else {
        R.string.documentation_details_modified_date_prefix to R.string.documentation_details_modified_date_pattern
    }

    val formatter = SimpleDateFormat(stringResource(patternRes), Locale.getDefault())
    return "${stringResource(prefixRes)}: ${formatter.format(Date(timestamp))}"
}



