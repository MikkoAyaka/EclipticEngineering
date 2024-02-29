package org.wolflink.minecraft.plugin.eclipticengineering.monster

import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.config.GameSettings
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.monster.strategy.OceanSpawnStrategy
import org.wolflink.minecraft.plugin.eclipticengineering.monster.strategy.PlayerFocusSpawnStrategy
import org.wolflink.minecraft.plugin.eclipticengineering.monster.strategy.SpawnStrategy
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.wolfird.framework.bukkit.scheduler.SubScheduler
import java.time.Duration
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 全局唯一决策者
 * 遍历并决策游戏中玩家当前的刷怪机制
 */
object StrategyDecider {
    /**
     * 决策周期(秒)
     */
    private const val DECIDE_PERIOD_SECS = 10

    /**
     * 判断抱团的半径(格)
     */
    private const val HUDDLE_RADIUS = 12
    private lateinit var spawnerAttribute: SpawnerAttribute
    private val subScheduler: SubScheduler = SubScheduler()

    /**
     * 玩家当前应用的决策
     */
    private val playerStrategyMap: MutableMap<UUID, SpawnStrategy> = ConcurrentHashMap<UUID, SpawnStrategy>()

    /**
     * 优先级从上往下，最上方的最优先进行决策
     */
    private lateinit var strategyList: List<SpawnStrategy>

    private fun init() {
        spawnerAttribute = SpawnerAttribute(GameSettings.difficulty)
        strategyList = listOf(
            OceanSpawnStrategy(spawnerAttribute),
            PlayerFocusSpawnStrategy(spawnerAttribute)
        )
    }

    fun enable() {
        init()
        gamingPlayers.forEach {
            it.showTitle(
                Title.title(
                    "".toComponent(), "<yellow>怪物们将在90秒后来袭，请做好准备...".toComponent(),
                    Title.Times.times(Duration.ofMillis(400), Duration.ofMillis(1000), Duration.ofMillis(400))
                )
            )
            it.playSound(it, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 0.8f)
        }
        spawnTimerTask()
        subScheduler.runTaskLaterAsync({
            subScheduler.runTaskTimerAsync(
                { updateStrategyMap() },
                20L * DECIDE_PERIOD_SECS,
                20L * DECIDE_PERIOD_SECS
            )
            subScheduler.runTaskTimerAsync(
                { updateAttribute() },
                20L * 60, 20L * 60
            )
            gamingPlayers.forEach {
                it.showTitle(
                    Title.title(
                        "<red>它们来了！".toComponent(), "".toComponent(),
                        Title.Times.times(Duration.ofMillis(400), Duration.ofMillis(1000), Duration.ofMillis(400))
                    )
                )
                it.playSound(it, Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1f, 1f)
            }
        }, 20L * 90)
    }

    private fun spawnTimerTask() {
        subScheduler.runTaskLaterAsync({
            spawnTask()
            spawnTimerTask()
        }, 20L * spawnerAttribute.spawnPeriodSecs)
    }

    private fun updateStrategyMap() {
        playerStrategyMap.clear()
        for (player in gamingPlayers) {
            val strategy: SpawnStrategy = decide(player)
            playerStrategyMap[player.uniqueId] = strategy
        }
    }

    /**
     * 预期总时长 90 分钟，夜晚不计入
     * 每1分钟 +1.0% 血量
     * 每1分钟 +0.8% 攻击
     */
    private fun updateAttribute() {
        // 夜晚不操作属性，否则会因为覆写 getter 溢出
        if(DayNightHandler.status == DayNightHandler.Status.NIGHT) return
        spawnerAttribute.healthMultiple += 0.01
        spawnerAttribute.damageMultiple += 0.008
    }

    private fun getSpawnEfficiency(playerAmount: Int): Double {
        return 1.0 / playerAmount
    }

    private fun getHuddlePlayersAmount(location: Location): Int {
        var amount = 0
        for (player in gamingPlayers) {
            if (player.world !== location.getWorld()) continue
            if (player.location.distance(location) <= HUDDLE_RADIUS) amount++
        }
        return amount
    }

    /**
     * 多位玩家抱团时降低刷怪效率(刷怪效率总和为 1.0 + 0.1 * 人数)
     */
    private fun spawnTask() {
        for ((key, value) in playerStrategyMap) {
            val player: Player = Bukkit.getPlayer(key) ?: continue
            if (!player.isOnline || player !in gamingPlayers) continue
            val location = player.location
            val nearbyPlayerAmount = getHuddlePlayersAmount(location)
            if (Math.random() < getSpawnEfficiency(nearbyPlayerAmount)) value.spawn(player)
        }
    }

    /**
     * 选择一个最合适该玩家的策略
     */
    private fun decide(player: Player): SpawnStrategy {
        for (spawnStrategy in strategyList) {
            if (spawnStrategy.isApplicable(player)) return spawnStrategy
        }
        return strategyList[0]
    }

    fun disable() {
        subScheduler.cancelAllTasks()
    }
}
