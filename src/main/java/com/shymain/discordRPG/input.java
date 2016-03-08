package com.shymain.discordRPG;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.HTTP429Exception;

public class Input {

	@EventSubscriber
	public static void commands(MessageReceivedEvent event) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		String command;
		String allArguments = "";
		String[] arguments = null;
		
		String rawMessage = event.getMessage().getContent();
		
		String parts[] = rawMessage.split(" ", 2);
		command = parts[0];
		if(parts.length == 2)
		{
			allArguments = parts[1];
			arguments = allArguments.split(" ");
		}
		if(event.getMessage().getChannel().getID().equalsIgnoreCase("156840527164211200"))
		{
			if(command.equalsIgnoreCase(".help"))
			{
				event.getMessage().getChannel().sendMessage("*.wares* displays the purchasable item.\n"
						+ "*.buy [item]* will purchase the specified item.\n"
						+ "*.price [item]* will show the selling price of the specified item.\n"
						+ "*.sell [item]* will sell the specified item.");
			}else if(command.equalsIgnoreCase(".wares") || command.equalsIgnoreCase(".items"))
			{
				Store.displayWares(event);
			}
		}else if(event.getMessage().getChannel().isPrivate()){
			if(command.equalsIgnoreCase(".help"))
			{
				event.getMessage().getChannel().sendMessage("*.fight* either starts a battle or attacks an enemy.");
			}else if(command.equalsIgnoreCase(".fight") || command.equalsIgnoreCase(".attack"))
			{
				if(Monster.currentFights.containsKey(event.getMessage().getAuthor()))
				{
					Monster.attack(event.getMessage().getAuthor(), event.getMessage().getChannel());
				}else{
					Monster.startFight(event);
				}
			}
		}
	}
	
}
