package com.shiroumi.shiroplayer.composable.main

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shiroumi.shiroplayer.Route
import com.shiroumi.shiroplayer.composable.activity
import com.shiroumi.shiroplayer.composable.common.Loading
import com.shiroumi.shiroplayer.composable.common.theme.ColorPalette
import com.shiroumi.shiroplayer.composable.navCtrl
import com.shiroumi.shiroplayer.composable.playlist.Playlist
import com.shiroumi.shiroplayer.composable.playlists.Playlists
import com.shiroumi.shiroplayer.composable.shiroViewModel
import com.shiroumi.shiroplayer.viewmodel.LibraryState

@Composable
fun Home() = Box(
    modifier = Modifier
        .fillMaxSize()
        .background(ColorPalette.main)
) {

    val libraryState by shiroViewModel.mLibraryState.observeAsState(LibraryState.Default)

    AnimatedVisibility(
        visible = libraryState is LibraryState.Updating,
        enter = fadeIn(),
        exit = fadeOut(),
        content = { Loading() }
    )

    Column {
        TitleBar(
            modifier = Modifier.titleBarModifier(ColorPalette.main)
        )

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = libraryState is LibraryState.NotEmpty,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Nav(modifier = Modifier.navHostModifier())
            shiroViewModel.refreshCurrentPlaylist()
        }

        AnimatedVisibility(
            visible = libraryState is LibraryState.Empty,
            enter = fadeIn(),
            exit = fadeOut(),
            content = { NoPathsDialog() }
        )
    }
}

@Composable
fun Nav(
    modifier: Modifier
) {
    val navCtrl = navCtrl.navCtrl ?: return
    NavHost(
        navController = navCtrl,
        startDestination = Route.Playlist.value,
        modifier = modifier.background(ColorPalette.main)
    ) {
        composable(Route.Playlist.value) {
            Playlist(modifier = Modifier.playlistModifier(ColorPalette.main))
        }
        composable(Route.Playlists.value) {
            Playlists(modifier = Modifier.playlistModifier(ColorPalette.main))
        }
    }
}

@Composable
fun NoPathsDialog() = Dialog(
    properties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    ),
    onDismissRequest = {}
) {
    val activity = activity
    val shiroViewModel = shiroViewModel

    Card(
        modifier = Modifier.wrapContentSize(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp)
        ) {
            Text(
                text = "添加媒体库路径",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                fontSize = 18.sp,
                color = Color.Gray,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "因系统限制无权限读取外部存储，需要手动选择媒体库路径",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                lineHeight = 22.sp
            )
            Button(
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.textButtonColors(),
                elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
                onClick = {
                    activity.launchSaf { result ->
                        result ?: run {
                            Log.e("saf", "empty saf result")
                            return@launchSaf
                        }
                        shiroViewModel.addLibraryPath(result)
                    }
                }
            ) {
                Text(
                    text = "去选择",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun Modifier.titleBarModifier(
    color: Color
) = fillMaxWidth()
    .wrapContentHeight()
//    .background(color)
    .padding(8.dp)

fun Modifier.navHostModifier() = fillMaxSize()

fun Modifier.playlistModifier(color: Color) = fillMaxSize()
//    .background(color)
