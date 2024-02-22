package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import kotlinx.coroutines.delay
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.FunctionalCondition
import org.wolflink.minecraft.plugin.eclipticengineering.stage.story.Story
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.Lighthouse
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

object LocationSurvey : Goal("定点勘察",300) {
    private const val SURVEY_DISTANCE = 275
    override val nextGoal: Goal = CollectResource
    override var intoStory: Story? = Story(
        "为了确保我们的安全和发展",
        "我们需要对周围的地区进行详细的勘察",
    )
    override var leaveStory: Story? = Story(
        "勘察完成，我们发现了新的资源和潜在的威胁",
        "这些信息对我们的未来发展至关重要",
    )
    override val finishConditions: List<Condition> = listOf(
        FunctionalCondition("在勘察坐标附近建造灯塔"){
            StructureRepository.findBy { it is Lighthouse && it.builder.buildLocation.distance(GoalHolder.specialLocation!!) < 50 && it.builder.status == Builder.Status.COMPLETED}.any()
        }
    )
    override val failedConditions: List<Condition> = listOf()

    override fun afterInto() {
        GoalHolder.specialLocation = randomSurveyLocation(SURVEY_DISTANCE)
    }

    /**
     * 随机生成勘察坐标
     * 以所有玩家的平均坐标为中心，在半径 SURVEY_DISTANCE 的圆的边上选取一点
     */
    private fun randomSurveyLocation(radius: Int): Location {
        var world: World? = null
        var totalX = 0
        var totalZ = 0
        gamingPlayers.forEach {
            if (world == null) world = it.world
            totalX += it.location.blockX
            totalZ += it.location.blockZ
        }
        if (world == null) {
            throw IllegalStateException("在生成随机勘察坐标时未找到世界")
        }
        val averX = totalX.toDouble() / gamingPlayers.size
        val averZ = totalZ.toDouble() / gamingPlayers.size
        val random = Random()
        val randomAngle = random.nextDouble(2 * Math.PI)
        val randomX = averX + radius * cos(randomAngle)
        val randomZ = averZ + radius * sin(randomAngle)
        val highestY = world!!.getHighestBlockYAt(randomX.toInt(), randomZ.toInt())
        return Location(world, randomX, highestY.toDouble(), randomZ)
    }

    override suspend fun timerTask() {
        // 勘察范围提示
        while (status == Status.IN_PROGRESS) {
            if (GoalHolder.specialLocation != null) {
                gamingPlayers.filter { it.location.distance(GoalHolder.specialLocation!!) < 50 }
                    .forEach { it.sendActionBar("<green>已进入勘察范围".toComponent()) }
            }
            delay(1000)
        }
    }

    /**
     * 根据完成状态发放奖励
     */
    override fun giveRewards() {
        // 发放精准采集
        val enchantBook = ItemStack(Material.ENCHANTED_BOOK)
        enchantBook.addEnchantment(Enchantment.SILK_TOUCH,1)
        gamingPlayers.random().inventory.addItem(enchantBook)
        gamingPlayers.forEach {
            it.inventory.addItem(ItemStack(Material.DIAMOND))
            it.inventory.addItem(ItemStack(Material.EXPERIENCE_BOTTLE,24))
        }
    }
}