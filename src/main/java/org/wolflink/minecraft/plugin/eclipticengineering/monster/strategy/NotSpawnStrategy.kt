package org.wolflink.minecraft.plugin.eclipticengineering.monster.strategy

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.monster.SpawnerAttribute
import org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold.LivingHouse
import org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold.MeetingPlace
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository

class NotSpawnStrategy(spawnerAttribute: SpawnerAttribute) : SpawnStrategy(spawnerAttribute)  {

    override fun isApplicable(player: Player): Boolean {
        val livingHouse = StructureRepository.findBy { it is LivingHouse }.firstOrNull()
        val meetingPlace = StructureRepository.findBy { it is MeetingPlace }.firstOrNull()
        return livingHouse?.zone?.contains(player.location) == true || meetingPlace?.zone?.contains(player.location) == true
    }

    override fun spawn(player: Player, mobAmount: Int, triedTimes: Int) {
    }

    /**
     * 生成单个怪物
     */
    override fun singleSpawn(player: Player, location: Location, mobType: EntityType) {
    }
}