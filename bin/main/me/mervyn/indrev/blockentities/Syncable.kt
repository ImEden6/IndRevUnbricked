package me.mervyn.indrev.blockentities

interface Syncable {
    fun markForUpdate(condition: () -> Boolean = { true })
}