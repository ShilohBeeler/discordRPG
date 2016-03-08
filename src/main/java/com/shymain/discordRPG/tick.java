package com.shymain.discordRPG;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

import org.json.JSONException;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.util.HTTP429Exception;

public class Tick extends TimerTask{

	public void run()
	{
		Iterator<Map.Entry<Event,Integer>> iter = DiscordRPG.timedEvents.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<Event,Integer> entry = iter.next();
		    int temp = entry.getValue();
		    temp--;
		    if(temp==0)
		    {
		    	try {
		    		iter.remove();
					DiscordRPG.eventDistributer(entry.getKey().eventType, entry.getKey().user, entry.getKey().channel);
				} catch (JSONException | IOException | MissingPermissionsException | HTTP429Exception
						| DiscordException e) {
					e.printStackTrace();
				}
		    }else{
		    	entry.setValue(temp);
		    }
		}
	}
	
}
