package com.ykim.snoozeloo.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ykim.snoozeloo.R
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeableItem(
    isRevealed: Boolean,
    modifier: Modifier = Modifier,
    onActionClicked: () -> Unit = {},
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    content: @Composable () -> Unit
) {
    var menuWidth by remember {
        mutableStateOf(0f)
    }
    var itemWidth by remember {
        mutableStateOf(0f)
    }
    val offset = remember {
        Animatable(initialValue = 0f)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isRevealed, menuWidth) {
        if (isRevealed) {
            offset.animateTo(-menuWidth)
        } else {
            offset.animateTo(0f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color.Red)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { itemWidth = it.width.toFloat() },
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier
                    .onSizeChanged {
                        menuWidth = it.width.toFloat()
                    }
                    .clickable(onClick = onActionClicked),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = Color.Red)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.delete),
                        style = MaterialTheme.typography.bodyMedium
                            .copy(color = Color.White),
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(offset.value.roundToInt(), 0) }
                    .pointerInput(menuWidth) {
                        detectHorizontalDragGestures(
                            onHorizontalDrag = { _, dragAmount ->
                                scope.launch {
                                    val newOffset = (offset.value + dragAmount)
                                        .coerceIn(-menuWidth, 0f)
                                    offset.snapTo(newOffset)
                                }
                            },
                            onDragEnd = {
                                when {
                                    offset.value < -menuWidth / 2 -> {
                                        scope.launch {
                                            offset.animateTo(-menuWidth)
                                            onExpanded()
                                        }
                                    }

                                    else -> {
                                        scope.launch {
                                            offset.animateTo(0f)
                                            onCollapsed()
                                        }
                                    }
                                }
                            }
                        )
                    }
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun SwipeableItemPreview() {
    SnoozelooTheme {
        SwipeableItem(false) {
            Box(
                modifier = Modifier
                    .background(Color.Blue)
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }
    }
}