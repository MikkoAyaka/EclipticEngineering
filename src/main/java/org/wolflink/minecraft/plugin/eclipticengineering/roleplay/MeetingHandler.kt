package org.wolflink.minecraft.plugin.eclipticengineering.roleplay

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.extension.scriptKill
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import java.util.*

/**
 * 会议管理者
 */
object MeetingHandler {
    // 会议最长时间(分钟)
    private const val MEETING_TIME_MINUTES = 6
    // 投票记录 发起投票者 -> 被投票者
    private val voteHistory = mutableMapOf<UUID,UUID?>()
    private var available = false
    private var lastTriggerDays = 0
    /**
     * 开始会议
     */
    fun startMeeting(player: Player) {
        if(available) {
            player.playSound(player, Sound.ENTITY_VILLAGER_NO,1f,1f)
            player.sendMessage("$MESSAGE_PREFIX <yellow>会议正在进行中。".toComponent())
            return
        }
        if(DayNightHandler.status != DayNightHandler.Status.DAY) {
            player.playSound(player, Sound.ENTITY_VILLAGER_NO,1f,1f)
            player.sendMessage("$MESSAGE_PREFIX <yellow>天色已晚，危机四伏，这并不是召开会议的好时候。".toComponent())
            return
        }
        if(DayNightHandler.days == lastTriggerDays) {
            player.playSound(player, Sound.ENTITY_VILLAGER_NO,1f,1f)
            player.sendMessage("$MESSAGE_PREFIX <yellow>今日已召开过会议，明天再来吧。".toComponent())
            return
        }
        available = true
        lastTriggerDays = DayNightHandler.days
        Bukkit.broadcast("$MESSAGE_PREFIX ${player.name} 召开了今日的会议，玩家们将陆续到达会议大厅，请尽快入座。<gray>(/eev vote ID 进行投票，/eev abstain 弃权)".toComponent())
        EEngineeringScope.launch {
            Bukkit.getOnlinePlayers().filter { it != player }.forEach {
                EclipticEngineering.runTask { it.teleport(player) }
                delay(1000)
            }
        }
        Bukkit.getScheduler().runTaskLater(EclipticEngineering.instance, Runnable {
            if(available) {
                endMeeting()
                Bukkit.broadcast("$MESSAGE_PREFIX <yellow>会议超时，本次会议已自动结束。".toComponent())
            }
        },20L * 60 * MEETING_TIME_MINUTES)
    }
    fun endMeeting() {
        available = false
        val mostVotePlayer = judgment() ?: return
        mostVotePlayer.scriptKill()
    }

    /**
     * 投票
     */
    fun vote(voter: Player,beenVoted: Player) {
        if(!available) {
            voter.sendMessage("$MESSAGE_PREFIX 未开始会议，无法进行投票。".toComponent())
            return
        }
        voteHistory[voter.uniqueId] = beenVoted.uniqueId
        voter.sendMessage("$MESSAGE_PREFIX 你在投票纸上写下了 ${beenVoted.name} 的名字。<gray>(先前的已被擦除)".toComponent())
        Bukkit.broadcast("$MESSAGE_PREFIX <green>${voter.name} 完成了投票。".toComponent())
        if(voteHistory.size >= gamingPlayers.size) endMeeting()
    }

    /**
     * 弃权
     */
    fun abstain(voter: Player) {
        if(!available) {
            voter.sendMessage("$MESSAGE_PREFIX 未开始会议，无法弃权。".toComponent())
            return
        }
        voteHistory[voter.uniqueId] = null
        Bukkit.broadcast("$MESSAGE_PREFIX <red>${voter.name} 弃权了。".toComponent())
        if(voteHistory.size >= gamingPlayers.size) endMeeting()
    }

    /**
     * 判决
     */
    private fun judgment(): Player? {
        // 最多得票数
        val mostVote = voteHistory.values
            .filterNotNull()
            .groupBy { it }
            .map { it.key to it.value.size }
            .maxBy { it.second }
        val mostVotePlayer = Bukkit.getPlayer(mostVote.first)!!
        if(mostVote.second < 0.5 * gamingPlayers.size) {
            Bukkit.broadcast("$MESSAGE_PREFIX 本次会议中 ${mostVotePlayer.name} 得票最高但未达半数，本次投票作废。".toComponent())
            return null
        }
        Bukkit.broadcast("$MESSAGE_PREFIX 本次会议中 ${mostVotePlayer.name} 得票最高，且本次投票有效，${mostVotePlayer.name} 将立刻受到制裁。".toComponent())
        return mostVotePlayer
    }
}