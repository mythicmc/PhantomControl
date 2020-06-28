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
        + Complete BukkitPhantomListener.
        - Allow disabling per-player spawning.
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
        val phantomCommand = PhantomCommand(this)
        getCommand("phantom")?.setExecutor(phantomCommand)
        getCommand("phantom")?.tabCompleter = phantomCommand

        // If server is Paper, initialize PhantomListener, else BukkitPhantomListener.
        if (server.name == "Paper") {
            logger.info("Detected Paper, registering Paper-specific listener.")
            server.pluginManager.registerEvents(PhantomListener(this), this)
        } else {
            logger.info("Did not detect Paper, falling back to untested Bukkit listener.")
            logger.info("====================== GET PAPER ======================")
            logger.info("PhantomControl uses the Paper API for more accurate data")
            logger.info("about phantom spawning. Paper also provides much more")
            logger.info("performance and works with existing Spigot and Bukkit")
            logger.info("plugins. See: https://papermc.io for more info")
            logger.info("=======================================================")
            server.pluginManager.registerEvents(BukkitPhantomListener(this), this)
        }
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

    private fun reloadStorage() {
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

