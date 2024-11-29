package com.ykim.snoozeloo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ykim.snoozeloo.presentation.detail.DetailScreenRoot
import com.ykim.snoozeloo.presentation.list.ListScreenRoot
import com.ykim.snoozeloo.presentation.model.Alarm
import com.ykim.snoozeloo.presentation.model.Ringtone
import com.ykim.snoozeloo.presentation.ringtone.RingtoneScreenRoot
import com.ykim.snoozeloo.presentation.util.KEY_RINGTONE_URI
import kotlin.reflect.typeOf

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = ListScreen) {
        composable<ListScreen> {
            ListScreenRoot(
                onItemClick = { alarm ->
                    navController.navigate(DetailScreen(alarm))
                },
                onAddClick = {
                    navController.navigate(DetailScreen(null))
                }
            )
        }
        composable<DetailScreen>(
            typeMap = mapOf(typeOf<Alarm?>() to AlarmType)
        ) {
            DetailScreenRoot(
                navController,
                onCloseScreen = {
                    navController.navigateUp()
                },
                onRingtoneClick = { ringtoneUri ->
                    navController.navigate(RingtoneScreen(ringtoneUri))
                }
            )
        }
        composable<RingtoneScreen> {
            RingtoneScreenRoot(
                onBackPress = { ringtoneUri ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        KEY_RINGTONE_URI,
                        ringtoneUri
                    )
                    navController.popBackStack<DetailScreen>(false)
                }
            )
        }
    }
}