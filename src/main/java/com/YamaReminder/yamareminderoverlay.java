package com.YamaReminder;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import javax.inject.Inject;
import java.awt.Graphics2D;
import java.awt.Dimension;

public class yamareminderoverlay extends Overlay {
    private final yamareminder plugin;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    public yamareminderoverlay(yamareminder plugin) {
        this.plugin = plugin;
        setPosition(OverlayPosition.TOP_LEFT);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!plugin.shouldShowReminder()) {
            return null;
        }
        panelComponent.getChildren().clear();
        panelComponent.getChildren().add(TitleComponent.builder()
            .text("Entering phase 3!")
            .color(java.awt.Color.WHITE)
            .build());
        // Darker red with transparency
        panelComponent.setBackgroundColor(new java.awt.Color(139, 0, 0, 180));
        return panelComponent.render(graphics);
    }
}
