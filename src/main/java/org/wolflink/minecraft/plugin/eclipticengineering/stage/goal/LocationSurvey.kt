package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import kotlinx.coroutines.delay
import org.bukkit.Location
import org.bukkit.World
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.stage.story.Story
import org.wolflink.minecraft.plugin.eclipticengineering.structure.decoration.Lighthouse
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

object LocationSurvey : Goal(300) {
    private const val SURVEY_DISTANCE = 500
    override val nextGoal: Goal = CollectResource
    override var intoStory: Story? = null
    override var leaveStory: Story? = null
    private var location: Location? = null
    override fun afterInto() {
        location = randomSurveyLocation(SURVEY_DISTANCE)
    }

    /**
     * 随机生成勘察坐标
     * 以所有玩家的平均坐标为中心，在半径 800m 的圆的边上选取一点
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
            if (location != null) {
                gamingPlayers.filter { it.location.distance(location!!) < 50 }
                    .forEach { it.sendActionBar("<green>已进入勘察范围".toComponent()) }
            }
            delay(1000)
        }
    }

    override suspend fun finishCheck() {
        while (status == Status.IN_PROGRESS) {
            if(location != null) {
                val result = StructureRepository
                    .findBy { it is Lighthouse && it.builder.buildLocation.distance(location!!) < 50}.any()
                if (result) {
                    finish()
                    break
                }
            }
            delay(3000)
        }
    }

    override suspend fun failedCheck() {
    }

    /**
     * 根据完成状态发放奖励
     */
    override fun giveRewards() {
    }
}