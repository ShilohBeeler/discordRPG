package com.shymain.discordRPG;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.UserJoinEvent;
import sx.blah.discord.handle.obj.IUser;

public class Player {
	
	public static String file = System.getProperty("user.home")+"/discordRPG/players.json";
	
	public static void create(UserJoinEvent event) throws JSONException, IOException
	{
		String template = "{"
           + "player: \""+event.getUser().getID()+"\","
           + "stats:" 
           + "{"
           +     "mining: 1,"
           +     "fighting: 1,"
           +     "magic: 1"
           + "},"
           + "inventory:"
           + "{"
           +     "steak: 1,"
           +     "iron_axe: 1"
           + "},"
           + "equipment:"
           + "{"
           +     "head: \"iron_head\","
           +     "body: \"iron_body\","
           +     "feet: \"iron_feet\","
           +     "weapon: \"sword\""
           + "},"
           + "floor: 1,"
           + "health: 10,"
           + "maxhealth: 10"
           +"}";
		JSONObject json = new JSONObject(discordRPG.readFile(file));
		JSONObject player = new JSONObject(template);
		json.getJSONArray("players").put(player);
		FileWriter r = new FileWriter(file);
		r.write(json.toString());
		r.flush();
		r.close();
	}
	
	public static String statsUp(IUser user, String stat) throws JSONException, IOException{
		JSONObject json = new JSONObject(discordRPG.readFile(file));
		statsUp:
		for(int i = 0; i<json.getJSONArray("players").length(); i++)
		{
			if(json.getJSONArray("players").getJSONObject(i).getString("player").equalsIgnoreCase(user.getID()))
				{
				if(!json.getJSONArray("players").getJSONObject(i).getJSONObject("stats").has(stat))
				{
					return "StatNotFoundError";
				}
				json.getJSONArray("players").getJSONObject(i).getJSONObject("stats").increment(stat);
				FileWriter r = new FileWriter(file);
				r.write(json.toString());
				r.flush();
				r.close();
				return "Success";
				}
		}
		return "PlayerNotFoundError";
	}
	
	public static String floorUp(IUser user) throws JSONException, IOException{
		JSONObject json = new JSONObject(discordRPG.readFile(file));
		floorUp:
		for(int i = 0; i<json.getJSONArray("players").length(); i++)
		{
			if(json.getJSONArray("players").getJSONObject(i).getString("player").equalsIgnoreCase(user.getID()))
				{
				json.getJSONArray("players").getJSONObject(i).increment("floor");
				FileWriter r = new FileWriter(file);
				r.write(json.toString());
				r.flush();
				r.close();
				return "Success";
				}
		}
		return "PlayerNotFoundError";
	}
	
	public static String inventoryAdd(IUser user, String item, int number) throws JSONException, IOException{
		JSONObject json = new JSONObject(discordRPG.readFile(file));
		inventoryAdd:
		for(int i = 0; i<json.getJSONArray("players").length(); i++)
		{
			if(json.getJSONArray("players").getJSONObject(i).getString("player").equalsIgnoreCase(user.getID()))
				{
				if(json.getJSONArray("players").getJSONObject(i).getJSONObject("inventory").has(item))
					{
					for(int j = 0; j < number; j++)
					{
						json.getJSONArray("players").getJSONObject(i).getJSONObject("inventory").increment(item);
					}
					}else
					{
						json.getJSONArray("players").getJSONObject(i).getJSONObject("inventory").put(item, number);
					}
				FileWriter r = new FileWriter(file);
				r.write(json.toString());
				r.flush();
				r.close();
				return "Success";
				}
		}
		return "PlayerNotFoundError";
	}
	
	public static String inventoryRemove(IUser user, String item, int number) throws JSONException, IOException{
		JSONObject json = new JSONObject(discordRPG.readFile(file));
		inventoryRemove:
		for(int i = 0; i<json.getJSONArray("players").length(); i++)
		{
			if(json.getJSONArray("players").getJSONObject(i).getString("player").equalsIgnoreCase(user.getID()))
				{
				if(json.getJSONArray("players").getJSONObject(i).getJSONObject("inventory").has(item))
					{
					int itemno = json.getJSONArray("players").getJSONObject(i).getJSONObject("inventory").getInt(item);
					for(int j = 0; j < number; j++)
					{
						itemno--;
					}
					if(itemno<0)
					{
						return "ItemNumberError";
					}
						json.getJSONArray("players").getJSONObject(i).getJSONObject("inventory").remove(item);
						if(itemno!=0){
						json.getJSONArray("players").getJSONObject(i).getJSONObject("inventory").put(item, itemno);
						}
					}else
					{
						return "ItemNotFoundError";
					}
				FileWriter r = new FileWriter(file);
				r.write(json.toString());
				r.flush();
				r.close();
				return "Success";
				}
		}
		return "PlayerNotFoundError";
	}
	
}
