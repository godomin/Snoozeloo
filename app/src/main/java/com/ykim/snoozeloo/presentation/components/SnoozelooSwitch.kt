package com.ykim.snoozeloo.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme

@Composable
fun SnoozelooSwitch(
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val thumbPosition by animateDpAsState(
        targetValue = if (checked) 21.dp else 0.dp,
        label = "ThumbPositionAnimation"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
            .size(width = 51.dp, height = 30.dp)
            .clickable(onClick = onToggle)
            .padding(2.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbPosition)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary)
                .size(26.dp),
            )
    }
}

@Preview
@Composable
private fun SnoozelooSwitchPreview() {
    SnoozelooTheme {
        var checked by remember {
            mutableStateOf(false)
        }
        SnoozelooSwitch(checked = checked, onToggle = {
            checked = !checked
        })
    }
}