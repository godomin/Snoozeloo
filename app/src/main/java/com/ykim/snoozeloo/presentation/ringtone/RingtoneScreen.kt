package com.ykim.snoozeloo.presentation.ringtone

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ykim.snoozeloo.presentation.components.SnoozelooButton
import com.ykim.snoozeloo.presentation.model.Ringtone
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme

@Composable
fun RingtoneScreenRoot(
    onBackPress: (Ringtone) -> Unit,
    viewModel: RingtoneViewModel = hiltViewModel(),
) {
    RingtoneScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun RingtoneScreen(
    state: RingtoneState,
    onAction: (RingtoneAction) -> Unit
) {

}

@Preview
@Composable
private fun RingtoneScreenPreview() {
    SnoozelooTheme {
        RingtoneScreen(
            state = RingtoneState(),
            onAction = {}
        )
    }
}