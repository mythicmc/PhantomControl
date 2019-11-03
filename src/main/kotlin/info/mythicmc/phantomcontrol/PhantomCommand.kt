package info.mythicmc.phantomcontrol

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class PhantomCommand(private val plugin: PhantomControl) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            args[0] == "on" && args.size == 1 -> {
                // If enabled, we tell them phantoms are already enabled.
                if (plugin.isEnabledForPlayer(sender.name))
                    sender.sendMessage("${ChatColor.RED}You already have phantoms enabled!")
                else {
                    // We exempt the player.
                    val list = plugin.storage.getStringList("exemptedPlayers")
                    if (plugin.isGloballyEnabled()) list.remove(sender.name) else list.add(sender.name)
                    plugin.storage.set("exemptedPlayers", list)
                    plugin.saveStorage()
                    sender.sendMessage("${ChatColor.GREEN}Phantoms have been enabled for you!")
                }
                return true
            }
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
            args[0] == "on" && args.size == 2 -> {
                // TODO: Check if player exists.
                // If enabled, we tell them phantoms are already enabled.
                if (plugin.isEnabledForPlayer(args[1]))
                    sender.sendMessage("${ChatColor.RED}They already have phantoms enabled!")
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
                // If disabled, we tell them phantoms are already disabled.
                if (!plugin.isEnabledForPlayer(sender.name))
                    sender.sendMessage("${ChatColor.RED}You already have phantoms disabled!")
                else {
                    // We remove the player's exemption.
                    val list = plugin.storage.getStringList("exemptedPlayers")
                    if (plugin.isGloballyEnabled()) list.add(sender.name)
                    else list.remove(sender.name)
                    plugin.storage.set("exemptedPlayers", list)
                    plugin.saveStorage()
                    sender.sendMessage("${ChatColor.GREEN}Phantoms have been enabled for you!")
                }
                return true
            }
            args[0] == "off" && args.size == 2 -> {
                // TODO: Check if player exists.
                // If disabled, we tell them phantoms are already disabled.
                if (!plugin.isEnabledForPlayer(args[1]))
                    sender.sendMessage("${ChatColor.RED}They already have phantoms disabled!")
                else {
                    // We remove the player's exemption.
                    val list = plugin.storage.getStringList("exemptedPlayers")
                    if (plugin.isGloballyEnabled()) list.add(args[1])
                    else list.remove(args[1])
                    plugin.storage.set("exemptedPlayers", list)
                    plugin.saveStorage()
                    sender.sendMessage("${ChatColor.GREEN}Phantoms have been enabled for them!")
                }
                return true
            }
            else -> return false
        }
    }
}