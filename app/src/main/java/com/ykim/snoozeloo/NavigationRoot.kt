package com.ykim.snoozeloo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ykim.snoozeloo.presentation.detail.DetailScreenRoot
import com.ykim.snoozeloo.presentation.list.ListScreenRoot
import com.ykim.snoozeloo.presentation.ringtone.RingtoneScreenRoot
import com.ykim.snoozeloo.presentation.util.KEY_RINGTONE_URI
import kotlinx.serialization.Serializable

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = ListScreen) {
        composable<ListScreen> {
            ListScreenRoot(
                onItemClick = { id ->
                    navController.navigate(DetailScreen(id))
                },
                onAddClick = {
                    navController.navigate(DetailScreen(null))
                }
            )
        }
        composable<DetailScreen> {
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

@Serializable
object ListScreen

@Serializable
data class DetailScreen(
    val id: Int?,
)

@Serializable
data class RingtoneScreen(
    val ringtoneUri: String
)