package org.wolflink.minecraft.plugin.eclipticengineering.stage

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Warden
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.extension.isDisguiser
import org.wolflink.minecraft.plugin.eclipticengineering.monster.StrategyDecider
import org.wolflink.minecraft.plugin.eclipticengineering.sculkinfection.OrnamentSculkInfection
import org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold.LivingHouse
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage
import java.util.UUID

class EndGameStage(stageHolder: StageHolder): Stage("最终游戏阶段",stageHolder) {
    override fun onEnter() {
        Bukkit.broadcast("$MESSAGE_PREFIX <#6326F2>?????</#6326F2> <red>已从幽匿深渊中苏醒...开拓者们，放..抵抗..空港<obf>.....</obf>".toComponent())
        gamingPlayers.forEach {
            it.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS,10 * 20,0))
            it.playSound(it, Sound.ENTITY_WARDEN_ROAR,1f,0.8f)
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN,1f,0.5f)
        }
        val livingHouse = StructureRepository.findBy { it is LivingHouse }.first()
        livingHouse.builder.buildLocation.world.spawn(
            livingHouse.builder.buildLocation.clone().add(0.0,6.0,0.0),
            Warden::class.java
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
            // 内鬼存活
            if(gamingPlayers.any{it.isDisguiser()}) {
                OrnamentSculkInfection.enable()
                getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.addModifier(AttributeModifier(
                    UUID.randomUUID(),
                    "warden-speed",
                    0.1,
                    AttributeModifier.Operation.MULTIPLY_SCALAR_1
                ))
            }
        }

    }
    override fun onLeave() {
        OrnamentSculkInfection.disable()
        StrategyDecider.disable()
    }
}