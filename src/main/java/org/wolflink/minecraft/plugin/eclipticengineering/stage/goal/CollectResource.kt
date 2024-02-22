package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.FunctionalCondition
import org.wolflink.minecraft.plugin.eclipticengineering.stage.story.Story
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.MiningStation
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.PipelineInterface
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

object CollectResource : Goal("物资收集",60) {
    override val nextGoal: Goal = WaveDefense
    override var intoStory: Story? = Story(
        "资源是生存和发展的基础",
        "现在，我们必须出发",
        "收集这片土地上的宝贵物资",
    )
    override var leaveStory: Story? = Story(
        "经过一番辛勤劳动",
        "我们收集到了足够的资源",
        "这将支持我们的下一步行动",
        "让我们的据点更加坚固",
    )
    override val finishConditions: List<Condition> = listOf(
        FunctionalCondition("在指定坐标附近建立开采站"){
            StructureRepository.findBy { it is MiningStation && it.builder.buildLocation.distance(GoalHolder.specialLocation!!) < 50 && it.builder.status == Builder.Status.COMPLETED}.any()
        },
        FunctionalCondition("在开采站附近建立运输接口"){
            StructureRepository.findBy { it is PipelineInterface && it.builder.buildLocation.distance(GoalHolder.specialLocation!!) < 50 && it.builder.status == Builder.Status.COMPLETED}.any()
        },
        FunctionalCondition("在据点附近建立运输接口"){
            StructureRepository.findBy { it is PipelineInterface && it.builder.buildLocation.distance(GoalHolder.footholdLocation!!) < 50 && it.builder.status == Builder.Status.COMPLETED}.any()
        },
        FunctionalCondition("端口是否连接"){
            // 开采站端口列表
            val miningStationPipelines = StructureRepository.findBy { it is PipelineInterface && it.builder.buildLocation.distance(GoalHolder.specialLocation!!) < 50 && it.builder.status == Builder.Status.COMPLETED}.map { it as PipelineInterface }
            // 基地端口列表
            val footholdPipelines = StructureRepository.findBy { it is PipelineInterface && it.builder.buildLocation.distance(GoalHolder.footholdLocation!!) < 50 && it.builder.status == Builder.Status.COMPLETED}.map { it as PipelineInterface }
            for (miningStationPipeline in miningStationPipelines) {
                for (footholdPipeline in footholdPipelines) {
                    // 存在至少一个基地的端口与开采站端口有连接
                    if(miningStationPipeline.hasConnection(footholdPipeline)){
                        return@FunctionalCondition true
                    }
                }
            }
            return@FunctionalCondition false
        }
    )
    override val failedConditions: List<Condition> = listOf()
    override fun giveRewards() {
        // 发放时运
        val enchantBook = ItemStack(Material.ENCHANTED_BOOK)
        enchantBook.addEnchantment(Enchantment.LUCK,2)
        gamingPlayers.random().inventory.addItem(enchantBook)
        gamingPlayers.forEach {
            it.inventory.addItem(ItemStack(Material.DIAMOND))
            it.inventory.addItem(ItemStack(Material.EXPERIENCE_BOTTLE,24))
        }
    }
}