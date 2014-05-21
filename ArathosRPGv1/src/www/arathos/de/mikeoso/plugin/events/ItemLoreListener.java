package www.arathos.de.mikeoso.plugin.events;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import www.arathos.de.mikeoso.plugin.Main;

public class ItemLoreListener implements Listener {
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Main.loreitemmanager.applyHpBonus(event.getPlayer());
		event.getPlayer().sendMessage("Haha");
		if (event.getPlayer() instanceof Player) {
			Main.loreitemmanager.handleArmorRestriction((Player)event.getPlayer());
		}
	}
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Main.loreitemmanager.applyHpBonus(event.getPlayer());
		
		if (event.getPlayer() instanceof Player) {
			Main.loreitemmanager.handleArmorRestriction((Player)event.getPlayer());
		}
	}
	@EventHandler(priority=EventPriority.MONITOR)
	public void onInventoryClose(InventoryCloseEvent event) {
		Main.loreitemmanager.applyHpBonus(event.getPlayer());
		
		if (event.getPlayer() instanceof Player) {
			Main.loreitemmanager.handleArmorRestriction((Player)event.getPlayer());
		}
	}
	@EventHandler(priority=EventPriority.NORMAL)
	public void modifyEntityDamage(EntityDamageByEntityEvent event) {
		if(event.isCancelled() || !(event.getEntity() instanceof LivingEntity)) {
			return;
		}

		if (event.getEntity() instanceof Player) {
		if(Main.loreitemmanager.dodgedAttack((Player)event.getEntity())) {
			event.setDamage(0);
			event.setCancelled(true);
			return;
		}
		}

		if(event.getDamager() instanceof LivingEntity) {
			LivingEntity damager = (LivingEntity)event.getDamager();

			if(Main.loreitemmanager.useRangeOfDamage(damager)) {
				event.setDamage(Math.max(0, Main.loreitemmanager.getDamageBonus(damager) - Main.loreitemmanager.getArmorBonus((LivingEntity)event.getEntity())));
			} else {
				event.setDamage(Math.max(0, event.getDamage() + Main.loreitemmanager.getDamageBonus(damager) - Main.loreitemmanager.getArmorBonus((LivingEntity)event.getEntity())));
			}
			if (damager instanceof Player) {
			damager.setHealth(Math.min(damager.getMaxHealth(), damager.getHealth() + Math.min(Main.loreitemmanager.getLifeSteal((Player)damager),event.getDamage())));	
			}
		} else if(event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow)event.getDamager();
			if(arrow.getShooter() != null && arrow.getShooter() instanceof LivingEntity) {
				LivingEntity damager = (LivingEntity)arrow.getShooter();				

				if(Main.loreitemmanager.useRangeOfDamage(damager)) {
					event.setDamage(Math.max(0, Main.loreitemmanager.getDamageBonus(damager) - Main.loreitemmanager.getArmorBonus((LivingEntity)event.getEntity())));
				} else {
					event.setDamage(Math.max(0, event.getDamage() + Main.loreitemmanager.getDamageBonus(damager)) - Main.loreitemmanager.getArmorBonus((LivingEntity)event.getEntity()));
				}
				if (damager instanceof Player) {
				damager.setHealth(Math.min(damager.getMaxHealth(), damager.getHealth() + Math.min(Main.loreitemmanager.getLifeSteal((Player)damager),event.getDamage())));
				}
			}
		}
	}	
}


