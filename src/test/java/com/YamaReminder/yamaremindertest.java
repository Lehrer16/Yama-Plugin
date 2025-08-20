package com.YamaReminder;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class yamaremindertest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(yamareminder.class);
		RuneLite.main(args);
	}
}