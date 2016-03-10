package com.shymain.discordRPG;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.util.HTTP429Exception;

public class Input {
	
	public static String command;
	public static String allArguments;
	public static String[] arguments;
	public static String rawMessage;
	public static String[] parts;
	public static String item;

	@EventSubscriber
	public static void commands(MessageReceivedEvent event) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		allArguments = "";
		arguments = null;
		
		rawMessage = event.getMessage().getContent();
		
		parts = rawMessage.split(" ", 2);
		command = parts[0];
		if(parts.length == 2)
		{
			allArguments = parts[1];
			arguments = allArguments.split(" ");
		}
		item = allArguments;
		item = item.replaceAll(" ", "_");
		commands:
		if(command.equalsIgnoreCase(".help")){
			IPrivateChannel pm = null;
			try {
				pm = event.getClient().getOrCreatePMChannel(event.getMessage().getAuthor());
			} catch (Exception e) {
				event.getMessage().getChannel().sendMessage("You managed to find a new exception. Good Job.");
				break commands;
			}
			pm.sendMessage("**Floor Commands:**\n"
					+ "*.join* if the bot didn't add you automatically.\n"
					+ "*.inv* displays your inventory.\n"
					+ "*.use [item]* uses said item.\n"
					+ "*.equip [item]* equips item.\n"
					+ "*.unequip [slot]* unequips item in slot.\n"
					+ "*.body* lists equipped items.\n"
					+ "*.skills* lists skills.");
		}
		else if(command.equalsIgnoreCase(".inv") || command.equalsIgnoreCase(".inventory"))
		{
			Player.getInventory(event);
		}else if(command.equalsIgnoreCase(".use"))
		{
			useHandling(event);
		}else if(command.equalsIgnoreCase(".equip")){
			Player.equip(event.getMessage().getAuthor(), event.getMessage().getChannel(), item);
		}else if(command.equalsIgnoreCase(".unequip")){
			Player.unequip(event.getMessage().getAuthor(), event.getMessage().getChannel(), item);
		}else if(command.equalsIgnoreCase(".equipment") || command.equalsIgnoreCase(".body")){
			Player.getEquip(event.getMessage().getAuthor(), event.getMessage().getChannel());
		}else if(command.equalsIgnoreCase(".skills") || command.equalsIgnoreCase(".stats")){
			Player.getSkills(event.getMessage().getAuthor(), event.getMessage().getChannel());
		}else if(event.getMessage().getChannel().getID().equalsIgnoreCase("156840527164211200"))
		{
			shop(event);
		}else if(event.getMessage().getChannel().isPrivate())
			{
			privateChannels(event);
		}else if(event.getMessage().getChannel().getGuild().getID().equalsIgnoreCase("149548522058809344"))
		{
			floorCommands(event);
		}
	}
	
	public static void privateChannels(MessageReceivedEvent event) throws MissingPermissionsException, HTTP429Exception, DiscordException, JSONException, IOException
	{
		if(command.equalsIgnoreCase(".help"))
		{
			event.getMessage().getChannel().sendMessage("PM Commands:\n"
					+ "*.fight* either starts a battle or attacks an enemy.\n");
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
	
	public static void shop(MessageReceivedEvent event) throws MissingPermissionsException, HTTP429Exception, DiscordException, JSONException, IOException
	{
		if(command.equalsIgnoreCase(".help"))
		{
			event.getMessage().getChannel().sendMessage("*.wares* displays the purchasable item.\n"
					+ "*.inv* will display your inventory.\n"
					+ "*.buy [item]* will purchase the specified item.\n"
					/*+ "*.price [item]* will show the selling price of the specified item.\n"
					+ "*.sell [item]* will sell the specified item."*/);
		}else if(command.equalsIgnoreCase(".wares") || command.equalsIgnoreCase(".items") || command.equalsIgnoreCase(".shop"))
		{
			Store.displayWares(event);
		}else if(command.equalsIgnoreCase(".buy"))
		{
			if(arguments==null)
			{
				event.getMessage().getChannel().sendMessage("Buy what?");
			}
			Store.buyItem(event, item);
		}else if(command.equalsIgnoreCase(".sell"))
		{
			String[] test = allArguments.split(" ");
			int k = test.length;
			int number = 0;
			String theitem = "";
			if(k<2)
			{
				return;
			}
			if(StringUtils.isNumeric(test[k-1]))
			{
				for(int j = 0;j<k-1;j++)
				{
					theitem+=test[j]+"_";
				}
				theitem = theitem.substring(0, theitem.lastIndexOf("_"));
				number = Integer.parseInt(test[k-1]);
			}
			else
		    {
		    	theitem = item;
		    	number = 1;
		    }
		    Store.sellItem(event, theitem, number);
		}else if(command.equalsIgnoreCase(".value") || command.equalsIgnoreCase(".price"))
		{
			Store.valueItem(event, item);
		}
	}
	
	public static void floorCommands(MessageReceivedEvent event) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		if(command.equalsIgnoreCase(".join"))
		{
			JSONObject json = new JSONObject(DiscordRPG.readFile(Player.file));
			if(json.getJSONObject("players").isNull(event.getMessage().getAuthor().getID()))
			{
				Player.create(event.getMessage().getAuthor());
				event.getMessage().getChannel().sendMessage("You have joined the game.\nMost commands are done via PM. Type .help anywhere to see available commands in that location.");
			}else{
				event.getMessage().getChannel().sendMessage("You are already in the system!");
			}
		}else if(command.equalsIgnoreCase(".help"))
		{
			event.getMessage().getChannel().sendMessage("*.join* if you are not already added to the game.");
		}else if(command.equalsIgnoreCase(".mine"))
		{
			event.getMessage().getChannel().sendMessage("You swing your pick at the rock.");
			Floor.mineRock(event.getMessage().getAuthor(), event.getMessage().getChannel());
		}else if(command.equalsIgnoreCase(".chop") || command.equalsIgnoreCase(".cut"))
		{
			event.getMessage().getChannel().sendMessage("You swing your axe at the tree.");
			Floor.cutTree(event.getMessage().getAuthor(), event.getMessage().getChannel());
		}
	}
	
	public static void useHandling(MessageReceivedEvent event) throws MissingPermissionsException, HTTP429Exception, DiscordException, JSONException, IOException
	{
		if(arguments==null)
		{
			event.getMessage().getChannel().sendMessage("Use what?");
			return;
		}
		ClassLoader classLoader = Input.class.getClassLoader();
		File iFile = new File(classLoader.getResource("items.json").getFile());
		JSONObject json = new JSONObject(DiscordRPG.readFile(Player.file));
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(iFile.getAbsolutePath()));
		JSONObject items = json2.getJSONObject("items");
		JSONObject player = json.getJSONObject("players").getJSONObject(event.getMessage().getAuthor().getID());
		if(player.getJSONObject("inventory").isNull(item))
		{
			event.getMessage().getChannel().sendMessage("Your \"summoning items from out of nowhere\" skill is not yet at level 99. Using an item you do not possess is not possible.");
			return;
		}
		if(items.isNull(item))
		{
			event.getMessage().getChannel().sendMessage("Maybe if you believe *just* a little harder, that nonexistant item will appear!");
			return;
		}
		
		event.getMessage().getChannel().sendMessage(items.getJSONObject(item).getString("flavor_text"));
		if(items.getJSONObject(item).getBoolean("toRemove"))
		{
			Player.inventoryRemove(event.getMessage().getAuthor(), item, 1);
		}
		if(items.getJSONObject(item).has("heal"))
		{
			Player.heal(event, items.getJSONObject(item).getInt("heal"));
		}
	}
	
}
