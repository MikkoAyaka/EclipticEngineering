package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureZoneRelationRepository
import org.wolflink.minecraft.plugin.eclipticstructure.repository.ZoneRepository

object BlockPlaceListener: Listener {
    private suspend fun inStructureZone(location: Location) = ZoneRepository.findByLocation(location)
        .mapNotNull { StructureZoneRelationRepository.find1(it) }
        .isNotEmpty()
    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    fun on(e: BlockPlaceEvent) {
        EEngineeringScope.launch {
            if(inStructureZone(e.block.location)) {
                EclipticEngineering.runTask {
                    e.block.breakNaturally()
                }
            }
        }
    }
}