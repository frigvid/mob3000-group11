package no.usn.mob3000.domain.viewmodel.game

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.PieceType
import com.github.bhlangonijr.chesslib.Rank
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.game.GameContext
import com.github.bhlangonijr.chesslib.game.GameMode
import com.github.bhlangonijr.chesslib.game.VariationType
import com.github.bhlangonijr.chesslib.move.Move
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.usn.mob3000.domain.utils.Logger

data class ChessBoardState(
    val board: Board = Board(
        GameContext(GameMode.HUMAN_VS_HUMAN, VariationType.NORMAL),
        true
    ),
    val draggedPiece: DraggedPiece? = null,
    val legalMoves: Set<Square> = emptySet(),
    val pendingPromotion: PendingPromotion? = null

)

data class PendingPromotion(
    val pieceType: PieceType,
    val pieceSide: Side,
    val squareRank: Rank,
    val dialogPosition: Pair<Float, Float>
)

data class DraggedPiece(
    val piece: Piece,
    val fromSquare: Square,
    val currentX: Float,
    val currentY: Float
)

sealed class ChessBoardEvent {
    data class InitializeBoard(val fen: String) : ChessBoardEvent()
    data class OnPieceDragStart(val square: Square, val x: Float, val y: Float) : ChessBoardEvent()
    data class OnPieceDragged(val x: Float, val y: Float) : ChessBoardEvent()
    data class OnPieceDragEnd(val toSquare: Square?) : ChessBoardEvent()
    data class OnPromotionPieceSelected(val piece: Piece) : ChessBoardEvent()
}

class ChessBoardViewModel : ViewModel() {
    private val _boardState = MutableStateFlow(ChessBoardState())
    val boardState = _boardState.asStateFlow()

    private val _boardPieceDraggedToSquare = MutableStateFlow<Square>(Square.NONE)
    val boardPieceDraggedToSquare = _boardPieceDraggedToSquare.asStateFlow()
    fun setBoardPieceDraggedToSquare(square: Square) {
        _boardPieceDraggedToSquare.value = square
    }

    fun getChessBoardState(): ChessBoardState {
        return boardState.value
    }



    // NOTE: Separated from the rest of the code, somewhat, to indicate its testy nature.
    private val _promotionLiveData = MutableLiveData<PromotionState?>()
    val promotionLiveData: LiveData<PromotionState?> = _promotionLiveData
    private var pendingPromotionMove: Pair<Square, Square>? = null

    data class PromotionState(
        val square: Square,
        val offset: Offset,
        val side: Side
    )

    private val _draggedPieceLiveData = MutableLiveData<DraggedPiece?>()
    val draggedPieceLiveData: LiveData<DraggedPiece?> = _draggedPieceLiveData



    fun onEvent(event: ChessBoardEvent) {
        Logger.v(message = "Received event: $event")

        when (event) {
            is ChessBoardEvent.InitializeBoard -> {
                try {
                    _boardState.value.board.loadFromFen(event.fen)
                    Logger.d("Loaded chess board from fen!")

                    _boardState.value = _boardState.value.copy(
                        draggedPiece = null,
                        legalMoves = emptySet()
                    )
                    Logger.d(message = "Board initialized using FEN notation: ${event.fen}")
                } catch (e: Exception) {
                    Logger.e(message = "Error initializing the chess board!", throwable = e)
                }
            }

            is ChessBoardEvent.OnPieceDragStart -> {
                Logger.i("Cleaning up promotion data and pending move.")
                _promotionLiveData.value = null
                pendingPromotionMove = null

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
                } catch (e: Exception) {
                    Logger.e(message = "Error during drag", throwable = e)
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

                            pendingPromotionMove = Pair(draggedPiece.fromSquare, event.toSquare)
                            Logger.d("Move: $pendingPromotionMove")

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
                        }

                        if (pendingPromotionMove != null) {
                            _boardState.value = _boardState.value.copy(
                                draggedPiece = null,
                                legalMoves = emptySet()
                            )
                        }

                        // TODO: Remove when complete.
                        //if (
                        //    draggedPiece.piece.pieceType === PieceType.PAWN &&
                        //    (
                        //        (draggedPiece.piece.pieceSide == Side.WHITE && event.toSquare.rank == Rank.RANK_8) ||
                        //        (draggedPiece.piece.pieceSide == Side.BLACK && event.toSquare.rank == Rank.RANK_1)
                        //    )
                        //) {
                        //    Logger.d(message = "Promotion pending! Side: ${draggedPiece.piece.pieceSide}. Piece: ${draggedPiece.piece.pieceType}. Rank: ${event.toSquare.rank}.")
                        //    viewModelScope.launch {
                        //        _promotionLiveData.value = PromotionState(
                        //            square = event.toSquare,
                        //            offset = Offset(draggedPiece.currentX, draggedPiece.currentY),
                        //            side = draggedPiece.piece.pieceSide
                        //        )
                        //    }
                        //} else {
                        //    if (_boardState.value.legalMoves.contains(event.toSquare)) {
                        //        val move = Move(draggedPiece.fromSquare, event.toSquare)
                        //        _boardState.value.board.doMove(move)
                        //    } else {
                        //        Logger.d(message = "Move is not legal")
                        //        _boardState.value = _boardState.value.copy(
                        //            draggedPiece = null,
                        //            legalMoves = emptySet()
                        //        )
                        //    }
                        //}
                    }

                    // TODO: Remove when complete.
                    //if (
                    //    draggedPiece != null &&
                    //    event.toSquare != null &&
                    //    _boardState.value.legalMoves.contains(event.toSquare)
                    //) {
                    //    val move = Move(draggedPiece.fromSquare, event.toSquare)
                    //    _boardState.value.board.doMove(move)
                    //} else {
                    //    Logger.d(message = "Move is not legal")
                    //    _boardState.value = _boardState.value.copy(
                    //        draggedPiece = null,
                    //        legalMoves = emptySet()
                    //    )
                    //}
                    //if (
                    //    (
                    //        draggedPiece!!.piece.pieceSide == Side.WHITE &&
                    //        (event.toSquare?.rank ?: Rank.NONE) == Rank.RANK_8
                    //    ) || (
                    //        draggedPiece.piece.pieceSide == Side.BLACK &&
                    //        (event.toSquare?.rank ?: Rank.NONE) == Rank.RANK_1
                    //    )
                    //) {
                    //    Logger.d(message = "Promotion pending! Side: ${draggedPiece.piece.pieceSide}. Piece: ${draggedPiece.piece.pieceType}. Rank: ${event.toSquare?.rank}.")
                    //    if (draggedPiece.piece.pieceType === PieceType.PAWN) {
                    //        // TODO: Use Move() with promotion piece. Show dialogue first, to set the promotion piece.
                    //        //       public Move(Square from, Square to, Piece promotion) {
                    //        //           this.promotion = promotion;
                    //        //           this.from = from;
                    //        //           this.to = to;
                    //        //       }
                    //    } else {
                    //        // TODO: Do a regular move.
                    //    }
                    //    viewModelScope.launch {
                    //        _promotionLiveData.value = PromotionState(
                    //            square = event.toSquare!!,
                    //            offset = Offset(draggedPiece.currentX, draggedPiece.currentY),
                    //            side = draggedPiece.piece.pieceSide
                    //        )
                    //    }
                    //    Logger.d(message = "Promotion pending! Board state: ${_boardState.value}")
                    //}
                    //Logger.d(message = "Cleaning up drag state")
                    //_boardState.value = _boardState.value.copy(
                    //    draggedPiece = null,
                    //    legalMoves = emptySet()
                    //)
                } catch (e: Exception) {
                    Logger.e("Error during drag end state!", throwable = e)

                    Logger.i("Cleaning up drag state.")
                    _boardState.value = _boardState.value.copy(
                        draggedPiece = null,
                        legalMoves = emptySet()
                    )

                    Logger.i("Cleaning up promotion data and pending move.")
                    _promotionLiveData.value = null
                    pendingPromotionMove = null
                }
            }

            is ChessBoardEvent.OnPromotionPieceSelected -> {
                pendingPromotionMove?.let { (fromSquare, toSquare) ->
                    Logger.d("Execute pending promotion move!")
                    val move = Move(fromSquare, toSquare, event.piece)
                    _boardState.value.board.doMove(move)

                    Logger.i("Cleaning up promotion data and pending move.")
                    _promotionLiveData.value = null
                    pendingPromotionMove = null

                    Logger.i("Cleaning up drag state.")
                    _boardState.value = _boardState.value.copy(
                        draggedPiece = null,
                        legalMoves = emptySet()
                    )

                    Logger.i("Promotion complete!")
                }
            }
        }
    }
}
