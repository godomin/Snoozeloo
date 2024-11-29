package com.ykim.snoozeloo.presentation.ringtone

import android.content.Context
import android.media.RingtoneManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ykim.snoozeloo.presentation.navigation.RingtoneScreen
import com.ykim.snoozeloo.presentation.model.Ringtone
import com.ykim.snoozeloo.presentation.util.getUri
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RingtoneViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(RingtoneState())
        private set

    init {
        viewModelScope.launch {
            val ringtone = savedStateHandle.toRoute<RingtoneScreen>().ringtoneUri
            val ringtoneList = withContext(Dispatchers.IO) {
                getRingtones(context)
            }
            state = state.copy(
                ringtoneList = ringtoneList,
                selectedUri = ringtone,
            )
        }
    }

    fun onAction(action: RingtoneAction) {
        when (action) {
            is RingtoneAction.OnItemClick -> {
                state = state.copy(selectedUri = action.ringtone.getUri())
            }
            else -> Unit
        }
    }

    private fun getRingtones(context: Context): List<Ringtone> {
        val list = mutableListOf<Ringtone>(Ringtone.Silent)
        val ringtoneManager = RingtoneManager(context).apply {
            setType(RingtoneManager.TYPE_RINGTONE)
        }
        val cursor = ringtoneManager.cursor
        while (cursor.moveToNext()) {
            val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
            val uri = ringtoneManager.getRingtoneUri(cursor.position)
            list.add(Ringtone.Normal(title, uri.toString()))
        }
        cursor.close()
        return list
    }
}