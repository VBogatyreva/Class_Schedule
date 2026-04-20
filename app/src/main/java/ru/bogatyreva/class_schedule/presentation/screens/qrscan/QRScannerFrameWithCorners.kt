package ru.bogatyreva.class_schedule.presentation.screens.qrscan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.bogatyreva.class_schedule.presentation.ui.theme.Transparent60
import ru.bogatyreva.class_schedule.presentation.ui.theme.White

@Preview(showBackground = false)
@Composable
private fun PreviewQRScannerFrameWithCorners() {
    QRScannerFrameWithCorners(transparentColor = Transparent60)
}

@Composable
fun QRScannerFrameWithCorners(
    modifier: Modifier = Modifier,
    frameColor: Color = White,
    cornerSize: Dp = 32.dp,
    cornerStrokeWidth: Dp = 6.dp,
    transparentColor: Color = Color(0x00000000)
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawWithContent {
                drawContent()

                val frameSize = size.width * 0.8f
                val centerX = size.width / 2
                val centerY = size.height / 2

                val cornerLength = cornerSize.toPx()
                val strokeWidth = cornerStrokeWidth.toPx()

                // Координаты углов рамки
                val left = centerX - frameSize / 2
                val top = centerY - frameSize / 2
                val right = centerX + frameSize / 2
                val bottom = centerY + frameSize / 2

                //затемнение пустого пространства
                drawLine(
                    color = transparentColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = top * 2 - strokeWidth
                )

                drawLine(
                    color = transparentColor,
                    start = Offset(0f, top - (strokeWidth / 2)),
                    end = Offset(0f, bottom + (strokeWidth / 2)),
                    strokeWidth = left * 2 - strokeWidth
                )

                drawLine(
                    color = transparentColor,
                    start = Offset(size.width, top - (strokeWidth / 2)),
                    end = Offset(size.width, bottom + (strokeWidth / 2)),
                    strokeWidth = left * 2 - strokeWidth
                )

                drawLine(
                    color = transparentColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = top * 2 - strokeWidth
                )


                // Верхний левый угол
                drawLine(
                    color = frameColor,
                    start = Offset(left, top),
                    end = Offset(left + cornerLength, top),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = frameColor,
                    start = Offset(left, top),
                    end = Offset(left, top + cornerLength),
                    strokeWidth = strokeWidth
                )

                // Верхний правый угол
                drawLine(
                    color = frameColor,
                    start = Offset(right, top),
                    end = Offset(right - cornerLength, top),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = frameColor,
                    start = Offset(right, top),
                    end = Offset(right, top + cornerLength),
                    strokeWidth = strokeWidth
                )

                // Нижний левый угол
                drawLine(
                    color = frameColor,
                    start = Offset(left, bottom),
                    end = Offset(left + cornerLength, bottom),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = frameColor,
                    start = Offset(left, bottom),
                    end = Offset(left, bottom - cornerLength),
                    strokeWidth = strokeWidth
                )

                // Нижний правый угол
                drawLine(
                    color = frameColor,
                    start = Offset(right, bottom),
                    end = Offset(right - cornerLength, bottom),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = frameColor,
                    start = Offset(right, bottom),
                    end = Offset(right, bottom - cornerLength),
                    strokeWidth = strokeWidth
                )
            })
}