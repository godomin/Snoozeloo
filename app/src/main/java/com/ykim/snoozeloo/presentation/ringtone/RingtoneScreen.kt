package com.ykim.snoozeloo.presentation.ringtone

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ykim.snoozeloo.R
import com.ykim.snoozeloo.presentation.model.Ringtone
import com.ykim.snoozeloo.presentation.util.getUri
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme

@Composable
fun RingtoneScreenRoot(
    onBackPress: (String) -> Unit,
    viewModel: RingtoneViewModel = hiltViewModel(),
) {
    RingtoneScreen(
        state = viewModel.state,
        onAction = {action ->
            when (action) {
                is RingtoneAction.OnBackPress -> { onBackPress(viewModel.state.selectedUri) }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun RingtoneScreen(
    state: RingtoneState,
    onAction: (RingtoneAction) -> Unit
) {
    BackHandler {
        onAction(RingtoneAction.OnBackPress)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.back_icon),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onAction(RingtoneAction.OnBackPress) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = state.ringtoneList,
                    key = { it.getUri() }
                ) { ringtone ->
                    RingtoneItem(
                        ringtone = ringtone,
                        isSelected = state.selectedUri == ringtone.getUri(),
                        onClick = { onAction(RingtoneAction.OnItemClick(ringtone)) }
                    )
                }
            }
        }
    }
}

@Composable
fun RingtoneItem(
    ringtone: Ringtone,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.onPrimary)
            .clickable(onClick = onClick)
            .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val (iconId, title) = when (ringtone) {
            is Ringtone.Normal -> R.drawable.icon_ringing to ringtone.title
            Ringtone.Silent -> R.drawable.icon_silent to stringResource(id = R.string.silent)
        }
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = iconId),
                contentDescription = "",
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.weight(1f).padding(end = 6.dp)
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        } else {
            Box(modifier = Modifier.size(18.dp))
        }
    }
}

@Preview(widthDp = 360, heightDp = 748)
@Composable
private fun RingtoneScreenPreview() {
    SnoozelooTheme {
        RingtoneScreen(
            state = RingtoneState(
                ringtoneList = listOf(
                    Ringtone.Silent,
                    Ringtone.Normal("Default(Bright Morning)", "uri1"),
                    Ringtone.Normal("Cuckoo Clock", "uri2"),
                    Ringtone.Normal("Over the Horizon 2022 produced by SUGA of BTS", "uri3"),
                ),
                selectedUri = "uri3"
            ),
            onAction = {}
        )
    }
}