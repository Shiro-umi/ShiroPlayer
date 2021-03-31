package com.shiroumi.shiroplayer.components

interface ListNode<T> {
    fun prev(): T?
    fun prev(listNode: T?)
    fun next(): T?
    fun next(listNode: T?)
}