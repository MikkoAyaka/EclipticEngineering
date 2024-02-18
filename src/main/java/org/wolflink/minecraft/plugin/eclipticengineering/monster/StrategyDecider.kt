package org.wolflink.minecraft.plugin.eclipticengineering.monster

import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.config.GameSettings
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.monster.strategy.OceanSpawnStrategy
import org.wolflink.minecraft.plugin.eclipticengineering.monster.strategy.PlayerFocusSpawnStrategy
import org.wolflink.minecraft.plugin.eclipticengineering.monster.strategy.SpawnStrategy
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.wolfird.framework.bukkit.scheduler.SubScheduler
import java.time.Duration
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList
import kotlin.math.ln

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
    private const val HUDDLE_RADIUS = 8
    private lateinit var spawnerAttribute: SpawnerAttribute
    private var spawnPeriodSecs: Int = 60
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
        spawnPeriodSecs = spawnerAttribute.spawnPeriodSecs
        strategyList = listOf(
            OceanSpawnStrategy(spawnerAttribute),
            PlayerFocusSpawnStrategy(spawnerAttribute)
        )
    }

    fun enable() {
        init()
        gamingPlayers.forEach {
            it.showTitle(Title.title("".toComponent(),"<yellow>怪物们将在90秒后来袭，请做好准备...".toComponent(),
                Title.Times.times(Duration.ofMillis(400), Duration.ofMillis(1000), Duration.ofMillis(400))))
            it.playSound(it,Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 0.8f)
        }
        subScheduler.runTaskLaterAsync({
            subScheduler.runTaskTimerAsync(
                { updateStrategyMap() },
                20L * DECIDE_PERIOD_SECS,
                20L * DECIDE_PERIOD_SECS
            )
            subScheduler.runTaskTimerAsync(
                { spawnTask() },
                20L * spawnPeriodSecs,
                20L * spawnPeriodSecs
            )
            subScheduler.runTaskTimerAsync(
                { updateAttribute() },
                20L * 60, 20L * 60
            )
            gamingPlayers.forEach {
                it.showTitle(Title.title("<red>它们来了！".toComponent(),"".toComponent(),
                    Title.Times.times(Duration.ofMillis(400), Duration.ofMillis(1000), Duration.ofMillis(400))))
                it.playSound(it,Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1f, 1f)
            }
        }, 20L * 90)
    }

    private fun updateStrategyMap() {
        playerStrategyMap.clear()
        for (player in gamingPlayers) {
            val strategy: SpawnStrategy = decide(player)
            playerStrategyMap[player.uniqueId] = strategy
        }
    }

    /**
     * 每1分钟 +0.8% 血量
     * 每1分钟 +1.2% 攻击
     */
    private fun updateAttribute() {
        spawnerAttribute.healthMultiple += 0.008
        spawnerAttribute.damageMultiple += 0.012
    }

    private fun getEfficiencyReduction(playerAmount: Int): Double {
        return 1 - (1.25 * ln(playerAmount.toDouble()) + 1.0) / playerAmount
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
     * 多位玩家抱团时降低刷怪效率
     */
    private fun spawnTask() {
        for ((key, value) in playerStrategyMap) {
            val player: Player = Bukkit.getPlayer(key) ?: continue
            if (!player.isOnline || player !in gamingPlayers) continue
            val location = player.location
            val nearbyPlayerAmount = getHuddlePlayersAmount(location)
            if (Math.random() >= getEfficiencyReduction(nearbyPlayerAmount)) value.spawn(player)
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
