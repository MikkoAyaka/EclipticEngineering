package org.wolflink.minecraft.plugin.eclipticengineering.resource

import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem
import org.wolflink.minecraft.plugin.eclipticstructure.extension.PRIMARY_TEXT_COLOR

class OreResourceCycle(number: Double): ResourceCycle(number) {
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
                Material.DIAMOND.createSpecialItem(
                    SpecialItemType.SPECIAL_ORE,
                    Quality.EPIC,
                    "辉晶矿石",
                    true,
                    listOf("    ${PRIMARY_TEXT_COLOR}每一面切割都精准地捕捉到了天际的璀璨，","    ${PRIMARY_TEXT_COLOR}仿佛夜空最明亮的星辰凝聚而成。")
                )
            }
            number <= 0.15 -> {
                Material.RAW_GOLD.createSpecialItem(
                    SpecialItemType.SPECIAL_ORE,
                    Quality.EXQUISITE,
                    "闪金矿石",
                    false,
                    listOf("    ${PRIMARY_TEXT_COLOR}携带着星辰的记忆，","    ${PRIMARY_TEXT_COLOR}每一粒金砂都闪烁着过往光年的辉煌。")
                )
            }
            else -> {
                Material.RAW_IRON.createSpecialItem(
                    SpecialItemType.SPECIAL_ORE,
                    Quality.RARE,
                    "陨铁矿石",
                    false,
                    listOf("    ${PRIMARY_TEXT_COLOR}质地坚硬无比，","    ${PRIMARY_TEXT_COLOR}散发着淡淡的星光。")
                )
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