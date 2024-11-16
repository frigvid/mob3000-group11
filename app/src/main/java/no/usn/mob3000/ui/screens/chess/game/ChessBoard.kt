package no.usn.mob3000.ui.screens.chess.game

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
import no.usn.mob3000.domain.utils.Logger
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
import no.usn.mob3000.domain.model.game.board.ChessResources
import no.usn.mob3000.domain.viewmodel.game.ChessBoardViewModel
import no.usn.mob3000.domain.model.game.board.DraggedPiece
import no.usn.mob3000.ui.theme.DefaultListItemBackground

// FIX: This is cursed. Common now.
val ChessboardCellLight = Color(0xFFf0d9b5)
val ChessboardCellDark = Color(0xFFb58863)
val LegalMoveHighlight = Color(0x668BC34A)

/**
 * The cursedest chessboard.
 *
 * @param modifier The modifier.
 * @param startingPosition FEN notation for the chess board's default piece starting position.
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
    // FIX: Replace with standard method.
    // NOTE: Debugging strange behavior.
    viewModel: ChessBoardViewModel = ChessBoardViewModel(),
    gameMode: GameMode = GameMode.HUMAN_VS_HUMAN,
    gameVariation: VariationType = VariationType.NORMAL,
    gameHistory: Boolean = true,
    gameInteractable: Boolean = true
) {
    // TODO: Implement better board setting than this atrocity.
    LaunchedEffect(startingPosition) {
        viewModel.onEvent(ChessBoardEvent.InitializeBoard(startingPosition))
    }

    val boardState by viewModel.boardState.collectAsState()
    var boardSize by remember { mutableFloatStateOf(0f) }
    val cellSize = boardSize / 8

    val textMeasurer = rememberTextMeasurer()

    val piecePainters = ChessResources.pieceToResourceMap.mapValues { (_, resourceId) ->
        painterResource(id = resourceId)
    }



    // NOTE: Separated from the rest of the code, somewhat, to indicate its testy nature.
    val lifecycleOwner = LocalLifecycleOwner.current
    var dragPiece by remember { mutableStateOf<DraggedPiece?>(null) }
    var promotionState by remember { mutableStateOf<ChessBoardViewModel.PromotionState?>(null) }

    DisposableEffect(lifecycleOwner) {
        val observer = Observer<ChessBoardViewModel.PromotionState?> { state ->
            promotionState = state
        }

        val observer2 = Observer<DraggedPiece?> { state ->
            dragPiece = state
        }

        viewModel.promotionLiveData.observe(lifecycleOwner, observer)
        Logger.i("Promotion state observer initialized!")
        viewModel.draggedPieceLiveData.observe(lifecycleOwner, observer2)
        Logger.i("Dragged piece state observer initialized! $dragPiece")

        onDispose {
            viewModel.promotionLiveData.removeObserver(observer)
            Logger.i("Promotion state observer destroyed!")

            viewModel.draggedPieceLiveData.removeObserver(observer2)
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
                            viewModel.onEvent(ChessBoardEvent.OnPieceDragStart(square, offset.x, offset.y))
                        }
                    },
                    onDrag = { change, _ ->
                        Logger.v(message = "Drag gesture update at ${change.position}")
                        viewModel.onEvent(
                            ChessBoardEvent.OnPieceDragged(
                            change.position.x,
                            change.position.y
                        ))
                    },
                    onDragEnd = {
                        Logger.d(message = "Drag gesture ended")
                        // TODO: Remove when done testing.
                        //val draggedPiece = boardState.draggedPiece
                        //if (draggedPiece != null) {
                        //    val file = (draggedPiece.currentX / cellSize).toInt().coerceIn(0, 7)
                        //    val rank = 7 - (draggedPiece.currentY / cellSize).toInt().coerceIn(0, 7)
                        //    val toSquare = Square.encode(
                        //        Rank.allRanks[rank],
                        //        File.allFiles[file]
                        //    )
                        //    Logger.d(message = "Drag ended at square $toSquare")
                        //    viewModel.onEvent(ChessBoardEvent.OnPieceDragEnd(toSquare))
                        //} else {
                        //    Logger.e(message = "Drag ended but no dragged piece found")
                        //}

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
                            if (toSquare != null) {
                                viewModel.setBoardPieceDraggedToSquare(toSquare)
                            }
                            viewModel.onEvent(ChessBoardEvent.OnPieceDragEnd(toSquare))
                        } else {
                            Logger.e(message = "Drag ended but no dragged piece found")
                        }
                    }
                )
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
                    val textLayoutResult = textMeasurer.measure(
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
                    if (boardState.legalMoves.contains(square)) {
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
                        val painter = piecePainters[piece]
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

            /* Draw the piece's new location. */
            boardState.draggedPiece?.let { draggedPiece ->
                val painter = piecePainters[draggedPiece.piece]
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

        promotionState?.let { state ->
            PromotionDialog(
                square = viewModel.boardPieceDraggedToSquare.value,
                offset = state.offset,
                side = state.side,
                onPieceSelected = { piece ->
                    viewModel.onEvent(ChessBoardEvent.OnPromotionPieceSelected(piece))
                }
            )
        }
    }
}

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

@Composable
private fun PromotionDialog(
    square: Square,
    offset: Offset,
    side: Side,
    onPieceSelected: (Piece) -> Unit
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
                TrianglePointer(true)
                TrianglePointer()
                //Canvas(modifier = Modifier
                //    .size(16.dp)
                //    .align(Alignment.CenterHorizontally)
                //) {
                //    val path = Path().apply {
                //        moveTo(size.width / 2, 0f)
                //        lineTo(size.width, size.height)
                //        lineTo(0f, size.height)
                //        close()
                //    }
                //    drawPath(path, Color.White, style = Fill)
                //}

                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    promotionPieces.forEach { piece ->
                        Image(
                            painter = painterResource(ChessResources.pieceToResourceMap[piece]!!),
                            contentDescription = piece.name,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable {
                                    onPieceSelected(piece)
                                }
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ColumnScope.TrianglePointer(
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
