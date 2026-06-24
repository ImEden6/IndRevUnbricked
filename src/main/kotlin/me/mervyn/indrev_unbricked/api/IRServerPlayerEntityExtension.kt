package me.mervyn.indrev_unbricked.api

interface IRServerPlayerEntityExtension : IRPlayerEntityExtension {
    fun indrev_shouldSync(): Boolean
    fun indrev_sync()
}