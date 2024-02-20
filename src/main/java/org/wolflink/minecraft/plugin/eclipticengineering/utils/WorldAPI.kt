package org.wolflink.minecraft.plugin.eclipticengineering.utils

import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import java.io.File

object WorldAPI {
    fun delete(worldName: String) {
        Bukkit.unloadWorld(worldName,false)
        deleteWorldFiles(File("${Bukkit.getPluginsFolder().parentFile}",worldName))
    }
    private fun deleteWorldFiles(path: File) {
        if (path.exists()) {
            val files: Array<out File>? = path.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isDirectory()) {
                        deleteWorldFiles(file)
                    } else {
                        file.delete()
                    }
                }
            }
        }
        path.delete()
    }
    fun create(worldName: String) {
        Bukkit.createWorld(WorldCreator(worldName))
    }
    fun regen(worldName: String) {
        delete(worldName)
        create(worldName)
    }
}