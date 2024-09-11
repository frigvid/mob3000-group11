package no.usn.mob3000.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * News model.
 *
 * @author frigvid
 * @created 2024-09-11
 */
class NewsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is the news fragment"
    }

    val text: LiveData<String> = _text
}