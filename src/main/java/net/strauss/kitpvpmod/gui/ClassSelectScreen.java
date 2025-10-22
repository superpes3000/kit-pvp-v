package net.strauss.kitpvpmod.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClassSelectScreen extends Screen {
    public ClassSelectScreen() {
        super(Component.literal("Моё окно"));
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(Component.literal("Закрыть"), button -> {
            this.onClose();
        }).bounds(width / 2 - 50, height / 2, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawCenteredString(this.font, "Привет из GUI!", this.width / 2, this.height / 2 - 30, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
