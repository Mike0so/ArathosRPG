package www.arathos.de.mikeoso.plugin.events;

import www.arathos.de.mikeoso.plugin.Main;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import com.dsh105.holoapi.HoloAPI;


public class IndicatorListener implements Listener {
	
	

	@EventHandler(priority = EventPriority.MONITOR)
	public void onHealthChangeE(EntityRegainHealthEvent event) {
		if (Main.holo != null) {
		
		if (event.getEntity() instanceof Player) {
			
			String color = Main.config.getString("General.Plugin.Support.Holograms.health.color");
			int amount = (int) event.getAmount() + Main.loreitemmanager.getRegenBonus((Player)event.getEntity());
			String prefix = Main.config.getString("General.Plugin.Support.Holograms.health.prefix");
			
			color.replaceAll("&", "§");
			
		HoloAPI.getManager().createSimpleHologram(event.getEntity().getLocation().add(0, 1.5, 0.5), 4,true,
								color + "+" + amount + " " + prefix);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		
		if (Main.holo != null) {
				if ((event.getDamager() instanceof Player))	{
			
		String color = Main.config.getString("General.Plugin.Support.Holograms.damage.color");
		String prefix = Main.config.getString("General.Plugin.Support.Holograms.damage.prefix");
		int amount = (int) event.getDamage();
		
		color.replaceAll("&", "§");
		
		HoloAPI.getManager().createSimpleHologram(event.getEntity().getLocation().add(0.5,1,0),4,true,
				color + "-" + amount + " " + prefix);			
		} else {
		if ((event.getDamager() instanceof Monster)) {
			
		String color = Main.config.getString("General.Plugin.Support.Holograms.damage.color");
		String prefix = Main.config.getString("General.Plugin.Support.Holograms.damage.prefix");
		int amount = (int) event.getDamage();
			
		HoloAPI.getManager().createSimpleHologram(event.getEntity().getLocation().add(0.5,1,0),4,true,
				color + "-" + amount + " " + prefix);
		}
		}
	}
}
}
