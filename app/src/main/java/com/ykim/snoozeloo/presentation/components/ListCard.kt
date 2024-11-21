package com.ykim.snoozeloo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ykim.snoozeloo.domain.DaysOfWeek
import com.ykim.snoozeloo.presentation.model.Alarm
import com.ykim.snoozeloo.presentation.util.selected
import com.ykim.snoozeloo.presentation.util.getNameResourceId
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListCard(
    modifier: Modifier = Modifier,
    data: Alarm,
    onToggle: () -> Unit = {},
    onDaySelected: (DaysOfWeek) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(16.dp),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            DaysOfWeek.entries.forEach { day ->
                SnoozelooChip(
                    text = stringResource(id = day.getNameResourceId()),
                    selected = data.enabledDays.selected(day),
                    onClick = { onDaySelected(day) }
                )
            }
        }
        if (data.bedTimeLeft.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = data.bedTimeLeft,
                style = MaterialTheme.typography.bodyMedium
            )
        }
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
                "",
                true,
                0b0110101,
                ""
            )
        )
    }
}