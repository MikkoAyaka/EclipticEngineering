package org.wolflink.minecraft.plugin.eclipticengineering.requirement

import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.resource.virtual.VirtualTeamInventory

class VirtualRequirement(private val virtualResourceType: VirtualResourceType,private val amount: Double): Requirement("需要 $amount ${virtualResourceType.displayName}") {
    constructor(virtualResourceType: VirtualResourceType,amount: Int): this(virtualResourceType,amount.toDouble())
    override fun delivery(player: Player?): Boolean {
        return VirtualTeamInventory.take(virtualResourceType,amount)
    }
    override fun isSatisfy(player: Player?): Boolean {
        return VirtualTeamInventory.has(virtualResourceType,amount)
    }
}