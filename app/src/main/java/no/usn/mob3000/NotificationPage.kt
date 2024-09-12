import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotificationPage() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.news_title)) })
        },
        content = {}
    )
}