package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import org.wolflink.minecraft.plugin.eclipticengineering.stage.story.Story

object WaveDefense : Goal(480) {
    override val nextGoal: Goal = LocationSurvey
    override var intoStory: Story? = null
    override var leaveStory: Story? = null

    override suspend fun finishCheck() {
    }

    override suspend fun failedCheck() {
    }

    override fun giveRewards() {
    }
}