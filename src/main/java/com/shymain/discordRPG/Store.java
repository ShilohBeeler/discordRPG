package com.shymain.discordRPG;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

public class Store {

	public static void displayWares(MessageReceivedEvent event) throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(Player.file));
		int floor = json.getJSONObject("players").getJSONObject(event.getMessage().getAuthor().getID()).getInt("floor");
		switch(floor)
		{
		case 1:
			//event.getMessage().getChannel().sendMessage("");
		}
	}
	
}
