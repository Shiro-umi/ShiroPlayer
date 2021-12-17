val composeVer by extra("1.0.4")
val kotlinVer by extra("1.5.31")
val navVer by extra("2.3.5")
val media2Ver by extra("1.1.2")
val roomVer by extra("2.4.0-beta01")
val activityVer by extra("1.4.0")
val navigationVer by extra("2.4.0-beta02")
val lifecycleVer by extra("2.4.0")
val constraintLayoutVer by extra("1.0.0-rc02")
val accompanistPagerVer by extra("0.17.0")
val accompanistSystemUiControllerVer by extra("0.17.0")
val permissionsDispatcherVer by extra("4.9.1")

val deps by extra(Deps())

val executeImplementation: DependencyHandlerScope.() -> Unit = {
    deps.common.forEach { dep ->
        dependencies.add("implementation", dep)
    }
    deps.compose.forEach { dep ->
        dependencies.add("implementation", dep)
    }
    deps.media2.forEach { dep ->
        dependencies.add("implementation", dep)
    }
    deps.mp3.forEach { dep ->
        dependencies.add("implementation", dep)
    }
    deps.room["kapt"]?.let { dep ->
        add("kapt", dep)
    }
    (deps.room["dependencies"] as? List<*>)?.forEach { dep ->
        dependencies.add("implementation", dep!!)
    }
    deps.permissionDispatcher["kapt"]?.let { dep ->
        add("kapt", dep)
    }
    deps.permissionDispatcher["dependencies"]?.let { dep ->
        dependencies.add("implementation", dep)
    }
}

val implementation by extra(executeImplementation)

class Deps {
    val common: List<String> = listOf(
        "androidx.appcompat:appcompat:$activityVer",
        "androidx.activity:activity-ktx:$activityVer",
        "com.google.android.material:material:$activityVer",
        "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVer",
        "androidx.core:core-ktx:1.7.0"
    )
    val compose: List<String> = listOf(
        "androidx.compose.ui:ui:$composeVer",
        "androidx.compose.ui:ui-tooling:$composeVer",
        "androidx.compose.foundation:foundation:$composeVer",
        "androidx.compose.material:material:$composeVer",
        "androidx.compose.material:material-icons-core:$composeVer",
        "androidx.compose.material:material-icons-extended:$composeVer",
        "androidx.compose.runtime:runtime:$composeVer",
        "androidx.compose.runtime:runtime-livedata:$composeVer",
        "androidx.activity:activity-compose:$activityVer",
        "androidx.navigation:navigation-compose:$navigationVer",
        "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVer",
        "androidx.constraintlayout:constraintlayout-compose:$constraintLayoutVer",
        "com.google.accompanist:accompanist-pager:$accompanistPagerVer",
        "com.google.accompanist:accompanist-pager-indicators:$accompanistPagerVer",
        "com.google.accompanist:accompanist-systemuicontroller:$accompanistSystemUiControllerVer"
    )
    val room: Map<String, *> = mapOf(
        "dependencies" to listOf(
            "androidx.room:room-runtime:$roomVer",
            "androidx.room:room-ktx:$roomVer"
        ),
        "kapt" to "androidx.room:room-compiler:$roomVer"
    )
    val media2: List<String> = listOf(
        "androidx.media2:media2-session:$media2Ver",
        "androidx.media2:media2-widget:$media2Ver",
        "androidx.media2:media2-player:$media2Ver"
    )
    val permissionDispatcher: Map<String, String> = mapOf(
        "dependencies" to "com.github.permissions-dispatcher:permissionsdispatcher:$permissionsDispatcherVer",
        "kapt" to "com.github.permissions-dispatcher:permissionsdispatcher-processor:$permissionsDispatcherVer"
    )
    val mp3: List<String> = listOf(
        "com.mpatric:mp3agic:0.9.1",
        "commons-io:commons-io:2.11.0"
    )
}