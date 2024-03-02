package org.wolflink.minecraft.plugin.eclipticengineering.monster

import org.bukkit.Material
import org.bukkit.entity.Monster
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.utils.RandomAPI
import org.wolflink.minecraft.plugin.eclipticengineering.utils.toRandomDistribution

object MonsterDropItemListener: Listener {
    private const val DROP_CHANCE = 0.4
    private val dropTable = mapOf(
        ItemStack(Material.IRON_INGOT) to 20,
        ItemStack(Material.GOLD_INGOT) to 7,
        ItemStack(Material.DIAMOND) to 3,
        ItemStack(Material.NETHERITE_SCRAP) to 2,
        ItemStack(Material.WITHER_SKELETON_SKULL) to 1
    ).toRandomDistribution()
    @EventHandler(ignoreCancelled = true)
    fun on(e: EntityDeathEvent) {
        // 死亡的是怪物
        if(e.entity is Monster && RandomAPI.nextDouble() <= DROP_CHANCE) {
            // 掉落资源
            e.entity.world.dropItemNaturally(e.entity.location, dropTable.random())
        }
    }
}