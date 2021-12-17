package com.shiroumi.shiroplayer.composable.playlists

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.shiroumi.shiroplayer.composable.common.theme.ColorPalette
import com.shiroumi.shiroplayer.composable.common.theme.Theme
import com.shiroumi.shiroplayer.composable.themeViewModel
import com.shiroumi.shiroplayer.composable.shiroViewModel

@Composable
fun Playlists(
    modifier: Modifier
) = Box {

    val playLists by shiroViewModel.mPlayLists.observeAsState(emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(playLists) { _, item ->
            Text(text = item.name, color = when(themeViewModel.currentTheme){
                Theme.Light -> ColorPalette.lighterText
                Theme.Dark -> ColorPalette.darkerText
            })
        }
    }
}
