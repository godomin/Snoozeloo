package com.ykim.snoozeloo.presentation.trigger

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ykim.snoozeloo.R
import com.ykim.snoozeloo.presentation.components.SnoozelooButton
import com.ykim.snoozeloo.presentation.components.SnoozelooOutlinedButton
import com.ykim.snoozeloo.presentation.util.ObserveAsEvents
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme
import java.util.Locale

@Composable
fun TriggerScreenRoot(
    alarmId: Int,
    alarmTime: String,
    alarmName: String,
    ringtoneUri: String,
    viewModel: TriggerViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is TriggerEvent.FinishScreen -> {
                (context as? Activity)?.finishAffinity()
            }
        }
    }
    LaunchedEffect(alarmId, alarmTime, alarmName) {
        viewModel.setInitialData(alarmId, alarmTime, alarmName)
        viewModel.ringAlarm(ringtoneUri)
    }
    TriggerScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun TriggerScreen(
    state: TriggerState,
    onAction: (TriggerAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.alarm_icon),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(62.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = state.time,
            style = MaterialTheme.typography.displayLarge
                .copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 82.sp
                )
        )
        if (state.name.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = state.name.uppercase(Locale.ENGLISH),
                style = MaterialTheme.typography.headlineMedium
                    .copy(fontWeight = FontWeight.SemiBold)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        SnoozelooButton(
            text = stringResource(id = R.string.turn_off),
            onClick = { onAction(TriggerAction.OnTurnOff) },
            fontStyle = MaterialTheme.typography.headlineMedium
                .copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.width(271.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        SnoozelooOutlinedButton(
            text = stringResource(id = R.string.snooze_for_5_minutes),
            onClick = { onAction(TriggerAction.OnSnooze) },
            fontStyle = MaterialTheme.typography.headlineMedium
                .copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.width(271.dp)
        )
    }
}

@Preview
@Composable
private fun TriggerScreenPreview() {
    SnoozelooTheme {
        TriggerScreen(
            state = TriggerState(
                time = "10:00",
                name = "Work"
            ),
            onAction = {}
        )
    }
}