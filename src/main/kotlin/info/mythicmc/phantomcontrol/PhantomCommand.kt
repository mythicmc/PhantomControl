package info.mythicmc.phantomcontrol

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class PhantomCommand(private val plugin: PhantomControl) : TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            args.isEmpty() || args[0] == "status" -> {
                sender.sendMessage("${ChatColor.AQUA}Phantoms are currently: ${
                    if (plugin.isEnabledForPlayer(sender.name)) "${ChatColor.GREEN}enabled" else "${ChatColor.RED}disabled"
                } ${ChatColor.AQUA}for you.")
                return true
            }
            /*
            (args[0] == "on" || args[0] == "off") && args.size == 2 && args[1] == "global" -> {
                // Check for permissions first.
                if (!sender.hasPermission("PhantomControl.globaltoggle"))
                    sender.sendMessage("${ChatColor.RED}You do not have enough permissions for this!")
                // If enabled, we tell them phantoms are already enabled globally.
                else if (plugin.isGloballyEnabled() && args[0] == "on")
                    sender.sendMessage("${ChatColor.RED}Phantoms are already enabled globally!")
                else if (!plugin.isGloballyEnabled() && args[0] == "off")
                    sender.sendMessage("${ChatColor.RED}Phantoms are already disabled globally!")
                else {
                    // We enable phantoms globally.
                    // TODO: Warn about exemptedPlayers getting inverted.
                    plugin.storage.set("global", args[0] == "on")
                    plugin.saveStorage()
                    sender.sendMessage("${ChatColor.GREEN}Phantoms have been ${
                    if (args[0] == "on") "enabled" else "disabled"
                    } globally!")
                }
                return true
            }
            */
            (args[0] == "on" || args[0] == "off") && args.size == 2 && args[1] == "global" -> {
                sender.sendMessage("${ChatColor.RED}Currently, enabling phantoms globally is not allowed.")
                return true
            }
            args[0] == "on" && args.size == 1 -> {
                when {
                    sender !is Player -> sender.sendMessage("${ChatColor.RED}This command cannot be executed from console!")
                    plugin.isEnabledForPlayer(sender.name) -> sender.sendMessage("${ChatColor.RED}You already have phantoms enabled!")
                    else -> {
                        // We exempt the player.
                        val list = plugin.storage.getStringList("exemptedPlayers")
                        if (plugin.isGloballyEnabled()) list.remove(sender.name) else list.add(sender.name)
                        plugin.storage.set("exemptedPlayers", list)
                        plugin.saveStorage()
                        sender.sendMessage("${ChatColor.GREEN}Phantoms have been enabled for you!")
                    }
                }
                return true
            }
            args[0] == "on" && args.size == 2 -> {
                // TODO: Check if player exists.
                if (!sender.hasPermission("phantomcontrol.globaltoggle"))
                    sender.sendMessage("${ChatColor.RED}You do not have permission for this!")
                // If enabled, we tell them phantoms are already enabled.
                else if (plugin.isEnabledForPlayer(args[1]))
                    sender.sendMessage("${ChatColor.RED}They already have phantoms enabled!")
                // else if (plugin.server.getPlayerExact(args[1]) == null)
                    // sender.sendMessage("${ChatColor.RED}This player does not exist!")
                else {
                    // We exempt the player.
                    val list = plugin.storage.getStringList("exemptedPlayers")
                    if (plugin.isGloballyEnabled()) list.remove(args[1]) else list.add(args[1])
                    plugin.storage.set("exemptedPlayers", list)
                    plugin.saveStorage()
                    sender.sendMessage("${ChatColor.GREEN}Phantoms have been enabled for them!")
                }
                return true
            }
            args[0] == "off" && args.size == 1 -> {
                if (sender !is Player)
                    sender.sendMessage("${ChatColor.RED}This command cannot be executed from console!")
                // If disabled, we tell them phantoms are already disabled.
                else if (!plugin.isEnabledForPlayer(sender.name))
                    sender.sendMessage("${ChatColor.RED}You already have phantoms disabled!")
                else if ((sender.world.time > 13800 || sender.world.isThundering) && !sender.hasPermission("phantomcontrol.globaltoggle"))
                    sender.sendMessage("${ChatColor.RED}Phantoms may not be toggled at night or during thunderstorms!")
                else {
                    // We remove the player's exemption.
                    val list = plugin.storage.getStringList("exemptedPlayers")
                    if (plugin.isGloballyEnabled()) list.add(sender.name) else list.remove(sender.name)
                    plugin.storage.set("exemptedPlayers", list)
                    plugin.saveStorage()
                    sender.sendMessage("${ChatColor.GREEN}Phantoms have been disabled for you!")
                }
                return true
            }
            args[0] == "off" && args.size == 2 -> {
                // TODO: Check if player exists.
                if (!sender.hasPermission("phantomcontrol.globaltoggle"))
                    sender.sendMessage("${ChatColor.RED}You do not have permission for this!")
                // If disabled, we tell them phantoms are already disabled.
                else if (!plugin.isEnabledForPlayer(args[1]))
                    sender.sendMessage("${ChatColor.RED}They already have phantoms disabled!")
                // else if (plugin.server.getPlayerExact(args[1]) == null)
                    // sender.sendMessage("${ChatColor.RED}This player does not exist!")
                else {
                    // We remove the player's exemption.
                    val list = plugin.storage.getStringList("exemptedPlayers")
                    if (plugin.isGloballyEnabled()) list.add(args[1]) else list.remove(args[1])
                    plugin.storage.set("exemptedPlayers", list)
                    plugin.saveStorage()
                    sender.sendMessage("${ChatColor.GREEN}Phantoms have been disabled for them!")
                }
                return true
            }
            args[0] == "toggle" && (args.size == 1 || args.size == 2) -> {
                // TODO: Check if player exists.
                if (sender !is Player)
                    sender.sendMessage("${ChatColor.RED}This command cannot be executed from console!")
                else if ((sender.world.time > 13800 || sender.world.isThundering) && !sender.hasPermission("phantomcontrol.globaltoggle"))
                    sender.sendMessage("${ChatColor.RED}Phantoms may not be toggled at night or during thunderstorms!")
                else if (args.size == 2 && !sender.hasPermission("phantomcontrol.globaltoggle"))
                    sender.sendMessage("${ChatColor.RED}You do not have permission for this!")
                // else if (args.size == 2 && plugin.server.getPlayerExact(args[1]) == null)
                // sender.sendMessage("${ChatColor.RED}This player does not exist!")
                else {
                    // We remove the player's exemption.
                    val player = if (args.size == 1) sender.name else args[1]
                    val list = plugin.storage.getStringList("exemptedPlayers")
                    if (list.contains(player)) list.remove(player) else list.add(player)
                    plugin.storage.set("exemptedPlayers", list)
                    plugin.saveStorage()
                    val res = if (plugin.isEnabledForPlayer(sender.name)) "enabled" else "disabled"
                    sender.sendMessage("${ChatColor.GREEN}Phantoms have been $res for ${if (player == sender.name) "you" else "them"}!")
                }
                return true
            }
            else -> return false
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        return when {
            args.isEmpty() -> listOf("on", "off", "status", "toggle")
            args.size == 1 -> listOf("on", "off", "status", "toggle").filter { it.startsWith(args[0]) }
            args.size == 2 && (args[0] == "on" || args[0] == "off") -> {
                plugin.server.onlinePlayers.map { it.name }.filter { it.startsWith(args[1]) }
            }
            else -> listOf("")
        }
    }
}