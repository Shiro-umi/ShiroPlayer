package com.shiroumi.shiroplayer.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.shiroumi.shiroplayer.R

@Composable
fun TitleBar(modifier: Modifier) {
    ConstraintLayout(
        modifier = modifier
            .padding(0.dp, 0.dp, 0.dp, 8.dp)
    ) {
        val title = createRef()
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
    }
}