package org.wolflink.minecraft.plugin.eclipticengineering.dictionary

import org.bukkit.Material

private val ORE_MATERIAL = setOf(
    Material.COAL_ORE,
    Material.DEEPSLATE_COAL_ORE,
    Material.IRON_ORE,
    Material.DEEPSLATE_IRON_ORE,
    Material.COPPER_ORE,
    Material.DEEPSLATE_COPPER_ORE,
    Material.GOLD_ORE,
    Material.DEEPSLATE_GOLD_ORE,
    Material.REDSTONE_ORE,
    Material.DEEPSLATE_REDSTONE_ORE,
    Material.EMERALD_ORE,
    Material.DEEPSLATE_EMERALD_ORE,
    Material.LAPIS_ORE,
    Material.DEEPSLATE_LAPIS_ORE,
    Material.DIAMOND_ORE,
    Material.DEEPSLATE_DIAMOND_ORE,
    Material.NETHER_GOLD_ORE,
    Material.NETHER_QUARTZ_ORE
)
private val PICKAXE_MATERIAL = setOf(
    Material.DIAMOND_PICKAXE,
    Material.IRON_PICKAXE,
    Material.GOLDEN_PICKAXE,
    Material.NETHERITE_PICKAXE,
    Material.WOODEN_PICKAXE,
    Material.STONE_PICKAXE
)
private val AXE_MATERIAL = setOf(
    Material.DIAMOND_AXE,
    Material.IRON_AXE,
    Material.GOLDEN_AXE,
    Material.NETHERITE_AXE,
    Material.WOODEN_AXE,
    Material.STONE_AXE,
)
private val HOE_MATERIAL = setOf(
    Material.DIAMOND_HOE,
    Material.IRON_HOE,
    Material.GOLDEN_HOE,
    Material.NETHERITE_HOE,
    Material.WOODEN_HOE,
    Material.STONE_HOE,
)
private val FURNACE_MATERIAL = setOf(
    Material.FURNACE,
    Material.BLAST_FURNACE
)
private val AUXILIARY_MATERIAL = setOf(
    Material.SCAFFOLDING,
    Material.LADDER,
    Material.RAIL,
    Material.ACTIVATOR_RAIL,
    Material.DETECTOR_RAIL,
    Material.POWERED_RAIL
)
private val REMOTE_WEAPON_MATERIAL = setOf(
    Material.BOW,
    Material.CROSSBOW
)
private val WOOD_MATERIAL = setOf(
    // 原木
    Material.OAK_LOG,
    Material.SPRUCE_LOG,
    Material.BIRCH_LOG,
    Material.JUNGLE_LOG,
    Material.ACACIA_LOG,
    Material.CHERRY_LOG,
    Material.DARK_OAK_LOG,
    Material.MANGROVE_LOG,
    // 去皮原木
    Material.STRIPPED_OAK_LOG,
    Material.STRIPPED_SPRUCE_LOG,
    Material.STRIPPED_BIRCH_LOG,
    Material.STRIPPED_JUNGLE_LOG,
    Material.STRIPPED_ACACIA_LOG,
    Material.STRIPPED_CHERRY_LOG,
    Material.STRIPPED_DARK_OAK_LOG,
    Material.STRIPPED_MANGROVE_LOG,
)
private val VIRTUAL_RESOURCE_MATERIAL = setOf(
    // 金属
    Material.IRON_INGOT,
    Material.GOLD_INGOT,
    Material.DIAMOND,
    // 石材
    Material.STONE,
    Material.COBBLESTONE,
    Material.DEEPSLATE,
    Material.COBBLED_DEEPSLATE,
    // 原木
    Material.OAK_LOG,
    Material.SPRUCE_LOG,
    Material.BIRCH_LOG,
    Material.JUNGLE_LOG,
    Material.ACACIA_LOG,
    Material.CHERRY_LOG,
    Material.DARK_OAK_LOG,
    Material.MANGROVE_LOG,
    // 去皮原木
    Material.STRIPPED_OAK_LOG,
    Material.STRIPPED_SPRUCE_LOG,
    Material.STRIPPED_BIRCH_LOG,
    Material.STRIPPED_JUNGLE_LOG,
    Material.STRIPPED_ACACIA_LOG,
    Material.STRIPPED_CHERRY_LOG,
    Material.STRIPPED_DARK_OAK_LOG,
    Material.STRIPPED_MANGROVE_LOG,
)
fun Material.isWood() = this in WOOD_MATERIAL
fun Material.isVirtualResource() = this in VIRTUAL_RESOURCE_MATERIAL
fun Material.isRemoteWeapon() = this in REMOTE_WEAPON_MATERIAL
fun Material.isAuxiliaryBlock() = this in AUXILIARY_MATERIAL
fun Material.isFurnace() = this in FURNACE_MATERIAL
fun Material.isHoe() = this in HOE_MATERIAL
fun Material.isAxe() = this in AXE_MATERIAL
fun Material.isPickaxe() = this in PICKAXE_MATERIAL
fun Material.isOre() = this in ORE_MATERIAL

@SuppressWarnings("只应该被 MetadataHandler 和 MetadataModifier 使用")
const val META_PROJECTILE_EXTRA_DAMAGE = "EclipticStructure-ExtraDamage"
@SuppressWarnings("只应该被 MetadataHandler 和 MetadataModifier 使用")
const val META_PROJECTILE_POTION_EFFECT = "EclipticStructure-PotionEffect"

const val SPLITER_COLOR = "<#9C9C9C>"
const val SECONDARY_TEXT_COLOR = "<#E8E8E8>"
const val PRIMARY_TEXT_COLOR = "<#FFFAFA>"