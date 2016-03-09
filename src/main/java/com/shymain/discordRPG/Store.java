package com.shymain.discordRPG;

import java.io.IOException;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.HTTP429Exception;

public class Store {

	public static void displayWares(MessageReceivedEvent event) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(Player.file));
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(Floor.file));
		int number = json.getJSONObject("players").getJSONObject(event.getMessage().getAuthor().getID()).getInt("floor");
		JSONObject floor = json2.getJSONObject("floors").getJSONObject(Integer.toString(number));
		Iterator<String> keys = floor.getJSONObject("shop").keys();
		String output = "```\nAvailable Items:\n";
		while(keys.hasNext())
		{
			String key = (String)keys.next();
			int cost = floor.getJSONObject("shop").getInt(key);
			String costs = Integer.toString(cost);
			output += key + ": Costs " + costs + " gold.\n";
		}
		output += "```";
		event.getMessage().getChannel().sendMessage(output);
	}
	
	public static void buyItem(MessageReceivedEvent event, String item) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(Player.file));
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(Floor.file));
		JSONObject player = json.getJSONObject("players").getJSONObject(event.getMessage().getAuthor().getID());
		int number = json.getJSONObject("players").getJSONObject(event.getMessage().getAuthor().getID()).getInt("floor");
		JSONObject floor = json2.getJSONObject("floors").getJSONObject(Integer.toString(number));
		if(floor.getJSONObject("shop").isNull(item))
		{
			event.getMessage().getChannel().sendMessage("That is not a valid item to buy.");
		}else
		{
			int cost = floor.getJSONObject("shop").getInt(item);
			if(player.getJSONObject("inventory").isNull("coins") || player.getJSONObject("inventory").getInt("coins")>cost)
			{
				Player.inventoryAdd(event.getMessage().getAuthor(), item, 1);
				Player.inventoryRemove(event.getMessage().getAuthor(), "coins", cost);
			}else{
				event.getMessage().getChannel().sendMessage("You do not have enough money!");
			}
		}
	}
	
}
