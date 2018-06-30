package com.reynixpvp.spellsplugin.spells;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class FireStorm extends Spell{
    Random rnd;
    List<Integer> removeArrows = new ArrayList<Integer>();
    public FireStorm(){
        rnd = new Random();
    }
    
	@Override
	public int getManaCost(Player pl) {
		return 20;
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
		pl.sendMessage(ChatColor.RED+"woosh, fire"+ChatColor.STRIKETHROUGH+"ball"+ChatColor.RESET+ChatColor.RED+" storm");
		/*Arrow fball = pl.launchProjectile(Arrow.class);
		fball.setFireTicks(10000);
		Vector v = pl.getLocation().getDirection().normalize();
		v.add(new Vector((rnd.nextInt(20)-10)/10,(rnd.nextInt(20)-10)/10,(rnd.nextInt(20)-10)/10));
		fball.setVelocity(v);*/
		for(int i = 0;i<20;i++) {
    	    Arrow a = pl.getWorld().spawnArrow(pl.getEyeLocation(), pl.getLocation().getDirection(), (25/10.0F), (100/10.0F));
    	    a.setFireTicks(2000);
    	    a.setVelocity(a.getVelocity());
    	    a.setShooter((ProjectileSource) pl);
    	    removeArrows.add(a.getEntityId());
		}
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
	    Entity en = e.getEntity();
    	if (en instanceof Arrow) {
            if(removeArrows.contains(en.getEntityId())) {
    	        removeArrows.remove(removeArrows.indexOf(en.getEntityId()));
    	        en.remove();
    	    }
    	}
	}
	
	@Override
	public String getName() {
		return "Fire arrow storm";
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
        return "Shoots a fire"+ChatColor.STRIKETHROUGH+"ball"+ChatColor.RESET+ChatColor.RED+" storm!";
    }

    @Override
    public String[] getExtraRequirements() {
        // TODO Auto-generated method stub
        return null;
    }
}
