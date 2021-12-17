package com.shiroumi.shiroplayer.composable.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import com.shiroumi.shiroplayer.composable.themeViewModel

object ThemeColors {
    val Main
        @Composable get() = when (themeViewModel.currentTheme) {
            Theme.Light -> Main_Light
            Theme.Dark -> MainDark
        }
    val LighterMain
        @Composable get() = when (themeViewModel.currentTheme) {
            Theme.Light -> LighterMain_Light
            Theme.Dark -> LighterMain_Dark
        }

    val DarkerMain
        @Composable get() = when (themeViewModel.currentTheme) {
            Theme.Light -> DarkerMain_Light
            Theme.Dark -> DarkerMain_Dark
        }

    val Text
        @Composable get() = when (themeViewModel.currentTheme) {
            Theme.Light -> Text_Light
            Theme.Dark -> Text_Dark
        }
    val LighterText
        @Composable get() = when (themeViewModel.currentTheme) {
            Theme.Light -> LighterText_Light
            Theme.Dark -> LighterText_Dark
        }

    val DarkerText
        @Composable get() = when (themeViewModel.currentTheme) {
            Theme.Light -> DarkerText_Light
            Theme.Dark -> DarkerText_Dark
        }
}

val Main_Light = White
val MainDark = BlueGrey900
val LighterMain_Light = White
val LighterMain_Dark = Color(0xff4f5b62)
val DarkerMain_Light = Color(0xffc7c7c7)
val DarkerMain_Dark = Color(0xff000a12)

val Text_Light = Grey900
val Text_Dark = Grey50
val LighterText_Light = Color(0xff484848)
val LighterText_Dark = White
val DarkerText_Light = Black
val DarkerText_Dark = Color(0xffc7c7c7)