package com.reynixpvp.spellsplugin.spells;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Windwalk extends Spell{
    List<Player> thoseWhoFly = new ArrayList<Player>();
    
    public Windwalk() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Bukkit.getServer().getPluginManager().getPlugin("SpellsPlugin"), new Runnable() {
            public void run() {
                List<Player> thoseWhoFlyClone = new ArrayList<Player>(thoseWhoFly);
                for(Player pl : thoseWhoFlyClone) {
                    Location p = pl.getLocation();
                    /*Packet packet = new PacketPlayOutWorldParticles("cloud", (float) p.getX(), (float) p.getY(),(float) p.getZ(), (float) .75, (float) 0, (float) .75, (float) .0001, 50);
                    sendPacket(packet);
                    pl.setFallDistance(0);
                    if(!pl.isFlying()) {
                        disengageFlight(pl);
                    }*/
                }
            }
        }, 0, 5L);
    }
    
	@Override
	public int getManaCost(Player pl) {
	    if(thoseWhoFly.contains(pl)) {
	        return 0;
	    }
		return 25;
	}

	@Override
	public boolean canPerform(Player pl, boolean b) {
	    if(b) {
	        if(pl.getItemInHand().getType()==Material.STICK) {
	            ItemMeta im = pl.getItemInHand().getItemMeta();
	            if(im.getDisplayName()!=null&&ChatColor.stripColor(im.getDisplayName()).endsWith("Wand")&&im.getDisplayName().startsWith(ChatColor.RESET+"")) {
	                return true;
	            } else {
	                pl.sendMessage(ChatColor.BLUE + "This spell requires a wand!");
	            }
	        } else {
                pl.sendMessage(ChatColor.BLUE + "This spell requires a wand!");
	        }
	        return false;
	    }
        return true;
	}

	@Override
	public void perform(Player pl, Event e) {
        if(thoseWhoFly.contains(pl)) {
            disengageFlight(pl);
        } else {
            engageFlight(pl);
            pl.getInventory().removeItem(new ItemStack(Material.FEATHER, 3));
        }
	}
	
    private void engageFlight(Player pl) {
        pl.sendMessage(ChatColor.YELLOW+""+ChatColor.ITALIC+"woosh *flying noises*");
        thoseWhoFly.add(pl);
        pl.setAllowFlight(true);
        pl.setFlying(true);
    }
    
    private void disengageFlight(Player pl) {
        pl.sendMessage(ChatColor.RED+""+ChatColor.ITALIC+"*crashing noises*");
        thoseWhoFly.remove(pl);
        if(!pl.getGameMode().equals(GameMode.CREATIVE)) {
            pl.setAllowFlight(false);
            pl.setFlying(false);
        }
    }

    public void actuallyFly(Player pl) {
        Location p = pl.getLocation();
        //Packet packet = new PacketPlayOutWorldParticles("cloud", (float) p.getX(), (float) p.getY(),(float) p.getZ(), (float) .75, (float) 0, (float) .75, (float) .0001, 50);
        //sendPacket(packet);
    }
    
    @SuppressWarnings("deprecation")
    public void sendPacket(Packet packet) {
        for(Player cplayer : Bukkit.getServer().getOnlinePlayers()) {
            ((CraftPlayer)cplayer).getHandle().playerConnection.sendPacket(packet);
        }
    }
	
	@Override
	public String getName() {
		return "Windwalk";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.YELLOW;
	}

    @Override
    public int getCooldown(Player pl) {
        return 3000;
    }

    @Override
    public String getDisplayName() {
        return getColor()+getName();
    }

    @Override
    public String whyNot(Player pl, boolean b) {
        if(pl.getItemInHand().getType()==Material.AIR) {
            return "";
        }
        return "";
    }

    @Override
    public String getDescription() {
        return "Walk on wind! (Fly)";
    }

    @Override
    public String[] getExtraRequirements() {
        String[] ret = {ChatColor.LIGHT_PURPLE+"Magical Wand"};
        return ret;
    }
}
