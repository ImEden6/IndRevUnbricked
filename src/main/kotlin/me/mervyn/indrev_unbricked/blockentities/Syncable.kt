package me.mervyn.indrev_unbricked.blockentities

interface Syncable {
    fun markForUpdate(condition: () -> Boolean = { true })
}