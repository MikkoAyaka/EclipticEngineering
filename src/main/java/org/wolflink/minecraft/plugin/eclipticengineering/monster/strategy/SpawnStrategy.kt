package org.wolflink.minecraft.plugin.eclipticengineering.monster.strategy

import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.META_MONSTER_BELONG_PLAYER
import org.wolflink.minecraft.plugin.eclipticengineering.monster.SpawnerAttribute
import org.wolflink.minecraft.plugin.eclipticengineering.monster.SpecialSpawnEntityEvent

/**
 * 怪物生成策略
 * 通过一定的算法在给定玩家附近生成一只怪物
 */
abstract class SpawnStrategy(val spawnerAttribute: SpawnerAttribute) {

    /**
     * 判断玩家是否适合应用该刷怪策略
     */
    abstract fun isApplicable(player: Player): Boolean

    /**
     * 具体的刷怪算法
     * 异步计算，同步生成
     * 异常重试 5 次
     */
    fun spawn(player: Player) {
        spawn(player, 5)
    }

    abstract fun spawn(player: Player, triedCount: Int)

    /**
     * 为生成的怪物附加额外信息
     */
    fun appendMetadata(player: Player, entity: Entity) {
        // 生成该怪物的玩家
        entity.setMetadata(
            META_MONSTER_BELONG_PLAYER,
            FixedMetadataValue(EclipticEngineering.instance, player.name)
        )
    }

    fun callEvent(player: Player, entity: Entity) {
        Bukkit.getPluginManager().callEvent(SpecialSpawnEntityEvent(player, entity))
    }
}
