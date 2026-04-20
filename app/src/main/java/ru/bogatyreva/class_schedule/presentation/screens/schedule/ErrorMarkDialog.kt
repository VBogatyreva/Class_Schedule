package ru.bogatyreva.class_schedule.presentation.screens.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.bogatyreva.class_schedule.R

@Preview
@Composable
private fun PreviewErrorMarkDialog() {
    ErrorMarkDialog()
}

@Composable
fun ErrorMarkDialog(
    onClickConfirm: () -> Unit = {},
    onClickDismiss: () -> Unit = {}
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.title_error_dialog),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = stringResource(R.string.description_error_dialog),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        onDismissRequest = { onClickDismiss() },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    Modifier
                        .width(1.dp)
                        .weight(1f)
                )
                Text(
                    modifier = Modifier.clickable { onClickConfirm() },
                    text = stringResource(R.string.ok_text_button_error_dialog),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    modifier = Modifier.clickable { onClickDismiss() },
                    text = stringResource(R.string.repeat_text_button_error_dialog),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
    )
}