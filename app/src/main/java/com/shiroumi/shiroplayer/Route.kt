package com.shiroumi.shiroplayer

private const val PLAYLIST = "playlist"
private const val PLAYLISTS = "playlists"

sealed class Route(val value: String) {
    object Playlist : Route(PLAYLIST)
    object Playlists : Route(PLAYLISTS)
}

fun String.toRoute() = when(this) {
    PLAYLIST -> Route.Playlist
    PLAYLISTS -> Route.Playlists
    else -> Route.Playlist
}