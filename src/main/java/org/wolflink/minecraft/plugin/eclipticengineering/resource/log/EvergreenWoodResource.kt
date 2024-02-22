package org.wolflink.minecraft.plugin.eclipticengineering.resource.log

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.data.BlockData
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.resource.ResourceBlock
import org.wolflink.minecraft.plugin.eclipticengineering.resource.ResourceCycle
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.EvergreenWood
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure

class EvergreenWoodResource(structure: Structure, location: Location): ResourceBlock(
    structure,
    location,
    EvergreenWoodResourceCycle(),
    75,
    10,
    Sound.ITEM_AXE_WAX_OFF,
    Ability.LOGGING,
    2
)

private class EvergreenWoodResourceCycle: ResourceCycle() {
    override val initialBlockData: BlockData = Material.OAK_LOG.createBlockData()
    override val finalBlockData: BlockData = Material.STRIPPED_OAK_LOG.createBlockData()
    override val droppedItem: ItemStack = EvergreenWood.defaultItem
}