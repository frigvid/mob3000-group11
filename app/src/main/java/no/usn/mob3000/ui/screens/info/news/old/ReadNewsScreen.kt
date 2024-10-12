package no.usn.mob3000.ui.screens.info.news.old

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.mob3000.R
import no.usn.mob3000.Viewport

/**
 *
 * Screen for showing each news clicked. The information here is generated based on the info from
 * the requested row in the database. You navigate here from {@link no.usn.mob3000.ui.screens.info.NewsScreen}
 *
 * TODO: Add more structure and make it 🌟💅pretty💅🌟
 *
 * @param news The [News] to display details about.
 * @author 258030 (Eirik)
 * @created 2024-10-11
 */
/*
@Composable
fun ReadNewsScreen(
    news: News?
) {
    Viewport { innerPadding ->
        if (news != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = news.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = stringResource(R.string.shocking_news),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = news.summary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                }

            }
        else {
            Text(text = "No news found")
        }
    }
}
*/
