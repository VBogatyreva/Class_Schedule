package ru.bogatyreva.class_schedule.presentation.screens.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.bogatyreva.class_schedule.R
import ru.bogatyreva.class_schedule.presentation.ui.theme.ActiveBackgroundColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.LessonTimeText
import ru.bogatyreva.class_schedule.presentation.ui.theme.ScanBackground
import ru.bogatyreva.class_schedule.presentation.ui.theme.White

@Preview
@Composable
private fun PreviewMarkView() {
    MarkView()
}

@Composable
fun MarkView() {
    Dialog(onDismissRequest = {}) {
        Surface(
            color = White,
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    color = ActiveBackgroundColor,
                    trackColor = ScanBackground
                )
                Text(
                    text = stringResource(R.string.mark_view_text),
                    color = LessonTimeText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}