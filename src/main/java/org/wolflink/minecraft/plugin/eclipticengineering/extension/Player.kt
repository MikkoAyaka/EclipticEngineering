package org.wolflink.minecraft.plugin.eclipticengineering.extension

import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.ability.AbilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.DisguiserBook
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toHexFormat
import java.time.Duration
import java.util.UUID

private val scriptKilledPlayers = mutableSetOf<UUID>()
private val disguiserSet = mutableSetOf<UUID>()
private val abilityTableMap = mutableMapOf<UUID,AbilityTable>()
val UUID.abilityTable
    get() = abilityTableMap.getOrPut(this) { AbilityTable(this) }
val Player.abilityTable
    get() = this.uniqueId.abilityTable
val OfflinePlayer.abilityTable
    get() = this.uniqueId.abilityTable

/**
 * 重置玩家的生存状态、药水效果、生命值、背包等
 */
fun Player.reset() {
    exp = 0f
    level = 0
    health = getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value!!
    foodLevel = 20
    inventory.clear()
    activePotionEffects.forEach { removePotionEffect(it.type) }
}
/**
 * 在游戏内死亡
 */
fun Player.inGameDeath() {
    health = getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value!!
    foodLevel = 20
    activePotionEffects.forEach { removePotionEffect(it.type) }
    gameMode = GameMode.SPECTATOR
    Bukkit.broadcast("$MESSAGE_PREFIX <red>玩家 <white>$name <red>阵亡了！".toComponent())
    playSound(this, Sound.ENTITY_WOLF_DEATH,1f,0.7f)
    showTitle(
        Title.title("<red>死".toComponent(),"<white>你已阵亡，请寻找最近的重生信标以复活".toComponent(),
        Title.Times.times(
            Duration.ofMillis(500),
            Duration.ofMillis(1000),
            Duration.ofMillis(500)
        )
    ))
}
/**
 * 剧情杀
 */
fun Player.scriptKill() {
    scriptKilledPlayers.add(uniqueId)
    reset()
    gameMode = GameMode.SPECTATOR
    if(isDisguiser()) {
        Bukkit.broadcast("$MESSAGE_PREFIX <yellow>$name <white>永远地死去了，他的身份是 <#6600CC>幽匿伪装者".toComponent())
    } else {
        Bukkit.broadcast("$MESSAGE_PREFIX <yellow>$name <white>永远地死去了，他的身份是 <#CCE5FF>开拓者".toComponent())
    }
    playSound(this, Sound.ENTITY_WOLF_DEATH,1f,0.7f)
    showTitle(
        Title.title("<red>出局".toComponent(),"<white>你已出局，请礼貌地旁观本场游戏".toComponent(),
            Title.Times.times(
                Duration.ofMillis(500),
                Duration.ofMillis(1000),
                Duration.ofMillis(500)
            )
        ))
}
fun Player.asDisguiser() {
    disguiserSet.add(this.uniqueId)
    // 发放伪装者手册
    DisguiserBook.give(this)
}
fun OfflinePlayer.isDisguiser() = this.uniqueId in disguiserSet
fun Player.isGaming() = gameMode == GameMode.SURVIVAL && world.name != Config.lobbyWorldName
val disguiserPlayers: Collection<Player> get() = disguiserSet.mapNotNull { Bukkit.getPlayer(it) }
val gamingPlayers: Collection<Player> get() = onlinePlayers.filter { it.isGaming() }
val pioneerPlayers: Collection<Player> get() = gamingPlayers.filter { !it.isDisguiser() }
val onlinePlayers: Collection<Player> get() = Bukkit.getOnlinePlayers()

/**
 * 更新玩家职业展示名称(TAB、聊天框)
 */
fun Player.updateOccupationDisplayName() {
    val occupationType = abilityTable.occupationType ?: return
    playerListName("${occupationType.color.toHexFormat()}${occupationType.displayName} <white>${name}".toComponent())
    displayName("${occupationType.color.toHexFormat()}${occupationType.displayName} <white>${name}".toComponent())
}