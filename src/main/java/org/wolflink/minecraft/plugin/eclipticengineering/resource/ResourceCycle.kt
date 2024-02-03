package org.wolflink.minecraft.plugin.eclipticengineering.resource

import org.bukkit.block.data.BlockData
import org.bukkit.inventory.ItemStack

abstract class ResourceCycle(var number: Double) {
    abstract val initialBlockData: BlockData
    abstract val finalBlockData: BlockData
    abstract val droppedItem: ItemStack
}
