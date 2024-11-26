package com.ykim.snoozeloo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ykim.snoozeloo.presentation.detail.DetailAction
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnoozelooSlider(
    value: Int = 0,
    onValueChange: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .padding(start = 4.dp)
                .width(20.dp)
                .height(6.dp)
                .clip(CircleShape)
                .align(Alignment.CenterStart)
                .background(color = MaterialTheme.colorScheme.primary),
            )
        Box(
            modifier = Modifier
                .padding(end = 4.dp)
                .width(20.dp)
                .height(6.dp)
                .clip(CircleShape)
                .align(Alignment.CenterEnd)
                .background(color = MaterialTheme.colorScheme.secondaryContainer),
        )
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Slider(
                modifier = Modifier.padding(top = (0.5).dp),
                value = value.toFloat(),
                onValueChange = { onValueChange(it.toInt()) },
                valueRange = 0f..100f,
                thumb = {
                    Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                },
                track = { sliderState ->
                    val fraction by remember {
                        derivedStateOf {
                            (sliderState.value - sliderState.valueRange.start) /
                                    (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth(fraction)
                                .height(6.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                )
                        )
                        Box(
                            Modifier
                                .fillMaxWidth(1f)
                                .height(6.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                )
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun SnoozelooSliderPreview() {
    SnoozelooTheme {
        SnoozelooSlider()
    }
}