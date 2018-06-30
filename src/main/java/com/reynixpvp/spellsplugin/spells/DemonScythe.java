package com.reynixpvp.spellsplugin.spells;

import java.util.HashSet;

import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;

public class DemonScythe extends Spell{

	@Override
	public int getManaCost(Player pl) {
		return 0;
	}

    @Override
    public boolean canPerform(Player pl, boolean b) {
        if(b) {
            if(pl.getItemInHand().getType()==Material.DIAMOND_HOE) {
                ItemMeta im = pl.getItemInHand().getItemMeta();
                if(im.getDisplayName()!=null&&ChatColor.stripColor(im.getDisplayName()).endsWith("Scythe")) {
                    return true;
                } else {
                    pl.sendMessage(ChatColor.BLUE + "This spell requires a scythe!");
                }
            } else {
                pl.sendMessage(ChatColor.BLUE + "This spell requires a scythe!");
            }
            return false;
        }
        return true;
    }

	@Override
	public void perform(Player pl, Event e) {
		BlockIterator bi = new BlockIterator(pl.getEyeLocation(), 0, 10);
		while (bi.hasNext()) {
		    Block bl = bi.next();
		    Location p = bl.getLocation();
		    /*Packet packet = new PacketPlayOutWorldParticles("magicCrit", (float) p.getX(), (float) p.getY(),(float) p.getZ(), (float) 0.1, (float) 0.1, (float) 0.1, (float) 0, 10);
		    sendPacket(packet);
		    for(Entity nE : getNearbyEntities(p,1)) {
		        if(nE instanceof LivingEntity) {
		            LivingEntity le = (LivingEntity)nE;
		            le.damage(16D);
		        }
		    }*/
		}
	}
	
    @SuppressWarnings("deprecation")
    public void sendPacket(Packet packet) {
        for(Player cplayer : Bukkit.getServer().getOnlinePlayers()) {
            ((CraftPlayer)cplayer).getHandle().playerConnection.sendPacket(packet);
        }
    }
	
    public static Entity[]  getNearbyEntities(Location l, int radius){
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16))/16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();
            for (int chX = 0 -chunkRadius; chX <= chunkRadius; chX ++){
                for (int chZ = 0 -chunkRadius; chZ <= chunkRadius; chZ++){
                    int x=(int) l.getX(),y=(int) l.getY(),z=(int) l.getZ();
                    for (Entity e : new Location(l.getWorld(),x+(chX*16),y,z+(chZ*16)).getChunk().getEntities()){
                        if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock()) radiusEntities.add(e);
                    }
                }
            }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }
    
	@Override
	public String getName() {
		return "Demon Scythe";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_PURPLE;
	}

    @Override
    public int getCooldown(Player pl) {
        return 0;
    }

    @Override
    public String getDisplayName() {
        return getColor()+getName();
    }

    @Override
    public String whyNot(Player pl, boolean b) {
        return "";
    }

    @Override
    public String getDescription() {
        return "Demon scythe!";
    }

    @Override
    public String[] getExtraRequirements() {
        String[] ret = {ChatColor.DARK_PURPLE+"Demon scythe"};
        return ret;
    }
}
