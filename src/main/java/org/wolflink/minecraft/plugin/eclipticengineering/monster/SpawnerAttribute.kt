package org.wolflink.minecraft.plugin.eclipticengineering.monster

import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.EEDifficulty
import org.wolflink.minecraft.plugin.eclipticengineering.eeLogger
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * 怪物生成器的属性类
 */

class SpawnerAttribute(eeDifficulty: EEDifficulty) {
    var spawnPeriodSecs = 0
        private set
        get() {
            // 夜晚刷怪效率增加
            return if(DayNightHandler.status == DayNightHandler.Status.NIGHT) (field / 1.8).toInt()
            else field
        }
    private val weightMap: MutableMap<EntityType, Int> = EnumMap(EntityType::class.java)
    var healthMultiple = 0.0
        get() {
            return if(DayNightHandler.status == DayNightHandler.Status.NIGHT) field * 1.8
            else field
        }
    var movementMultiple = 0.0
        get() {
            return if(DayNightHandler.status == DayNightHandler.Status.NIGHT) field * 1.2
            else field
        }
    var damageMultiple = 0.0
        get() {
            return if(DayNightHandler.status == DayNightHandler.Status.NIGHT) field * 1.2
            else field
        }

    init {
        when (eeDifficulty) {
            EEDifficulty.EASY -> { // 轻松
                healthMultiple = 0.5
                movementMultiple = 0.8
                damageMultiple = 0.5
                spawnPeriodSecs = 30
                weightMap[EntityType.ZOMBIE] = 50
                weightMap[EntityType.HUSK] = 50
                weightMap[EntityType.ZOMBIE_VILLAGER] = 50
                weightMap[EntityType.SKELETON] = 20
                weightMap[EntityType.STRAY] = 20
                weightMap[EntityType.SILVERFISH] = 20
                weightMap[EntityType.SPIDER] = 40
                weightMap[EntityType.CREEPER] = 20
            }

            EEDifficulty.NORMAL -> { // 常规
                healthMultiple = 0.8
                movementMultiple = 1.0
                damageMultiple = 0.6
                spawnPeriodSecs = 28
                weightMap[EntityType.ZOMBIE] = 50
                weightMap[EntityType.HUSK] = 50
                weightMap[EntityType.ZOMBIE_VILLAGER] = 50
                weightMap[EntityType.SKELETON] = 20
                weightMap[EntityType.STRAY] = 20
                weightMap[EntityType.SILVERFISH] = 20
                weightMap[EntityType.SPIDER] = 40
                weightMap[EntityType.CREEPER] = 20
            }

            EEDifficulty.HARD -> { // 困难
                healthMultiple = 1.05
                movementMultiple = 1.1
                damageMultiple = 1.0
                spawnPeriodSecs = 16
                weightMap[EntityType.ZOMBIE] = 45
                weightMap[EntityType.HUSK] = 45
                weightMap[EntityType.ZOMBIE_VILLAGER] = 45
                weightMap[EntityType.SKELETON] = 20
                weightMap[EntityType.STRAY] = 20
                weightMap[EntityType.SILVERFISH] = 20
                weightMap[EntityType.SPIDER] = 40
                weightMap[EntityType.CREEPER] = 20
                weightMap[EntityType.VEX] = 4
                weightMap[EntityType.VINDICATOR] = 5
                weightMap[EntityType.WITCH] = 5
                weightMap[EntityType.RABBIT] = 1
            }
            EEDifficulty.HADES -> { // 专家
                healthMultiple = 1.25
                movementMultiple = 1.25
                damageMultiple = 1.2
                spawnPeriodSecs = 12
                weightMap[EntityType.ZOMBIE] = 40
                weightMap[EntityType.HUSK] = 40
                weightMap[EntityType.ZOMBIE_VILLAGER] = 40
                weightMap[EntityType.SKELETON] = 20
                weightMap[EntityType.STRAY] = 20
                weightMap[EntityType.SILVERFISH] = 20
                weightMap[EntityType.SPIDER] = 40
                weightMap[EntityType.CREEPER] = 20
                weightMap[EntityType.VINDICATOR] = 5
                weightMap[EntityType.VEX] = 4
                weightMap[EntityType.WITCH] = 5
                weightMap[EntityType.RABBIT] = 1
            }
        }
    }

    /**
     * 根据权重随机获得怪物类型
     */
    fun randomType(): EntityType {
        val random: ThreadLocalRandom = ThreadLocalRandom.current()
        val totalWeight =
            weightMap.values.stream().reduce(0) { a: Int, b: Int -> Integer.sum(a, b) }
        var randomInt: Int = random.nextInt(totalWeight)
        for ((key, value) in weightMap) {
            if (randomInt < value) return key
            randomInt -= value
        }
        // 不可触及
        eeLogger.warning("怪物类型随机时出现问题。")
        return EntityType.ZOMBIE
    }
}
