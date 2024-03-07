package org.wolflink.minecraft.plugin.eclipticengineering.roleplay

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.Times
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.GameRoom
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.config.GameSettings
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.stage.GameStage
import org.wolflink.minecraft.plugin.eclipticengineering.stage.StageHolder
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
        private set(value) {
            if(field == value) return
            field = value
            Bukkit.broadcast("$MESSAGE_PREFIX 距离最终决战还有 ${GameSettings.totalDays + 1 - field} 天时间。".toComponent())
            if(field == GameSettings.totalDays+1 && StageHolder.thisStage is GameStage) {
                EclipticEngineering.runTask { StageHolder.next() }
            }
        }
    enum class Status(val displayName: String,val description: String,val minutes: Int,val gameTime: Int) {
        DAWN("黎明","$MESSAGE_PREFIX 太阳即将升起，新的一天就要到来了。",1,23500),
        DAY("白天","",9,6000), // 新的一天从这里开始
        HUSK("黄昏","$MESSAGE_PREFIX 太阳落山了，到处弥漫着阴森的气息，强力怪物们就要到来了。",1,12800),
        NIGHT("夜晚","$MESSAGE_PREFIX 尽快回到居住屋吧，当然，你也可以不回去。",3,18000);
        fun next() = entries[(this.ordinal+1) % entries.size]
    }
    var status = Status.NIGHT
        private set(value) {
            if(value == field) return
            field = value
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
            if(field.description.isNotEmpty()) {
                Bukkit.broadcast(field.description.toComponent())
            }
            EclipticEngineering.runTask {
                DayNightEvent(field).call()
                Config.gameWorld.time = field.gameTime.toLong()
            }
        }

    private var available = false
    fun start() {
        available = true
        status = Status.DAY
        if(Config.debugMode) days = GameSettings.totalDays
        EEngineeringScope.launch { startTimer() }
    }
    fun stop() {
        available = false
        stopTimer()
    }
    //当前状态已持续时间(秒)
    private var statusSeconds = 0
    private var timePass = false
    /**
     * 开始记时
     */
    suspend fun startTimer() {
        timePass = true
        while (timePass) {
            delay(1000)
            statusSeconds++
            if(statusSeconds >= status.minutes * 60) {
                statusSeconds = 0
                status = status.next()
            }
        }
    }

    /**
     * 停止计时
     */
    fun stopTimer() {
        timePass = false
    }
}