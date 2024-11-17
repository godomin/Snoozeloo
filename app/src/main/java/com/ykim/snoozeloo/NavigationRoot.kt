package com.ykim.snoozeloo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ykim.snoozeloo.presentation.detail.DetailScreenRoot
import com.ykim.snoozeloo.presentation.list.ListScreenRoot
import com.ykim.snoozeloo.presentation.model.Alarm
import kotlinx.serialization.Serializable

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = List) {
        composable<List> {
            ListScreenRoot(
                onItemClick = { alarm ->
                    navController.navigate(Detail(alarm))
                },
                onAddClick = {
                    navController.navigate(Detail(null))
                }
            )
        }
        composable<Detail> {
            DetailScreenRoot()
        }
        // TODO:
    }
}

@Serializable
object List
@Serializable
data class Detail(
    val alarm: Alarm?
)
@Serializable
object Trigger