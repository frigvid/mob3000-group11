package no.usn.mob3000.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * News model.
 *
 * @author frigvid
 * @created 2024-09-11
 */
class HomeViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is the home fragment"
    }

    val text: LiveData<String> = _text
}