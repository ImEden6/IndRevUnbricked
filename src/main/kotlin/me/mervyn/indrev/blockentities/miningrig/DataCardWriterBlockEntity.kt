package me.mervyn.indrev.blockentities.miningrig
 
import me.mervyn.indrev.api.OreDataCards
import me.mervyn.indrev.api.machines.Tier
import me.mervyn.indrev.blockentities.MachineBlockEntity
import me.mervyn.indrev.components.autosync
import me.mervyn.indrev.config.BasicMachineConfig
import me.mervyn.indrev.config.IRConfig
import me.mervyn.indrev.inventories.inventory
import me.mervyn.indrev.registry.IRItemRegistry
import me.mervyn.indrev.registry.MachineRegistry
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.util.math.BlockPos
 
class DataCardWriterBlockEntity (tier: Tier, pos: BlockPos, state: BlockState)
    : MachineBlockEntity<BasicMachineConfig>(tier, MachineRegistry.DATA_CARD_WRITER_REGISTRY, pos, state) {
    init {
        this.inventoryComponent = inventory(this) {
            input {
                0 filter { itemStack, _ -> itemStack.item == IRItemRegistry.ORE_DATA_CARD }
                1 until 13 filter { stack, _ -> OreDataCards.isAllowed(stack) }
                13 until 16 filter { stack, _ -> OreDataCards.Modifier.isModifierItem(stack.item) }
            }
        }
    }
 
    var processTime by autosync(PROCESS_ID, 0)
    var totalProcessTime by autosync(TOTAL_PROCESS_ID, 0)
 
    private val modifiersToAdd = mutableMapOf<OreDataCards.Modifier, Int>()
    private val toWrite = mutableListOf<ItemStack>()
    private val consumedModifiers = mutableListOf<ItemStack>()
 
    override fun machineTick() {
        if (totalProcessTime > 0 && use(getEnergyCost())) {
            if (processTime >= totalProcessTime)
                finish()
            else {
                processTime++
                workingState = true
            }
        } else {
            workingState = false
        }
    }
 
    fun start() {
        val inventory = inventoryComponent!!.inventory
        val cardStack = inventory.getStack(0)
 
        if (IRConfig.miningRigConfig.dataCardWriterRequiresCard) {
            if (cardStack.isEmpty || cardStack.item != IRItemRegistry.ORE_DATA_CARD || cardStack.count != 1) {
                return
            }
        } else {
            if (!cardStack.isEmpty && (cardStack.item != IRItemRegistry.ORE_DATA_CARD || cardStack.count != 1)) {
                return
            }
        }
 
        val oldData = OreDataCards.readNbt(cardStack)
 
        val requiredOreStackSize = IRConfig.miningRigConfig.dataCardWriterRequiredOreStackSize
        ORES_SLOTS.forEach { slot ->
            val stack = inventory.getStack(slot)
            if (!stack.isEmpty && stack.count == requiredOreStackSize) {
                toWrite.add(stack.copy())
                inventory.setStack(slot, ItemStack.EMPTY)
            }
        }
 
        if (toWrite.isEmpty() && oldData == null) {
            return
        }
 
        MODIFIERS_SLOTS.forEach { slot ->
            val stack = inventory.getStack(slot)
            val modifier = OreDataCards.Modifier.byItem(stack.item) ?: return@forEach
            var level = (modifiersToAdd[modifier] ?: 0) + (oldData?.modifiersUsed?.get(modifier) ?: 0)
            var consumedCount = 0
            val modifierItem = stack.item
            when (modifier) {
                OreDataCards.Modifier.RICHNESS -> {
                    while (stack.count >= 16 && level < 40) {
                        stack.decrement(16)
                        level++
                        consumedCount += 16
                    }
                }
                OreDataCards.Modifier.SPEED, OreDataCards.Modifier.SIZE -> {
                    while (stack.count >= 64) {
                        stack.decrement(64)
                        level++
                        consumedCount += 64
                    }
                }
                OreDataCards.Modifier.RNG -> {
                    if (level == 0) {
                        stack.decrement(1)
                        consumedCount += 1
                        val r = world!!.random.nextDouble()
                        if (r > 0.95 && r <= 0.98) {
                            level = -1
                        } else if (r > 0.98) {
                            level = 1
                        }
                    }
                }
            }
            if (consumedCount > 0) {
                consumedModifiers.add(ItemStack(modifierItem, consumedCount))
            }
            modifiersToAdd[modifier] = level - (oldData?.modifiersUsed?.get(modifier) ?: 0)
        }
 
        processTime = 0
        totalProcessTime = 20*10 + (toWrite.size * (5*modifiersToAdd.map { it.value }.sum()))
    }
 
    private fun finish() {
        val inventory = inventoryComponent!!.inventory
        val cardStack = inventory.getStack(0)
 
        if (cardStack.isEmpty || cardStack.item != IRItemRegistry.ORE_DATA_CARD || cardStack.count != 1) {
            refundInputs()
            modifiersToAdd.clear()
            toWrite.clear()
            consumedModifiers.clear()
            processTime = 0
            totalProcessTime = 0
            return
        }
 
        val oldData = OreDataCards.readNbt(cardStack)
 
        val oreTypes = toWrite.map { it.item }.distinct().count()
        val richnessDecrease = if (oreTypes == 1) 0.02 else 0.04
        val richnessModifier = ((modifiersToAdd[OreDataCards.Modifier.RICHNESS] ?: 0) * 0.01).coerceAtMost(0.2)
        val richness = ((oldData?.richness ?: 1.0) - (richnessDecrease * toWrite.size) + richnessModifier).coerceIn(richnessDecrease, 1.0)
 
        val speedModifier = ((oldData?.modifiersUsed?.get(OreDataCards.Modifier.SPEED) ?: 0) + (modifiersToAdd[OreDataCards.Modifier.SPEED] ?: 0)) * 20
        val speed = 100 + (richness * 1100) - speedModifier + (modifiersToAdd[OreDataCards.Modifier.SIZE] ?: 0) * 2
 
        val rng = oldData?.rng ?: modifiersToAdd[OreDataCards.Modifier.RNG] ?: 0
 
        val oreEnergyRequired = toWrite.sumOf { OreDataCards.getCost(it) * 16 }
        val energyRequired = (oldData?.energyRequired ?: 32) + 8 * (modifiersToAdd[OreDataCards.Modifier.SPEED] ?: 0) + oreEnergyRequired
 
        val cyclesModifiers = (modifiersToAdd[OreDataCards.Modifier.SIZE] ?: 0) * 128
        val maxCycles = (oldData?.maxCycles ?: 0) + (toWrite.size * 64) + cyclesModifiers
 
        val items = mutableMapOf<Item, Int>()
        oldData?.entries?.forEach { entry ->
            items[entry.item] = entry.count
        }
        toWrite.forEach { stack ->
            items[stack.item] = items.getOrDefault(stack.item, 0) + stack.count
        }
        val entries = items.keys.map { OreDataCards.OreEntry(it, items[it]!!) }
 
        val modifiersMap = mutableMapOf<OreDataCards.Modifier, Int>()
        modifiersToAdd.forEach { (modifier, level) ->
            modifiersMap[modifier] = (oldData?.modifiersUsed?.get(modifier)?: 0) + level
        }
        val data = OreDataCards.Data(entries, modifiersMap, richness, speed.toInt(), rng, energyRequired, maxCycles, oldData?.used ?: 0)
 
        OreDataCards.writeNbt(cardStack, data)
 
        modifiersToAdd.clear()
        toWrite.clear()
        consumedModifiers.clear()
        processTime = 0
        totalProcessTime = 0
    }
 
    private fun refundInputs() {
        val inventory = inventoryComponent!!.inventory
 
        // Refund ores
        for (stack in toWrite) {
            var remaining = stack
            remaining = insertIntoSlots(inventory, ORES_SLOTS, remaining)
            if (!remaining.isEmpty) {
                spawnItemEntity(remaining)
            }
        }
 
        // Refund modifiers
        for (stack in consumedModifiers) {
            var remaining = stack
            remaining = insertIntoSlots(inventory, MODIFIERS_SLOTS, remaining)
            if (!remaining.isEmpty) {
                spawnItemEntity(remaining)
            }
        }
    }
 
    private fun insertIntoSlots(inventory: net.minecraft.inventory.Inventory, slots: IntRange, stack: ItemStack): ItemStack {
        val remaining = stack.copy()
 
        // First pass: try to merge with existing stacks
        for (slot in slots) {
            val existing = inventory.getStack(slot)
            if (!existing.isEmpty && ItemStack.canCombine(existing, remaining)) {
                val maxCount = Math.min(existing.maxCount, inventory.maxCountPerStack)
                val toAdd = Math.min(maxCount - existing.count, remaining.count)
                if (toAdd > 0) {
                    existing.increment(toAdd)
                    remaining.decrement(toAdd)
                    if (remaining.isEmpty) {
                        return ItemStack.EMPTY
                    }
                }
            }
        }
 
        // Second pass: put into empty slots
        for (slot in slots) {
            val existing = inventory.getStack(slot)
            if (existing.isEmpty) {
                val maxCount = Math.min(remaining.maxCount, inventory.maxCountPerStack)
                if (remaining.count <= maxCount) {
                    inventory.setStack(slot, remaining)
                    return ItemStack.EMPTY
                } else {
                    inventory.setStack(slot, remaining.split(maxCount))
                }
            }
        }
 
        return remaining
    }
 
    private fun spawnItemEntity(stack: ItemStack) {
        val world = this.world ?: return
        if (world.isClient) return
        val itemEntity = net.minecraft.entity.ItemEntity(
            world,
            pos.x + 0.5,
            pos.y + 1.0,
            pos.z + 0.5,
            stack
        )
        itemEntity.setToDefaultPickupDelay()
        world.spawnEntity(itemEntity)
    }
 
    override fun getEnergyCost(): Long {
        return IRConfig.machines.dataCardWriter.energyCost
    }
 
    override fun fromTag(tag: NbtCompound) {
        super.fromTag(tag)
        if (tag.contains("toWrite")) {
            val toWriteList = tag.getList("toWrite", 10)
            toWrite.clear()
            toWriteList.forEach { element ->
                toWrite.add(ItemStack.fromNbt(element as NbtCompound))
            }
        }
        if (tag.contains("consumedModifiers")) {
            val consumedModifiersList = tag.getList("consumedModifiers", 10)
            consumedModifiers.clear()
            consumedModifiersList.forEach { element ->
                consumedModifiers.add(ItemStack.fromNbt(element as NbtCompound))
            }
        }
        if (tag.contains("modifiersToAdd")) {
            val modifiersList = tag.getList("modifiersToAdd", 10)
            modifiersToAdd.clear()
            modifiersList.forEach { element ->
                val compound = element as NbtCompound
                val modifierVal = compound.getInt("Modifier")
                if (modifierVal >= 0 && modifierVal < OreDataCards.Modifier.values().size) {
                    val modifier = OreDataCards.Modifier.values()[modifierVal]
                    val level = compound.getInt("Level")
                    modifiersToAdd[modifier] = level
                }
            }
        }
        processTime = tag.getInt("ProcessTime")
        totalProcessTime = tag.getInt("TotalProcessTime")
    }
 
    override fun toTag(tag: NbtCompound) {
        super.toTag(tag)
        val toWriteList = NbtList()
        toWrite.forEach { stack ->
            toWriteList.add(stack.writeNbt(NbtCompound()))
        }
        tag.put("toWrite", toWriteList)
 
        val consumedModifiersList = NbtList()
        consumedModifiers.forEach { stack ->
            consumedModifiersList.add(stack.writeNbt(NbtCompound()))
        }
        tag.put("consumedModifiers", consumedModifiersList)
 
        val modifiersList = NbtList()
        modifiersToAdd.forEach { (modifier, level) ->
            val compound = NbtCompound()
            compound.putInt("Modifier", modifier.ordinal)
            compound.putInt("Level", level)
            modifiersList.add(compound)
        }
        tag.put("modifiersToAdd", modifiersList)
 
        tag.putInt("ProcessTime", processTime)
        tag.putInt("TotalProcessTime", totalProcessTime)
    }
 
    companion object {
        const val PROCESS_ID = 2
        const val TOTAL_PROCESS_ID = 3
 
        val MODIFIERS_SLOTS = 13 until 16
        val ORES_SLOTS = 1 until 13
    }
}