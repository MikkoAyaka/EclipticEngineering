package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.eeLogger
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureZoneRelationRepository
import org.wolflink.minecraft.plugin.eclipticstructure.repository.ZoneRepository
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure

class PlaceShrieker(disguiser: Player): PlayerGoal(disguiser) {
    override val description = "在日落之前放置5个幽匿尖啸体在不同的建筑结构中"
    private val structureSet = mutableSetOf<Structure>()
    override fun init() {
        disguiser.inventory.addItem(ItemStack(Material.SCULK_SHRIEKER,5))
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    fun on(e: BlockPlaceEvent) {
        if(e.player == disguiser && e.block.type == Material.SCULK_SHRIEKER) {
            val structures = ZoneRepository.findByLocation(e.block.location).mapNotNull {
                StructureZoneRelationRepository.find1(it)
            }
            if(structures.size > 1) eeLogger.warning("不可能出现的情况：同一坐标内存在多个建筑结构，请检查代码。")
            val structure = structures.firstOrNull() ?: return
            if(structure in structureSet) return
            structureSet.add(structure)
            e.isCancelled = false
            if(structureSet.size >= 5) {
                finished()
            } else noticeInProgress()
        }
    }
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.NIGHT) failed() }

}