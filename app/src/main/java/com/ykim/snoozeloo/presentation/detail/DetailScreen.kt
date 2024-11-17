package com.ykim.snoozeloo.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ykim.snoozeloo.Detail
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme

@Composable
fun DetailScreenRoot(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    DetailScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun DetailScreen(
    state: DetailState,
    onAction: (DetailAction) -> Unit
) {

}

@Preview
@Composable
private fun DetailScreenPreview() {
    SnoozelooTheme {
        DetailScreen(
            state = DetailState(),
            onAction = {}
        )
    }
}