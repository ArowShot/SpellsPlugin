package com.reynixpvp.spellsplugin;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_12_R1.PlayerConnection;

import com.google.gson.Gson;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;

import com.reynixpvp.spellsplugin.spells.Spell;
import com.reynixpvp.spellsplugin.spells.Spells;

public class SpellPlugin extends JavaPlugin implements Listener {
	public Plugin pl;
	public BukkitScheduler scheduler;
	public Map<Player, Map<Integer, Spell>> selectedSpells = new HashMap<Player, Map<Integer, Spell>>();
    public Map<Player, Map<Spell, Long>> cooldowns = new HashMap<Player, Map<Spell, Long>>();
	public Map<Player, Integer> mana = new HashMap<Player, Integer>();
    
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player pl = e.getPlayer();
		resetSpells(pl);
		setMana(pl, mana.containsKey(pl)?getMana(pl):50);
	}
	
	@SuppressWarnings("deprecation")
    @Override
	public void onEnable() {
	    getServer().getScoreboardManager().getMainScoreboard().clearSlot(DisplaySlot.SIDEBAR);
	    this.pl = this;
        this.scheduler = getServer().getScheduler();
		getServer().getPluginManager().registerEvents(this, this);
		
		ItemStack wand = new ItemStack(Material.STICK, 1);
		ItemMeta im = wand.getItemMeta();
		im.setDisplayName(ChatColor.RESET+""+ChatColor.LIGHT_PURPLE+"Magical Wand");
		List<String> lore = im.getLore()!=null ? im.getLore() : new ArrayList<String>();
		lore.add(ChatColor.RESET+""+ChatColor.AQUA+"A magical wand that");
		lore.add(ChatColor.RESET+""+ChatColor.AQUA+"does magical things");
		im.setLore(lore);
		wand.setItemMeta(im);
		ShapedRecipe wandRecipe = new ShapedRecipe(wand);
		wandRecipe.shape(new String[]{" n","s "}).setIngredient('n', Material.GOLD_NUGGET).setIngredient('s', Material.STICK);
		getServer().addRecipe(wandRecipe);
		
		//TODO REMOVE
        ItemStack scythe = new ItemStack(Material.DIAMOND_HOE, 1);
        im = scythe.getItemMeta();
        im.setDisplayName(ChatColor.RESET+""+ChatColor.LIGHT_PURPLE+"Demon Scythe");
        lore = im.getLore()!=null ? im.getLore() : new ArrayList<String>();
        lore.add(ChatColor.RESET+""+ChatColor.AQUA+"A magical weapon that");
        lore.add(ChatColor.RESET+""+ChatColor.AQUA+"hurts things magically");
        im.setLore(lore);
        scythe.setItemMeta(im);
        ShapedRecipe scytheRecipe = new ShapedRecipe(scythe);
        scytheRecipe.shape(new String[]{"nnn","nsn","nnn"}).setIngredient('n', Material.GOLD_NUGGET).setIngredient('s', Material.DIAMOND_HOE);
        getServer().addRecipe(scytheRecipe);
        
        
		for(Player pl:getServer().getOnlinePlayers()) {
			resetSpells(pl);
			setMana(pl, mana.containsKey(pl)?getMana(pl):50);
		}
		
		for(Spells sp : Spells.values()) {
			getServer().getPluginManager().registerEvents(sp.getSpell(), this);
		}
		
		scheduler.scheduleSyncRepeatingTask(this, new BukkitRunnable(){
            @Override
            public void run() {
                for(Player pl:mana.keySet()) {
                    if(getMana(pl)<50) {
                        addMana(pl, 1);
                    }
                }
            }
		}, 0L, 20L);
	}
	
	private void setMana(Player pl, int mana) {
		this.mana.put(pl, mana);
	}
	
	private int getMana(Player pl) {
		return this.mana.get(pl);
	}
	
	private void reduceMana(Player pl, int mana) {
		this.mana.put(pl, getMana(pl)-mana);
	}
	
	private void addMana(Player pl, int mana) {
		this.mana.put(pl, getMana(pl)+mana);
	}
	
	private void resetSpells(Player pl) {
		Map<Integer, Spell> spells = new HashMap<Integer, Spell>();
		spells.put(0, null);
		spells.put(1, null);
		spells.put(2, null);
		spells.put(3, null);
		spells.put(4, null);
		spells.put(5, null);
		spells.put(6, null);
		spells.put(7, null);
		spells.put(8, null);
		selectedSpells.put(pl, spells);
	}
	
	private byte getGlassColor(ChatColor c) {
        if(c==ChatColor.AQUA)
            return 3;
        if(c==ChatColor.BLACK)
            return 15;
        if(c==ChatColor.BLUE)
            return 3;
        if(c==ChatColor.DARK_AQUA)
            return 9;
        if(c==ChatColor.DARK_BLUE)
            return 11;
        if(c==ChatColor.DARK_GRAY)
            return 7;
        if(c==ChatColor.DARK_GREEN)
            return 13;
        if(c==ChatColor.DARK_PURPLE)
            return 10;
        if(c==ChatColor.DARK_RED)
            return 14;
        if(c==ChatColor.GOLD)
            return 1;
        if(c==ChatColor.GRAY)
            return 8;
        if(c==ChatColor.GREEN)
            return 5;
        if(c==ChatColor.LIGHT_PURPLE)
            return 2;
        if(c==ChatColor.RED)
            return 6;
        if(c==ChatColor.WHITE)
            return 0;
        if(c==ChatColor.YELLOW)
            return 4;
	    return 0;
	}
	
	
	private void setSpell(Player pl, int slot, Spell spell) {
		Map<Integer, Spell> spells = selectedSpells.get(pl);
		spells.put(slot, spell);
		selectedSpells.put(pl, spells);
		if(spell!=null) {
    		ItemStack spellItem = new ItemStack(Material.STAINED_GLASS, 1, getGlassColor(spell.getColor()));
    		ItemMeta im = spellItem.getItemMeta();
    		im.setDisplayName(ChatColor.RESET+""+ChatColor.BLUE+"Bound spell: "+spell.getDisplayName());
    		List<String> lore = new ArrayList<String>();
    		String htxt = getHoverText(spell, pl);
    		for(String cs:htxt.split("\n")) {
    		    lore.add(cs);
    		}
    		im.setLore(lore);
    		spellItem.setItemMeta(im);
    		pl.getInventory().setItem(slot, spellItem);
		}
	}
	
	public void playSoundEffect(String sE, Player pl) {
        //PlayerConnection pc = ((CraftPlayer) pl).getHandle().playerConnection;
        //Location p = pl.getLocation();
        //Packet soundPacket = new PacketPlayOutNamedSoundEffect(sE, (float) p.getX(), (float) p.getY(),(float) p.getZ(), 1, 63);
        //pc.sendPacket(soundPacket);
	}
	
	
	//
	@EventHandler
	public void dropItem(PlayerDropItemEvent e) {
	    ItemStack thrownItem = e.getItemDrop().getItemStack();
	    ItemMeta im = thrownItem.getItemMeta();
        if(im!=null&&im.getDisplayName()!=null&&im.getDisplayName().startsWith(ChatColor.RESET+""+ChatColor.BLUE+"Bound spell: ")) {
            e.getItemDrop().remove();
            playSoundEffect("random.break", e.getPlayer());
            setSpell(e.getPlayer(), e.getPlayer().getInventory().getHeldItemSlot(), null);
            e.getPlayer().sendMessage(ChatColor.BLUE + "Successfully unbound slot "+ChatColor.AQUA+(e.getPlayer().getInventory().getHeldItemSlot()+1)+ChatColor.BLUE+"!");
        } else if(im!=null&&im.getDisplayName()!=null&&im.getDisplayName().endsWith("Wand")&&im.getDisplayName().startsWith(ChatColor.RESET+"")) {
            setSpell(e.getPlayer(), e.getPlayer().getInventory().getHeldItemSlot(), selectedSpells.get(e.getPlayer()).get(e.getPlayer().getInventory().getHeldItemSlot()));
        }
	}
	
    @EventHandler
    public void dropItem(PlayerPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack();
        ItemMeta im = item.getItemMeta();
        if(im!=null&&im.getDisplayName()!=null&&im.getDisplayName().startsWith(ChatColor.RESET+""+ChatColor.BLUE+"Bound spell: ")) {
            e.getItem().remove();
            e.setCancelled(true);
        }
    }
	
	@EventHandler
	public void inventoryInteractEvent(InventoryClickEvent e) {
	    if(e.getRawSlot()==-1) return;
	    ItemStack clickedItem = e.getView().getItem(e.getRawSlot());
        //System.out.println(e.getRawSlot());
        //System.out.println(e.getSlot());
        //System.out.println(e.getView().getTopInventory().getSize());
	    if(clickedItem!=null) {
	        //System.out.println("notnull");
	        ItemMeta im = clickedItem.getItemMeta();
	        if(im!=null&&im.getDisplayName()!=null&&im.getDisplayName().startsWith(ChatColor.RESET+""+ChatColor.BLUE+"Bound spell: ")) {
	            //System.out.println("namenotnullandisboundspell");
	            ItemStack curItem = e.getWhoClicked().getItemOnCursor();
	            ItemMeta cim = curItem.getItemMeta();
	            if(cim!=null&&cim.getDisplayName()!=null) {
	                if(cim.getDisplayName().endsWith("Wand")&&cim.getDisplayName().startsWith(ChatColor.RESET+"")) {
	                    e.setCancelled(true);
	                    e.getView().setItem(e.getRawSlot(), curItem);
	                    e.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
	                } else {
	                    e.setCancelled(true);
	                    e.getView().setItem(e.getRawSlot(), new ItemStack(Material.AIR));
	                    playSoundEffect("random.break", ((Player)e.getWhoClicked()));
	                    setSpell((Player) e.getWhoClicked(), e.getSlot(), null);
	                    ((Player)e.getWhoClicked()).sendMessage(ChatColor.BLUE + "Successfully unbound slot "+ChatColor.AQUA+(e.getSlot()+1)+ChatColor.BLUE+"!");
	                }
	            } else {
                    e.setCancelled(true);
                    e.getView().setItem(e.getRawSlot(), new ItemStack(Material.AIR));
                    playSoundEffect("random.break", ((Player)e.getWhoClicked()));
                    setSpell((Player) e.getWhoClicked(), e.getSlot(), null);
                    ((Player)e.getWhoClicked()).sendMessage(ChatColor.BLUE + "Successfully unbound slot "+ChatColor.AQUA+(e.getSlot()+1)+ChatColor.BLUE+"!");
	            }
	        } else if(im!=null&&im.getDisplayName()!=null&&im.getDisplayName().endsWith("Wand")&&im.getDisplayName().startsWith(ChatColor.RESET+"")&&e.getView().getTopInventory().getSize()<e.getRawSlot()&&e.getSlot()<9) {
                ItemStack curItem = e.getWhoClicked().getItemOnCursor();
                //ItemStack cItem = e.getView().getBottomInventory().getItem(e.getSlot());
                if(selectedSpells.get((Player) e.getWhoClicked()).get(e.getSlot())!=null) {
                    if(curItem.getType()==Material.AIR) {
                        e.getWhoClicked().setItemOnCursor(clickedItem);
                        setSpell((Player)e.getWhoClicked(), e.getSlot(), selectedSpells.get((Player) e.getWhoClicked()).get(e.getSlot()));
                        e.setCancelled(true);
                    } else {
                        e.setCancelled(true);
                    }
                }
            }
	    }
	}
	
	private void displayMana(Player pl) {
		String manaString = "";
		int mana = getMana(pl);
		for(int i = 0; i < mana;) {
			manaString+=ChatColor.BLUE+":";
			i++;
		}
		for(int i = 0; i < (50-mana);) {
			manaString+=ChatColor.DARK_GRAY+":";
			i++;
		}
		manaString+=" "+ChatColor.GRAY+mana+"/"+(50);
		pl.sendMessage(ChatColor.GRAY+"Your current mana:");
		pl.sendMessage(manaString);
	}
	
	@EventHandler
	public void onItemUse(PlayerInteractEvent e) {
		Player pl = e.getPlayer();
		//pl.sendMessage(ChatColor.GRAY + "You just performed action " + ChatColor.AQUA + e.getAction().toString() + ChatColor.GRAY + " with slot " + ChatColor.AQUA + "" + pl.getInventory().getHeldItemSlot() + "" + ChatColor.GRAY + " with a(n) " + ChatColor.AQUA + pl.getItemInHand().getType().toString());
		if(e.getAction()==Action.LEFT_CLICK_AIR || e.getAction()==Action.LEFT_CLICK_BLOCK) {
			Spell selSpell = selectedSpells.get(pl).get(pl.getInventory().getHeldItemSlot());
			ItemStack heldItem = pl.getItemInHand();
			ItemMeta im = heldItem.getItemMeta();
			if(im!=null&&im.getDisplayName()!=null){
			    if(!(im.getDisplayName().startsWith(ChatColor.RESET+""+ChatColor.BLUE+"Bound spell: ")||(im.getDisplayName().startsWith(ChatColor.RESET+"")&&im.getDisplayName().endsWith("Wand")))) {
    			    setSpell(pl, pl.getInventory().getHeldItemSlot(), null);
    			    return;
			    }
			} else {
			    setSpell(pl, pl.getInventory().getHeldItemSlot(), null);
			    return;
			}
			if(selSpell != null) {
		        //pl.sendMessage(ChatColor.BLUE + selSpell.getDisplayName());
    			if(getMana(pl) >= selSpell.getManaCost(pl)) {
    			    if(selSpell.canPerform(pl, true)) {
                        if(cooldowns.get(pl)==null) {
                            cooldowns.put(pl, new HashMap<Spell, Long>());
                        }
        			    if(cooldowns.get(pl).get(selSpell)==null) {
        			        cooldowns.get(pl).put(selSpell, 0L);
        			    }
        			    if(cooldowns.get(pl).get(selSpell)<=System.currentTimeMillis()) {
        			        //pl.sendMessage(ChatColor.BLUE + "Attempting to perform spell");//TODO
            			    reduceMana(pl, selSpell.getManaCost(pl));
            				selSpell.perform(pl, e);
            				cooldowns.get(pl).put(selSpell, System.currentTimeMillis()+selSpell.getCooldown(pl));
        			    } else {
                            pl.sendMessage(ChatColor.BLUE + "This spell is currently on cooldown! Try again in " + ChatColor.AQUA + ((double)((int)(cooldowns.get(pl).get(selSpell)-System.currentTimeMillis())/100))/10 + ChatColor.BLUE + " seconds!");
        			    }
    			    }
    			} else {
    	            /*MessagePart sendMsg = new MessagePart();
    	            List<MessagePart> extra = new ArrayList<MessagePart>();
    	            
                    MessagePart spellLEntry = new MessagePart();
                    spellLEntry.text = "You don't have enough mana for ";
                    spellLEntry.color = "blue";
                    
                    MessagePart spellText = new MessagePart();
                    spellText.text = selSpell.getDisplayName();
                    HoverEvent heEntry = new HoverEvent();
                    heEntry.value = getHoverText(selSpell, pl);
                    spellText.hoverEvent = heEntry;
                    
                    extra.add(spellLEntry);
                    extra.add(spellText);
                    
                    MessagePart p2 = new MessagePart();
                    p2.text = "!";
                    p2.color = "blue";
                    extra.add(p2);
                    sendMsg.extra = extra;
                    sendJsonMessage(((CraftPlayer) pl).getHandle().playerConnection, chatSeralizerGson.toJson(sendMsg));*/
    				TextComponent message = new TextComponent("You don't have enough mana for ");
    				message.setColor(net.md_5.bungee.api.ChatColor.BLUE);
    				
    				TextComponent spellLEntry = new TextComponent(selSpell.getDisplayName());
    				HoverEvent heEntry = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getHoverText(selSpell, pl)).create());
    				spellLEntry.setHoverEvent(heEntry);
    				message.addExtra(spellLEntry);
    				
    				TextComponent end = new TextComponent("!");
    				end.setColor(net.md_5.bungee.api.ChatColor.BLUE);
    				message.addExtra(end);
    				
    				pl.spigot().sendMessage(message);
    			}
			}
		}
	}
	
	public static String getHoverText(Spell spell, Player pl) {
	    String htxt = "";
	    htxt = spell.getColor() + spell.getDescription() + "\n\n";
	    htxt+= ChatColor.BLUE + "Requirments: \n";
	    htxt+=ChatColor.AQUA+""+spell.getManaCost(pl)+ChatColor.BLUE+" mana\n";
	    if(spell.getExtraRequirements()!=null) {
	        for(String er:spell.getExtraRequirements()) {
	            htxt+=er+"\n";
	        }
	    }
	    htxt=htxt.substring(0,htxt.length()-1);
	    return htxt;
	}
	
	public static String codeToColorName(String c) {
	    c = c.replace("\247","");
	    switch(c) {
	        case "a":
	            return "green";
            case "b":
                return "aqua";
            case "c":
                return "red";
            case "d":
                return "light_purple";
            case "e":
                return "yellow";
            case "f":
                return "white";
            case "0":
                return "black";
            case "1":
                return "dark_blue";
            case "2":
                return "dark_green";
            case "3":
                return "dark_aqua";
            case "4":
                return "dark_red";
            case "5":
                return "dark_purple";
            case "6":
                return "gold";
            case "7":
                return "gray";
            case "8":
                return "dark_gray";
            case "9":
                return "blue";
	    }
	    return null;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player pl = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("mana")) {
			displayMana(pl);
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("spells")) {
			TextComponent firstmessage = new TextComponent("Spells you currently know:");
			firstmessage.setColor(net.md_5.bungee.api.ChatColor.GRAY);
			
			TextComponent message = new TextComponent();
			
			boolean firstLoop = true;
			for(Spells sp : Spells.values()) {
				if(!firstLoop) {
					TextComponent comma = new TextComponent(", ");
					comma.setColor(net.md_5.bungee.api.ChatColor.GRAY);
					message.addExtra(comma);
				}
				Spell spl = sp.getSpell();
				
				TextComponent spellLEntry = new TextComponent(spl.getDisplayName());
				HoverEvent heEntry = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getHoverText(spl, pl)).create());
				spellLEntry.setHoverEvent(heEntry);
				message.addExtra(spellLEntry);
				
				firstLoop = false;
			}
			
			TextComponent end = new TextComponent("!");
			end.setColor(net.md_5.bungee.api.ChatColor.BLUE);
			message.addExtra(end);
			
			pl.spigot().sendMessage(firstmessage);
			pl.spigot().sendMessage(message);
			
			
			/*MessagePart sendMsg = new MessagePart();
			List<MessagePart> extra = new ArrayList<MessagePart>();
			for(Spells sp : Spells.values()) {
				Spell spl = sp.getSpell();
				
				MessagePart spellLEntry = new MessagePart();
				spellLEntry.text = spl.getName();
				spellLEntry.color = codeToColorName(spl.getColor()+"");
				
				HoverEvent heEntry = new HoverEvent();
				heEntry.value = getHoverText(spl, pl);
				spellLEntry.hoverEvent = heEntry;
				extra.add(spellLEntry);
				
				MessagePart comma = new MessagePart();
				comma.text = ", ";
				comma.color = "gray";
				extra.add(comma);
				//spellList+=spl.getDisplayName()+ChatColor.GRAY+", ";
			}
			//spellList = spellList.substring(0, spellList.length()-2);
			extra.remove(extra.size()-1);
			sendMsg.extra = extra;
			pl.sendMessage(ChatColor.GRAY+"Spells you currently know:");
			sendJsonMessage(((CraftPlayer) pl).getHandle().playerConnection, chatSeralizerGson.toJson(sendMsg));*/
			return true;
		}
		
        if (cmd.getName().equalsIgnoreCase("spell")) {
            if(args.length>=1) {
                String spl = "";
                for(String str : args) {
                    spl += str+" ";
                }
                spl=spl.substring(0,spl.length()-1);
                if(spl.equalsIgnoreCase("unbind")) {
                    setSpell(pl, pl.getInventory().getHeldItemSlot(), null);
                    pl.sendMessage(ChatColor.BLUE + "Successfully unbound slot "+ChatColor.AQUA+(pl.getInventory().getHeldItemSlot()+1)+ChatColor.BLUE+"!");
                } else {
	                if(pl.getInventory().getItemInHand().getType()!=Material.AIR) {
	                    pl.sendMessage(ChatColor.BLUE+"Please only bind a spell with an empty hand.");
	                    return false;
	                }
	                List<Spell> foundSpells = new ArrayList<Spell>();
	                for(Spells sp:Spells.values()) {
	                    if(sp.getName().toLowerCase().startsWith(spl.toLowerCase())) {
	                        foundSpells.add(sp.getSpell());
	                    }
	                }
	                if(foundSpells.size()==1) {
	                	setSpell(pl, pl.getInventory().getHeldItemSlot(), foundSpells.get(0));
	                	
	        			TextComponent message = new TextComponent("Successfully bound ");
	        			message.setColor(net.md_5.bungee.api.ChatColor.BLUE);
	        			
	        			Spell spell = foundSpells.get(0);
	        			
	    				TextComponent spellLEntry = new TextComponent(spell.getDisplayName());
	    				HoverEvent heEntry = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getHoverText(spell, pl)).create());
	    				spellLEntry.setHoverEvent(heEntry);
	    				message.addExtra(spellLEntry);
	
	        			TextComponent tomsg = new TextComponent(" to slot ");
	        			tomsg.setColor(net.md_5.bungee.api.ChatColor.BLUE);
	        			message.addExtra(tomsg);
	        			
	        			TextComponent slot = new TextComponent(""+(pl.getInventory().getHeldItemSlot()+1));
	        			slot.setColor(net.md_5.bungee.api.ChatColor.AQUA);
	        			message.addExtra(slot);
	        			
	        			TextComponent end = new TextComponent("!");
	        			end.setColor(net.md_5.bungee.api.ChatColor.BLUE);
	        			message.addExtra(end);
	        			
	        			
	        			pl.spigot().sendMessage(message);
	                	
	                    /*MessagePart sendMsg = new MessagePart();
	                    List<MessagePart> extra = new ArrayList<MessagePart>();
	                    
	                    MessagePart spellLEntry = new MessagePart();
	                    spellLEntry.text = "Successfully bound ";
	                    spellLEntry.color = "blue";
	                    
	                    MessagePart spellText = new MessagePart();
	                    spellText.text = foundSpells.get(0).getDisplayName();
	                    HoverEvent heEntry = new HoverEvent();
	                    heEntry.value = getHoverText(foundSpells.get(0), pl);
	                    spellText.hoverEvent = heEntry;
	                    
	                    MessagePart p2 = new MessagePart();
	                    p2.text = " to slot ";
	                    p2.color = "blue";
	                    
	                    MessagePart p3 = new MessagePart();
	                    p3.text = ""+(pl.getInventory().getHeldItemSlot()+1);
	                    p3.color = "aqua";
	                    
	                    MessagePart p4 = new MessagePart();
	                    p4.text = "!";
	                    p4.color = "blue";
	                    extra.add(spellLEntry);
	                    extra.add(spellText);
	                    extra.add(p2);
	                    extra.add(p3);
	                    extra.add(p4);
	                    sendMsg.extra = extra;
	                    sendJsonMessage(((CraftPlayer) pl).getHandle().playerConnection, chatSeralizerGson.toJson(sendMsg));*/
	                } else if(foundSpells.size()==0) {
	                    pl.sendMessage(ChatColor.BLUE+"Couldn't find spell \""+ChatColor.AQUA+spl+ChatColor.BLUE+"\"!");
	                } else {
	                    pl.sendMessage(ChatColor.BLUE+"Spell \""+ChatColor.AQUA+spl+ChatColor.BLUE+"\" returned multiple results! Could be:");
	                    
	        			TextComponent message = new TextComponent();
	        			
	        			boolean firstLoop = true;
	        			for(Spell sp : foundSpells) {
	        				if(!firstLoop) {
	        					TextComponent comma = new TextComponent(", ");
	        					comma.setColor(net.md_5.bungee.api.ChatColor.GRAY);
	        					message.addExtra(comma);
	        				}
	        				
	        				TextComponent spellLEntry = new TextComponent(sp.getDisplayName());
	        				HoverEvent heEntry = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getHoverText(sp, pl)).create());
	        				spellLEntry.setHoverEvent(heEntry);
	        				message.addExtra(spellLEntry);
	        				
	        				firstLoop = false;
	        			}
	        			
	        			pl.spigot().sendMessage(message);
	                    
	                    /*MessagePart sendMsg = new MessagePart();
	                    List<MessagePart> extra = new ArrayList<MessagePart>();
	                    for(Spell sp : foundSpells) {
	                        MessagePart spellLEntry = new MessagePart();
	                        spellLEntry.text = sp.getDisplayName();
	                        HoverEvent heEntry = new HoverEvent();
	                        heEntry.value = getHoverText(sp, pl);
	                        spellLEntry.hoverEvent = heEntry;
	                        extra.add(spellLEntry);
	                        MessagePart comma = new MessagePart();
	                        comma.text = ", ";
	                        comma.color = "gray";
	                        extra.add(comma);
	                    }
	                    extra.remove(extra.size()-1);
	                    sendMsg.extra = extra;
	                    sendJsonMessage(((CraftPlayer) pl).getHandle().playerConnection, chatSeralizerGson.toJson(sendMsg));*/
	                }
                }
            } else {
                pl.sendMessage(ChatColor.DARK_RED+"Error in syntax, try "+ChatColor.RED+" /spell <spell name>");
            }
            return true;
        }
		return false;
	}
}
