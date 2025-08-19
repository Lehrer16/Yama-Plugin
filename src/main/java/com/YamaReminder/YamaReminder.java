
package com.YamaReminder;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.api.NPC;
import net.runelite.client.util.Text;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import java.awt.image.BufferedImage;

@PluginDescriptor(
	name = "Yama Reminder",
	description = "Reminds you when you are about to enter Phase 3",
	tags = {"boss", "reminder", "health", "yama", "Yama"}
)
@Slf4j
public class YamaReminder extends Plugin {
	@Inject
	private ClientToolbar clientToolbar;
	@Inject
	private net.runelite.client.ui.overlay.OverlayManager overlayManager;
	@Inject
	private YamaReminderOverlay overlay;
	private NavigationButton navButton;
	@Inject
	private net.runelite.api.Client client;

	// Boss tracking and reminder state
	private NPC trackedBoss;
	private boolean reminded = false;
	private boolean showReminder = false;
	private long reminderShownTime = 0;

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		if (trackedBoss != null)
		{
			// Hide reminder if player dies
			if (client.getLocalPlayer() != null && client.getLocalPlayer().getHealthRatio() == 0) {
				showReminder = false;
				reminderShownTime = 0;
				return;
			}
			int threshold = 33; // Show reminder at 33% HP or lower
			int hideThreshold = 25; // Hide reminder below 25% HP
			int ratio = trackedBoss.getHealthRatio();
			int scale = trackedBoss.getHealthScale();
			double hpPercent = scale > 0 ? (ratio / (double) scale) * 100 : 0;
			if (hpPercent > 0 && hpPercent <= threshold && hpPercent >= hideThreshold && !reminded)
			{
				reminded = true;
				showReminder = true;
				reminderShownTime = System.currentTimeMillis();
			}
			if (hpPercent < hideThreshold && showReminder)
			{
				if (reminderShownTime > 0 && System.currentTimeMillis() - reminderShownTime >= 35000) {
					showReminder = false;
					reminderShownTime = 0;
				}
			}
			// If boss despawns or dies, reset
			if (ratio == 0)
			{
				trackedBoss = null;
				reminded = false;
				showReminder = false;
				reminderShownTime = 0;
			}
		}
	}
	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		NPC npc = event.getNpc();
		if (npc.getId() == 	14176)
		{
			trackedBoss = npc;
			reminded = false;
			showReminder = false;
		}
	}

	// Called by overlay to know when to show the reminder box
	public boolean shouldShowReminder()
	{
		return showReminder;
	}

	@Override
	protected void startUp() throws Exception {
		super.startUp();
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception {
		super.shutDown();
		overlayManager.remove(overlay);
	}

	// Method to manually trigger the reminder for testing
	public void testReminder()
	{
		showReminder = true;
	}
}

