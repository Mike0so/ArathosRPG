package www.arathos.de.mikeoso.plugin.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import www.arathos.de.mikeoso.plugin.Main;

import com.dsh105.holoapi.HoloAPI;
import com.herocraftonline.heroes.characters.classes.HeroClass;

public class LoreCommands implements CommandExecutor {

	   public int id = 0;
	   public int amount = 0;
	   public int amountlines = 0;
	   public int amountlines2 = 1;
	   
	   private Main plugin;
	   
	   public LoreCommands(Main plugin) {
		   this.plugin = plugin;
	   }
	    
	   public ArrayList<String> holog = new ArrayList<String>();
		@Override
		public boolean onCommand(CommandSender cs, Command cmd, String label,String[] args) {
			if (args.length >= 1 && args[0].equalsIgnoreCase("help")) {
				if (cs instanceof Player && Main.perms.has(cs, "arpg.help")) {
					Player player = (Player)cs;
					if (args.length == 1) {
					player.sendMessage(ChatColor.GOLD + "***ArathosRPG-Command***");
					player.sendMessage(ChatColor.RED + "/arpg help - Display's any Commands with Description.");
					player.sendMessage(ChatColor.RED + "/arpg rename - Renames or adds a name to the Item in you hand.[/arpg rename <newname>");
					player.sendMessage(ChatColor.RED + "/arpg addlore - This will add a new lore to the item in your hand.[/arpg addlore <amountoflines> <line1> <line2> max is 4]");
					player.sendMessage(ChatColor.RED + "/arpg duplicate - Clones the item in your hand.");
					player.sendMessage(ChatColor.RED + "/arpg classholo <heroesclassname> - Spawns a class hologram for your'e heroes class.");
					return true;
					}	
				} else {
					cs.sendMessage(ChatColor.GOLD + "[ArathosRPG]:" + ChatColor.RED + " " + "You dont have the permissions to use this command." );
				}
			} else if (args.length >= 1 && args[0].equalsIgnoreCase("duplicate")) {
				if (cs instanceof Player && Main.perms.has(cs, "arpg.duplicate")) {
					Player player = (Player)cs;
					if(args.length == 1) {
						ItemStack iteminhand = player.getItemInHand();
						if(iteminhand.getType().equals(Material.AIR)) {
							player.sendMessage(ChatColor.GOLD + "[ArathosRPG]" + ChatColor.RED + "You need to hold an Item for this Command.");
							return true;
						}else {
							ItemStack iteminhand2 = player.getItemInHand();
							ItemMeta itemnamemeta = iteminhand2.getItemMeta();
							ItemStack duplicated = new ItemStack(iteminhand);
							duplicated.setItemMeta(itemnamemeta);
							player.getInventory().addItem(new ItemStack[] { duplicated });
							player.sendMessage(ChatColor.GOLD + "[ArathosRPG]" + ChatColor.RED + "Item successfuly duplicated.");
							return true;
						}
					}
				} else {
					cs.sendMessage(ChatColor.GOLD + "[ArathosRPG]:" + ChatColor.RED + " " + "You dont have the permissions to use this command.");
				}
			} else if (args.length >= 1 && args[0].equalsIgnoreCase("rename")) {
				if (cs instanceof Player && Main.perms.has(cs, "arpg.rename")) {
					Player player = (Player)cs;
					if (args.length == 1) {
						cs.sendMessage(ChatColor.GOLD + "[ArathosRPG]" + ChatColor.RED + "You need to insert a new name, for example /arpg rename newItem");
						return true;
					}else if (args.length == 2) {
						String newname = args[1];
						newname = newname.replaceAll("_", " ");
						newname = newname.replaceAll("&", "§");
						try {
							ItemStack iteminhand = player.getItemInHand();
							ItemMeta itemmeta = iteminhand.getItemMeta();
							itemmeta.setDisplayName(newname);
							iteminhand.setItemMeta(itemmeta);
							return true;
						} catch (Exception e) {
							player.sendMessage(ChatColor.GOLD + "[ArathosRPG]" + ChatColor.RED + "You have nothing in your hand to rename.");
							return false;
						}
					}
				} else {
					cs.sendMessage(ChatColor.GOLD + "[ArathosRPG]:" + ChatColor.RED + " " + "You dont have the permissions to use this command.");
				}
			} else if (args.length >= 1 && args[0].equalsIgnoreCase("addlore")) {
				if (cs instanceof Player && Main.perms.has(cs, "arpg.addlore")) {
					Player player = (Player)cs;
					try {
						@SuppressWarnings("unused")
						List<String> getLore;
						getLore = player.getItemInHand().getItemMeta().getLore();
					} catch (Exception e) {
						player.sendMessage("das ist ein test");
						return false;
					}
					List<String> getLore = null;
					if (getLore ==  null) {
						getLore = new ArrayList<String>();
					}
					ItemStack handitem = player.getItemInHand();
					ItemMeta itemlore = handitem.getItemMeta();
					if (args.length > 2) {
						try {
							amountlines2 = Integer.parseInt(args[1]);
						} catch (NumberFormatException ex) {
							player.sendMessage(ChatColor.GOLD + "[ArathosRPG]:" + ChatColor.RED + " " + "You must use an Integer for the [AmountOfLines]");
							return true;
						}
						if (amountlines2 == 1) {
			                String getlore1 = args[2];
			                getlore1 = getlore1.replaceAll("_", " ");
			                getlore1 = getlore1.replaceAll("&", "§");
			                getLore.add(getlore1);
			                itemlore.setLore(getLore);
			                handitem.setItemMeta(itemlore);
			                player.sendMessage(ChatColor.GOLD + "[ArathosRPG]:" + ChatColor.RED + " " + "Added 1 line of lore to the item");
						} else if (amountlines2 == 2) {
							if(args.length == 4) {
				                  String getlore1 = args[2];
				                  getlore1 = getlore1.replaceAll("_", " ");
				                  getlore1 = getlore1.replaceAll("&", "§");
				                  String getlore2 = args[3];
				                  getlore2 = getlore2.replaceAll("_", " ");
				                  getlore2 = getlore2.replaceAll("&", "§");
				                  getLore.add(getlore1);
				                  getLore.add(getlore2);
				                  itemlore.setLore(getLore);
				                  handitem.setItemMeta(itemlore);
				                  player.sendMessage(ChatColor.GOLD + "[ArathosRPG]:" + ChatColor.RED + " " + "Added 2 lines of lore to the item");
							}else {
							player.sendMessage(ChatColor.GOLD + "[ArathosRPG]:" + ChatColor.RED + " " + "No valid amount");
							
							} 
						} else if (amountlines2 == 3) {
			                if (args.length == 5) {
			                    String getlore1 = args[2];
			                    getlore1 = getlore1.replaceAll("_", " ");
			                    getlore1 = getlore1.replaceAll("&", "§");
			                    String getlore2 = args[3];
			                    getlore2 = getlore2.replaceAll("_", " ");
			                    getlore2 = getlore2.replaceAll("&", "§");
			                    String getlore3 = args[4];
			                    getlore3 = getlore3.replaceAll("_", " ");
			                    getlore3 = getlore3.replaceAll("&", "§");
			                    getLore.add(getlore1);
			                    getLore.add(getlore2);
			                    getLore.add(getlore3);
			                    itemlore.setLore(getLore);
			                    handitem.setItemMeta(itemlore);
			                    player.sendMessage(ChatColor.GOLD + "[ArathosRPG]:" + ChatColor.RED + " " + "Added 3 lines of lore to the item");
						} else { 
						player.sendMessage(ChatColor.GOLD + "[ArathosRPG]:" + ChatColor.RED + " " + "No valid amount");
						}
					}else if (amountlines2 == 4) {
		                if (args.length == 6) {
		                    String getlore1 = args[2];
		                    getlore1 = getlore1.replaceAll("_", " ");
		                    getlore1 = getlore1.replaceAll("&", "§");
		                    String getlore2 = args[3];
		                    getlore2 = getlore2.replaceAll("_", " ");
		                    getlore2 = getlore2.replaceAll("&", "§");
		                    String getlore3 = args[4];
		                    getlore3 = getlore3.replaceAll("_", " ");
		                    getlore3 = getlore3.replaceAll("&", "§");
		                    String getlore4 = args[5];
		                    getlore4 = getlore4.replaceAll("_", " ");
		                    getlore4 = getlore4.replaceAll("&", "§");
		                    getLore.add(getlore1);
		                    getLore.add(getlore2);
		                    getLore.add(getlore3);
		                    getLore.add(getlore4);
		                    itemlore.setLore(getLore);
		                    handitem.setItemMeta(itemlore);
		                    player.sendMessage(ChatColor.GOLD + "[ArathosRPG]:" + ChatColor.RED + " " + "Added 4 lines of lore to the item");
					} else {
						player.sendMessage(ChatColor.GOLD + "[ArathosRPG]:" + ChatColor.RED + " " + "No valid amount");
					}
					}else if (amountlines2 > 4) {
						player.sendMessage(ChatColor.GOLD + "[ArathosRPG]:" + ChatColor.RED + " " + "Incorrect number of lore's you can only have 4");
					}
				} else {
					player.sendMessage(ChatColor.GOLD + "[ArathosRPG]:" + ChatColor.RED + " " + "Incorrect number of lore's you can only have 4");
				}
			} else {
				cs.sendMessage(ChatColor.GOLD + "[ArathosRPG]:" + ChatColor.RED + " " + "You dont have the permissions to use this command.");
			}
				return true;
			} if (args.length >= 1 && args[0].equalsIgnoreCase("classholo")) {
				if (cs instanceof Player && Main.perms.has(cs, "arpg.hologram")) {
					if (args.length == 1) {
						if (Main.holo != null) {
						HeroClass hc = Main.heroes.getClassManager().getClass(args[1]);
						
						holog.add(ChatColor.GOLD + "******" + hc.getName() + "*******");
						holog.add(ChatColor.GOLD + "Description:" + hc.getDescription());
						holog.add(ChatColor.GOLD + "Skills:" + hc.getSkillNames());
						holog.add(ChatColor.GOLD + "*******Attribute*******");
						holog.add(ChatColor.GOLD + "Base-Health:" + hc.getBaseMaxHealth());
						holog.add(ChatColor.GOLD + "Base-Mana:" + hc.getBaseMaxMana());
						
						HoloAPI.getInstance();
						HoloAPI.getManager().createSimpleHologram(
								((Player)cs).getEyeLocation(),
							Main.config.getInt("General.Plugin.Support.holo-shownfor"), holog);
						} else {
							cs.sendMessage(ChatColor.GOLD + "You need to enable the heroes and holoapi support for this command.");

						}
					}
				}
				return true;
			} else if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
				if (cs instanceof Player && Main.perms.has(cs, "arpg.reload")) {
					if (args.length == 1) {
						this.plugin.reloadConfig();
						cs.sendMessage( ChatColor.RED + "[ArathosRPG]config reloaded");
						return true;
					}
				}			
			}
			return false;
		}

	}
