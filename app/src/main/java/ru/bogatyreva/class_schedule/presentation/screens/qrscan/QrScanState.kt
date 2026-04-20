package ru.bogatyreva.class_schedule.presentation.screens.qrscan

sealed class QrScanState {
    data object Scanning : QrScanState()
    data object Error : QrScanState()
    data object Wait : QrScanState()
    data object Success: QrScanState()
}