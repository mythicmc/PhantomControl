package info.mythicmc.phantomcontrol

import org.bukkit.entity.EntityType
import org.bukkit.entity.Phantom
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent

class PhantomListener(private val plugin: PhantomControl) : Listener {
    @EventHandler
    fun onCreatureSpawnEvent(e: CreatureSpawnEvent) {
        if (e.entityType != EntityType.PHANTOM) return
        // Check if phantoms are globally disabled and disabled for the player.
        // val player = (e as Phantom).target
        if (!plugin.getGlobalSetting() && !plugin.getPlayerSetting()) {
            e.isCancelled = true
        }
    }

    /*
    @EventHandler
    fun onEntityDamageByEntityEvent(e: EntityDamageByEntityEvent) {
        // TODO: Check for phantom damaging player who spawned it with disabled phantoms and vice-versa.
        // TODO: Check for phantom damaging player with disabled phantoms and did not spawn phantom.
    }
    */
}