package no.usn.mob3000.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import no.usn.mob3000.ui.screens.chess.train.opening.Opening

/**
 * ViewModel to save state where necessary.
 *
 * @author frigvid
 * @created 2024-10-08
 */
class CBViewModel : ViewModel() {
    private val _openings = mutableStateOf<List<Opening>>(emptyList())
    val openings: State<List<Opening>> = _openings

    private val _selectedOpening = mutableStateOf<Opening?>(null)
    val selectedOpening: State<Opening?> = _selectedOpening

    fun setOpenings(openings: List<Opening>) {
        _openings.value = openings
    }

    fun setSelectedOpening(opening: Opening) {
        _selectedOpening.value = opening
    }
}