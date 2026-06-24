package me.mervyn.indrev_unbricked.components.multiblock

import net.minecraft.util.Identifier

class StructureIdentifier(val modId: String, val structure: String, val variant: String): Identifier(modId, "$structure/$variant")