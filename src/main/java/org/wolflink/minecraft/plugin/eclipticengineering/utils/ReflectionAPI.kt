package org.wolflink.minecraft.plugin.eclipticengineering.utils

import java.lang.reflect.Field

object ReflectionAPI {
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun getFieldValue(instance: Any, fieldName: String): Any {
        val clazz: Class<*> = instance.javaClass
        val field: Field = getField(clazz, fieldName)
            ?: throw NoSuchFieldException("Field '" + fieldName + "' not found in class " + clazz.getName() + " and its superclasses.")
        field.setAccessible(true)
        return field.get(instance)
    }

    private fun getField(clazz: Class<*>, fieldName: String): Field? {
        var currentClass: Class<*>? = clazz
        while (currentClass != null) {
            currentClass = try {
                return currentClass.getDeclaredField(fieldName)
            } catch (e: NoSuchFieldException) {
                currentClass.superclass
            }
        }
        return null
    }
}