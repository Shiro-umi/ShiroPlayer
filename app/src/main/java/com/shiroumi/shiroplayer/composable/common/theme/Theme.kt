package com.shiroumi.shiroplayer.composable.common.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun CustomTheme(
    theme: Theme,
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val colorPalette = ColorPaletteImpl(
        main = ThemeColors.Main,
        lighterMain = ThemeColors.LighterMain,
        darkerMain = ThemeColors.DarkerMain,
        text = ThemeColors.Text,
        lighterText = ThemeColors.LighterText,
        darkerText = ThemeColors.DarkerText
    )

    val main = animateColorAsState(
        targetValue = colorPalette.main,
        animationSpec = tween(500)
    )
    val lighterMain = animateColorAsState(
        targetValue = colorPalette.lighterMain,
        animationSpec = tween(500)
    )

    val darkerMain = animateColorAsState(
        targetValue = colorPalette.darkerMain,
        animationSpec = tween(500)
    )

    val text = animateColorAsState(
        targetValue = colorPalette.text,
        animationSpec = tween(500)
    )

    val lighterText = animateColorAsState(
        targetValue = colorPalette.lighterText,
        animationSpec = tween(500)
    )

    val darkerText = animateColorAsState(
        targetValue = colorPalette.darkerText,
        animationSpec = tween(500)
    )
    CompositionLocalProvider(
        LocalColorPalette provides ColorPaletteImpl(
            main = main.value,
            lighterMain = lighterMain.value,
            darkerMain = darkerMain.value,
            text = text.value,
            lighterText = lighterText.value,
            darkerText = darkerText.value
        )
    ) {
        when (theme) {
            Theme.Light -> {
                systemUiController.setSystemBarsColor(ColorPalette.main)
                content()
            }
            Theme.Dark -> {
                systemUiController.setSystemBarsColor(ColorPalette.main)
                content()
            }
        }
    }
}

private val LocalColorPalette = compositionLocalOf { ColorPaletteImpl() }
val ColorPalette: ColorPaletteImpl
    @Composable get() = LocalColorPalette.current

data class ColorPaletteImpl(
    val main: Color = Main_Light,
    val lighterMain: Color = LighterMain_Light,
    val darkerMain: Color = DarkerMain_Light,
    val text: Color = Text_Light,
    val lighterText: Color = LighterText_Light,
    val darkerText: Color = DarkerText_Light,
)

sealed class Theme {
    object Light : Theme()
    object Dark : Theme()
}