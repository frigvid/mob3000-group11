package no.usn.mob3000.domain.helper.game

import kotlinx.coroutines.runBlocking
import no.usn.mob3000.data.repository.game.OpeningsRepository
import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.domain.viewmodel.game.OpeningsViewModel

private const val FETCH_THRESHOLD = 0.3f

/**
 * Helper function to get pure kotlin data class openings from a list of string IDs matching
 * openings.
 *
 * Attempts to get the openings from the cache first, and only fetch the ones that are missing,
 * unless the threshold is exceeded, at which point it just fetches all of them.
 *
 * ## Note
 *
 * This isn't included in the [OpeningsRepository], despite thematically matching, mostly because
 * this assumes an already instantiated view model. Which, depending on *when* this is called, can
 * be bad. I'd like to avoid instantiating view models directly in the data layer.
 *
 * - TODO: This would likely be significantly less ... disgusting, if a local Room was implemented,
 *         as that would make it easier to fetch truly cached openings and not just stateful data
 *         masquerading as proper caches.
 *
 * @param openingIds List of opening IDs to get.
 * @param openingsViewModel The openings view model.
 * @param openingsRepository The openings repository.
 * @return List of [Opening] objects.
 * @author frigvid
 * @created 2024-11-15
 */
fun mapOpeningIdsToOpenings(
    openingIds: List<String>,
    openingsViewModel: OpeningsViewModel = OpeningsViewModel(),
    openingsRepository: OpeningsRepository = OpeningsRepository()
): List<Opening> {
    if (openingIds.isEmpty()) return emptyList()

    val result = mutableListOf<Opening>()
    val openingsToFetch = mutableListOf<String>()

    openingIds.forEach { id ->
        val cachedOpening = openingsViewModel.openings.value.find { it.id == id }
        if (cachedOpening != null) {
            result.add(cachedOpening)
        } else {
            openingsToFetch.add(id)
        }
    }

    if (openingsToFetch.isEmpty()) {
        return result
    }

    try {
        runBlocking {
            if (openingsToFetch.size.toFloat() / openingIds.size > FETCH_THRESHOLD) {
                val fetchedOpenings = openingsRepository.getOpenings()
                result.addAll(
                    fetchedOpenings.filter {
                        it.id in openingsToFetch
                    }
                )
            } else {
                openingsToFetch.forEach { id ->
                    try {
                        val opening = openingsRepository.getOpeningSingle(id)
                        result.add(opening)
                    } catch (error: Exception) {
                        Logger.e("Failed to fetch opening $id.", error)
                    }
                }
            }
        }
    } catch (error: Exception) {
        Logger.e("Error fetching openings.", error)
    }

    return result
}
