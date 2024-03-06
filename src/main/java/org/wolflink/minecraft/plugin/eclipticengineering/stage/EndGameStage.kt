package org.wolflink.minecraft.plugin.eclipticengineering.stage

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.GameRoom
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.disguiserPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.extension.isDisguiser
import org.wolflink.minecraft.plugin.eclipticengineering.extension.pioneerPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.monster.StrategyDecider
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal.PlayerGoalHolder
import org.wolflink.minecraft.plugin.eclipticengineering.sculkinfection.OrnamentSculkInfection
import org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold.LivingHouse
import org.wolflink.minecraft.plugin.eclipticstructure.extension.parsePapi
import org.wolflink.minecraft.plugin.eclipticstructure.extension.register
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.unregister
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage

class EndGameStage(stageHolder: StageHolder): Stage("最终游戏阶段",stageHolder),Listener {
    override fun onEnter() {
        this.register(EclipticEngineering.instance)
        DayNightHandler.stop()
        Config.gameWorld.time = 14000
        Bukkit.broadcast("$MESSAGE_PREFIX <#6326F2>?????</#6326F2> <red>即将从幽匿深渊中苏醒...开拓者们，放..抵抗..空港<obf>.....</obf>".toComponent())
        gamingPlayers.forEach {
            it.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS,10 * 20,0))
            it.playSound(it, Sound.ENTITY_WARDEN_ROAR,1f,0.8f)
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN,1f,0.5f)
        }
        val livingHouse = StructureRepository.findBy { it is LivingHouse }.firstOrNull()
        // 内鬼存活而且已经完成目标
        if(disguiserPlayers.any {PlayerGoalHolder.hasFinished(it)}) {
            giveBossItem()
            OrnamentSculkInfection.enable()
        } else if(disguiserPlayers.isNotEmpty()) {
            disguiserPlayers.forEach {
                if(pioneerPlayers.size >= 3) it.inventory.addItem(ItemStack(Material.TOTEM_OF_UNDYING))
                if(pioneerPlayers.size >= 5) {
                    it.inventory.addItem(ItemStack(Material.ENCHANTED_GOLDEN_APPLE))
                }
                if(pioneerPlayers.size >= 7) {
                    it.inventory.addItem(ItemStack(Material.TNT,16))
                    it.inventory.addItem(ItemStack(Material.REDSTONE_BLOCK,16))
                }
                it.addPotionEffect(PotionEffect(PotionEffectType.HEALTH_BOOST,20 * 60 * 5,4,false,false))
                it.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION,20 * 60 * 5,1,false,false))
                it.addPotionEffect(PotionEffect(PotionEffectType.SPEED,20 * 60 * 5,0,false,false))
                it.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE,20 * 60 * 5,0,false,false))
            }
            giveBossItem()
        } else {
            Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                Config.getSpawnBossCmd(livingHouse?.builder?.buildLocation?.clone()?.add(0.0,50.0,0.0) ?: gamingPlayers.random().location)
            )
            Bukkit.broadcast("$MESSAGE_PREFIX <red>沉睡千年的灵魂即刻苏醒，开拓者们，协力战胜它！".toComponent())
        }
        livingHouse?.destroy()
    }
    private fun giveBossItem() {
        val disguiser = disguiserPlayers.random()
        disguiser.playSound(disguiser,Sound.ENTITY_VILLAGER_YES,1f,1f)
        disguiser.sendMessage("$MESSAGE_PREFIX 你拿到了<#ff3030>远古幽匿尖啸体</#ff3030>，是时候唤醒沉睡的灵魂了。".toComponent())
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),Config.bossItemCmd.parsePapi(disguiser))
    }
    override fun onLeave() {
        this.unregister()
        OrnamentSculkInfection.disable()
        StrategyDecider.disable()
    }
    @EventHandler
    fun on(e: EntityDeathEvent) {
        if(e.entityType == EntityType.WARDEN && (e.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 0.0) > 1000) {
            GameRoom.result = GameRoom.Result.PIONEER_WIN
            StageHolder.next()
        } else if(e.entityType == EntityType.PLAYER) {
            if(gamingPlayers.isEmpty() || gamingPlayers.all { it.isDisguiser() }) {
                GameRoom.result = GameRoom.Result.DISGUISER_WIN
                StageHolder.next()
            }
        }
    }
}