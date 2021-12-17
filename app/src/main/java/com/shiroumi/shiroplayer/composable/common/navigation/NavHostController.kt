package com.shiroumi.shiroplayer.composable.common.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.shiroumi.shiroplayer.Route
import com.shiroumi.shiroplayer.toRoute

class NavHostController(
    val navCtrl: NavHostController? = null
) {
    init {
        navCtrl?.addOnDestinationChangedListener { _, destination, _ ->
            _route.postValue(destination.route?.toRoute())
        }
    }

    private val _route = MutableLiveData<Route>(Route.Playlist)
    val route get() = _route as LiveData<Route>
    val currentRoute get() = _route.value

    fun navigate(route: Route) {
        _route.postValue(route)
        navCtrl?.navigate(route.value) { launchSingleTop = true }
    }
}

