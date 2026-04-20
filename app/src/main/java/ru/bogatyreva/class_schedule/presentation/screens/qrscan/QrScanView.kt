package ru.bogatyreva.class_schedule.presentation.screens.qrscan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.bogatyreva.class_schedule.R
import ru.bogatyreva.class_schedule.presentation.ui.theme.BlueSelected
import ru.bogatyreva.class_schedule.presentation.ui.theme.Transparent
import ru.bogatyreva.class_schedule.presentation.ui.theme.Transparent60
import ru.bogatyreva.class_schedule.presentation.ui.theme.White

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun PreviewQrScanView() {
    QrScanView()
}

@Composable
fun QrScanView(
    isDetectedQrCode: (row: String) -> Unit = {},
    onClickCancel: () -> Unit = {},
) {
    var torch by remember { mutableStateOf(false) }

    var state by remember { mutableStateOf<QrScanState>(QrScanState.Scanning) } // пока что не нужно
    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = Transparent
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // для верстки дизайна нужно закоментировать CameraView
            CameraView(result = { row ->
                val list = row.replace('\n', ' ').split(' ')
                list.forEach {
                    isDetectedQrCode(it)
                }
            }, torch = torch)

            QRScannerFrameWithCorners(
                transparentColor = Transparent60, //цвет заливки
                frameColor = White // цвет рамок сканнера
            )

            IconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 30.dp),
                onClick = {
                    torch = !torch
                }) {
                Icon(
                    painter = painterResource(R.drawable.ic_flash_cam),
                    contentDescription = null,
                    tint = White
                )
            }

            TextButton(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 150.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Transparent
                ),
                onClick = { onClickCancel() }) {
                Text(
                    text = stringResource(R.string.qr_scan_cancel_button_text),
                    fontSize = 20.sp,
                    color = White
                )
            }
        }
    }
}