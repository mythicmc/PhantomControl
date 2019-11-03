package info.mythicmc.phantomcontrol

import org.bukkit.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.nio.file.Files

class PhantomControl : JavaPlugin() {
    lateinit var storage: YamlConfiguration

    override fun onEnable() {
        /*
        TODO:
        - Complete PhantomCommand.
        - Complete PhantomListener.
        - Complete BukkitPhantomListener.
        - Allow disabling per-player spawning.
        - Create YamlConfiguration storage system.
        - Add /phantomtoggle or /phantom toggle.
        - Add tab autocomplete.
        */

        // Initialize storage.
        saveDefaultStorage()
        reloadStorage()

        // Initialize /phantomreload and /phantom.
        getCommand("phantomreload")?.setExecutor { sender, _, _, _ ->
            reloadConfig()
            reloadStorage()
            sender.sendMessage("${ChatColor.GREEN}Successfully reloaded plugin!")
            return@setExecutor true
        }
        getCommand("phantom")?.setExecutor(PhantomCommand(this))
    }

    fun isGloballyEnabled(): Boolean {
        return storage.getBoolean("global")
    }

    fun isEnabledForPlayer(player: String): Boolean {
        val exempted = storage.getStringList("exemptedPlayers").contains(player)
        return (isGloballyEnabled() && !exempted) || (!isGloballyEnabled() && exempted)
    }

    fun saveStorage() {
        storage.save(File(dataFolder, "storage.yml"))
    }

    fun reloadStorage() {
        storage = YamlConfiguration.loadConfiguration(File(dataFolder, "storage.yml"))
    }

    private fun saveDefaultStorage() {
        if (!dataFolder.exists())
            dataFolder.mkdir()

        val file = File(dataFolder, "storage.yml")

        if (!file.exists()) {
            val resource = getResource("storage.yml")
            if (resource == null) {
                logger.severe("This JAR is corrupted and does not have a valid storage.yml file!")
                server.pluginManager.disablePlugin(this)
                return
            }
            try {
                Files.copy(resource, file.toPath())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}