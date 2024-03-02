package org.wolflink.minecraft.plugin.eclipticengineering.stage

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.entity.Warden
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.GameRoom
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.disguiserPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.extension.isDisguiser
import org.wolflink.minecraft.plugin.eclipticengineering.monster.StrategyDecider
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal.PlayerGoalHolder
import org.wolflink.minecraft.plugin.eclipticengineering.sculkinfection.OrnamentSculkInfection
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold.LivingHouse
import org.wolflink.minecraft.plugin.eclipticstructure.extension.register
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.unregister
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage
import java.util.UUID

class EndGameStage(stageHolder: StageHolder): Stage("最终游戏阶段",stageHolder),Listener {
    private var warden: Warden? = null
    override fun onEnter() {
        this.register(EclipticEngineering.instance)
        DayNightHandler.stop()
        Config.gameWorld.time = 14000
        Bukkit.broadcast("$MESSAGE_PREFIX <#6326F2>?????</#6326F2> <red>已从幽匿深渊中苏醒...开拓者们，放..抵抗..空港<obf>.....</obf>".toComponent())
        gamingPlayers.forEach {
            it.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS,10 * 20,0))
            it.playSound(it, Sound.ENTITY_WARDEN_ROAR,1f,0.8f)
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN,1f,0.5f)
        }
        val livingHouse = StructureRepository.findBy { it is LivingHouse }.first()
        warden = livingHouse.builder.buildLocation.world.spawn(
            livingHouse.builder.buildLocation.clone().add(0.0,6.0,0.0),
            Warden::class.java,
            CreatureSpawnEvent.SpawnReason.CUSTOM
        ).apply {
            getAttribute(Attribute.GENERIC_MAX_HEALTH)?.addModifier(AttributeModifier(
                UUID.randomUUID(),
                "warden-health",
                1500.0,
                AttributeModifier.Operation.ADD_NUMBER
            ))
            getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.addModifier(AttributeModifier(
                UUID.randomUUID(),
                "warden-damage",
                -0.6,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
            ))
            // 内鬼存活而且已经完成目标
            if(disguiserPlayers.any {PlayerGoalHolder.hasFinished(it)}) {
                Bukkit.broadcast("$MESSAGE_PREFIX <red>幽匿伪装者在没被发现的情况下完成了所有任务！开拓者们即将迎来一场噩耗...做好觉悟吧！".toComponent())
                Bukkit.broadcast("$MESSAGE_PREFIX <red>幽匿伪装者将被允许击杀其它玩家。".toComponent())
                OrnamentSculkInfection.enable()
                getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.addModifier(AttributeModifier(
                    UUID.randomUUID(),
                    "warden-speed",
                    0.1,
                    AttributeModifier.Operation.MULTIPLY_SCALAR_1
                ))
            } else if(disguiserPlayers.isNotEmpty()) {
                Bukkit.broadcast("$MESSAGE_PREFIX <red>幽匿伪装者没能在规定时间内完成指标，在临死前决定带走几个人陪葬！".toComponent())
                disguiserPlayers.forEach {
                    it.addPotionEffect(PotionEffect(PotionEffectType.HEALTH_BOOST,20 * 60 * 5,4,false,false))
                    it.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION,20 * 60 * 5,1,false,false))
                    it.addPotionEffect(PotionEffect(PotionEffectType.SPEED,20 * 60 * 5,0,false,false))
                    it.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE,20 * 60 * 5,0,false,false))
                }
            }
        }
        livingHouse.destroy()
    }
    override fun onLeave() {
        this.unregister()
        OrnamentSculkInfection.disable()
        StrategyDecider.disable()
    }
    @EventHandler
    fun on(e: EntityDeathEvent) {
        if(warden == null) throw IllegalStateException("监守者实体指针为空，请检查代码。")
        else if(e.entity == warden) {
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