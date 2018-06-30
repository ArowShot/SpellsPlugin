package com.reynixpvp.spellsplugin.spells;

public enum Spells {
	DEMON_SCYTHE(new DemonScythe()),
	GROUND_POUND(new GroundPound()),
	FIRE_BALL(new FireBall()),
	FIRE_STORM(new FireStorm()),
	WIND_WALK(new Windwalk()),
	MANA_DRAIN(new ManaDrain()),
	SUPER_MANA_DRAIN(new SuperManaDrain());
	
	private Spell spell;
	
	Spells(Spell spell) {
		this.spell = spell;
	}
	
	public String getName() {
		return spell.getName();
	}
	
	public Spell getSpell() {
		return spell;
	}
}
