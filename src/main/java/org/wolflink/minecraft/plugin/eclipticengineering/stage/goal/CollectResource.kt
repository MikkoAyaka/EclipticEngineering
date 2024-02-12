package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import org.wolflink.minecraft.plugin.eclipticengineering.stage.story.Story

object CollectResource : Goal(60) {
    override val nextGoal: Goal = WaveDefense
    override var intoStory: Story? = null
    override var leaveStory: Story? = null

    override suspend fun finishCheck() {
    }

    override suspend fun failedCheck() {
    }
    override fun giveRewards() {
    }
}