package org.wolflink.minecraft.plugin.eclipticengineering.roleplay

import org.bukkit.Bukkit
import org.bukkit.entity.Player
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
    private val voteHistory = mutableMapOf<UUID,UUID>()
    private var available = false
    private var lastUpdated = Calendar.getInstance()
    /**
     * 开始会议
     */
    fun startMeeting() {
        available = true
        lastUpdated = Calendar.getInstance()
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
            voter.sendMessage("$MESSAGE_PREFIX 未开始会议，无法进行投票。")
            return
        }
        voteHistory[voter.uniqueId] = beenVoted.uniqueId
        voter.sendMessage("$MESSAGE_PREFIX 你在投票纸上写下了 ${beenVoted.name} 的名字。<gray>(先前的已被擦除)")
    }

    /**
     * 判决
     */
    private fun judgment(): Player? {
        // 最多得票数
        val mostVote = voteHistory.values
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