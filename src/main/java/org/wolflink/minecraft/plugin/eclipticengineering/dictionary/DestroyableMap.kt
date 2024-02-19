package org.wolflink.minecraft.plugin.eclipticengineering.dictionary

import com.destroystokyo.paper.Namespaced
import org.bukkit.Material

object DestroyableMap {
    private val map = mutableMapOf<Material,Set<Namespaced>>()
    init {
        // 木镐
        map[Material.WOODEN_PICKAXE] = setOf(
            Material.COBBLESTONE.key,
            Material.COBBLED_DEEPSLATE.key,
            Material.COAL_ORE.key,
            Material.DEEPSLATE_COAL_ORE.key
        )
        // 石镐
        map[Material.STONE_PICKAXE] = map[Material.WOODEN_PICKAXE]!! + setOf(
            Material.IRON_ORE.key,
            Material.DEEPSLATE_IRON_ORE.key,
            Material.COPPER_ORE.key,
            Material.DEEPSLATE_COPPER_ORE.key,
        )
        // 铁镐
        map[Material.IRON_PICKAXE] = map[Material.STONE_PICKAXE]!! + setOf(
            Material.GOLD_ORE.key,
            Material.DEEPSLATE_GOLD_ORE.key,
            Material.REDSTONE_ORE.key,
            Material.DEEPSLATE_REDSTONE_ORE.key,
            Material.LAPIS_ORE.key,
            Material.DEEPSLATE_LAPIS_ORE.key,
            Material.DIAMOND_ORE.key,
            Material.DEEPSLATE_DIAMOND_ORE.key
        )
        // 金镐
        map[Material.GOLDEN_PICKAXE] = map[Material.WOODEN_PICKAXE]!!
        // 钻石镐
        map[Material.DIAMOND_PICKAXE] = map[Material.IRON_PICKAXE]!! + setOf(
            Material.EMERALD_ORE.key,
            Material.DEEPSLATE_EMERALD_ORE.key
        )
        // 下届合金镐
        map[Material.NETHERITE_PICKAXE] = map[Material.DIAMOND_PICKAXE]!!
        // 斧头
        val woodKeys = WOOD_MATERIAL.map { material -> material.key }.toSet()
        AXE_MATERIAL.forEach { map[it] = woodKeys }
        // 锄头
        val cropKeys = HARVESTABLE_CROP_MATERIAL.map { material -> material.key }.toSet()
        HOE_MATERIAL.forEach { map[it] = cropKeys }
    }

    fun get(type: Material): Set<Namespaced> {
        return map[type] ?: setOf()
    }
}