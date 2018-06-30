package com.reynixpvp.spellsplugin.spells;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public abstract class Spell implements Listener {
	public abstract int getManaCost(Player pl);
	public abstract boolean canPerform(Player pl, boolean b);
	public abstract void perform(Player pl, Event e);
	public abstract String getName();
	public abstract ChatColor getColor();
	public abstract int getCooldown(Player pl);
	public abstract String getDisplayName();
	public abstract String whyNot(Player pl, boolean b);
	public abstract String getDescription();
	public abstract String[] getExtraRequirements();
}
