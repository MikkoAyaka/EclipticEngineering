package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.wolflink.minecraft.plugin.eclipticengineering.config.GameSettings
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.isDisguiser
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

object PlayerGoalHolder : Listener {
    private val randomPlayerGoals = setOf(
        CantJump::class.java,
        DeathOfFalling::class.java,
        SomeoneHitMe::class.java,
        HitSomeone::class.java,
        LowerHead::class.java,
        PlaceShrieker::class.java,
        PreventBuildStructure::class.java,
        StayAtStructure::class.java,
        PlaceMeme::class.java,
        EatRottenFlesh::class.java,
        EatPufferfish::class.java,
        LetSomeoneEatPufferfish::class.java,
        CantSprint::class.java,
        MustSprint::class.java,
        FollowSomeone::class.java,
        KeepAlone::class.java,
        PickaxeKiller::class.java
    )

    // 完成次数
    private val finishCounter = mutableMapOf<UUID,AtomicInteger>()
    fun hasFinished(player: Player) = (finishCounter[player.uniqueId]?.get() ?: 0) >= GameSettings.disguiserWinGoalAmount
    fun addFinishCount(player: Player) {
        if(finishCounter[player.uniqueId] == null) finishCounter[player.uniqueId] = AtomicInteger(0)
        val count = finishCounter[player.uniqueId]!!.incrementAndGet()
        if(count >= GameSettings.disguiserWinGoalAmount) player.sendMessage("$MESSAGE_PREFIX <green>你已完成指标，可以自由行动了，尽情捣乱吧！但不要被发现了。".toComponent())
    }
    private val disguiserGoals = mutableMapOf<UUID,PlayerGoal>()
    // 今日已刷新过目标的玩家，每日重置
    private val refreshCache = mutableSetOf<UUID>()
    private fun clearCache() {
        disguiserGoals.clear()
        refreshCache.clear()
    }
    @EventHandler
    fun on(e: DayNightEvent) {
        if(e.nowTime == DayNightHandler.Status.DAY) clearCache()
    }
    fun getPlayerGoal(player: Player) = disguiserGoals[player.uniqueId]

    fun refreshPlayerGoal(player: Player) {
        if(!player.isDisguiser()) {
            player.sendMessage("$MESSAGE_PREFIX 你不是幽匿伪装者，无法刷新目标。".toComponent())
            return
        }
        if(hasFinished(player)) {
            player.sendMessage("$MESSAGE_PREFIX 你已达成指标，无需刷新目标。".toComponent())
            return
        }
        if(refreshCache.contains(player.uniqueId)) {
            player.sendMessage("$MESSAGE_PREFIX 今天你已经刷新过目标了。".toComponent())
            return
        }
        refreshCache.add(player.uniqueId)
        val playerGoal:PlayerGoal = run {
            var tempGoal = randomPlayerGoals.random().getConstructor(Player::class.java).newInstance(player)
            repeat(10) {
                if(!tempGoal.available()) tempGoal = randomPlayerGoals.random().getConstructor(Player::class.java).newInstance(player)
            }
            if(tempGoal.available()) tempGoal else null
        } ?: run {
            player.sendMessage("$MESSAGE_PREFIX 多次尝试后仍未获取到可用的目标，请汇报给开发者。".toComponent())
            return
        }
        disguiserGoals[player.uniqueId] = playerGoal
        playerGoal.enable()
    }
}