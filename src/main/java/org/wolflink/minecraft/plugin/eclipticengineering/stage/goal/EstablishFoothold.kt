package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.FunctionalCondition
import org.wolflink.minecraft.plugin.eclipticengineering.stage.story.Story
import org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold.LivingHouse
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

object EstablishFoothold : Goal("建立据点",0) {
    override val nextGoal: Goal = UpgradeFoothold
    override var intoStory: Story? = Story(
        "在这个被幽匿感染侵蚀的世界里",
        "你们，无畏的开拓者",
        "被选中重建家园的重任",
        "带着希望和决心",
        "踏上了地面之旅",
        "地面充满了重重险境",
        "但你们的目标只有一个",
        "找到一片净土",
        "重建我们的家园",
    )
    override var leaveStory: Story? = Story(
        "随着你的决心和勇气",
        "新的旅程正式开始",
        "在这片荒凉之地上",
        "寻找生存和重建的希望",
        "就是你现在的使命",
    )
    override val finishConditions: List<Condition> = listOf(
        FunctionalCondition("寻找合适的地点作为据点，建造居住屋"){
            StructureRepository.findBy { it is LivingHouse && it.builder.status == Builder.Status.COMPLETED }.any()
        }
    )
    override val failedConditions: List<Condition> = listOf()
    override fun beforeFinish() {
        val structure = StructureRepository.findBy { it is LivingHouse }.first()
        val footholdLocation = structure.builder.buildLocation
        // 更新据点坐标
        GoalHolder.footholdLocation = footholdLocation
        // 更新边界
        val gameWorld = Config.gameWorld
        gameWorld.worldBorder.size = 1600.0
        gameWorld.worldBorder.setCenter(footholdLocation.x,footholdLocation.z)
        gameWorld.worldBorder.setSize(200.0,300)
    }
    /**
     * 根据完成状态发放奖励
     */
    override fun giveRewards() {
        gamingPlayers.forEach {
            it.inventory.addItem(ItemStack(Material.DIAMOND))
            it.inventory.addItem(ItemStack(Material.EXPERIENCE_BOTTLE,24))
        }
    }
}