package org.wolflink.minecraft.plugin.eclipticengineering.roleplay

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.Times
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticstructure.extension.call
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
    enum class Status(val displayName: String,val description: String,val minutes: Int,val gameTime: Int) {
        DAWN("黎明","$MESSAGE_PREFIX 太阳即将升起，新的一天就要到来了。",1,23500),
        DAY("白天","",12,6000), // 新的一天从这里开始
        HUSK("黄昏","$MESSAGE_PREFIX 太阳落山了，开拓者们，是时候回去休息了。",1,12800),
        NIGHT("夜晚","",4,18000)
    }
    var status = Status.NIGHT
        private set(value) {
            if(value == field) return
            field = value
            EclipticEngineering.runTask {
                DayNightEvent(field).call()
                Config.gameWorld.time = field.gameTime.toLong()
                if(field.description.isNotEmpty()) {
                    Bukkit.broadcast(field.description.toComponent())
                }
                if(field == Status.DAY) {
                    days++
                    gamingPlayers.forEach{
                        it.showTitle(Title.title(
                            "<#FFFFFF>白昼".toComponent(),
                            "<#E0E0E0>距离夜晚降临还有 <white>${status.minutes} <#E0E0E0>分钟".toComponent(),
                            Times.times(Duration.ofMillis(500), Duration.ofMillis(1500),Duration.ofMillis(500)))
                        )
                        it.playSound(it, Sound.ENTITY_CHICKEN_AMBIENT,1f,1f)
                    }
                }
                if(field == Status.NIGHT) {
                    gamingPlayers.forEach{
                        it.showTitle(Title.title(
                            "<#003366>深夜".toComponent(),
                            "<#E0E0E0>距离白天到来还有 <white>${status.minutes} <#E0E0E0>分钟".toComponent(),
                            Times.times(Duration.ofMillis(500), Duration.ofMillis(1500),Duration.ofMillis(500)))
                        )
                        it.playSound(it, Sound.ENTITY_WOLF_HOWL,1f,1f)
                    }
                }
            }
        }

    private var available = false
    fun start() {
        available = true
        status = Status.DAY
        EEngineeringScope.launch { dayNightPass() }
    }
    private suspend fun dayNightPass() {
        while (available) {
            delay(1000L * 60 * status.minutes)
            status = Status.entries[(status.ordinal+1) % 4]
        }
    }
}