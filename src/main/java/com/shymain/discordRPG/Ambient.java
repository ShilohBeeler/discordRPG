package com.shymain.discordRPG;

import java.util.Random;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.HTTP429Exception;

public class Ambient {
	
	public static String weather;
	
	public static void initialize(ReadyEvent event)
	{
		weather = "clear";
		IChannel channel = event.getClient()
				.getGuildByID("157558660732682241")
				.getChannels()
				.get(0);
		Event weatherCycle = new Event("WeatherTestEvent", null, channel);
		DiscordRPG.timedEvents.put(weatherCycle, 60);
	}
	
	public static void weatherTest(IChannel channel) throws MissingPermissionsException, HTTP429Exception, DiscordException
	{
		Random r = new Random();
		int random = r.nextInt(100);
		int testnum = 0;
		String toChange = "";
		String changeMessage = "";
		if(weather.equalsIgnoreCase("clear"))
		{
			testnum = 2;
			toChange = "rain";
			changeMessage = "Rain starts to pour.";
		}else if(weather.equalsIgnoreCase("rain"))
		{
			testnum = 5;
			toChange = "clear";
			changeMessage = "The clouds disperse to reveal a crystal clear sky.";
		}
		if(random<testnum)
		{
			weather = toChange;
			channel.sendMessage(changeMessage);
		}
		Event weatherCycle = new Event("WeatherTestEvent", null, channel);
		DiscordRPG.timedEvents.put(weatherCycle, 60);
	}

}
