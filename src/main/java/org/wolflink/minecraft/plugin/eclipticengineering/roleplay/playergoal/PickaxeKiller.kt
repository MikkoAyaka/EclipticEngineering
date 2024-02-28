package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import org.bukkit.Material
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.isPickaxe
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

class PickaxeKiller(disguiser: Player): PlayerGoal(disguiser,Difficulty.EASY) {
    override val description: String = "使用镐子击杀 15 只怪物"
    private var count = 0
    override fun init() {
    }
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY) failed() }
    @EventHandler
    fun on(e: EntityDeathEvent) {
        if(e.entity is Monster && e.entity.killer?.uniqueId == disguiser.uniqueId) {
            if(disguiser.inventory.itemInMainHand.type.isPickaxe()) {
                count++
                if(count >= 15) finished()
            }
        }
    }
}