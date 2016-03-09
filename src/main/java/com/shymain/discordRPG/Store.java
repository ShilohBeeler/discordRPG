package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.HTTP429Exception;

public class Store {
	
	public static String file = System.getProperty("user.home")+"/discordRPG/ranks.json";
	
	public static void initialize() throws JSONException, IOException
	{
		String template = "{"
				+ "shop:{"
	            + "\"health_potion\":10,"
	            + "\"iron_pickaxe\":100"
	            + "},"
	            + "monsters:["
	            + "\"Gnome\""
	            + "]"
	            + "}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject rank = new JSONObject(template);
		json.getJSONObject("ranks").put("1", rank);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}

	public static void displayWares(MessageReceivedEvent event) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(Player.file));
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(file));
		int number = json.getJSONObject("players").getJSONObject(event.getMessage().getAuthor().getID()).getInt("rank");
		JSONObject shop = json2.getJSONObject("ranks").getJSONObject(Integer.toString(number)).getJSONObject("shop");
		Iterator<String> keys = shop.keys();
		String output = "```\nAvailable Items:\n";
		while(keys.hasNext())
		{
			String key = (String)keys.next();
			int cost = shop.getInt(key);
			String costs = Integer.toString(cost);
			output += key + ": Costs " + costs + " gold.\n";
		}
		output += "```";
		event.getMessage().getChannel().sendMessage(output);
	}
	
	public static void buyItem(MessageReceivedEvent event, String item) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(Player.file));
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(event.getMessage().getAuthor().getID());
		int number = json.getJSONObject("players").getJSONObject(event.getMessage().getAuthor().getID()).getInt("rank");
		JSONObject shop = json2.getJSONObject("ranks").getJSONObject(Integer.toString(number)).getJSONObject("shop");
		if(shop.isNull(item))
		{
			event.getMessage().getChannel().sendMessage("That is not a valid item to buy.");
		}else
		{
			int cost = shop.getInt(item);
			if(!player.getJSONObject("inventory").isNull("coins") && player.getJSONObject("inventory").getInt("coins")>=cost)
			{
				Player.inventoryAdd(event.getMessage().getAuthor(), item, 1);
				Player.inventoryRemove(event.getMessage().getAuthor(), "coins", cost);
				event.getMessage().getChannel().sendMessage("Successfully bought "+ item + "!");
			}else{
				event.getMessage().getChannel().sendMessage("You do not have enough money!");
			}
		}
	}
	
}
