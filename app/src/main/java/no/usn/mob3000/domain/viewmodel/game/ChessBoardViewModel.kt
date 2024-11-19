package no.usn.mob3000.domain.viewmodel.game

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.PieceType
import com.github.bhlangonijr.chesslib.Rank
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.data.model.game.GameDataDto
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.game.GameDataSource
import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.domain.model.game.board.ChessBoardEvent
import no.usn.mob3000.domain.model.game.board.ChessBoardGameState
import no.usn.mob3000.domain.model.game.board.ChessBoardState
import no.usn.mob3000.domain.model.game.board.DraggedPiece
import no.usn.mob3000.domain.model.game.board.PromotionState
import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.domain.usecase.game.gamedata.FetchGameDataUseCase
import no.usn.mob3000.domain.usecase.game.gamedata.UpdateGameDataUseCase

/**
 * The `ChessBoard` components view model. It's primarily used there and in the `PlayScreen`.
 *
 * This serves as the main handler for everything related to it; movement of chess pieces,
 * the state of the chess board, whether a piece is to be promoted or not, the game's status
 * and the end user's stats.
 *
 * @property fetchGameDataUseCase The game state fetching use case.
 * @property updateGameDataUseCase The game state updating use case.
 * @property authDataSource The authentication data source.
 * @author frigvid
 * @created 2024-11-03
 */
class ChessBoardViewModel(
    private val fetchGameDataUseCase: FetchGameDataUseCase = FetchGameDataUseCase(GameDataSource()),
    private val updateGameDataUseCase: UpdateGameDataUseCase = UpdateGameDataUseCase(GameDataSource()),
    private val authDataSource: AuthDataSource = AuthDataSource()
) : ViewModel() {
    /* The chess board's state. */
    private val _boardState = MutableStateFlow(ChessBoardState())
    val boardState = _boardState.asStateFlow()

    /* The openings selected to play via the openings screen or groups screen. */
    private val _selectedBoardOpenings = mutableStateOf<List<Opening>>(emptyList())
    val selectedBoardOpenings = _selectedBoardOpenings

    /* Promotion tracking. */
    private val _promotionLiveData = MutableLiveData<PromotionState?>()
    var promotionLiveData: LiveData<PromotionState?> = _promotionLiveData
    private var _pendingPromotionMove: Pair<Square, Square>? = null

    /* Stores the chess piece while in motion. */
    private val _draggedPieceLiveData = MutableLiveData<DraggedPiece?>()
    val draggedPieceLiveData: LiveData<DraggedPiece?> = _draggedPieceLiveData

    /* The game state is used for the game's status message as well as user game stats. */
    private val _gameState = MutableStateFlow(
        ChessBoardGameState(
            status = "",
            wins = 0,
            losses = 0,
            draws = 0
        )
    )
    val gameState = _gameState.asStateFlow()

    /**
     * Game data and game status are used in both the `ChessBoard` and `PlayScreen`,
     * and data should thus be life-cycle aware outside its composable.
     *
     * @author frigvid
     * @created 2024-11-17
     */
    init {
        viewModelScope.launch {
            loadGameData()
            updateGameStatus()
        }
    }

    /**
     * Function to handle events that occur on the chess board.
     *
     * E.g. whether the board is to be initialized, whether or not
     * it has a piece being dragged, etc.
     *
     * @param event The [ChessBoardEvent].
     * @author frigvid
     * @created 2024-11-03
     */
    fun onEvent(event: ChessBoardEvent) {
        Logger.v(message = "Received event: $event")

        when (event) {
            is ChessBoardEvent.InitializeBoard -> {
                try {
                    _boardState.value.board.loadFromFen(event.fen)
                    Logger.d("Loaded chess board from fen!")
                    updateGameStatus()

                    updateBoardState {
                        it.copy(
                            draggedPiece = null,
                            legalMoves = emptySet()
                        )
                    }

                    Logger.d(message = "Board initialized using FEN notation: ${event.fen}")
                } catch (error: Exception) {
                    Logger.e(message = "Error initializing the chess board!", throwable = error)
                }
            }

            is ChessBoardEvent.OnPieceDragStart -> {
                Logger.i("Cleaning up promotion data and pending move.")
                _promotionLiveData.value = null
                _pendingPromotionMove = null

                try {
                    val piece = _boardState.value.board.getPiece(event.square)
                    Logger.d(message = "Drag start: square=${event.square}, piece=$piece")

                    if (piece != Piece.NONE && piece.pieceSide == _boardState.value.board.sideToMove) {
                        val legalMoves = _boardState.value.board.legalMoves()
                            .filter { it.from == event.square }
                            .map { it.to }
                            .toSet()

                        Logger.d(message = "Legal moves for $piece at ${event.square}: $legalMoves")

                        _boardState.value = _boardState.value.copy(
                            draggedPiece = DraggedPiece(
                                piece = piece,
                                fromSquare = event.square,
                                currentX = event.x,
                                currentY = event.y
                            ),
                            legalMoves = legalMoves
                        )
                    }
                } catch (e: Exception) {
                    Logger.e(message = "Error during drag start", throwable = e)
                }
            }

            is ChessBoardEvent.OnPieceDragged -> {
                try {
                    _boardState.value.draggedPiece?.let { draggedPiece ->
                        _boardState.value = _boardState.value.copy(
                            draggedPiece = draggedPiece.copy(
                                currentX = event.x,
                                currentY = event.y
                            )
                        )
                        Logger.v(message = "Piece dragged to x=${event.x}, y=${event.y}")
                    }
                } catch (error: Exception) {
                    Logger.e(message = "Error during drag", throwable = error)
                }
            }

            is ChessBoardEvent.OnPieceDragEnd -> {
                try {
                    val draggedPiece = _boardState.value.draggedPiece
                    Logger.d(message = "Drag end: draggedPiece=$draggedPiece, toSquare=${event.toSquare}")

                    if (draggedPiece != null && event.toSquare != null && _boardState.value.legalMoves.contains(event.toSquare)) {
                        val isPromotion =
                            draggedPiece.piece.pieceType === PieceType.PAWN && (
                                (draggedPiece.piece.pieceSide == Side.WHITE && event.toSquare.rank == Rank.RANK_8) ||
                                (draggedPiece.piece.pieceSide == Side.BLACK && event.toSquare.rank == Rank.RANK_1)
                            )

                        if (isPromotion) {
                            Logger.i("Promotion pending!")
                            Logger.d("Side: ${draggedPiece.piece.pieceSide}. Piece: ${draggedPiece.piece.pieceType}. Rank: ${event.toSquare.rank}.")

                            Logger.i("Storing reference of original board state.")
                            val currentBoard = _boardState.value.board

                            _pendingPromotionMove = Pair(draggedPiece.fromSquare, event.toSquare)
                            Logger.d("Move: $_pendingPromotionMove")

                            updateBoardState {
                                it.copy(
                                    draggedPiece = null,
                                    legalMoves = emptySet()
                                )
                            }

                            _promotionLiveData.value = PromotionState(
                                square = event.toSquare,
                                offset = Offset(draggedPiece.currentX, draggedPiece.currentY),
                                side = draggedPiece.piece.pieceSide
                            )

                            Logger.d("State: ${_promotionLiveData.value}")
                            Logger.i("Promotion data set!")
                        } else {
                            val move = Move(draggedPiece.fromSquare, event.toSquare)
                            _boardState.value.board.doMove(move)

                            updateBoardState {
                                it.copy(
                                    draggedPiece = null,
                                    legalMoves = emptySet()
                                )
                            }
                        }
                    } else {
                        updateBoardState {
                            it.copy(
                                draggedPiece = null,
                                legalMoves = emptySet()
                            )
                        }
                    }

                    updateGameStatus()
                } catch (error: Exception) {
                    Logger.e("Error during drag end state!", throwable = error)

                    Logger.i("Cleaning up drag state.")
                    updateBoardState {
                        it.copy(
                            draggedPiece = null,
                            legalMoves = emptySet()
                        )
                    }

                    Logger.i("Cleaning up promotion data and pending move.")
                    _promotionLiveData.value = null
                    _pendingPromotionMove = null
                }
            }

            is ChessBoardEvent.OnPromotionPieceSelected -> {
                _pendingPromotionMove?.let { (fromSquare, toSquare) ->
                    Logger.d("Execute pending promotion move!")
                    val move = Move(fromSquare, toSquare, event.piece)
                    _boardState.value.board.doMove(move)
                    updateGameStatus()

                    Logger.i("Cleaning up promotion data and pending move.")
                    _promotionLiveData.value = null
                    _pendingPromotionMove = null

                    Logger.i("Cleaning up drag state.")
                    updateBoardState {
                        it.copy(
                            draggedPiece = null,
                            legalMoves = emptySet()
                        )
                    }

                    Logger.i("Promotion complete!")
                }
            }
        }
    }

    /**
     * Update the game's status.
     *
     * @author frigvid
     * @created 2024-11-17
     */
    private fun updateGameStatus() {
        try {
            val board = _boardState.value.board
            Logger.d("Current game state before update: ${_gameState.value}")

            val newStatus = when {
                board.isMated() -> {
                    val winner = if (board.sideToMove == Side.WHITE) "Black" else "White"
                    Logger.d("Game status update, is mated: $winner")
                    "$winner won by checkmate!"
                }

                board.isDraw() -> "Game drawn!"

                board.isKingAttacked -> {
                    val side = if (board.sideToMove == Side.WHITE) "White" else "Black"
                    Logger.d("Game status update, in check: $side")
                    "$side is in check!"
                }

                else -> {
                    val side = if (board.sideToMove == Side.WHITE) "White" else "Black"
                    Logger.d("Game status update, regular move: $side")
                    "$side's turn to move"
                }
            }

            Logger.d("Status: $newStatus")
            val newState = when {
                board.isMated() -> {
                    if (board.sideToMove == Side.WHITE) {
                        _gameState.value.copy(
                            status = newStatus,
                            losses = _gameState.value.losses + 1
                        )
                    } else {
                        _gameState.value.copy(
                            status = newStatus,
                            wins = _gameState.value.wins + 1
                        )
                    }
                }
                board.isDraw() -> {
                    _gameState.value.copy(
                        status = newStatus,
                        draws = _gameState.value.draws + 1
                    )
                }
                else -> _gameState.value.copy(status = newStatus)
            }
            Logger.d("New state: $newState")

            viewModelScope.launch {
                Logger.d("Updating game state with new status: $newStatus")
                _gameState.emit(newState)
                Logger.d("Game state after update: ${_gameState.value}")

                if (board.isMated() || board.isDraw()) {
                    val gameData = GameDataDto(
                        userId = authDataSource.getCurrentUserId(),
                        gameWins = newState.wins,
                        gameLosses = newState.losses,
                        gameDraws = newState.draws
                    )

                    updateGameDataUseCase(gameData).fold(
                        onSuccess = { Logger.d("Game stats updated successfully") },
                        onFailure = { error -> Logger.e("Failed to update game stats, $error") }
                    )
                }
            }
        } catch (error: Exception) {
            Logger.e("Failed to update game status", throwable = error)
        }
    }

    /**
     * Updates the chess board state.
     *
     * @author frigvid
     * @created 2024-11-17
     */
    private fun updateBoardState(
        update: (ChessBoardState) -> ChessBoardState
    ) {
        val currentState = _boardState.value
        val newState = update(currentState)
        _boardState.value = newState.copy(board = currentState.board)
    }

    /**
     * Load game data.
     *
     * @author frigvid
     * @created 2024-11-17
     */
    private suspend fun loadGameData() {
        fetchGameDataUseCase().fold(
            onSuccess = { gameData ->
                _gameState.value = _gameState.value.copy(
                    wins = gameData.gameWins ?: 0,
                    losses = gameData.gameLosses ?: 0,
                    draws = gameData.gameDraws ?: 0
                )
            },
            onFailure = { error ->
                Logger.e("Failed to fetch initial game data, $error")
            }
        )
    }

    /**
     * Reset the game board's state back to default.
     *
     * @author frigvid
     * @created 2024-11-17
     */
    fun resetGame() {
        Logger.d("Resetting game...")
        _boardState.value = ChessBoardState()
        _boardState.value.board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")

        _promotionLiveData.value = null
        _pendingPromotionMove = null

        updateBoardState {
            it.copy(
                draggedPiece = null,
                legalMoves = emptySet()
            )
        }

        _gameState.value = ChessBoardGameState(
            status = "White's turn to move",
            wins = _gameState.value.wins,
            losses = _gameState.value.losses,
            draws = _gameState.value.draws
        )

        Logger.d("Game reset complete")
    }

    /**
     * Undoes last move.
     *
     * @author frigvid
     * @created 2024-11-17
     */
    fun undoLastMove() {
        Logger.d("Attempting to undo last move...")
        if (_boardState.value.board.backup.isNotEmpty()) {
            _boardState.value.board.undoMove()
            Logger.d("Move undone successfully")

            _promotionLiveData.value = null
            _pendingPromotionMove = null

            updateBoardState {
                it.copy(
                    draggedPiece = null,
                    legalMoves = emptySet()
                )
            }

            updateGameStatus()
        } else {
            Logger.d("No moves to undo!")
        }
    }

    /**
     * Used to set the list of opening to train against or to set
     * the board's initial position, that is then fetched through [selectedBoardOpenings].
     *
     * @author frigvid
     * @created 2024-11-16
     */
    fun setSelectedBoardOpenings(openings: List<Opening>) {
        _selectedBoardOpenings.value = openings
    }
}
