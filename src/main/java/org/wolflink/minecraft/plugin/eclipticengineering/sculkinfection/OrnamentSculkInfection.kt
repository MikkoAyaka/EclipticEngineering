package org.wolflink.minecraft.plugin.eclipticengineering.sculkinfection

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.stage.EndGameStage
import org.wolflink.minecraft.plugin.eclipticengineering.stage.StageHolder
import org.wolflink.minecraft.plugin.eclipticengineering.utils.BlockAPI
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.wolfird.framework.bukkit.scheduler.SubScheduler
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object OrnamentSculkInfection {
    val sculkTypes = setOf(Material.SCULK, Material.SCULK_CATALYST)

    /**
     * 感染值
     */
    private val infectionMap: MutableMap<UUID, Int> = ConcurrentHashMap<UUID, Int>()
    private val subScheduler: SubScheduler = SubScheduler()


    private val listener = SculkInfectionListener(this)

    /**
     * 增加感染值
     */
    fun addInfectionValue(player: Player, value: Int) {
        val pUuid: UUID = player.uniqueId
        // 喝了牛奶不再增加感染值
        if (value > 0 && listener.milkPlayers.contains(pUuid)) return
        val oldValue = getInfectionValue(player.uniqueId)
        var newValue = oldValue + value
        if (newValue < 0) newValue = 0
        if (newValue >= 1200) newValue = 1200
        infectionMap[pUuid] = newValue
    }

    fun getInfectionValue(uuid: UUID): Int {
        return infectionMap.getOrDefault(uuid, 0)
    }

    /**
     * 刷新玩家的感染值
     * 玩家站在潜声方块上，每秒获得 20 点感染值
     * 每秒获得 附近6格内潜声方块数量x1.25 - 20 点感染值，，最多检测64个方块
     * 如果不处在附近，则每秒 -20 点感染值
     * 牛奶可以减少 500 点感染值
     *
     *
     * 轻度感染 达到 300 点 间歇性虚弱+间歇性挖掘疲劳+走过的方块有概率变成潜声方块
     * 中度感染 达到 600 点 虚弱+挖掘疲劳+缓慢+走过的方块有概率变成潜声方块
     * 重度感染 达到 1000 点 虚弱+挖掘疲劳+走过的方块有概率变成潜声方块+凋零+缓慢+失明
     */
    private fun updateInfectionValue(player: Player) {
        if(player.location.world != Config.gameWorld) return
        val nearbySculks: List<Location> = BlockAPI.searchBlock(Material.SCULK, player.location, 6)
        var sculkAmount = (nearbySculks.size * 1.25).toInt()
        if (sculkAmount >= 40) sculkAmount = 40
        addInfectionValue(player, sculkAmount - 20)
        val blockType = player.location.add(0.0, -1.0, 0.0).block.type
        if (sculkTypes.contains(blockType)) addInfectionValue(player, 10)
    }

    private fun applyInfectionEffect(player: Player, value: Int) {
        val random = Random()
        val randDouble = random.nextDouble()
        // 玩家已经不在任务或者任务不在最终阶段
        if (StageHolder.thisStage !is EndGameStage) {
            infectionMap.remove(player.uniqueId)
            return
        }
        // 其它世界保护
        if (player.world != Config.gameWorld) return
        if (value >= 1000) {
            player.playSound(player.location, Sound.BLOCK_SCULK_CHARGE, 1f, 1f)
            player.sendActionBar("$MESSAGE_PREFIX <red>你被幽匿方块严重感染了！".toComponent())
            player.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 40, 0, false, false, false))
            player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 40, 1, false, false, false))
            player.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 40, 0, false, false, false))
            player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 0, false, false, false))
            val material: Material = if (random.nextDouble() <= 0.2) Material.SCULK_CATALYST else Material.SCULK
            player.location.clone().add(0.0, -1.0, 0.0).block.type = material
        } else if (value >= 600) {
            player.playSound(player.location, Sound.BLOCK_SCULK_CHARGE, 1f, 1f)
            player.sendActionBar("$MESSAGE_PREFIX <purple>你变得寸步难行...".toComponent())
            player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 40, 0, false, false, false))
            player.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 40, 0, false, false, false))
            player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 0, false, false, false))
            if (randDouble <= 0.6) {
                val material: Material = if (random.nextDouble() <= 0.15) Material.SCULK_CATALYST else Material.SCULK
                val location = player.location.clone().add(0.0, -1.0, 0.0)
                if (location.block.type.isSolid) location.block.type = material
            }
        } else if (value >= 300) {
            player.playSound(player.location, Sound.BLOCK_SCULK_CHARGE, 1f, 1f)
            player.sendActionBar("$MESSAGE_PREFIX <yellow>你感到有些不适...".toComponent())
            player.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 10, 0, false, false, false))
            player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, 10, 0, false, false, false))
            if (randDouble <= 0.3) {
                val material: Material = if (random.nextDouble() <= 0.1) Material.SCULK_CATALYST else Material.SCULK
                player.location.clone().add(0.0, -1.0, 0.0).block.type = material
            }
        }
    }

    fun enable() {
        listener.setEnabled(true)
        subScheduler.runTaskTimerAsync({ gamingPlayers.forEach { updateInfectionValue(it) } },
            20, 20
        )
        subScheduler.runTaskTimer({
            infectionMap.forEach { (uuid: UUID, value: Int) ->
                val player: Player? = Bukkit.getPlayer(uuid)
                if (player == null || !player.isOnline) return@forEach
                applyInfectionEffect(player, value)
            }
        }, 20, 20)
    }

    fun disable() {
        listener.setEnabled(false)
        subScheduler.cancelAllTasks()
    }
}