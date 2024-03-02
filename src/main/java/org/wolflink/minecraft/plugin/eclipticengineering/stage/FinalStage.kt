package org.wolflink.minecraft.plugin.eclipticengineering.stage

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.GameRoom
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage
import java.util.*

class FinalStage(stageHolder: StageHolder) : Stage("结算阶段", stageHolder) {
    override fun onEnter() {
        Bukkit.broadcast("".toComponent())
        if(GameRoom.result == GameRoom.Result.DISGUISER_WIN) {
            Bukkit.broadcast("      <#7FFF00>本场获胜 <#8A2BE2>幽匿伪装者      ".toComponent())
        } else if(GameRoom.result == GameRoom.Result.PIONEER_WIN) {
            Bukkit.broadcast("      <#7FFF00>本场获胜  <#FFF5EE>开拓者        ".toComponent())
        }
        Bukkit.broadcast("".toComponent())
        val calendar = Calendar.getInstance()
        Bukkit.getOnlinePlayers().forEach {
            it.playSound(it, Sound.UI_TOAST_CHALLENGE_COMPLETE,1.5f,1.2f)
            it.gameMode = GameMode.CREATIVE
            EEngineeringScope.launch {
                while (Calendar.getInstance().timeInMillis - calendar.timeInMillis <= 16000) {
                    val random = Random()
                    val type = FireworkEffect.Type.BALL_LARGE
                    val r1 = random.nextInt(256)
                    val g1 = random.nextInt(256)
                    val b1 = random.nextInt(256)
                    val r2 = random.nextInt(256)
                    val g2 = random.nextInt(256)
                    val b2 = random.nextInt(256)
                    val c1 = Color.fromRGB(r1, g1, b1)
                    val c2 = Color.fromRGB(r2, g2, b2)
                    EclipticEngineering.runTask {
                        val firework = it.world
                            .spawnEntity(it.location, EntityType.FIREWORK) as Firework
                        val fireworkMeta = firework.fireworkMeta
                        val fireworkEffect =
                            FireworkEffect.builder().flicker(true).withColor(c1).withFade(c2).with(type).trail(true)
                                .build()
                        fireworkMeta.addEffect(fireworkEffect)
                        fireworkMeta.power = random.nextInt(2) + 1
                        firework.fireworkMeta = fireworkMeta
                    }
                    delay(2000)
                }
            }
        }
        EEngineeringScope.launch {
            Bukkit.broadcast("$MESSAGE_PREFIX 房间将在 30 秒后关闭重置，需要大约 2 分钟。".toComponent())
            delay(30000)
            EclipticEngineering.runTask { Bukkit.getServer().shutdown() }
        }
    }
    override fun onLeave() {
    }
}