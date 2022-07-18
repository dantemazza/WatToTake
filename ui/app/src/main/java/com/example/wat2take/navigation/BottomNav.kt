package com.example.wat2take.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wat2take.Global.Companion.WATERLOO_YELLOW

@Composable
fun BottomNav(navController: NavController) {
    val navItems = listOf(BottomNavItem.MyCourses, BottomNavItem.MyCourseRecs, BottomNavItem.UploadTranscript)
    BottomNavigation(
        backgroundColor = WATERLOO_YELLOW
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        navItems.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painter = painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 10.5.sp)},
                selected = currentRoute == item.navRoute,
                onClick = {
                    navController.navigate(item.navRoute)
                })
            }
        }
    }