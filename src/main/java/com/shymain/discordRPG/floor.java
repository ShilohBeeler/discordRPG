package com.shymain.discordRPG;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class Floor {

	public static Encounter getMonster(int floor) throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(Monster.file));
		JSONObject mob = new JSONObject();
		String name = "";
		switch(floor)
		{
		case 1:
			mob = json.getJSONObject("monsters").getJSONObject("Gnome");
			name = "Gnome";
		}
		Encounter newEncounter = new Encounter(name, mob.getInt("maxhealth"), mob.getInt("health"), mob.getInt("attack"), mob.getInt("speed"));
		return newEncounter;
	}
	
}
