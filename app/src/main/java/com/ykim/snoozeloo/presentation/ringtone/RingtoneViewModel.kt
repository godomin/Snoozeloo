package com.ykim.snoozeloo.presentation.ringtone

import android.content.Context
import android.media.RingtoneManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.ykim.snoozeloo.RingtoneScreen
import com.ykim.snoozeloo.presentation.model.Ringtone
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RingtoneViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(RingtoneState())
        private set

    init {
        val selectedUri = savedStateHandle.toRoute<RingtoneScreen>().ringtoneUri
        state = state.copy(selectedUri = selectedUri)
        getRingtones(context)
    }

    private fun getRingtones(context: Context) {
        val list = mutableListOf<Ringtone>()
        val ringtoneManager = RingtoneManager(context).apply {
            setType(RingtoneManager.TYPE_RINGTONE)
        }
        val cursor = ringtoneManager.cursor
        while (cursor.moveToNext()) {
            val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
            val uri = ringtoneManager.getRingtoneUri(cursor.position)
            list.add(Ringtone(title, uri.toString()))
        }
        cursor.close()
        state = state.copy(ringtoneList = list)
    }
}