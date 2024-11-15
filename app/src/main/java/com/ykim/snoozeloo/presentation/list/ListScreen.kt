package com.ykim.snoozeloo.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ykim.snoozeloo.R
import com.ykim.snoozeloo.presentation.components.ListCard
import com.ykim.snoozeloo.presentation.components.SnoozelooFloatingActionButton
import com.ykim.snoozeloo.presentation.model.Alarm
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme

@Composable
fun ListScreenRoot(
    navController: NavController,
    viewModel: ListViewModel = hiltViewModel(),
) {
    ListScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ListScreen(
    state: ListState,
    onAction: (ListAction) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            SnoozelooFloatingActionButton(
                icon = ImageVector.vectorResource(id = R.drawable.plus),
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(bottom = 12.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.alarm_list_title),
                style = MaterialTheme.typography.headlineMedium
            )
            if (state.alarmList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.alarm),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(62.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = stringResource(id = R.string.alarm_list_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(24.dp))
                LazyColumn(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(state.alarmList) { index, alarm ->
                        ListCard(
                            data = alarm
                        )
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 748)
@Composable
private fun ListScreenPreview() {
    SnoozelooTheme {
        ListScreen(
            state = ListState(
                alarmList = listOf(
                    Alarm(
                        "Wake Up",
                        "10:00",
                        "AM",
                        "Alarm in 30min",
                        true
                    ),
                    Alarm(
                        "Education",
                        "04:00",
                        "PM",
                        "Alarm in 5h 30min",
                        false
                    )
                )
            ),
            onAction = {}
        )
    }
}