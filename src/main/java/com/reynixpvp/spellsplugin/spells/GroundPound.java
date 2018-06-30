package com.reynixpvp.spellsplugin.spells;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class GroundPound extends Spell{

	@Override
	public int getManaCost(Player pl) {
		return 0;
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

	@SuppressWarnings("deprecation")
    @Override
	public void perform(Player pl, Event ev) {
	    PlayerInteractEvent e = (PlayerInteractEvent) ev;
		if(e.getAction()==Action.LEFT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
		    BlockBreakEvent bbe = new BlockBreakEvent(b, pl);
		    Bukkit.getServer().getPluginManager().callEvent(bbe);
		    if(!bbe.isCancelled()){
    		    if(b.getPistonMoveReaction()==PistonMoveReaction.MOVE) {
        		    FallingBlock en = pl.getWorld().spawnFallingBlock(b.getLocation().add(0.5, 0, 0.5), b.getType(), b.getData());
        		    en.setVelocity(new Vector(0,0.2+Math.random(),0));
                    b.setType(Material.AIR);
    		    }
                pl.sendMessage(b.getPistonMoveReaction().toString());
		    }
		}
	}

	@Override
	public String getName() {
		return "Ground Pound";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.GREEN;
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
        if(pl.getItemInHand().getType()==Material.AIR) {
            return "";
        }
        return "";
    }

    @Override
    public String getDescription() {
        return "HULK SMASH!";
    }

    @Override
    public String[] getExtraRequirements() {
        // TODO Auto-generated method stub
        return null;
    }
}
