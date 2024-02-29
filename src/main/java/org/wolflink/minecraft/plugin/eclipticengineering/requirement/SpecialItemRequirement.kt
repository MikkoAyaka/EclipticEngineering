package org.wolflink.minecraft.plugin.eclipticengineering.requirement

import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.SpecialItem

class SpecialItemRequirement(specialItem: SpecialItem,amount: Int): ItemRequirement("需要 $amount 个 ${specialItem.defaultItem.displayName()}",specialItem.defaultItem.clone().apply { this.amount = amount })