package com.shiroumi.shiroplayer.composable.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QueueMusic
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.shiroumi.shiroplayer.R
import com.shiroumi.shiroplayer.Route
import com.shiroumi.shiroplayer.composable.LocalCommonViewModel
import com.shiroumi.shiroplayer.composable.common.theme.ColorPalette
import com.shiroumi.shiroplayer.composable.common.theme.CustomTheme
import com.shiroumi.shiroplayer.composable.common.theme.Theme
import com.shiroumi.shiroplayer.composable.themeViewModel
import com.shiroumi.shiroplayer.composable.navCtrl
import com.shiroumi.shiroplayer.viewmodel.ThemeViewModel

@Composable
fun TitleBar(
    modifier: Modifier
) = ConstraintLayout(modifier = modifier) {

    val navCtrl = navCtrl
    val currentRoute by navCtrl.route.observeAsState()

    val (title, button) = createRefs()

    TitleText(
        modifier = Modifier
            .wrapContentHeight()
            .constrainAs(title) {
                start.linkTo(parent.start, margin = 8.dp)
            }
    )

    DefaultActionButton(
        modifier = Modifier
            .size(48.dp)
            .constrainAs(button) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            },
        visible = currentRoute is Route.Playlist,
        onClick = {
            navCtrl.navigate(Route.Playlists)
        }
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            imageVector = Icons.Rounded.QueueMusic,
            contentDescription = "Playlist",
            tint = ColorPalette.text
        )
    }
}

@Composable
fun TitleText(modifier: Modifier) = Text(
    text = stringResource(id = R.string.app_name),
    modifier = modifier,
    fontSize = 32.sp,
    fontWeight = FontWeight.Bold,
    color = ColorPalette.text
)

@Composable
private fun DefaultActionButton(
    modifier: Modifier,
    visible: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) = AnimatedVisibility(
    visible = visible,
    modifier = modifier,
    enter = fadeIn(),
    exit = fadeOut()
) {
    val vm = themeViewModel
    Button(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(48.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = ColorPalette.main),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
        contentPadding = PaddingValues(8.dp),
        onClick = {
            vm.themeAnimStart()
            when (vm.currentTheme){
                Theme.Dark -> vm.light()
                Theme.Light -> vm.dark()
            }
        },
    ) {
        content()
    }
}


// preview
@Preview
@Composable
fun TitleBarPrev() = CompositionLocalProvider(
    LocalCommonViewModel provides ThemeViewModel()
) {
    CustomTheme(theme = Theme.Dark) {
        TitleBar(
            modifier = Modifier.titleBarModifier(ColorPalette.main)
        )
    }

}