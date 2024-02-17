package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition
import org.wolflink.minecraft.plugin.eclipticengineering.stage.story.Story

abstract class Goal(val displayName: String,private val prepareTimeSeconds: Int) {
    var prepareTimeLeft = prepareTimeSeconds
        private set
    // 下一个目标
    abstract val nextGoal: Goal
    protected abstract var intoStory: Story?
    protected abstract var leaveStory: Story?
    abstract val finishConditions: List<Condition>
    abstract val failedConditions: List<Condition>

    // 任务完成状态
    var status = Status.NOT_STARTED
        private set(value) {
            if (value == field) return
            field = value
            if (field == Status.FINISHED || field == Status.FAILED) {
                giveRewards()
                EEngineeringScope.launch { leave() }
            }
        }

    enum class Status {
        NOT_STARTED,
        PREPARING,
        IN_PROGRESS,
        FINISHED,
        FAILED
    }

    /**
     * 进入当前任务
     */
    fun into() {
        status = Status.PREPARING
        EEngineeringScope.launch {
            while (prepareTimeLeft-- > 0) delay(1000)
            status = Status.IN_PROGRESS
            intoStory?.broadcast()
            EEngineeringScope.launch { timerTask() }
        }
        afterInto()
    }
    protected open fun afterInto(){}

    /**
     * 离开当前目标
     */
    suspend fun leave() {
        leaveStory?.broadcast()
    }

    protected open fun beforeFinish(){}
    fun finish() {
        beforeFinish()
        status = Status.FINISHED
        GoalHolder.next()
    }

    fun failed() {
        status = Status.FAILED
        GoalHolder.next()
    }

    protected open suspend fun timerTask(){}
    /**
     * 根据完成状态发放奖励
     */
    protected abstract fun giveRewards()
}