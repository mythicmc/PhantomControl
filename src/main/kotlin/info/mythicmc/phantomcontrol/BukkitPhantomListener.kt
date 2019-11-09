package info.mythicmc.phantomcontrol

import org.bukkit.entity.EntityType
import org.bukkit.entity.Phantom
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent

class BukkitPhantomListener(private val plugin: PhantomControl) : Listener {
    @EventHandler
    fun onCreatureSpawnEvent(e: CreatureSpawnEvent) {
        // If the entity isn't a phantom with a player target, we don't care.
        if (e.entityType != EntityType.PHANTOM || (e as Phantom).target !is Player) return
        // Check if phantoms are globally disabled and disabled for the player.
        val player = (e as Phantom).target as Player // TODO: Confirm whether this works or not.
        if (!plugin.isEnabledForPlayer(player.name)) {
            e.isCancelled = true
        }
    }

    /*
    @EventHandler
    fun onEntityDamageByEntityEvent(e: EntityDamageByEntityEvent) {
        / Check for phantom damaging player who spawned it with disabled phantoms and vice-versa.
        // If either, despawn the phantom.
        // Also, check for phantom damaging player with disabled phantoms and did not spawn phantom.
        if (e.damager.type == EntityType.PHANTOM && e.entityType == EntityType.PLAYER && !plugin.isEnabledForPlayer(e.entity.name)) {
            e.isCancelled = true
            if ((e.damager as Phantom).spawningEntity == e.entity.uniqueId) e.damager.remove() // TODO: Eliminate Paper.
        } else if (e.damager.type == EntityType.PLAYER && e.entityType == EntityType.PHANTOM && !plugin.isEnabledForPlayer(e.damager.name)) {
            if ((e.entity as Phantom).spawningEntity == e.damager.uniqueId) { // TODO: Eliminate Paper.
                e.isCancelled = true
                e.entity.remove()
            }
        }
    }
    */
}