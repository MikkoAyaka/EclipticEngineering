package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.extension.isDisguiser
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

class LetSomeoneEatPufferfish(disguiser: Player): PlayerGoal(disguiser,Difficulty.HADES) {
    private lateinit var randomPlayer: Player
    override val description: String
        get() = "让 ${randomPlayer.name} 吃下一只河豚"
    override fun init() {
        randomPlayer = gamingPlayers.filter { !it.isDisguiser() }.random()
    }
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY) failed() }
    @EventHandler
    fun on(e: FoodLevelChangeEvent) {
        if(e.entity.uniqueId != randomPlayer.uniqueId) return
        if(e.item == null) return
        if(e.item!!.type == Material.PUFFERFISH) finished()
    }
}