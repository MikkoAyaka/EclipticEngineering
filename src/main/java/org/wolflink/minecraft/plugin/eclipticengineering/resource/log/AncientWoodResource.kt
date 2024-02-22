package org.wolflink.minecraft.plugin.eclipticengineering.resource.log

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.data.BlockData
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.PRIMARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem
import org.wolflink.minecraft.plugin.eclipticengineering.resource.ResourceBlock
import org.wolflink.minecraft.plugin.eclipticengineering.resource.ResourceCycle
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.AncientWood
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure

// TODO 需要重新制作建筑结构
class AncientWoodResource(structure: Structure, location: Location): ResourceBlock(
    structure,
    location,
    AncientWoodResourceCycle(),
    75,
    8,
    Sound.ITEM_AXE_WAX_OFF,
    Ability.LOGGING,
    2
)
private class AncientWoodResourceCycle: ResourceCycle() {
    override val initialBlockData: BlockData = Material.OAK_LOG.createBlockData()
    override val finalBlockData: BlockData = Material.STRIPPED_OAK_LOG.createBlockData()
    override val droppedItem: ItemStack = AncientWood.defaultItem
}