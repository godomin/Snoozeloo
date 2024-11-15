package com.ykim.snoozeloo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ykim.snoozeloo.domain.model.AlarmData
import com.ykim.snoozeloo.presentation.list.ListAction
import com.ykim.snoozeloo.presentation.model.Alarm
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme

@Composable
fun ListCard(
    modifier: Modifier = Modifier,
    data: Alarm,
    onToggle: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
        ) {
            if (data.name != null) {
                Text(
                    text = data.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = data.time,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.alignBy(FirstBaseline)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = data.period,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.alignBy(FirstBaseline)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data.timeLeft,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        SnoozelooSwitch(checked = data.enabled, onToggle = onToggle)
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xff000000,
    widthDp = 328
)
@Composable
private fun ListCardPreview() {
    SnoozelooTheme {
        ListCard(
            data = Alarm(
                "Wake Up",
                "10:00",
                "AM",
                "Alarm in 30min",
                true
            )
        )
    }
}