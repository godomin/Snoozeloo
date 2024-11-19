package com.ykim.snoozeloo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ykim.snoozeloo.R
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme

@Composable
fun SnoozelooDialog(
    modifier: Modifier = Modifier,
    initialText: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = initialText,
                selection = TextRange(initialText.length)
            )
        )
    }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
    Dialog(
        onDismiss,
        DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = stringResource(id = R.string.alarm_name),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.surfaceDim,
                            RoundedCornerShape(4.dp)
                        )

                ) {
                    BasicTextField(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 10.dp)
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        value = textFieldValueState,
                        onValueChange = { textFieldValueState = it },
                        textStyle = MaterialTheme.typography.bodyMedium
                            .copy(color = MaterialTheme.colorScheme.onSurface),
                        decorationBox = { innerTextField ->
                            innerTextField()
                        }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                SnoozelooButton(
                    modifier = Modifier
                        .align(Alignment.End),
                    text = stringResource(id = R.string.save),
                    onClick = {
                        onSave(textFieldValueState.text)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun SnoozelooDialogPreview() {
    SnoozelooTheme {
        SnoozelooDialog(onDismiss = {}, onSave = {}, initialText = "Work")
    }
}