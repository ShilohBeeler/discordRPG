package com.shymain.discordRPG;

public class Encounter {
	
	public String name;
	public int health;
	public int maxhealth;
	public int attack;
	public int speed;
	
	public Encounter(String name, int health, int maxhealth, int attack, int speed)
	{
		this.name = name;
		this.health = health;
		this.maxhealth = maxhealth;
		this.attack = attack;
		this.speed = speed;
	}

}
