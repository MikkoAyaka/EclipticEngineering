package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import org.wolflink.minecraft.plugin.eclipticengineering.stage.story.Story

object LocationSurvey : Goal(300) {
    override val nextGoal: Goal = CollectResource
    override var intoStory: Story? = null
    override var leaveStory: Story? = null

    override suspend fun finishCheck() {
    }

    override suspend fun failedCheck() {
    }

    /**
     * 根据完成状态发放奖励
     */
    override fun giveRewards() {
    }
}