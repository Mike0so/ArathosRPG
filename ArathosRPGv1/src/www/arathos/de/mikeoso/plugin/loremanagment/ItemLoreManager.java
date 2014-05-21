package www.arathos.de.mikeoso.plugin.loremanagment;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.herocraftonline.heroes.characters.Hero;

import www.arathos.de.mikeoso.plugin.Main;

public class ItemLoreManager {

	private Pattern healthRegex;
	private Pattern levelRegex;
	private Pattern regenRegex;
	private Pattern damageValueRegex;
	private Pattern damageRangeRegex;
	private Pattern dodgeRegex;
	private Pattern critChanceRegex;
	private Pattern critDamageRegex;
	private Pattern lifestealRegex;
	private Pattern armorRegex;
	
	
	@SuppressWarnings("unused")
	private Main plugin;
	public static Random generator;

	
	public ItemLoreManager (Main plugin) {
		
		this.plugin = plugin;
		generator = new Random();
		
		
		healthRegex = Pattern.compile("[+](\\d+)[ ](" + Main.config.getString("General.Lores.Health.keyword").toLowerCase() + ")");
		regenRegex = Pattern.compile("[+](\\d+)[ ](" + Main.config.getString("General.Lores.HealthRegen.keyword").toLowerCase() + ")");
		damageValueRegex = Pattern.compile("[+](\\d+)[ ](" + Main.config.getString("General.Lores.Damage.keyword").toLowerCase() + ")");
		damageRangeRegex = Pattern.compile("(\\d+)(-)(\\d+)[ ](" + Main.config.getString("General.Lores.Damage.keyword").toLowerCase() + ")");
		dodgeRegex = Pattern.compile("[+](\\d+)[%][ ](" + Main.config.getString("General.Lores.Dodge.keyword").toLowerCase() +")");
		critChanceRegex = Pattern.compile("[+](\\d+)[%][ ](" + Main.config.getString("General.Lores.Critt-Chance.keyword").toLowerCase() +")");
		critDamageRegex = Pattern.compile("[+](\\d+)[ ](" + Main.config.getString("General.Lores.Critt-Damage.keyword").toLowerCase() + ")");
		lifestealRegex = Pattern.compile("[+](\\d+)[ ](" + Main.config.getString("General.Lores.Life-Steal.keyword").toLowerCase() + ")");
		armorRegex = Pattern.compile("[+](\\d+)[ ](" + Main.config.getString("General.Lores.Armour.keyword").toLowerCase() + ")");
		levelRegex = Pattern.compile("level (\\d+)()");
	}
	
	
					//####LEVEL LORE####
	
	
	  //##Check if the player has any item with the levelRex lore.
	 //##If the player level or (heroes level) is < than the lore.
	//## Fire the handleArmorRestriction.
	  public boolean canUse(Player player, ItemStack item) {
		  
		    if ((item != null) && 
		      (item.hasItemMeta()) && 
		      (item.getItemMeta().hasLore())) {
		      List<String> lore = item.getItemMeta().getLore();
		      String allLore = lore.toString().toLowerCase();
		      Matcher valueMatcher = this.levelRegex.matcher(allLore);

		      if (valueMatcher.find()) {
		    	  if (player.getLevel() < Integer.valueOf(valueMatcher.group(1)).intValue()) {
		        	player.sendMessage(Main.config.getString("General.Lores.Level.message"));
		        	return false;
		        	  }else if (Main.heroes != null) {
		        		  Hero hero = Main.heroes.getCharacterManager().getHero(player);
		        		  if (hero.getLevel() < Integer.valueOf(valueMatcher.group(1)).intValue()) {
		        			  hero.getPlayer().sendMessage(Main.config.getString("General.Lores.Level.message"));
		        			  return false;
		        		}
		            }
		       	}   
		    }
		    return true;
		    
	  }
	  	  //##If !canUse remove the ItemStack and set it into the next free slot,
	     //else drop in on the ground.
		  @SuppressWarnings("deprecation")
		public void handleArmorRestriction(Player player) {
		  
		    if (!canUse(player, player.getInventory().getBoots())) {
		      if (player.getInventory().firstEmpty() >= 0)
		        player.getInventory().addItem(new ItemStack[] { player.getInventory().getBoots() });
		      else {
		        player.getWorld().dropItem(player.getLocation(), player.getInventory().getBoots());
		      }
		      player.getInventory().setBoots(new ItemStack(0));
		    }

		    if (!canUse(player, player.getInventory().getChestplate())) {
		      if (player.getInventory().firstEmpty() >= 0)
		        player.getInventory().addItem(new ItemStack[] { player.getInventory().getChestplate() });
		      else {
		        player.getWorld().dropItem(player.getLocation(), player.getInventory().getChestplate());
		      }
		      player.getInventory().setChestplate(new ItemStack(0));
		    }

		    if (!canUse(player, player.getInventory().getHelmet())) {
		      if (player.getInventory().firstEmpty() >= 0)
		        player.getInventory().addItem(new ItemStack[] { player.getInventory().getHelmet() });
		      else {
		        player.getWorld().dropItem(player.getLocation(), player.getInventory().getHelmet());
		      }
		      player.getInventory().setHelmet(new ItemStack(0));
		    }

		    if (!canUse(player, player.getInventory().getLeggings())) {
		      if (player.getInventory().firstEmpty() >= 0)
		        player.getInventory().addItem(new ItemStack[] { player.getInventory().getLeggings() });
		      else {
		        player.getWorld().dropItem(player.getLocation(), player.getInventory().getLeggings());
		      }
		      player.getInventory().setLeggings(new ItemStack(0));
		    }

		    if (!canUse(player, player.getItemInHand())) {
		      if (player.getInventory().firstEmpty() >= 0)
		        player.getInventory().addItem(new ItemStack[] { player.getItemInHand() });
		      else {
		        player.getWorld().dropItem(player.getLocation(), player.getItemInHand());
		      }
		      player.getInventory().setItemInHand(new ItemStack(0));
		    }
		  }
		  
		  				//####HEALTH LORE####
		  
		  
		  //Get the players max health config or supported plugin.
		  //Get all armor contents / 5/6 slot and add it to the heroes current max-health.
		  //Check if the players max-health is not lower then the players health
		  //If so set the health to the players max-health amount
		  public void applyHpBonus(LivingEntity entity) {
			  
			    if (!entity.isValid()) {
			      return;
			    }
			    Integer hpToAdd = Integer.valueOf(getHpBonus((Player)entity));

			    if ((entity instanceof Player)) {
			      if (entity.getHealth() > getBaseHealth((Player)entity) + hpToAdd.intValue()) {
			        entity.setHealth(getBaseHealth((Player)entity) + hpToAdd.intValue());
			      }
			      entity.setMaxHealth(getBaseHealth((Player)entity) + hpToAdd.intValue());
			      
			    }
			  }
		  
		  //Read all armor contents and the 5 / 6 eq slot.
		  //If those items have a health lore.
		  //Add this lore to the hpToAdd integer.
		  public int getHpBonus(Player player) {
			  
			    Integer hpToAdd = Integer.valueOf(0);
			    for (ItemStack item : player.getInventory().getArmorContents()) {
			      if ((item != null) && (item.hasItemMeta()) && (item.getItemMeta().hasLore())) {
			        List<String> lore = item.getItemMeta().getLore();
			        String allLore = lore.toString().toLowerCase();

			        Matcher matcher = this.healthRegex.matcher(allLore);
			        if (matcher.find()) {
			          hpToAdd += Integer.valueOf(hpToAdd.intValue() + Integer.valueOf(matcher.group(1)).intValue());
			        }
			      }
			    }
			    ItemStack slot5 = player.getInventory().getItem(9);
				if(slot5!=null) {
					if(slot5.hasItemMeta()) {
						if(slot5.getItemMeta().hasLore()) {	
							List<String> lore = slot5.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = healthRegex.matcher(allLore);
							if(valueMatcher.find()) {
								hpToAdd += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}
				ItemStack slot6 = player.getInventory().getItem(10);
				if(slot6!=null) {
					if(slot6.hasItemMeta()) {
						if(slot6.getItemMeta().hasLore()) {	
							List<String> lore = slot6.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = healthRegex.matcher(allLore);
							if(valueMatcher.find()) {
								hpToAdd += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}

			    return hpToAdd.intValue();
			  }
		  
		  
		  //Check the config for the base-health.
		  //then return the amount of the base-health written in the config.
		  //If heroes is active base-health = heroes maxhealth
		  public int getBaseHealth(Player player) {
			  int hp = Main.config.getInt("General.Lores.Health.base-health");
			  if (Main.heroes != null) {
			  		hp = getHeroesHealth(player);
			  }
		    	return hp;
		  }
		  //If heroes is not null , check for each player his heroes class max health
		  //Then set this to the hp int of the base-health so the base-health will be overwritten.
		  public int getHeroesHealth(Player player) {
			  if (Main.heroes != null) {
				  if (Main.heroes.getCharacterManager() != null) {
					  if (Main.heroes.getCharacterManager().getHero(player) != null) {
						  return (int) Main.heroes.getCharacterManager().getHero(player).resolveMaxHealth();
					  }
				  }
			  }
			return Main.config.getInt("General.Lores.Health.base-health");
		  }
		  
		  
		  
		  				//Health Regen lore
		  
		  
		  //Check all items for the regen lore
		  //then add it to the regenBonus int
			public int getRegenBonus(Player entity) {
				if(!entity.isValid()) {
					return 0;
				}
				Integer regenBonus = 0;
				for(ItemStack item : entity.getEquipment().getArmorContents()) {
					if(item!=null) {
						if(item.hasItemMeta()) {
							if(item.getItemMeta().hasLore()) {
								List<String> lore = item.getItemMeta().getLore();
								String allLore = lore.toString().toLowerCase();

								Matcher matcher = regenRegex.matcher(allLore);
								if(matcher.find()) {
									regenBonus += Integer.valueOf(matcher.group(1));
								}					
							}
						}
					}
				}
				ItemStack slot5 = entity.getInventory().getItem(9);
				if(slot5!=null) {
					if(slot5.hasItemMeta()) {
						if(slot5.getItemMeta().hasLore()) {	
							List<String> lore = slot5.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = regenRegex.matcher(allLore);
							if(valueMatcher.find()) {
								regenBonus += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}
				ItemStack slot6 = entity.getInventory().getItem(10);
				if(slot6!=null) {
					if(slot6.hasItemMeta()) {
						if(slot6.getItemMeta().hasLore()) {	
							List<String> lore = slot6.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = regenRegex.matcher(allLore);
							if(valueMatcher.find()) {
								regenBonus += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}
				return regenBonus;
			}
			
			
		  
		               //####Dodge Lore####
		  
		  //Check all Items for the Dodge lore and set the dodge chance as int
			public int getDodgeBonus(Player player) {
				Integer dodgeBonus = 0;
				for(ItemStack item : player.getInventory().getArmorContents()) {
					if(item!=null) {
						if(item.hasItemMeta()) {
							if(item.getItemMeta().hasLore()) {	
								List<String> lore = item.getItemMeta().getLore();
								String allLore = lore.toString().toLowerCase();

								Matcher valueMatcher = dodgeRegex.matcher(allLore);
								if(valueMatcher.find()) {
									dodgeBonus += Integer.valueOf(valueMatcher.group(1));
								}
							}
						}
					}

				}
				ItemStack item = player.getEquipment().getItemInHand();
				if(item!=null) {
					if(item.hasItemMeta()) {
						if(item.getItemMeta().hasLore()) {	
							List<String> lore = item.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = dodgeRegex.matcher(allLore);
							if(valueMatcher.find()) {
								dodgeBonus += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}
				ItemStack slot5 = player.getInventory().getItem(9);
				if(slot5!=null) {
					if(slot5.hasItemMeta()) {
						if(slot5.getItemMeta().hasLore()) {	
							List<String> lore = slot5.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = dodgeRegex.matcher(allLore);
							if(valueMatcher.find()) {
								dodgeBonus += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}
				ItemStack slot6 = player.getInventory().getItem(10);
				if(slot6!=null) {
					if(slot6.hasItemMeta()) {
						if(slot6.getItemMeta().hasLore()) {	
							List<String> lore = slot6.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = dodgeRegex.matcher(allLore);
							if(valueMatcher.find()) {
								dodgeBonus += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}
				return dodgeBonus;
			}
			
			
			//PLACEHOLDER
			//PLACEHOLDER
			//PLACEHOLDER
			public boolean dodgedAttack(Player player) {
				if(!player.isValid()) {
					return false;
				}
				Integer chance= getDodgeBonus(player);	


				Integer roll = generator.nextInt(100)+1;

				if(chance>=roll) {
					return true;
				}
				return false;		
			}
			
			
					//####Critt-Chance Lore####
			
			//Check all armor contents for the crit chance lore
			//add it to the chance int then.
			
			
			private int getCritChance(LivingEntity entity) {
				Integer chance = 0;

				for(ItemStack item : entity.getEquipment().getArmorContents()) {
					if(item!=null) {
						if(item.hasItemMeta()) {
							if(item.getItemMeta().hasLore()) {	
								List<String> lore = item.getItemMeta().getLore();
								String allLore = lore.toString().toLowerCase();

								Matcher valueMatcher = critChanceRegex.matcher(allLore);
								if(valueMatcher.find()) {
									chance += Integer.valueOf(valueMatcher.group(1));
								}
							}
						}
					}

				}
				ItemStack item = entity.getEquipment().getItemInHand();
				if(item!=null) {
					if(item.hasItemMeta()) {
						if(item.getItemMeta().hasLore()) {	
							List<String> lore = item.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = critChanceRegex.matcher(allLore);
							if(valueMatcher.find()) {
								chance += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}
				if (entity instanceof Player) {

				ItemStack slot5 = ((Player) entity).getInventory().getItem(9);
				if(slot5!=null) {
					if(slot5.hasItemMeta()) {
						if(slot5.getItemMeta().hasLore()) {	
							List<String> lore = slot5.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = critChanceRegex.matcher(allLore);
							if(valueMatcher.find()) {
								chance += Integer.valueOf(valueMatcher.group(1));
							}
							}
						}
					}
				ItemStack slot6 = ((Player) entity).getInventory().getItem(10);
				if(slot6!=null) {
					if(slot6.hasItemMeta()) {
						if(slot6.getItemMeta().hasLore()) {	
							List<String> lore = slot6.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = critChanceRegex.matcher(allLore);
							if(valueMatcher.find()) {
								chance += Integer.valueOf(valueMatcher.group(1));
							}
							}
						}
					}
				}	
				return chance;
			}

			//PLACEHOLDER
			//PLACEHOLDER
			//PLACEHOLDER
			private boolean critAttack(LivingEntity entity) {
				if(!entity.isValid()) {
					return false;
				}
				Integer chance=getCritChance(entity);		

				Integer roll = generator.nextInt(100)+1;

				if(chance>=roll) {
					return true;
				}
				return false;			
			}
			
			//####Critt-Damage Lore####
			
			//If the strike is not critical (chance)
			//cancel and return 0
			//Else read all crit damage from the players slots
			//then add it to the damage int
			public int getCritDamage(LivingEntity entity) {
				if(!critAttack(entity)) {
					return 0;
				}
				Integer damage=0;

				for(ItemStack item : entity.getEquipment().getArmorContents()) {
					if(item!=null) {
						if(item.hasItemMeta()) {
							if(item.getItemMeta().hasLore()) {	
								List<String> lore = item.getItemMeta().getLore();
								String allLore = lore.toString().toLowerCase();

								Matcher valueMatcher = critDamageRegex.matcher(allLore);
								if(valueMatcher.find()) {
									damage += Integer.valueOf(valueMatcher.group(1));
								}
							}
						}
					}

				}
				ItemStack item = entity.getEquipment().getItemInHand();
				if(item!=null) {
					if(item.hasItemMeta()) {
						if(item.getItemMeta().hasLore()) {	
							List<String> lore = item.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = critDamageRegex.matcher(allLore);
							if(valueMatcher.find()) {
								damage += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}
				if (entity instanceof Player) {

				ItemStack slot5 = ((Player) entity).getInventory().getItem(9);
				if(slot5!=null) {
					if(slot5.hasItemMeta()) {
						if(slot5.getItemMeta().hasLore()) {	
							List<String> lore = slot5.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = critDamageRegex.matcher(allLore);
							if(valueMatcher.find()) {
								damage += Integer.valueOf(valueMatcher.group(1));
							}
							}
						}
					}
				ItemStack slot6 = ((Player) entity).getInventory().getItem(10);
				if(slot6!=null) {
					if(slot6.hasItemMeta()) {
						if(slot6.getItemMeta().hasLore()) {	
							List<String> lore = slot6.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = critDamageRegex.matcher(allLore);
							if(valueMatcher.find()) {
								damage += Integer.valueOf(valueMatcher.group(1));
							}
							}
						}
					}
				}
				return damage;
			}

					//####Armor Lore
			
			
			//Read all items for the armor lore and add it to the
			//armor int then.
			public int getArmorBonus(LivingEntity entity) {
				Integer armor=0;

				for(ItemStack item : entity.getEquipment().getArmorContents()) {
					if(item!=null) {
						if(item.hasItemMeta()) {
							if(item.getItemMeta().hasLore()) {	
								List<String> lore = item.getItemMeta().getLore();
								String allLore = lore.toString().toLowerCase();

								Matcher valueMatcher = armorRegex.matcher(allLore);
								if(valueMatcher.find()) {
									armor += Integer.valueOf(valueMatcher.group(1));
								}
							}
						}
					}

				}
				ItemStack item = entity.getEquipment().getItemInHand();
				if(item!=null) {
					if(item.hasItemMeta()) {
						if(item.getItemMeta().hasLore()) {	
							List<String> lore = item.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = armorRegex.matcher(allLore);
							if(valueMatcher.find()) {
								armor += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}
				if (entity instanceof Player) {

				ItemStack slot5 = ((Player) entity).getInventory().getItem(9);
				if(slot5!=null) {
					if(slot5.hasItemMeta()) {
						if(slot5.getItemMeta().hasLore()) {	
							List<String> lore = slot5.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = armorRegex.matcher(allLore);
							if(valueMatcher.find()) {
								armor += Integer.valueOf(valueMatcher.group(1));
							}
							}
						}
					}
				ItemStack slot6 = ((Player) entity).getInventory().getItem(10);
				if(slot6!=null) {
					if(slot6.hasItemMeta()) {
						if(slot6.getItemMeta().hasLore()) {	
							List<String> lore = slot6.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = armorRegex.matcher(allLore);
							if(valueMatcher.find()) {
								armor += Integer.valueOf(valueMatcher.group(1));
							}
							}
						}
					}
				}	
				return armor;
			}
			
			
			
			
							//####Life Steal lore
			
			
			//Read all items of the player for the steal lore
			//add the amount to the steal int then
			public int getLifeSteal(Player entity) {
				Integer steal=0;

				for(ItemStack item : entity.getEquipment().getArmorContents()) {
					if(item!=null) {
						if(item.hasItemMeta()) {
							if(item.getItemMeta().hasLore()) {	
								List<String> lore = item.getItemMeta().getLore();
								String allLore = lore.toString().toLowerCase();

								Matcher valueMatcher = lifestealRegex.matcher(allLore);
								if(valueMatcher.find()) {
									steal += Integer.valueOf(valueMatcher.group(1));
								}
							}
						}
					}

				}
				ItemStack item = entity.getEquipment().getItemInHand();
				if(item!=null) {
					if(item.hasItemMeta()) {
						if(item.getItemMeta().hasLore()) {	
							List<String> lore = item.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = lifestealRegex.matcher(allLore);
							if(valueMatcher.find()) {
								steal += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}
				ItemStack slot5 = entity.getInventory().getItem(9);
				if(slot5!=null) {
					if(slot5.hasItemMeta()) {
						if(slot5.getItemMeta().hasLore()) {	
							List<String> lore = slot5.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = lifestealRegex.matcher(allLore);
							if(valueMatcher.find()) {
								steal += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}
				ItemStack slot6 = entity.getInventory().getItem(10);
				if(slot6!=null) {
					if(slot6.hasItemMeta()) {
						if(slot6.getItemMeta().hasLore()) {	
							List<String> lore = slot6.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher valueMatcher = lifestealRegex.matcher(allLore);
							if(valueMatcher.find()) {
								steal += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}
				return steal;
			}

			
						//####Damage lore####
			//PLACEHOLDER
			//PLACEHOLDER
			//PLACEHOLDER
			public int getDamageBonus(LivingEntity entity) {
				if(!entity.isValid()) {
					return 0;
				}
				Integer damageMin = 0;
				Integer damageMax = 0;
				Integer damageBonus = 0;
				for(ItemStack item : entity.getEquipment().getArmorContents()) {
					if(item!=null) {
						if(item.hasItemMeta()) {
							if(item.getItemMeta().hasLore()) {	
								List<String> lore = item.getItemMeta().getLore();
								String allLore = lore.toString().toLowerCase();

								Matcher rangeMatcher = damageRangeRegex.matcher(allLore);
								Matcher valueMatcher = damageValueRegex.matcher(allLore);
								if(rangeMatcher.find()) {
									damageMin+= Integer.valueOf(rangeMatcher.group(1));
									damageMax+= Integer.valueOf(rangeMatcher.group(3));
								}
								if(valueMatcher.find()) {
									damageBonus += Integer.valueOf(valueMatcher.group(1));
								}
							}
						}
					}

				}
				ItemStack item = entity.getEquipment().getItemInHand();
				if(item!=null) {
					if(item.hasItemMeta()) {
						if(item.getItemMeta().hasLore()) {	
							List<String> lore = item.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher rangeMatcher = damageRangeRegex.matcher(allLore);
							Matcher valueMatcher = damageValueRegex.matcher(allLore);
							if(rangeMatcher.find()) {
								damageMin+= Integer.valueOf(rangeMatcher.group(1));
								damageMax+= Integer.valueOf(rangeMatcher.group(3));
							}
							if(valueMatcher.find()) {
								damageBonus += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}
				if (entity instanceof Player) {
				ItemStack slot5 = ((Player) entity).getInventory().getItem(9);
				if(slot5!=null) {
					if(slot5.hasItemMeta()) {
						if(slot5.getItemMeta().hasLore()) {	
							List<String> lore = slot5.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher rangeMatcher = damageRangeRegex.matcher(allLore);
							Matcher valueMatcher = damageValueRegex.matcher(allLore);
							if(rangeMatcher.find()) {
								damageMin+= Integer.valueOf(rangeMatcher.group(1));
								damageMax+= Integer.valueOf(rangeMatcher.group(3));
							}
							if(valueMatcher.find()) {
								damageBonus += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}	
				ItemStack slot6 = ((Player) entity).getInventory().getItem(10);
				if(slot6!=null) {
					if(slot6.hasItemMeta()) {
						if(slot6.getItemMeta().hasLore()) {	
							List<String> lore = slot6.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher rangeMatcher = damageRangeRegex.matcher(allLore);
							Matcher valueMatcher = damageValueRegex.matcher(allLore);
							if(rangeMatcher.find()) {
								damageMin+= Integer.valueOf(rangeMatcher.group(1));
								damageMax+= Integer.valueOf(rangeMatcher.group(3));
							}
							if(valueMatcher.find()) {
								damageBonus += Integer.valueOf(valueMatcher.group(1));
							}
						}
					}
				}
				}
				return (int) Math.round(Math.random()*(damageMax - damageMin) + damageMin + damageBonus + getCritDamage(entity));		
			}

			public boolean useRangeOfDamage(LivingEntity entity) {
				if(!entity.isValid()) {
					return false;
				}
				for(ItemStack item : entity.getEquipment().getArmorContents()) {
					if(item!=null) {
						if(item.hasItemMeta()) {
							if(item.getItemMeta().hasLore()) {	
								List<String> lore = item.getItemMeta().getLore();
								String allLore = lore.toString().toLowerCase();

								Matcher rangeMatcher = damageRangeRegex.matcher(allLore);
								if(rangeMatcher.find()) {
									return true;
								}					
							}
						}
					}
				}
				ItemStack item = entity.getEquipment().getItemInHand();
				if(item!=null) {
					if(item.hasItemMeta()) {
						if(item.getItemMeta().hasLore()) {
							List<String> lore = item.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher rangeMatcher = damageRangeRegex.matcher(allLore);
							if(rangeMatcher.find()) {
								return true;
							}				
						}
					}
				}
				if (entity instanceof Player) {
				ItemStack slot5 = ((Player) entity).getInventory().getItem(9);
				if(slot5!=null) {
					if(slot5.hasItemMeta()) {
						if(slot5.getItemMeta().hasLore()) {	
							List<String> lore = slot5.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher rangeMatcher = damageRangeRegex.matcher(allLore);
							if(rangeMatcher.find()) {
								return true;
							}	
						}
					}
				}
				ItemStack slot6 = ((Player) entity).getInventory().getItem(10);
				if(slot6!=null) {
					if(slot6.hasItemMeta()) {
						if(slot6.getItemMeta().hasLore()) {	
							List<String> lore = slot6.getItemMeta().getLore();
							String allLore = lore.toString().toLowerCase();

							Matcher rangeMatcher = damageRangeRegex.matcher(allLore);
							if(rangeMatcher.find()) {
								return true;
							}	
						}
					}
				}
				}
				return false;
			}
			
}
