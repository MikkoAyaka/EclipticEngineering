package org.wolflink.minecraft.plugin.eclipticengineering.resource.crop

import org.bukkit.Material
import org.bukkit.block.data.Ageable
import org.bukkit.block.data.BlockData
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.PRIMARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem
import org.wolflink.minecraft.plugin.eclipticengineering.resource.ResourceCycle

class CropResourceCycle: ResourceCycle() {
    override val finalBlockData: BlockData
        get() = when {
            number <= 0.25 -> {
                Material.WHEAT_SEEDS.createBlockData().apply {
                    (this as Ageable).age = maximumAge
                }
            }
            number <= 0.5 -> {
                Material.POTATOES.createBlockData().apply {
                    (this as Ageable).age = maximumAge
                }
            }
            number <= 0.75 -> {
                Material.CARROTS.createBlockData().apply {
                    (this as Ageable).age = maximumAge
                }
            }
            else -> {
                Material.BEETROOTS.createBlockData().apply {
                    (this as Ageable).age = maximumAge
                }
            }
        }
    override val droppedItem: ItemStack
        get() = when {
            number <= 0.25 -> {
                Material.WHEAT.createSpecialItem(
                    SpecialItemType.SPECIAL_RESOURCE,
                    Quality.RARE,
                    "金穗",
                    false,
                    listOf("    ${PRIMARY_TEXT_COLOR}穗端闪耀着夏日阳光的金色之礼，","    ${PRIMARY_TEXT_COLOR}孕育了一季的希望与丰收。")
                )
            }
            number <= 0.5 -> {
                Material.POTATO.createSpecialItem(
                    SpecialItemType.SPECIAL_RESOURCE,
                    Quality.RARE,
                    "泥芋",
                    false,
                    listOf("    ${PRIMARY_TEXT_COLOR}隐于泥土之下，","    ${PRIMARY_TEXT_COLOR}蕴藏着朴实无华却滋养万物的力量。")
                )
            }
            number <= 0.75 -> {
                Material.CARROT.createSpecialItem(
                    SpecialItemType.SPECIAL_RESOURCE,
                    Quality.RARE,
                    "泥芋",
                    false,
                    listOf("    ${PRIMARY_TEXT_COLOR}隐于泥土之下，","    ${PRIMARY_TEXT_COLOR}蕴藏着朴实无华却滋养万物的力量。")
                )
            }
            else -> {
                Material.BEETROOT.createSpecialItem(
                    SpecialItemType.SPECIAL_RESOURCE,
                    Quality.RARE,
                    "甜葱",
                    false,
                    listOf("    ${PRIMARY_TEXT_COLOR}质地坚硬无比，","    ${PRIMARY_TEXT_COLOR}散发着淡淡的星光。")
                )
            }
        }
    override val initialBlockData: BlockData
        get() = when {
            number <= 0.25 -> {
                Material.WHEAT.createBlockData()
            }
            number <= 0.5 -> {
                Material.POTATOES.createBlockData()
            }
            number <= 0.75 -> {
                Material.CARROTS.createBlockData()
            }
            else -> {
                Material.BEETROOTS.createBlockData()
            }
        }
}