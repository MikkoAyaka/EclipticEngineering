package org.wolflink.minecraft.plugin.eclipticengineering.monster.strategy

import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.monster.SpawnerAttribute
import org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold.LivingHouse
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository

class NotSpawnStrategy(spawnerAttribute: SpawnerAttribute) : SpawnStrategy(spawnerAttribute)  {

    override fun isApplicable(player: Player): Boolean {
        val livingHouse = StructureRepository.findBy { it is LivingHouse }.firstOrNull() ?: return false
        return player.location in livingHouse.zone
    }

    override fun spawn(player: Player, triedCount: Int) {
    }
}