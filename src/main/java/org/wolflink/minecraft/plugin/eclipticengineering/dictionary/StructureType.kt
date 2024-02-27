package org.wolflink.minecraft.plugin.eclipticengineering.dictionary

import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.other.SpinTieMeme
import org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold.*
import org.wolflink.minecraft.plugin.eclipticengineering.structure.survival.EnchantRoom
import org.wolflink.minecraft.plugin.eclipticengineering.structure.survival.ForgeRoom
import org.wolflink.minecraft.plugin.eclipticengineering.structure.survival.LargeCampfire
import org.wolflink.minecraft.plugin.eclipticengineering.structure.defense.BlazeTurret
import org.wolflink.minecraft.plugin.eclipticengineering.structure.defense.SnowGolemTurret
import org.wolflink.minecraft.plugin.eclipticengineering.structure.defense.TowerArrow
import org.wolflink.minecraft.plugin.eclipticengineering.structure.resource.*
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder
import org.wolflink.minecraft.plugin.eclipticstructure.structure.registry.StructureRegistry

enum class StructureType(
    val displayName: String,
    val clazz: Class<out GameStructure>,
    val blueprints: List<Blueprint>,
    private val supplier: (structureLevel: Int, builder: Builder) -> Structure
) {
    FARMING_PLACE("农场",FarmingPlace::class.java,FarmingPlace.blueprints,FarmingPlace::create),
    MINING_PLACE("采矿场",MiningPlace::class.java,MiningPlace.blueprints,MiningPlace::create),
    LOGGING_PLACE("伐木场",LoggingPlace::class.java,LoggingPlace.blueprints,LoggingPlace::create),
    GENERATOR_ORE("精炼矿场",GeneratorOre::class.java,GeneratorOre.blueprints, GeneratorOre::create),
    GENERATOR_LOG("精培木场",GeneratorLog::class.java,GeneratorLog.blueprints, GeneratorLog::create),
    GENERATOR_CROP("精培农场",GeneratorCrop::class.java,GeneratorCrop.blueprints, GeneratorCrop::create),

    LIGHT_HOUSE("幽光灯塔", Lighthouse::class.java, Lighthouse.blueprints, Lighthouse::create),// 灯塔
    RESPAWN_BEACON("幽光重生信标",RespawnBeacon::class.java,RespawnBeacon.blueprints,RespawnBeacon::create),// 重生信标
    ENERGY_SOURCE("幽光能量发生场",EnergySource::class.java,EnergySource.blueprints,EnergySource::create),// 能源中心
    MINING_STATION("勘探站台",MiningStation::class.java,MiningStation.blueprints,MiningStation::create),
    PIPELINE_INTERFACE("管道接口",PipelineInterface::class.java,PipelineInterface.blueprints,PipelineInterface::create),
//    HOT_SPRING(HotSpring.blueprint,{ HotSpring(it)}),// 温泉
//
//    TOTEM_EXPERIENCE(TotemExperience.blueprint,{TotemExperience(it)}),// 经验图腾
//    TOTEM_SPEED(TotemSpeed.blueprint,{TotemSpeed(it)}),// 速度图腾
//    TOTEM_STRENGTH(TotemStrength.blueprint,{TotemStrength(it)}),// 力量图腾
//
    TOWER_ARROW("弓箭高塔",TowerArrow::class.java,TowerArrow.blueprints,TowerArrow::create),// 弓箭塔
//    TOWER_LOOKOUT(TowerLookout.blueprint,{TowerLookout(it)}),// 瞭望塔
//    TOWER_LIGHTNING(TowerLightning.blueprint,{TowerLightning(it)}),// 引雷塔
//    TOWER_POTION(TowerPotion.blueprint,{TowerPotion(it)}),// 药水塔

    TURRET_BLAZE("烈焰炮台",BlazeTurret::class.java,BlazeTurret.blueprints,BlazeTurret::create),// 烈焰防御塔台
    TURRET_SNOW_GOLEM("雪人炮台",SnowGolemTurret::class.java,SnowGolemTurret.blueprints,SnowGolemTurret::create),// 雪人防御塔台

    ENCHANT_ROOM("附魔站台",EnchantRoom::class.java,EnchantRoom.blueprints,EnchantRoom::create),
    FORGE_ROOM("锻造站台",ForgeRoom::class.java,ForgeRoom.blueprints,ForgeRoom::create),
    LARGE_CAMPFIRE("大型营火",LargeCampfire::class.java,LargeCampfire.blueprints,LargeCampfire::create),

    LIVING_HOUSE("居住屋",LivingHouse::class.java,LivingHouse.blueprints,LivingHouse::create),
    SPIN_TIE_MEME("非常好建筑，使我的领带旋转",SpinTieMeme::class.java,SpinTieMeme.blueprints,SpinTieMeme::create),
    MEETING_PLACE("会议大厅",MeetingPlace::class.java,MeetingPlace.blueprints,MeetingPlace::create)
    ;
    companion object {
        fun parse(structure: Structure) = entries.firstOrNull { structure::class.java == it.clazz }
    }
    fun register() {
        StructureRegistry.register(EclipticEngineering.instance, name, blueprints, supplier)
    }

    fun unregister() {
        StructureRegistry.unregister(name)
    }
}