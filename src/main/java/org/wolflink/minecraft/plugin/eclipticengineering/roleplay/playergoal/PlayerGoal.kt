package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import org.bukkit.Color
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticstructure.extension.register
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toHexFormat
import org.wolflink.minecraft.plugin.eclipticstructure.extension.unregister
import java.util.Calendar

/**
 * 玩家目标，编写完成后需要前往 PlayerGoalHolder 注册
 */
abstract class PlayerGoal(val disguiser: Player,val difficulty: Difficulty) : Listener {
    enum class Difficulty(val displayName: String,val color: Color) {
        EASY("简单",Color.fromRGB(0,255,127)),
        NORMAL("普通", Color.fromRGB(0,191,255)),
        HARD("困难",Color.fromRGB(160,32,240)),
        HADES("炼狱",Color.fromRGB(255,48,48))
    }
    enum class Status {
        IN_PROGRESS,
        FINISHED,
        FAILED
    }
    abstract val description: String

    fun formatDescription() = "${difficulty.color.toHexFormat()}${difficulty.displayName} $description"
    /**
     * 该目标是否可以正常使用(可以在此处进行条件检测)
     */
    open fun available(): Boolean {
        return true
    }

    /**
     * 该目标当前是否已激活(启用相关检测)
     */
    protected var enabled = false
        private set
    var status = Status.IN_PROGRESS
        private set
    fun enable() {
        if(enabled) return
        enabled = true
        triggerTime = Calendar.getInstance()
        disguiser.sendMessage("$MESSAGE_PREFIX 将在20秒后开始进行目标检测，请做好准备。".toComponent())
        init()
        this.register(EclipticEngineering.instance)
    }
    private var triggerTime = Calendar.getInstance()
    protected abstract fun init()

    /**
     * 一旦禁用后不允许再次启用
     */
    private fun disable() {
        if(!enabled) return
        enabled = false
        this.unregister()
    }

    /**
     * 通知玩家目标正在顺利进行
     */
    protected fun noticeInProgress() {
        disguiser.sendActionBar("$MESSAGE_PREFIX 目标正在顺利进行".toComponent())
    }
    fun finished() {
        if(!enabled) return
        if(Calendar.getInstance().timeInMillis - triggerTime.timeInMillis < 20 * 1000) return
        status = Status.FINISHED
        disguiser.playSound(disguiser,Sound.UI_TOAST_CHALLENGE_COMPLETE,0.5f,2f)
        disguiser.sendMessage("$MESSAGE_PREFIX 今日目标已完成。".toComponent())
        PlayerGoalHolder.addFinishCount(disguiser)
        disable()
    }
    fun failed() {
        if(!enabled) return
        if(Calendar.getInstance().timeInMillis - triggerTime.timeInMillis < 20 * 1000) return
        status = Status.FAILED
        disguiser.playSound(disguiser,Sound.ENTITY_WOLF_HOWL,0.5f,1.5f)
        disguiser.sendMessage("$MESSAGE_PREFIX 今日目标失败，等待明日刷新。".toComponent())
        disable()
    }
}