package org.d3if3062.mobpro1.asessmen3.system.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.d3if3062.mobpro1.asessmen3.system.database.SystemViewModel
import org.d3if3062.mobpro1.asessmen3.system.database.model.ApiProfile
import org.d3if3062.mobpro1.asessmen3.ui.screen.PublicChat
import org.d3if3062.mobpro1.asessmen3.ui.screen.ScreenContent

@Composable
fun NavigationGraph(navController: NavHostController, apiProfile: List<ApiProfile>, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Base.route
    ) {
        /*----------------[Main Route]----------------*/

        composable(route = Screen.Base.route) {
            PublicChat(
                systemViewModel = SystemViewModel(),
                apiProfile = apiProfile,
                modifier
            )
//            ScreenContent(
//                name = Screen.Base.route,
//                onClick = { },
//                viewModel = SystemViewModel()
//            )
        }
//        composable(route = Screen.History.route) {
//            ScreenContent(
//                name = Screen.Task.route,
//                onClick = { }
//            )
//        }
    }
}