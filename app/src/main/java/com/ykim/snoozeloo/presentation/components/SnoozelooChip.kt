package com.ykim.snoozeloo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
fun SnoozelooChip(
    modifier: Modifier = Modifier,
    text: String = "",
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(38.dp)
            .height(26.dp)
            .clip(RoundedCornerShape(38.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
       Text(
           text = text,
           style = MaterialTheme.typography.bodySmall.copy(
               color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
           ),
       )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun SnoozelooChipPreview() {
    SnoozelooTheme {
        FlowRow {
            SnoozelooChip(text="Mo", selected =  true, onClick = {})
            Spacer(modifier = Modifier.width(5.dp))
            SnoozelooChip(text="Tu", selected =  false, onClick = {})
        }
    }
}