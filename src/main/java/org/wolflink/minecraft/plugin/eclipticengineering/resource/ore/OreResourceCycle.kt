package org.wolflink.minecraft.plugin.eclipticengineering.resource.ore

import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.PRIMARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem
import org.wolflink.minecraft.plugin.eclipticengineering.resource.ResourceCycle
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.SpecialDiamond
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.SpecialGold
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.SpecialIron
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.SpecialItem

class OreResourceCycle: ResourceCycle() {
    override val finalBlockData: BlockData
        get() = when {
            number <= 0.03 -> {
                Material.DIAMOND_ORE.createBlockData()
            }
            number <= 0.15 -> {
                Material.GOLD_ORE.createBlockData()
            }
            else -> {
                Material.IRON_ORE.createBlockData()
            }
        }
    override val droppedItem: ItemStack
        get() = when {
            number <= 0.03 -> {
                SpecialDiamond.defaultItem
            }
            number <= 0.15 -> {
                SpecialGold.defaultItem
            }
            else -> {
                SpecialIron.defaultItem
            }
        }
    override val initialBlockData: BlockData
        get() = when {
            number <= 0.03 -> {
                Material.COBBLESTONE.createBlockData()
            }
            number <= 0.15 -> {
                Material.COBBLESTONE.createBlockData()
            }
            else -> {
                Material.STONE.createBlockData()
            }
        }
}