package com.shiroumi.shiroplayer.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import com.shiroumi.shiroplayer.activity.MainActivity
import com.shiroumi.shiroplayer.composable.common.navigation.NavHostController
import com.shiroumi.shiroplayer.viewmodel.ThemeViewModel
import com.shiroumi.shiroplayer.viewmodel.ShiroViewModel

val LocalMainActivity = compositionLocalOf { MainActivity() }

val LocalCommonViewModel = compositionLocalOf { ThemeViewModel() }

val LocalShiroViewModel = compositionLocalOf { ShiroViewModel() }

val LocalNavCtrl = compositionLocalOf { NavHostController() }

val activity: MainActivity
    @Composable get() = LocalMainActivity.current

val shiroViewModel: ShiroViewModel
    @Composable get() = LocalShiroViewModel.current

val themeViewModel: ThemeViewModel
    @Composable get() = LocalCommonViewModel.current

val navCtrl: NavHostController
    @Composable get() = LocalNavCtrl.current
