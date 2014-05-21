package www.arathos.de.mikeoso.plugin.loremanagment;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.herocraftonline.heroes.characters.Hero;

import www.arathos.de.mikeoso.plugin.Main;

public class FoodLoreManager {

	private Pattern FhealthRegex;
	private Pattern FmanaRegex;
	
	@SuppressWarnings("unused")
	private Main plugin;
	
	public FoodLoreManager(Main plugin) {
		
		this.plugin = plugin;
		
		FhealthRegex = Pattern.compile("[+](\\d+)[ ](" + Main.config.getString("General.Lores.Health.keyword").toLowerCase() + ")");
		FmanaRegex = Pattern.compile("[+](\\d+)[ ](" + Main.config.getString("General.Lores.FoodMana.keyword").toLowerCase() + ")");
	}
			
	
	//FOOD HEALTH LORE
	  
	
	//Placeholder
	//Placeholder
	  public void applyFoodManaBonus(Hero hero) {
		  
		  if (!hero.getPlayer().isValid()) {
			  return;
		  }
		  Integer manaBonus = Integer.valueOf(getFoodMana(hero.getPlayer())).intValue();
		  
		  if (hero.getPlayer() instanceof Player) {
			  if (hero.getMana() + manaBonus > hero.getMaxMana()) {
				  hero.setMana(hero.getMaxMana());
			  }
			  hero.setMana(hero.getMana() + manaBonus);
		  }
	  }
	  
	  public int getFoodMana(Player player) {
		  
		  Integer manaBonus = Integer.valueOf(0);
		  ItemStack hand = player.getItemInHand();
		  
		  if((hand != null) && (hand.hasItemMeta()) && (hand.getItemMeta().hasLore())) {
			  List<String> lore = hand.getItemMeta().getLore();
			  String allLore = lore.toString().toLowerCase();
			  
			  Matcher matcher = this.FmanaRegex.matcher(allLore);
			  if (matcher.find()) {
				  manaBonus += Integer.valueOf(manaBonus.intValue() + Integer.valueOf(matcher.group(1)).intValue());
			  }
		  }
		  return manaBonus.intValue();
	  }
	  
	  
	  //MANA HEALTH LORE
	  
	  
	  //Placeholder
	  //Placeholder
	  //Placeholder
	  public void applyFoodBonus(Player player) {
		  if (!player.isValid()) {
			  return;
		  }
		  Integer foodBonus = Integer.valueOf(getFoodHealth(player));
		  Integer maxhealth = Integer.valueOf((int)player.getMaxHealth());
		  Integer health = Integer.valueOf((int)player.getHealth());
		  
		  if ((player instanceof Player)) {
			  if (health.intValue() + foodBonus.intValue() > maxhealth.intValue()) {
				  player.setHealth(maxhealth.intValue());
				  return;
			  }
			  player.setHealth(health.intValue() + foodBonus.intValue());
		  }
	  }
	  
	  	//PLACEHOLDER
	  	//PLACEHOLDER
	  	public int getFoodHealth(Player player) {
	  		Integer foodBonus = Integer.valueOf(0);
	  		ItemStack hand = player.getItemInHand();
	  		
	  		if ((hand != null) && (hand.hasItemMeta()) && (hand.getItemMeta().hasLore())) {
	  			List<String> lore = hand.getItemMeta().getLore();
	  			String allLore = lore.toString().toLowerCase();
	  			
	  			Matcher matcher = this.FhealthRegex.matcher(allLore);
	  			if (matcher.find()) {
	  				foodBonus += Integer.valueOf(foodBonus.intValue() + Integer.valueOf(matcher.group(1)).intValue());
	  			}
	  		}
			return foodBonus.intValue();
	  	}

}
