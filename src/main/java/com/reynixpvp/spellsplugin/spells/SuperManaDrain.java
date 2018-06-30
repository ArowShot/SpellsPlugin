package com.reynixpvp.spellsplugin.spells;

import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_12_R1.PlayerConnection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class SuperManaDrain extends Spell{

	@Override
	public int getManaCost(Player pl) {
		return 20;
	}

	@Override
    public boolean canPerform(Player pl, boolean b) {
		return true;
	}

	@SuppressWarnings("deprecation")
    @Override
	public void perform(Player pl, Event e) {
		pl.sendMessage(ChatColor.GOLD+"Drained 20 mana!");
		for(Player pls:Bukkit.getServer().getOnlinePlayers()) {
		    PlayerConnection pc = ((CraftPlayer) pls).getHandle().playerConnection;
		    Location p = pl.getLocation();
		    //Packet effectPacket = new PacketPlayOutWorldParticles("witchMagic", (float) p.getX(), (float) p.getY()+1,(float) p.getZ(), (float) .5, (float) 1, (float) .5, (float) 1, 25);
		    //Packet soundPacket = new PacketPlayOutNamedSoundEffect("random.orb", (float) p.getX(), (float) p.getY(),(float) p.getZ(), 1, 63);
		    //pc.sendPacket(effectPacket);
		    //pc.sendPacket(soundPacket);
		}
	}

	@Override
	public String getName() {
		return "Super Mana Drain";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_PURPLE;
	}

    @Override
    public int getCooldown(Player pl) {
        return 2000;
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
        return "Drains your mana! Faster! WOOOOO!";
    }

    @Override
    public String[] getExtraRequirements() {
        // TODO Auto-generated method stub
        return null;
    }
}
