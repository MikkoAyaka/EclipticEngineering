package org.wolflink.minecraft.plugin.eclipticengineering.monster.strategy

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.monster.SpawnerAttribute
import org.wolflink.minecraft.plugin.eclipticengineering.utils.AttributeAPI
import org.wolflink.minecraft.plugin.eclipticengineering.utils.BlockAPI
import java.util.*

class OceanSpawnStrategy(spawnerAttribute: SpawnerAttribute) : SpawnStrategy(spawnerAttribute) {
    private val random = Random()
    override fun isApplicable(player: Player): Boolean {
        return BlockAPI.checkIsOcean(player.location)
    }

    override fun spawn(player: Player, triedCount: Int) {
        if (triedCount <= 0) return
        val world = player.world
        val x = player.location.blockX
        val y = player.location.blockY
        val z = player.location.blockZ
        val newX = x + random.nextInt(SAFE_RADIUS, SAFE_RADIUS + 10)
        val newZ = z + random.nextInt(SAFE_RADIUS, SAFE_RADIUS + 10)
        var newY = y + random.nextInt(-4, 4)
        if (player.world.getBlockAt(newX, newY, newZ).type.isSolid()) {
            spawn(player, triedCount - 1)
            return
        }
        if (newY > player.world.getHighestBlockYAt(newX, newZ)) newY = player.world.getHighestBlockYAt(newX, newZ)
        val summonLocation = Location(player.world, newX.toDouble(), newY.toDouble(), newZ.toDouble())
        Bukkit.getScheduler().runTask(EclipticEngineering.instance, Runnable {
            if (!world.getNearbyEntities(
                    summonLocation,
                    8.0,
                    4.0,
                    8.0
                ) { entity: Entity -> entity.type == EntityType.PLAYER }
                    .isEmpty()
            ) {
                spawn(player, triedCount - 1)
                return@Runnable
            }
            val entityType = if (random.nextDouble() < 0.85) EntityType.DROWNED else EntityType.GUARDIAN
            val monster = world.spawnEntity(summonLocation, entityType) as Monster
            AttributeAPI.multiplyAttribute(
                monster, "o_health",
                Attribute.GENERIC_MAX_HEALTH, spawnerAttribute.healthMultiple
            )
            monster.health = monster.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
            AttributeAPI.multiplyAttribute(
                monster, "o_speed",
                Attribute.GENERIC_MOVEMENT_SPEED, spawnerAttribute.movementMultiple
            )
            AttributeAPI.multiplyAttribute(
                monster, "o_attack",
                Attribute.GENERIC_ATTACK_DAMAGE, spawnerAttribute.damageMultiple
            )
            appendMetadata(player, monster)
            callEvent(player, monster)
        })
    }

    companion object {
        private const val SAFE_RADIUS = 8
    }
}
