package org.wolflink.minecraft.plugin.eclipticengineering.requirement

import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.resource.VirtualTeamInventory

class VirtualRequirement(description: String,private val virtualResourceType: VirtualResourceType,private val amount: Int): Requirement(description) {
    override fun delivery(player: Player?): Boolean {
        return VirtualTeamInventory.take(virtualResourceType,amount)
    }
    override fun isSatisfy(player: Player?): Boolean {
        return VirtualTeamInventory.has(virtualResourceType,amount)
    }
}