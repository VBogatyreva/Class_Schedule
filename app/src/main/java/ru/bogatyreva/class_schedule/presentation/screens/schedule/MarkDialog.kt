package ru.bogatyreva.class_schedule.presentation.screens.schedule

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.bogatyreva.class_schedule.R
import ru.bogatyreva.class_schedule.presentation.ui.theme.BlueSelected
import ru.bogatyreva.class_schedule.presentation.ui.theme.MonthText
import ru.bogatyreva.class_schedule.presentation.ui.theme.TitleText

@Preview
@Composable
private fun PreviewMarkDialog() {
    MarkDialog()
}

@Composable
fun MarkDialog(
    onClickConfirm: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = { onClickConfirm() },
        confirmButton = {
            TextButton(onClick = { onClickConfirm() }) {
                Text(
                    text = stringResource(R.string.confirm_button_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = BlueSelected
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.mark_dialog_title),
                style = MaterialTheme.typography.titleLarge,
                color = TitleText
            )
        },
        text = {
            Text(
                text = stringResource(R.string.mark_dialog_description),
                color = MonthText,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    )
}
