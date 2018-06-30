package com.reynixpvp.spellsplugin.spells;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

//import com.gmail.filoghost.holograms.api.Hologram;
//import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;

public class ManaDrain extends Spell{
    //Hologram arowHolo;
    
	@Override
	public int getManaCost(Player pl) {
		return 5;
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
	public void perform(Player pl, Event e) {
		pl.sendMessage(ChatColor.GREEN+"Drained 5 mana!");
		
		ScoreboardManager sbm = Bukkit.getScoreboardManager();
		Scoreboard sb = sbm.getNewScoreboard();
		Objective ob = sb.registerNewObjective(ChatColor.LIGHT_PURPLE+"QubeCraft HUD", "dummy");
        Score xp = ob.getScore(ChatColor.GOLD+"XP: 400/1024");
        Score bl = ob.getScore(ChatColor.RESET+"");
        Score bl2 = ob.getScore(ChatColor.RESET+" ");
        Score mana2 = ob.getScore(ChatColor.GREEN+"Mana: 634/900");
		Score mana = ob.getScore(ChatColor.DARK_AQUA+"■■■■■■■■"+ChatColor.RED+"■■■");
		Score p1 = ob.getScore(ChatColor.AQUA+""+ChatColor.BOLD+"Party");
        Score p2 = ob.getScore(ChatColor.GREEN+"✶INightLig 40");
        Score p3 = ob.getScore("ArowShot 41");
        Score p4 = ob.getScore("RandomMcSomet 2");
        Score p5 = ob.getScore("webwedz 40");
        Score p6 = ob.getScore("cj6x6 20");
        Score p7 = ob.getScore("CleanestJoke 25");
		
		ob.setDisplaySlot(DisplaySlot.SIDEBAR);
		xp.setScore(11);
		bl.setScore(10);
        mana2.setScore(9);
		mana.setScore(8);
		bl2.setScore(7);
        p1.setScore(6);
        p2.setScore(5);
        p3.setScore(4);
        p4.setScore(3);
        p5.setScore(2);
        p6.setScore(1);
        p7.setScore(0);
		for(Player pu:Bukkit.getOnlinePlayers()) {
		    pu.setScoreboard(sb);
		}
		
		//for(Player pu:Bukkit.getOnlinePlayers()) {
		    //CraftPlayer cp = (CraftPlayer) pu;
		    //Location p = pu.getLocation();
		    //Packet s = new PacketPlayOutWorldParticles("footstep", (float) p.getX(), (float) p.getY()+0.03F,(float) p.getZ(), (float) .5, (float) 0, (float) .5, (float) 0, 6);
		    //cp.getHandle().playerConnection.sendPacket(s);
		//}
		
		/*WitherSkull diamond = (WitherSkull) pl.getWorld().spawn(pl.getEyeLocation().add(0, 1, 0), WitherSkull.class);
		Horse horse = pl.getWorld().spawn(pl.getEyeLocation().add(0, 1, 0), Horse.class);
		diamond.setPassenger(horse);
		diamond.setDirection(new Vector(0,0,0));
		diamond.setVelocity(new Vector(0,0,0));
		horse.setAge(-1700000);
		horse.setAgeLock(true);
		horse.setCustomName(ChatColor.GREEN+"<>");
		horse.setCustomNameVisible(true);*/
		
		//arowHolo = HolographicDisplaysAPI.createHologram(Bukkit.getPluginManager().getPlugin("SpellsPlugin"), pl.getEyeLocation(), ChatColor.GOLD+"♦");
		
	}
	
	@EventHandler(ignoreCancelled=true,priority=EventPriority.MONITOR)
	public void move(PlayerMoveEvent e) {
	    if(e.getPlayer().getName().equalsIgnoreCase("ArowShot")) {
	        //arowHolo.teleport(e.getPlayer().getLocation().add(0, 2.4, 0));
	    }
	}
	
	@Override
	public String getName() {
		return "Mana Drain";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.LIGHT_PURPLE;
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
        if(pl.getItemInHand().getType()==Material.AIR) {
            return "";
        }
        return "";
    }

    @Override
    public String getDescription() {
        return "Drains your mana!";
    }

    @Override
    public String[] getExtraRequirements() {
        // TODO Auto-generated method stub
        return null;
    }
}
