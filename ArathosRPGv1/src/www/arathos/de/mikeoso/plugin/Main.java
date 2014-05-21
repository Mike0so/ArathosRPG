package www.arathos.de.mikeoso.plugin;

import www.arathos.de.mikeoso.plugin.commands.LoreCommands;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import www.arathos.de.mikeoso.plugin.events.FoodLoreListener;
import www.arathos.de.mikeoso.plugin.events.HeroesListener;
import www.arathos.de.mikeoso.plugin.events.IndicatorListener;
import www.arathos.de.mikeoso.plugin.events.ItemLoreListener;
import www.arathos.de.mikeoso.plugin.events.RegenListenerFix;
import www.arathos.de.mikeoso.plugin.loremanagment.FoodLoreManager;
import www.arathos.de.mikeoso.plugin.loremanagment.ItemLoreManager;

import com.dsh105.holoapi.HoloAPI;
import com.herocraftonline.heroes.Heroes;

public class Main extends JavaPlugin {
	
	//Main Configuration
	public static FileConfiguration config = null;
	
	//External plugins
	public static Heroes heroes;
	public static Vault vault;
	public static HoloAPI holo;
	//Permissions class
	public static Permission perms;
	
	//Lore manager
	public static ItemLoreManager loreitemmanager;
	public static FoodLoreManager lorefoodmanager;
	
	public void onEnable() {
		loadConfig();
		enablePluginSupport();
		
    	getServer().getPluginManager().registerEvents(new ItemLoreListener(), this);
    	getServer().getPluginManager().registerEvents(new FoodLoreListener(), this);
    	getServer().getPluginManager().registerEvents(new IndicatorListener(), this);
    	
    	if (config.getBoolean("General.heroes")) {
    		getServer().getPluginManager().registerEvents(new HeroesListener(), this);
    	} else {
    		getServer().getPluginManager().registerEvents(new RegenListenerFix(), this);
    	}
    	loadManager();
		setupPermissions();
	}

	public void onDisable() {
		
	}
	
	//Check for all supported plugins
	public void enablePluginSupport() {
		heroes = (Heroes)getServer().getPluginManager().getPlugin("Heroes");
		vault = (Vault)getServer().getPluginManager().getPlugin("Vault");
		holo = (HoloAPI)getServer().getPluginManager().getPlugin("HoloAPI");
		vault = (Vault)getServer().getPluginManager().getPlugin("Vault");
			
		if (heroes != null) {
			getServer().getLogger().info(ChatColor.YELLOW + "[ArathosRPG]Heroes support enabled, features active");
		} else {
			heroes = null;
		}
		if (vault != null)  {
			getServer().getLogger().info(ChatColor.YELLOW + "[ArathosRPG]Vault found on this Server, permissions enabled.");
		} else {
			vault = null;
		}
		if (holo != null) {
			getServer().getLogger().info(ChatColor.YELLOW + "[ArathosRPG]HoloAPI found on this Server, class hologram enabled.");
		} else {
			holo = null;
		}
	}
	
	
	//load all lore manager features
	public void loadManager() {
		
		if (loreitemmanager == null) {
			loreitemmanager = new ItemLoreManager(this);
		}
		if (lorefoodmanager == null) {
			lorefoodmanager = new FoodLoreManager(this);
		}
	
		getCommand("arpg").setExecutor(new LoreCommands(this));
	}
	
	
	//load default config and copy defaults
	public void loadConfig() {
		config = getConfig();
		config.options().copyDefaults(true);
		saveConfig();
	}
	
	
	//Setup all permissions for the plugin and players.
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    
    }

