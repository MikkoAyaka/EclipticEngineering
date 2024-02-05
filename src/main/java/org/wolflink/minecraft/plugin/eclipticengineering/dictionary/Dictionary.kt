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
fun Material.isAuxiliaryBlock() = this in AUXILIARY_MATERIAL
fun Material.isFurnace() = this in FURNACE_MATERIAL
fun Material.isHoe() = this in HOE_MATERIAL
fun Material.isAXe() = this in AXE_MATERIAL
fun Material.isPickaxe() = this in PICKAXE_MATERIAL
fun Material.isOre() = this in ORE_MATERIAL
