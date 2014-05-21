package www.arathos.de.mikeoso.plugin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import www.arathos.de.mikeoso.plugin.Main;

import com.dsh105.holoapi.HoloAPI;
import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.events.HeroRegainHealthEvent;
import com.herocraftonline.heroes.api.events.HeroRegainManaEvent;
import com.herocraftonline.heroes.characters.Hero;

public class HeroesListener implements Listener {
	
	
	
	
	
	
	
	
	public void onHeroRegainHealth(HeroRegainHealthEvent event) {
		
		if ((event.getHero() instanceof Hero) && (Main.heroes != null)) {
			event.setAmount(event.getAmount() + Main.loreitemmanager.getRegenBonus((event.getHero().getPlayer())));
		}
	}

	//Heroes Indicators
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onManaChange(HeroRegainManaEvent event) {

		if (Main.heroes != null && Main.holo != null) {

		Hero hero = Heroes.getInstance().getCharacterManager().getHero(event.getHero().getPlayer());
		Player player = hero.getPlayer();

		if (player instanceof Player) {
	
			String color = Main.config.getString("General.Plugin.Support.Holograms.mana.color");
			int amount = event.getAmount();
			String prefix = Main.config.getString("General.Plugin.Support.Holograms.mana.prefix");
			
			color.replaceAll("&", "§");
		
		HoloAPI.getManager().createSimpleHologram(
						player.getEyeLocation().add(0, 0.5, 0), 4,true,color + "+" + amount + " " + prefix);
		}
	}
}
}
