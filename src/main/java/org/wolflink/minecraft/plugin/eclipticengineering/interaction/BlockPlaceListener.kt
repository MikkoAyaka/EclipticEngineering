package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import kotlinx.coroutines.launch
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureZoneRelationRepository
import org.wolflink.minecraft.plugin.eclipticstructure.repository.ZoneRepository

object BlockPlaceListener: Listener {
    private suspend fun inStructureZone(location: Location) = ZoneRepository.findByLocation(location)
        .mapNotNull { StructureZoneRelationRepository.find1(it) }
        .isNotEmpty()
    @EventHandler
    fun on(e: BlockPlaceEvent) {
        EEngineeringScope.launch {
            if(inStructureZone(e.block.location)) {
                e.block.breakNaturally()
            }
        }
    }
}