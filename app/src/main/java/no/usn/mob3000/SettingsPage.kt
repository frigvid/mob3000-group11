import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsPage() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.settings_title)) })
        },
        content = {}
    )
}