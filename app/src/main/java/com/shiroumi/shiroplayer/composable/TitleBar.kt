package com.shiroumi.shiroplayer.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QueueMusic
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import com.shiroumi.shiroplayer.R
import com.shiroumi.shiroplayer.activity.MainActivity
import com.shiroumi.shiroplayer.viewmodel.PlayerViewModel
import kotlin.contracts.ExperimentalContracts

@ExperimentalComposeUiApi
@ExperimentalContracts
@Composable
fun TitleBar(
    viewModel: PlayerViewModel,
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .padding(0.dp, 0.dp, 0.dp, 8.dp)
    ) {
        val (title, playlist) = createRefs()
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(
                    18.dp, 4.dp, 18.dp, 4.dp
                )
                .wrapContentHeight(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )

        val context = LocalContext.current

        Icon(
            modifier = Modifier
                .size(28.dp)
                .constrainAs(playlist) {
                    end.linkTo(parent.end, margin = 12.dp)
                    linkTo(parent.top, parent.bottom, bias = 0.7f)
                }
                .rippleClickable(
                    bounded = false,
                    radius = 32.dp
                ) {
                    viewModel.refreshMusicDb()
//                    (context as MainActivity).launchSaf()
                },
            imageVector = Icons.Rounded.QueueMusic,
            contentDescription = "Playlist",
            tint = Color(0xFF666666)
        )
    }
}