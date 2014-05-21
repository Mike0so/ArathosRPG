package www.arathos.de.mikeoso.plugin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import www.arathos.de.mikeoso.plugin.Main;

public class RegenListenerFix implements Listener {
	
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onRegenHealth(EntityRegainHealthEvent event) {
		if(event.isCancelled()) {
			return;
		}
			if (event.getEntity() instanceof Player) {
					event.setAmount(event.getAmount() + Main.loreitemmanager.getRegenBonus((Player)event.getEntity()));
			}
			if (event.getAmount() <= 0) {
				event.setCancelled(true);
			}
		}
}
