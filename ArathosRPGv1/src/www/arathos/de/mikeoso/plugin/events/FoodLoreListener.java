package www.arathos.de.mikeoso.plugin.events;

import www.arathos.de.mikeoso.plugin.Main;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import com.herocraftonline.heroes.characters.Hero;

public class FoodLoreListener implements Listener {
	
	
	//PLACEHOLDER
	//PLACEHOLDER
	@EventHandler(priority=EventPriority.MONITOR)
	public void onConsume(PlayerItemConsumeEvent event) {
		
		if (event.getPlayer() instanceof Player) {
			Main.lorefoodmanager.applyFoodBonus(event.getPlayer());
			if (Main.heroes  != null){
				Hero hero = Main.heroes.getCharacterManager().getHero(event.getPlayer());
				Main.lorefoodmanager.applyFoodManaBonus(hero);
			}
		}
	}

	
	//PLACEHOLDER
	//PLACEHOLDER
}
