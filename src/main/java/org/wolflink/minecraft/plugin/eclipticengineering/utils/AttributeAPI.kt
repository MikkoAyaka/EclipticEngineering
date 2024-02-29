package org.wolflink.minecraft.plugin.eclipticengineering.utils

import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

object AttributeAPI {
    /**
     * 给指定attribute直接增加给定值
     */
    fun addAttribute(attributable: Attributable, modifierName: String?, attribute: Attribute?, value: Double) {
        val attributeInstance = attributable.getAttribute(attribute!!)
        attributeInstance?.addModifier(
            AttributeModifier(
                modifierName!!,
                value,
                AttributeModifier.Operation.ADD_NUMBER
            )
        )
    }

    /**
     * 给指定attribute的值乘以value
     */
    fun multiplyAttribute(attributable: Attributable, modifierName: String?, attribute: Attribute?, value: Double) {
        val attributeInstance = attributable.getAttribute(attribute!!)
        attributeInstance?.addModifier(
            AttributeModifier(
                modifierName!!,
                value - 1,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
            )
        )
    }
}
