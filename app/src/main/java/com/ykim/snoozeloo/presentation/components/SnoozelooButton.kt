package com.ykim.snoozeloo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme

@Composable
fun SnoozelooButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .background(if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceDim)
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall
                .copy(color = MaterialTheme.colorScheme.onPrimary)
        )
    }
}

@Preview
@Composable
private fun SnoozelooButtonPreview() {
    SnoozelooTheme {
        SnoozelooButton("Save", true, {})
    }
}