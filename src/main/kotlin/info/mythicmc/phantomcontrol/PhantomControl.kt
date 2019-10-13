package info.mythicmc.phantomcontrol

import org.bukkit.plugin.java.JavaPlugin

class PhantomControl : JavaPlugin() {
    override fun onEnable() {
        /*
        TODO:
        - Complete PhantomListener.
        - Allow disabling per-player spawning.
        - Create YamlConfiguration storage system.
        */
    }

    fun getGlobalSetting(): Boolean {
        return true
    }

    fun getPlayerSetting(): Boolean {
        return true
    }

    /*
    fun saveWhitelist() {
        whitelistFile.save(File(dataFolder, "whitelist.yml"))
    }

    fun reloadWhitelist() {
        whitelistFile = YamlConfiguration.loadConfiguration(File(dataFolder, "whitelist.yml"))
    }

    private fun saveDefaultWhitelist() {
        if (!dataFolder.exists())
            dataFolder.mkdir()

        val file = File(dataFolder, "whitelist.yml")

        if (!file.exists()) {
            val resource = getResource("whitelist.yml")
            if (resource == null) {
                logger.severe("This JAR is corrupted and does not have a valid whitelist.yml file!")
                server.pluginManager.disablePlugin(this)
                return
            }
            try {
                Files.copy(resource, file.toPath())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    */
}