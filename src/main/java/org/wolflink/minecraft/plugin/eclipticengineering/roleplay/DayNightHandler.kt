package org.wolflink.minecraft.plugin.eclipticengineering.roleplay

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.Times
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import java.time.Duration

/**
 * 昼夜管理者
 * 白天 16 分钟 夜晚 8 分钟
 */
object DayNightHandler {
    // 游戏天数
    var days = 0
        private set
    enum class Status(val minutes: Int) {
        DAY(16),NIGHT(8)
    }
    var status = Status.DAY
        private set(value) {
            if(value == field) return
            field = value
            DayNightEvent(field)
        }

    private var available = false
    fun start() {
        days = 1
        available = true
        status = Status.DAY
        Config.gameWorld.time = 6000
        EEngineeringScope.launch { toggleDayNight() }
    }
    private suspend fun toggleDayNight() {
        if(available)
        delay(1000L * 60 * status.minutes - 1)
        if(status == Status.DAY) {
            Config.gameWorld.time = 12800
            Bukkit.broadcast("$MESSAGE_PREFIX 太阳落山了，开拓者们，是时候回去休息了。".toComponent())
            delay(1000 * 60)
            Config.gameWorld.time = 18000
            status = Status.NIGHT
            gamingPlayers.forEach{
                it.showTitle(Title.title(
                    "<#003366>深夜".toComponent(),
                    "<#E0E0E0>距离白天到来还有 <white>${status.minutes} <#E0E0E0>分钟".toComponent(),
                    Times.times(Duration.ofMillis(500), Duration.ofMillis(1500),Duration.ofMillis(500)))
                )
                it.playSound(it, Sound.ENTITY_WOLF_HOWL,1f,1f)
            }
        }
        if(status == Status.NIGHT) {
            Config.gameWorld.time = 23500
            Bukkit.broadcast("$MESSAGE_PREFIX 太阳升起，新的一天就要到来了。".toComponent())
            delay(1000 * 60)
            Config.gameWorld.time = 6000
            status = Status.DAY
            gamingPlayers.forEach{
                it.showTitle(Title.title(
                    "<#FFFFFF>白昼".toComponent(),
                    "<#E0E0E0>距离夜晚降临还有 <white>${status.minutes} <#E0E0E0>分钟".toComponent(),
                    Times.times(Duration.ofMillis(500), Duration.ofMillis(1500),Duration.ofMillis(500)))
                )
                it.playSound(it, Sound.ENTITY_CHICKEN_AMBIENT,1f,1f)
            }
            days++
        }
        toggleDayNight()
    }
}