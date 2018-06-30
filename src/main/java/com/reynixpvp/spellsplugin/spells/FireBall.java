package com.reynixpvp.spellsplugin.spells;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class FireBall extends Spell{

	@Override
	public int getManaCost(Player pl) {
		return 10;
	}

	@Override
	public boolean canPerform(Player pl, boolean b) {
	    if(b) {
	        if(pl.getItemInHand().getType()==Material.AIR) {
	            return true;
	        }
	    }
        return true;
	}
	
	@Override
	public void perform(Player pl, Event e) {
		pl.sendMessage(ChatColor.RED+"woosh, fireball");
		pl.launchProjectile(Fireball.class);
	}

	@Override
	public String getName() {
		return "Fireball";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.RED;
	}

    @Override
    public int getCooldown(Player pl) {
        return 1000;
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
        return "Shoots a fireball!";
    }

    @Override
    public String[] getExtraRequirements() {
        // TODO Auto-generated method stub
        return null;
    }
}
