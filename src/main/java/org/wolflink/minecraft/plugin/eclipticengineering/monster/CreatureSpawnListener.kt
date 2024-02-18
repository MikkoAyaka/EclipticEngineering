package org.wolflink.minecraft.plugin.eclipticengineering.monster

import org.bukkit.entity.Monster
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent

object CreatureSpawnListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onCreatureSpawn(e: CreatureSpawnEvent) {
        if (e.entity !is Monster) return
        if (
            e.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL
            || e.spawnReason == CreatureSpawnEvent.SpawnReason.DEFAULT
            || e.spawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER
            ) {
            e.isCancelled = true
        }
//        if (e.spawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER) {
//            e.entity.setMetadata("bySpawner", FixedMetadataValue(EclipticEngineering.instance, true))
//        }
    }
}
