package org.wolflink.minecraft.plugin.eclipticengineering.structure.api

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureAvailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureUnavailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure

class EnergyRequiredListener(private val structure: GameStructure): IStructureListener {
    private suspend fun energyCheck() {
        while (structure.available) {
            if(!structure.hasEnergySource()) {
                structure.doDamage(40,Structure.DamageSource.LACK_OF_ENERGY)
            }
            delay(5 * 1000)
        }
    }
    private var job: Job? = null
    override fun onAvailable(e: StructureAvailableEvent) {
        if(job == null || job?.isCompleted == true) job = EEngineeringScope.launch { energyCheck() }
    }

    override fun onUnavailable(e: StructureUnavailableEvent) {
    }
}