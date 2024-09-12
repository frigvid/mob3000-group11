package no.usn.mob3000.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Docs model.
 *
 * @author frigvid
 * @created 2024-09-12
 */
class ProfileViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is the user profile fragment"
    }

    val text: LiveData<String> = _text
}