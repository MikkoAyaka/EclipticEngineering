package org.wolflink.minecraft.plugin.eclipticengineering.monster.strategy

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.GameRoom
import org.wolflink.minecraft.plugin.eclipticengineering.monster.SpawnerAttribute
import org.wolflink.minecraft.plugin.eclipticengineering.utils.AttributeAPI
import org.wolflink.minecraft.plugin.eclipticengineering.utils.LocationAPI
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class PlayerFocusSpawnStrategy(spawnerAttribute: SpawnerAttribute) : SpawnStrategy(spawnerAttribute) {
    override fun isApplicable(player: Player): Boolean {
        return true
    }
    override fun singleSpawn(player: Player,location: Location,mobType: EntityType) {
        if(GameRoom.getFoothold()?.zone?.contains(location) == true) return
        val world = location.world
        val entity = world.spawnEntity(location, mobType)
        if (mobType == EntityType.RABBIT) {
            val rabbit = entity as Rabbit
            rabbit.rabbitType = Rabbit.Type.THE_KILLER_BUNNY
        } else if (mobType == EntityType.CREEPER) {
            val creeper = entity as Creeper
            if (random.nextDouble() < 0.06) creeper.isPowered = true
        } else if (entity is Monster) {
            AttributeAPI.multiplyAttribute(
                entity, "pf_health",
                Attribute.GENERIC_MAX_HEALTH, spawnerAttribute.healthMultiple
            )
            entity.health = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
            AttributeAPI.multiplyAttribute(
                entity, "pf_speed",
                Attribute.GENERIC_MOVEMENT_SPEED, spawnerAttribute.movementMultiple
            )
            AttributeAPI.multiplyAttribute(
                entity, "pf_attack",
                Attribute.GENERIC_ATTACK_DAMAGE, spawnerAttribute.damageMultiple
            )
        }
        appendMetadata(player, entity)
        callEvent(player, entity)
    }
    override fun spawn(player: Player,mobAmount: Int, triedTimes: Int) {
        if (triedTimes <= 0) return
        val locList: MutableList<Location> = ArrayList()
        val firstLoc = player.location
        val available = booleanArrayOf(true)
        val lastLoc = arrayOf(player.location)
        // 每 0.1 秒获取一次 Yaw
        val taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(EclipticEngineering.instance, Runnable {
            if (!available[0]) return@Runnable
            val playerLoc = player.location
            // 与初始坐标不在同一世界
            if (firstLoc.world !== playerLoc.world) {
                available[0] = false
                return@Runnable
            }
            // 在0.1秒移动了长达20格
            if (lastLoc[0].distance(playerLoc) >= 20) {
                available[0] = false
                return@Runnable
            }
            lastLoc[0] = playerLoc
            locList.add(playerLoc)
        }, 2, 2).taskId
        // 3秒后
        Bukkit.getScheduler().runTaskLaterAsynchronously(EclipticEngineering.instance,Runnable mainR@{
            Bukkit.getScheduler().cancelTask(taskId)
            if (!available[0]) {
                return@mainR  // 如果检测异常，则不生成怪物
            }
            val copyList: List<Location> = ArrayList(locList)
            val averX = copyList.stream().map { obj: Location -> obj.x }
                .reduce(0.0,Double::plus) / copyList.size
            val averY = copyList.stream().map { obj: Location -> obj.y }
                .reduce(0.0,Double::plus) / copyList.size
            val averZ = copyList.stream().map { obj: Location -> obj.z }
                .reduce(0.0,Double::plus) / copyList.size
            val averYaw = copyList.stream().map { obj: Location -> obj.yaw }
                .reduce(0.0f, Float::plus) / copyList.size
            // TODO 暂时忽略 pitch，未来可以考虑引入
            val averLocation = Location(firstLoc.getWorld(), averX, averY, averZ, averYaw, 0f)
            val minYaw = averYaw - 45
            val maxYaw = averYaw + 45
            val random: ThreadLocalRandom = ThreadLocalRandom.current()
            val randYaw: Float = random.nextFloat(minYaw, maxYaw)
            val randDistance: Double = random.nextDouble(SAFE_RADIUS.toDouble(), MAX_RADIUS.toDouble())
            val goalLocation: Location = LocationAPI.getLocationByAngle(averLocation, randYaw.toDouble(), randDistance)
            val summonLocation: Location? = LocationAPI.getNearestSurface(goalLocation, 16)
            if (summonLocation == null) {
                spawn(player, triedTimes - 1)
                return@mainR
            }
            Bukkit.getScheduler().runTask(EclipticEngineering.instance,Runnable subR@{
                val world = firstLoc.world!!
                if (!world.getNearbyEntities(
                        summonLocation, 8.0, 4.0, 8.0
                    ) { entity: Entity -> entity.type == EntityType.PLAYER }.isEmpty()
                ) {
                    spawn(player, triedTimes - 1)
                    return@subR
                }
                // 批量生成
                repeat(mobAmount) {
                    val entityType: EntityType = spawnerAttribute.randomType()
                    singleSpawn(player,summonLocation,entityType)
                }
            })
        }, 20 * 3L)
    }

    companion object {
        private const val SAFE_RADIUS = 20
        private const val MAX_RADIUS = 40
    }
}
