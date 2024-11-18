package no.usn.mob3000.domain.model.game.board

/**
 * Chess board practice modes.
 *
 * @author frigvid
 * @created 2024-11-16
 */
enum class PracticeMode {
    /* Train only a single opening. */
    SINGLE,
    /* Train a group of openings. */
    GROUP,
    /* Not practice. */
    NONE
}
