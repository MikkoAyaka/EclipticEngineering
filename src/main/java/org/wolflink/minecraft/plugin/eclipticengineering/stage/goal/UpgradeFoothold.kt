package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.FunctionalCondition
import org.wolflink.minecraft.plugin.eclipticengineering.stage.story.Story
import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorCrop
import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorLog
import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorOre
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

object UpgradeFoothold : Goal("据点扩张",0) {
    override val nextGoal = LocationSurvey
    override var intoStory: Story? = Story(
        "尽管我们已经初步建立了据点",
        "但仍需要扩大并巩固这里",
        "怪物们正在黑暗之中伺机而动",
        "抓紧时间吧！",
    )
    override var leaveStory: Story? = Story(
        "这个据点看上去已经能够正常运作了",
        "稍作整顿，接下来将有困难的任务等待着你们",
    )
    override val finishConditions: List<Condition> = listOf(
        FunctionalCondition("建造精培木场"){
            StructureRepository.findBy { it is GeneratorLog && it.builder.status == Builder.Status.COMPLETED }.any()
        },
        FunctionalCondition("建造精培农场"){
            StructureRepository.findBy { it is GeneratorCrop && it.builder.status == Builder.Status.COMPLETED }.any()
        },
        FunctionalCondition("建造精炼矿场"){
            StructureRepository.findBy { it is GeneratorOre && it.builder.status == Builder.Status.COMPLETED }.any()
        }
    )
    override val failedConditions: List<Condition> = listOf()
    /**
     * 根据完成状态发放奖励
     */
    override fun giveRewards() {
        gamingPlayers.forEach {
            it.inventory.addItem(ItemStack(Material.WHITE_SHULKER_BOX))
            it.inventory.addItem(ItemStack(Material.DIAMOND))
            it.inventory.addItem(ItemStack(Material.EXPERIENCE_BOTTLE,24))
        }
    }
}