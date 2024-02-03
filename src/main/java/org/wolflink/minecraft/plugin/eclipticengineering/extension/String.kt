package org.wolflink.minecraft.plugin.eclipticengineering.extension

val ROMA_VALUES = intArrayOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
val ROMA_SYMBOLS = arrayOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")
fun Int.toRoma():String {
    var number = this
    val romanBuilder = StringBuilder()
    for (i in ROMA_VALUES.indices) {
        while (number >= ROMA_VALUES[i]) {
            number -= ROMA_VALUES[i]
            romanBuilder.append(ROMA_SYMBOLS[i])
        }
    }
    return romanBuilder.toString()
}