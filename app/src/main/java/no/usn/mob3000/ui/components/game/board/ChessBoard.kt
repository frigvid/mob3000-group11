package no.usn.mob3000.ui.components.game.board

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import no.usn.mob3000.ui.theme.ChessboardCellDark
import no.usn.mob3000.ui.theme.ChessboardCellLight
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.bhlangonijr.chesslib.File
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Rank
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square
import no.usn.mob3000.domain.helper.Logger
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.github.bhlangonijr.chesslib.game.GameMode
import com.github.bhlangonijr.chesslib.game.VariationType
import no.usn.mob3000.domain.model.game.board.ChessBoardEvent
import no.usn.mob3000.domain.model.game.board.ChessPieces
import no.usn.mob3000.domain.viewmodel.game.ChessBoardViewModel
import no.usn.mob3000.domain.model.game.board.DraggedPiece
import no.usn.mob3000.domain.model.game.board.PromotionState
import no.usn.mob3000.ui.theme.DefaultListItemBackground
import no.usn.mob3000.ui.theme.LegalMoveHighlight

/**
 * The chess board component provides a a mostly functional chess board.
 *
 * ## Note
 *
 * As a rule of thumb, we've been passing screens the view models functions and values via
 * parameters as this makes them easier to test, were we to need or want for such a thing.
 *
 * However, the chess board is very tightly interwoven with its view model, and passing
 * around its functions and values via parameters would cause strange and unnecessary
 * dependency chains. Mostly because aside from the `PlayScreen`, most others have no
 * need for the full interactable experience.
 *
 * Thus, the view model is provided and instantiated via a default parameter value in
 * the composable function's parameter list instead. I'd prefer another method, but it's
 * a discussion of complexity versus simplicity, as well as the cost in time. Which, as of
 * this date, there's not a lot left to go around.
 *
 * ## Warning
 *
 * The chess board, from a lack of development time, is somewhat fragile. You'll notice this
 * appear in some places. In debug builds, via the [Logger] helper function, you'll be able
 * to see the logs for movement and state changes. Most of the issues are caused by a fragile
 * relationship between the logic board and the graphical user-interface board.
 *
 * Here's some things that may come up:
 *
 * 1. Dragging pieces may not seem to work, though, if you keep at it, it usually does by the
 *    second or third attempt.
 *
 * 2. Managing to reach the other side with a pawn, and opening the promotion dialogue resets
 *    the board.
 *
 * 3. The "reset board" and "undo move" buttons have their logic implemented, but again, a
 *    fragile relationship between the graphical and the logical layers, without additional
 *    development time, means they don't seem to work. They do on the logical layer, however,
 *    so it's mostly just a case of debugging until it works.
 *
 * Most of the functionality is there though, it's just a matter of a good few more days of
 * development time that we don't have.
 *
 * Currently, you can move pieces, consume other pieces, end up in mate and check mate and
 * potentially soft-lock the board if you win/lose. Should be fixed by exiting the screen
 * and entering it again, however.
 *
 * @param modifier The modifier.
 * @param startingPosition FEN notation for the chess board's default piece starting position.
 * @param chessBoardViewModel The tightly coupled chess board view model.
 * @param gameMode The game mode. E.g. vs humans or machine.
 * @param gameVariation The chess variation to play.
 * @param gameHistory Whether history is enabled for the chess board.
 * @param gameInteractable Whether the board is interactable or not.
 * @author frigvid
 * @created 2024-10-30
 */
@Composable
fun ChessBoard(
    modifier: Modifier = Modifier,
    startingPosition: String = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
    chessBoardViewModel: ChessBoardViewModel = ChessBoardViewModel(),
    gameMode: GameMode = GameMode.HUMAN_VS_HUMAN,
    gameVariation: VariationType = VariationType.NORMAL,
    gameHistory: Boolean = true,
    gameInteractable: Boolean = true
) {
    LaunchedEffect(startingPosition) {
        chessBoardViewModel.onEvent(ChessBoardEvent.InitializeBoard(startingPosition))
    }

    val boardState by chessBoardViewModel.boardState.collectAsState()
    var boardSize by remember { mutableFloatStateOf(0f) }
    val cellSize = boardSize / 8

    val chessBoardCellTextMeasurer = rememberTextMeasurer()

    val chessBoardPiecePainters =
        ChessPieces.pieceToResourceMap.mapValues { (_, resourceId) ->
            painterResource(id = resourceId)
        }

    val lifecycleOwner = LocalLifecycleOwner.current
    var dragPiece by remember { mutableStateOf<DraggedPiece?>(null) }
    var promotionState by remember { mutableStateOf<PromotionState?>(null) }

    DisposableEffect(lifecycleOwner) {
        val promotionStateObserver = Observer<PromotionState?> { state -> promotionState = state }

        chessBoardViewModel.promotionLiveData.observe(lifecycleOwner, promotionStateObserver)
        Logger.i("Promotion state observer initialized!")

        onDispose {
            chessBoardViewModel.promotionLiveData.removeObserver(promotionStateObserver)
            Logger.i("Promotion state observer destroyed!")
        }
    }

    DisposableEffect(lifecycleOwner) {
        val draggedPieceObserver = Observer<DraggedPiece?> { state -> dragPiece = state }

        chessBoardViewModel.draggedPieceLiveData.observe(lifecycleOwner, draggedPieceObserver)
        Logger.i("Dragged piece state observer initialized! $dragPiece")

        onDispose {
            chessBoardViewModel.draggedPieceLiveData.removeObserver(draggedPieceObserver)
            Logger.i("Dragged piece state observer destroyed!")
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .onGloballyPositioned { coordinates ->
                boardSize = minOf(
                    coordinates.size.width.toFloat(),
                    coordinates.size.height.toFloat()
                )
            }
            .pointerInput(Unit) {
                if (gameInteractable) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            Logger.i(message = "Drag gesture started at $offset")
                            val file = (offset.x / cellSize).toInt()
                            val rank = 7 - (offset.y / cellSize).toInt()
                            if (file in 0..7 && rank in 0..7) {
                                val square = Square.encode(
                                    Rank.allRanks[rank],
                                    File.allFiles[file]
                                )
                                chessBoardViewModel.onEvent(ChessBoardEvent.OnPieceDragStart(square, offset.x, offset.y))
                            }
                        },
                        onDrag = { change, _ ->
                            Logger.v(message = "Drag gesture update at ${change.position}")
                            chessBoardViewModel.onEvent(
                                ChessBoardEvent.OnPieceDragged(
                                change.position.x,
                                change.position.y
                            ))
                        },
                        onDragEnd = {
                            Logger.d(message = "Drag gesture ended")
                            val draggedPiece = boardState.draggedPiece
                            if (draggedPiece != null) {
                                val file = (draggedPiece.currentX / cellSize).toInt()
                                val rank = 7 - (draggedPiece.currentY / cellSize).toInt()
                                val toSquare = if (file in 0..7 && rank in 0..7) {
                                    Square.encode(
                                        Rank.allRanks[rank],
                                        File.allFiles[file]
                                    )
                                } else null

                                Logger.d(message = "Drag ended at square $toSquare")
                                chessBoardViewModel.onEvent(ChessBoardEvent.OnPieceDragEnd(toSquare))
                            } else {
                                Logger.e(message = "Drag ended but no dragged piece found")
                            }
                        }
                    )
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw board squares
            for (rank in 0..7) {
                for (file in 0..7) {
                    val isLight = (rank + file) % 2 == 0
                    val squareColor = if (isLight) ChessboardCellLight else ChessboardCellDark

                    val square = Square.encode(
                        Rank.allRanks[7 - rank],
                        File.allFiles[file]
                    )

                    /* Draw the chessboard square, and then add the ID. */
                    drawRect(
                        color = squareColor,
                        topLeft = Offset(file * cellSize, rank * cellSize),
                        size = Size(cellSize, cellSize)
                    )

                    val textColor = if (isLight) ChessboardCellDark else ChessboardCellLight
                    val textLayoutResult = chessBoardCellTextMeasurer.measure(
                        text = square.toString(),
                        style = TextStyle(
                            color = textColor,
                            fontSize = (cellSize * 0.08f).sp
                        )
                    )

                    translate(
                        left = file * cellSize + cellSize * 0.1f,
                        top = rank * cellSize + cellSize * 0.1f
                    ) {
                        drawText(textLayoutResult)
                    }

                    /* Show a highlight for a legal move when dragging a piece. */
                    if (boardState.legalMoves.contains(square) && gameInteractable) {
                        drawCircle(
                            color = LegalMoveHighlight,
                            radius = cellSize * 0.3f,
                            center = Offset(
                                file * cellSize + cellSize / 2,
                                rank * cellSize + cellSize / 2
                            )
                        )
                    }

                    /* Draw the piece. */
                    val piece = boardState.board.getPiece(square)
                    if (
                        piece != Piece.NONE && (
                            boardState.draggedPiece == null ||
                            boardState.draggedPiece!!.fromSquare != square
                        )
                    ) {
                        val painter = chessBoardPiecePainters[piece]
                        if (painter != null) {
                            drawPiece(
                                painter,
                                Offset(file * cellSize, rank * cellSize),
                                cellSize
                            )
                        }
                    }
                }
            }

            if (gameInteractable) {
                /* Draw the piece's new location. */
                boardState.draggedPiece?.let { draggedPiece ->
                    val painter = chessBoardPiecePainters[draggedPiece.piece]
                    if (painter != null) {
                        drawPiece(
                            painter,
                            Offset(
                                draggedPiece.currentX - cellSize / 2,
                                draggedPiece.currentY - cellSize / 2
                            ),
                            cellSize
                        )
                    }
                }
            }
        }

        if (gameInteractable) {
            promotionState?.let { state ->
                PromotionDialog(
                    square = state.square,
                    offset = state.offset,
                    side = state.side,
                    onPieceSelected = { piece ->
                        chessBoardViewModel.onEvent(ChessBoardEvent.OnPromotionPieceSelected(piece))
                    },
                    onDismissRequest = {
                        promotionState = null
                    }
                )
            }
        }
    }
}

/**
 * Draw the chess board's chess piece.
 *
 * @author frigvid
 * @created 2024-11-16
 */
private fun DrawScope.drawPiece(
    painter: Painter,
    topLeft: Offset,
    size: Float
) {
    translate(topLeft.x, topLeft.y) {
        with(painter) {
            draw(
                size = Size(size, size)
            )
        }
    }
}

/**
 * Composable promotion dialogue for finishing "upgrading" chess pieces.
 *
 * @author frigvid
 * @created 2024-11-16
 */
@Composable
private fun PromotionDialog(
    square: Square,
    offset: Offset,
    side: Side,
    onPieceSelected: (Piece) -> Unit,
    onDismissRequest: () -> Unit
) {
    Logger.i("Promotion dialogue opened!\n\tSquare: $square\n\tOffset: $offset\n\tSide: $side\n\tOn Piece Selected: $onPieceSelected")

    val promotionPieces = if (side == Side.WHITE) {
        listOf(Piece.WHITE_QUEEN, Piece.WHITE_ROOK, Piece.WHITE_BISHOP, Piece.WHITE_KNIGHT)
    } else {
        listOf(Piece.BLACK_QUEEN, Piece.BLACK_ROOK, Piece.BLACK_BISHOP, Piece.BLACK_KNIGHT)
    }

    Popup(
        properties = PopupProperties(focusable = true),
        alignment = Alignment.TopStart,
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                //.offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = DefaultListItemBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column {
                // TODO: Dynamic placement, horizontal, vertical. TrianglePointer(true)
                TrianglePointer()

                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    promotionPieces.forEach { piece ->
                        Image(
                            painter = painterResource(ChessPieces.pieceToResourceMap[piece]!!),
                            contentDescription = piece.name,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable {
                                    onPieceSelected(piece)
                                    onDismissRequest()
                                }
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * The triangle pointer used in the [PromotionDialog].
 *
 * Together with it, this is meant to be used to be
 * re-drawn on the fly, so that it's always correctly
 * positioned on the top or bottom row of the dialogue
 * and always pointing at the correct chess board square.
 *
 * @author frigvid
 * @created 2024-11-16
 */
@Composable
private fun ColumnScope.TrianglePointer(
    isAbove: Boolean = false,
) {
    return Canvas(
        modifier = Modifier
            .size(16.dp)
            .align(Alignment.CenterHorizontally)
    ) {
        val path = Path().apply {
            if (!isAbove) {
                moveTo(size.width / 2, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            } else {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width / 2, size.height)
                close()
            }
        }
        drawPath(path, Color.White, style = Fill)
    }
}
