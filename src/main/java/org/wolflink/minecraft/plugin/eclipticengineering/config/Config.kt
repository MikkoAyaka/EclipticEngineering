package org.wolflink.minecraft.plugin.eclipticengineering.config

import org.bukkit.Bukkit
import org.bukkit.Location
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Zone

object Config {
    val buildMenuCmd = "dm open 建造菜单 %player_name%"
    val mainMenuCmd = "dm open 据点看板 %player_name%"
    val lobbyWorldName = "spawn"
    val lobbyWallStrings = listOf(
        "33,100,-9 33,95,-14",
        "24,95,15 19,103,15",
        "-5,99,15 -13,95,15",
        "-27,103,29 -32,95,29",
        "-39,95,30 -47,103,30",
        "-52,95,29 -55,103,29"
    )
    val lobbyWalls: Set<Zone> get() {
        val result = mutableSetOf<Zone>()
        for (str in lobbyWallStrings) {
            val zone = Zone.create(str.toPairLocation(lobbyWorldName))
            result.add(zone)
        }
        return result
    }
    val lobbyLocation get() = Location(Bukkit.getWorld(lobbyWorldName),-25.0,91.0,2.0)
}
private fun String.toPairLocation(worldName: String): Pair<Location,Location> {
    val locationStrings = this.split(' ')
    return locationStrings[0].toLocation(worldName) to locationStrings[1].toLocation(worldName)
}
private fun String.toLocation(worldName: String): Location {
    val xyz = this.split(',')
    return Location(Bukkit.getWorld(worldName),xyz[0].toDouble(),xyz[1].toDouble(),xyz[2].toDouble())
}