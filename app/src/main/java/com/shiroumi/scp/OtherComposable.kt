package com.shiroumi.scp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController


@Composable
fun MainPage(navController: NavController, viewModel: MainViewModel) {
    Column {
        val title = viewModel.title.observeAsState("1111111")
        Text(title.value)
        Button(onClick = { navController.navigate("second") }) {
            Text(text = "to second")
        }
    }
}


@Composable
fun SecondPage(navController: NavController) {
    Column {
        Row {
            Text(text = "2222222")
            Button(onClick = { navController.navigate("third") }) {
                Text(text = "to third")
            }
        }
        Row {
            val nextNavController = rememberNavController()
            NavHost(navController = nextNavController, "third/fourth") {
                composable("third/fourth") {
                    FourthPage(navController = nextNavController)
                }
                composable("third/fifth") {
                    FifthPage(navController = nextNavController)
                }
            }
        }
    }
}

@Composable
fun ThirdPage() {
    Text(text = "3333333")
}

@Composable
fun FourthPage(navController: NavController) {
    Text(text = "4444444")
    Button(onClick = {
        navController.popBackStack()
        navController.navigate("third/fifth")
    }) {
        Text(text = "to third/fifth")
    }
}

@Composable
fun FifthPage(navController: NavController) {
    Text(text = "5555555")
    Button(onClick = {
        navController.popBackStack()
        navController.navigate("third/fourth")
    }) {
        Text(text = "to third/fourth")
    }
}