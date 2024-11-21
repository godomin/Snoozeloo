package com.ykim.snoozeloo.presentation.list

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FabPosition
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ykim.snoozeloo.R
import com.ykim.snoozeloo.presentation.components.ListCard
import com.ykim.snoozeloo.presentation.components.SnoozelooButton
import com.ykim.snoozeloo.presentation.components.SnoozelooFloatingActionButton
import com.ykim.snoozeloo.presentation.model.Alarm
import com.ykim.snoozeloo.presentation.util.hasNotificationPermission
import com.ykim.snoozeloo.presentation.util.shouldShowNotificationRationale
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme

@Composable
fun ListScreenRoot(
    onItemClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    viewModel: ListViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
) {
    ListScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is ListAction.OnAddAlarmClick -> onAddClick()
                is ListAction.OnEditAlarmClick -> onItemClick(action.id)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun ListScreen(
    state: ListState,
    onAction: (ListAction) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        val activity = context as ComponentActivity
        val showRationale = activity.shouldShowNotificationRationale()
        onAction(
            ListAction.SubmitNotificationPermissionInfo(
                showRationale = showRationale
            )
        )
    }

    LaunchedEffect(key1 = Unit) {
        val activity = context as ComponentActivity
        val showRationale = activity.shouldShowNotificationRationale()

        onAction(
            ListAction.SubmitNotificationPermissionInfo(
                showRationale = showRationale
            )
        )

        if (!showRationale) {
            permissionLauncher.requestPermissions(context)
        }
    }

    if (state.shouldShowNotificationRationale) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = stringResource(id = R.string.permission_title)) },
            text = { Text(text = stringResource(id = R.string.notification_rationale)) },
            confirmButton = {
                SnoozelooButton(
                    text = stringResource(id = R.string.okay),
                    onClick = { permissionLauncher.requestPermissions(context) }
                )
            }
        )
    }

    var showRationaleDialog by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = state.isOverlayPermissionGranted) {
        showRationaleDialog = !state.isOverlayPermissionGranted
    }
    if (showRationaleDialog) {
        OverlayPermissionDialog(
            onDismiss = { showRationaleDialog = false }
        )
    }
    Scaffold(
        floatingActionButton = {
            SnoozelooFloatingActionButton(
                icon = ImageVector.vectorResource(id = R.drawable.plus),
                onClick = { onAction(ListAction.OnAddAlarmClick) },
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
            if (state.alarmList.isEmpty() && !state.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.alarm_icon),
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
                    items(
                        items = state.alarmList,
                        key = { alarm -> alarm.id ?: 0 }
                    ) { alarm ->
                        ListCard(
                            modifier = Modifier.clickable {
                                alarm.id?.let { id ->
                                    onAction(ListAction.OnEditAlarmClick(id))
                                }
                            },
                            data = alarm,
                            onToggle = {
                                onAction(ListAction.OnAlarmToggleClick(alarm))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OverlayPermissionDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.overlay_permission_title)) },
        text = { Text(text = stringResource(id = R.string.overlay_permission_description)) },
        confirmButton = {
            SnoozelooButton(
                text = stringResource(id = R.string.grant_permission),
                onClick = {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${context.packageName}")
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                }
            )
        },
        dismissButton = {
            SnoozelooButton(
                text = stringResource(id = R.string.cancel),
                onClick = onDismiss
            )
        }
    )
}

private fun ActivityResultLauncher<String>.requestPermissions(context: Context) {
    val hasPermission = context.hasNotificationPermission()
    if (!hasPermission) {
        launch(android.Manifest.permission.POST_NOTIFICATIONS)
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