package no.usn.mob3000.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Settings model.
 *
 * @author frigvid
 * @created 2024-09-12
 */
class SettingsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is the settings fragment"
    }

    val text: LiveData<String> = _text
}