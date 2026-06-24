package me.mervyn.indrev_unbricked.registry

import me.mervyn.indrev_unbricked.utils.identifier
import me.mervyn.indrev_unbricked.utils.itemSettings
import me.mervyn.indrev_unbricked.utils.item
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.client.render.RenderLayer
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class MaterialHelper(private val id: String, private val block: MaterialHelper.() -> Unit) {

    fun withItems(vararg variants: String): MaterialHelper {
        variants.forEach { variant ->
            val identifier = identifier("${id}_$variant")
            map[identifier] = { identifier.item(Item(itemSettings())) }
        }
        return this
    }

    fun withItem(): MaterialHelper {
        identifier(id).item(Item(itemSettings()))
        return this
    }

    fun withOre(rawOre: Boolean = true, supplier: (FabricBlockSettings) -> Block = { Block(it) }): MaterialHelper {
        val ore = supplier(FabricBlockSettings.copyOf(Blocks.IRON_ORE).requiresTool().strength(3f, 3f))
        val identifier = identifier("${id}_ore")
        map[identifier] = {
            Registry.register(Registries.BLOCK, identifier, ore)
            identifier.item(BlockItem(ore, itemSettings()))
        }

        val deepslateOre = supplier(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_COAL_ORE).requiresTool().strength(3f, 3f))
        val deepslateId = identifier("deepslate_${id}_ore")
        map[deepslateId] = {
            Registry.register(Registries.BLOCK, deepslateId, deepslateOre)
            deepslateId.item(BlockItem(deepslateOre, itemSettings()))
        }

        if (rawOre) {
            val rawOreBlock = supplier(FabricBlockSettings.copyOf(Blocks.RAW_COPPER_BLOCK).requiresTool().strength(3f, 3f))
            val rawOreId = identifier("raw_${id}")
            val rawOreBlockId = identifier("raw_${id}_block")
            map[rawOreId] = {
                Registry.register(Registries.BLOCK, rawOreBlockId, rawOreBlock)
                rawOreBlockId.item(BlockItem(rawOreBlock, itemSettings()))
                rawOreId.item(Item(itemSettings()))
            }
        }

        return this
    }

    fun withTools(pickaxe: PickaxeItem, axe: AxeItem, shovel: ShovelItem, sword: SwordItem, hoe: HoeItem) {
        map[identifier("${id}_pickaxe")] = {
            identifier("${id}_pickaxe").item(pickaxe)
        }
        map[identifier("${id}_axe")] = {
            identifier("${id}_axe").item(axe)
        }
        map[identifier("${id}_shovel")] = {
            identifier("${id}_shovel").item(shovel)
        }
        map[identifier("${id}_sword")] = {
            identifier("${id}_sword").item(sword)
        }
        map[identifier("${id}_hoe")] = {
            identifier("${id}_hoe").item(hoe)
        }
    }

    fun withArmor(material: ArmorMaterial) {
        map[identifier("${id}_helmet")] = {
            identifier("${id}_helmet").item(ArmorItem(material, ArmorItem.Type.HELMET, itemSettings()))
        }
        map[identifier("${id}_chestplate")] = {
            identifier("${id}_chestplate").item(ArmorItem(material, ArmorItem.Type.CHESTPLATE, itemSettings()))
        }
        map[identifier("${id}_leggings")] = {
            identifier("${id}_leggings").item(ArmorItem(material, ArmorItem.Type.LEGGINGS, itemSettings()))
        }
        map[identifier("${id}_boots")] = {
            identifier("${id}_boots").item(ArmorItem(material, ArmorItem.Type.BOOTS, itemSettings()))
        }
    }

    fun withBlock(): MaterialHelper {
        val block =
            Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).requiresTool().strength(5f, 6f))
        val id = identifier("${id}_block")
        map[id] = {
            Registry.register(Registries.BLOCK, id, block)
            id.item(BlockItem(block, itemSettings()))
        }
        if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())
        }
        return this
    }

    fun register() = block()

    companion object {
        val map = HashMap<Identifier, () -> Unit>()

        fun register() {
            map.entries.sortedWith(
                compareBy<MutableMap.MutableEntry<Identifier, () -> Unit>> { id -> id.key.path.contains("ore") && !id.key.path.contains("purified") }
                    .then(compareBy { id -> id.key.path.contains("raw") })
                    .then(compareBy { id -> id.key.path.contains("block") })
                    .then(compareBy { id -> id.key.path.contains("ingot") })
                    .then(compareBy { id -> id.key.path.contains("chunk") })
                    .then(compareBy { id -> id.key.path.contains("dust") })
                    .then(compareBy { id -> id.key.path.contains("purified") })
                    .then(compareBy { id -> id.key.path.contains("plate") && !id.key.path.contains("chestplate") })
                    .then(compareBy { id -> id.key.path.contains("nugget") })
                    .then(compareBy<MutableMap.MutableEntry<Identifier, () -> Unit>> { id -> id.key.path.substring(0, id.key.path.indexOf("_")) }
                        .then(compareBy { id -> id.key.path.contains("sword") })
                        .then(compareBy { id -> id.key.path.contains("pickaxe") })
                        .then(compareBy { id -> id.key.path.contains("axe") && !id.key.path.contains("pickaxe") })
                        .then(compareBy { id -> id.key.path.contains("shovel") })
                        .then(compareBy { id -> id.key.path.contains("hoe") })
                        .then(compareBy { id -> id.key.path.contains("helmet") })
                        .then(compareBy { id -> id.key.path.contains("chestplate") })
                        .then(compareBy { id -> id.key.path.contains("leggings") })
                        .then(compareBy { id -> id.key.path.contains("boots") })
                    )
            ).asReversed().forEach { it.value() }
        }
    }
}
