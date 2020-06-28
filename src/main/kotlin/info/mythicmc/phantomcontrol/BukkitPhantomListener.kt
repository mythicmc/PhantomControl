package info.mythicmc.phantomcontrol

import org.bukkit.entity.EntityType
import org.bukkit.entity.Phantom
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import java.util.UUID

class BukkitPhantomListener(private val plugin: PhantomControl) : Listener {
    private val phantomMap = hashMapOf<UUID, String>()

    @EventHandler
    fun onCreatureSpawnEvent(e: CreatureSpawnEvent) {
        
        // If the entity isn't a phantom with a player target, we don't care.
        if (e.entityType != EntityType.PHANTOM || (e.entity as Phantom).target !is Player) return
        // Check if phantoms are globally disabled and disabled for the player.
        val player = (e.entity as Phantom).target as Player? // TODO: Confirm whether this works or not.
        if (player != null && !plugin.isEnabledForPlayer(player.name)) {
            e.isCancelled = true
        }
        if (player != null) phantomMap[e.entity.uniqueId] = player.name
    }

    @EventHandler
    fun onEntityDamageByEntityEvent(e: EntityDamageByEntityEvent) {
        // Check for phantom damaging player who spawned it with disabled phantoms and vice-versa.
        // If either, despawn the phantom.
        // Also, check for phantom damaging player with disabled phantoms and did not spawn phantom.
        if (e.damager.type == EntityType.PHANTOM && e.entityType == EntityType.PLAYER && !plugin.isEnabledForPlayer(e.entity.name)) {
            e.isCancelled = true
            if (phantomMap[e.damager.uniqueId] == e.entity.name) e.damager.remove()
        } else if (e.damager.type == EntityType.PLAYER && e.entityType == EntityType.PHANTOM && !plugin.isEnabledForPlayer(e.damager.name)) {
            if (phantomMap[e.damager.uniqueId] == e.damager.name) {
                e.isCancelled = true
                e.entity.remove()
            }
        }
    }

    @EventHandler
    fun onEntityDeathEvent(e: EntityDeathEvent) {
        // Keep the Map clean.
        if (e.entityType == EntityType.PHANTOM && phantomMap.containsKey(e.entity.uniqueId)) {
            phantomMap.remove(e.entity.uniqueId)
        }
    }
}
