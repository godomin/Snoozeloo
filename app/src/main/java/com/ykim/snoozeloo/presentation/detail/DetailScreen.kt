package com.ykim.snoozeloo.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import com.ykim.snoozeloo.R
import com.ykim.snoozeloo.domain.DaysOfWeek
import com.ykim.snoozeloo.presentation.components.SnoozelooButton
import com.ykim.snoozeloo.presentation.components.SnoozelooChip
import com.ykim.snoozeloo.presentation.components.SnoozelooDialog
import com.ykim.snoozeloo.presentation.util.KEY_RINGTONE_URI
import com.ykim.snoozeloo.presentation.util.addFocusCleaner
import com.ykim.snoozeloo.presentation.util.getNameResourceId
import com.ykim.snoozeloo.presentation.util.selected
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme

@Composable
fun DetailScreenRoot(
    savedStateHandle: SavedStateHandle?,
    onCloseScreen: () -> Unit,
    onRingtoneClick: (String) -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val newRingtoneUri = savedStateHandle?.get<String>(KEY_RINGTONE_URI)
    if (newRingtoneUri != null) {
        viewModel.onAction(DetailAction.OnRingtoneChange(newRingtoneUri))
    }
    DetailScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                DetailAction.OnClose, DetailAction.OnSave -> onCloseScreen()
                is DetailAction.OnRingtoneClick -> onRingtoneClick( viewModel.state.ringtoneUri)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun DetailScreen(
    state: DetailState,
    onAction: (DetailAction) -> Unit
) {
    var showNameDialog by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .padding(padding)
        ) {
            if (showNameDialog) {
                SnoozelooDialog(
                    onDismiss = { showNameDialog = false },
                    onSave = { onAction(DetailAction.OnNameChange(it)) },
                    initialText = state.name
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.remove_rectangle),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.surfaceDim,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onAction(DetailAction.OnClose) }
                )
                SnoozelooButton(
                    text = stringResource(id = R.string.save),
                    enabled = state.isValidTime,
                    onClick = { onAction(DetailAction.OnSave) }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            WhiteCard(
                innerPadding = 24.dp,
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    NumberTextField(
                        text = state.hour,
                        isValid = state.isValidTime,
                        modifier = Modifier.weight(1f),
                        onValueChanged = { onAction(DetailAction.OnHourChange(it)) }
                    )
                    Column(
                        modifier = Modifier
                            .width(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ClockDot()
                        Spacer(modifier = Modifier.height(5.dp))
                        ClockDot()
                    }
                    NumberTextField(
                        text = state.minute,
                        isValid = state.isValidTime,
                        modifier = Modifier.weight(1f),
                        onValueChanged = { onAction(DetailAction.OnMinuteChange(it)) }
                    )
                }
                if (state.isValidTime) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = state.timeLeft,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            WhiteCard(
                innerPadding = 16.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        focusManager.clearFocus()
                        showNameDialog = true
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.alarm_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = state.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            WhiteCard(
                innerPadding = 16.dp,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(id = R.string.repeat),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    DaysOfWeek.entries.forEach { day ->
                        SnoozelooChip(
                            text = stringResource(id = day.getNameResourceId()),
                            selected = state.enabledDays.selected(day),
                            onClick = { onAction(DetailAction.OnDayChange(day)) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            WhiteCard(
                innerPadding = 16.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAction(DetailAction.OnRingtoneClick(state.ringtoneUri)) },
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.alarm_ringtone),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = state.ringtoneTitle,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun WhiteCard(
    innerPadding: Dp = 0.dp,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    composable: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(innerPadding),
        horizontalAlignment = horizontalAlignment
    ) {
        composable()
    }
}

@Composable
fun NumberTextField(
    text: String,
    isValid: Boolean = false,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(text))
    }
    LaunchedEffect(key1 = text) {
        textFieldValue = textFieldValue.copy(text = text)
    }
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        val tmp = textFieldValue.text
                        textFieldValue = textFieldValue.copy(
                            selection = TextRange(0, tmp.length)
                        )
                    } else {
                        when (text.length) {
                            0 -> onValueChanged("00")
                            1 -> onValueChanged("0$text")
                            else -> Unit
                        }
                    }
                },
            value = textFieldValue,
            onValueChange = {
                if (it.text.length <= 2) {
                    textFieldValue = it
                    onValueChanged(it.text)
                }
            },
            textStyle = MaterialTheme.typography.displayLarge
                .copy(
                    color = if (isValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inverseSurface,
                    textAlign = TextAlign.Center
                ),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = { innerTextField ->
                innerTextField()
            }
        )
    }
}

@Composable
fun ClockDot(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(4.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.inverseSurface)
    )
}

@Preview(widthDp = 360, heightDp = 748)
@Composable
private fun DetailScreenPreview() {
    SnoozelooTheme {
        DetailScreen(
            state = DetailState(
                hour = "10",
                minute = "00",
                name = "Work",
                isValidTime = true,
                enabledDays = 0b0110101,
                timeLeft = "Alarm in 3h 35m",
                ringtoneTitle = "Default"
            ),
            onAction = {}
        )
    }
}