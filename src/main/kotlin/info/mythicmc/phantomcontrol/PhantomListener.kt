package info.mythicmc.phantomcontrol

import com.destroystokyo.paper.event.entity.PhantomPreSpawnEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Phantom
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent

class PhantomListener(private val plugin: PhantomControl) : Listener {
    private val naturalSpawnReason = CreatureSpawnEvent.SpawnReason.NATURAL

    @EventHandler
    fun onPhantomPreSpawnEvent(e: PhantomPreSpawnEvent) {
        // If the entity is not naturally spawned, we don't care.
        if (e.reason != naturalSpawnReason || e.spawningEntity !is Player) return
        // Check if phantoms are disabled for the player.
        val player = e.spawningEntity ?: return
        if (!plugin.isEnabledForPlayer(player.name)) {
            e.isCancelled = true
            e.setShouldAbortSpawn(true)
        }
    }

    @EventHandler
    fun onEntityDamageByEntityEvent(e: EntityDamageByEntityEvent) {
        // Check for phantom damaging player who spawned it with disabled phantoms and vice-versa.
        // If either, de-spawn the phantom.
        // Also check for phantom damaging player with disabled phantoms and did not spawn phantom.
        // If so, cancel the damage. Players attacking phantoms is allowed.
        if (
                e.damager.type == EntityType.PHANTOM &&
                e.damager.entitySpawnReason == naturalSpawnReason &&
                e.entityType == EntityType.PLAYER &&
                !plugin.isEnabledForPlayer(e.entity.name)
        ) {
            // Cancel damage if player has phantoms disabled and has been attacked.
            e.isCancelled = true
            // Additionally, if the player spawned this phantom, it must be de-spawned.
            if ((e.damager as Phantom).spawningEntity == e.entity.uniqueId) e.damager.remove()
        }
        else if (
                e.damager.type == EntityType.PLAYER &&
                e.entityType == EntityType.PHANTOM &&
                e.entity.entitySpawnReason == naturalSpawnReason &&
                !plugin.isEnabledForPlayer(e.damager.name) &&
                (e.entity as Phantom).spawningEntity == e.damager.uniqueId
        ) {
            // Cancel damage and de-spawn the phantom if the player spawned it and attacked it.
            e.isCancelled = true
            e.entity.remove()
        }
        // For beds.
        if (
                e.damager.type == EntityType.PHANTOM &&
                e.damager.entitySpawnReason == naturalSpawnReason &&
                e.entityType == EntityType.PLAYER &&
                plugin.isEnabledForPlayer(e.entity.name) &&
                (e.entity as Player).isSleeping
        ) {
            // Cancel damage if player has phantoms enabled and has been attacked in bed.
            e.isCancelled = true
            // Additionally, if the player spawned this phantom, it must be de-spawned.
            if ((e.damager as Phantom).spawningEntity == e.entity.uniqueId) e.damager.remove()
        }
    }
}
