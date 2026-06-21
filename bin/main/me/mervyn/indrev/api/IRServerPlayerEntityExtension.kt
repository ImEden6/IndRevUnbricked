package me.mervyn.indrev.api

interface IRServerPlayerEntityExtension : IRPlayerEntityExtension {
    fun indrev_shouldSync(): Boolean
    fun indrev_sync()
}