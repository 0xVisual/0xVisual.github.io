package oxteam.com.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class TargetHud {

    public static void render(GuiGraphics g) {
        if (!oxteam.com.client.ZeroXVisualClient.enableTargetHud) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        // Цель из перекрестия (уже Entity, а не EntityHitResult)
        Entity target = mc.crosshairPickEntity;
        if (target == null) return;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int x = screenWidth / 2 - 60;
        int y = screenHeight - 90;

        String name = target.getName().getString();
        if (name.length() > 16) name = name.substring(0, 16) + "…";

        // Фон
        g.fill(x, y, x + 120, y + 40, 0xCC000000);
        g.fill(x, y, x + 120, y + 1, 0xFFFF3333);
        g.fill(x, y + 39, x + 120, y + 40, 0xFFFF3333);
        g.fill(x, y, x + 1, y + 40, 0xFFFF3333);
        g.fill(x + 119, y, x + 120, y + 40, 0xFFFF3333);

        // Имя
        g.drawString(mc.font, name, x + 6, y + 4, 0xFFFFFFFF, true);

        if (target instanceof LivingEntity living) {
            float health = living.getHealth();
            float maxHealth = living.getMaxHealth();
            float percent = health / maxHealth;

            int barX = x + 6;
            int barY = y + 20;
            int barWidth = 108;
            int barHeight = 8;

            g.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF1A1A1A);

            int color;
            if (percent > 0.66f) color = 0xFF33FF33;
            else if (percent > 0.33f) color = 0xFFFFCC00;
            else color = 0xFFFF3333;

            int fillWidth = (int) (barWidth * percent);
            g.fill(barX, barY, barX + fillWidth, barY + barHeight, color);

            String hpText = String.format("%.1f / %.1f", health, maxHealth);
            g.drawString(mc.font, hpText, x + 6, y + 30, 0xFFFFFFFF, true);

            String dist = String.format("%.1f", mc.player.distanceTo(target));
            g.drawString(mc.font, dist + "m", x + 100, y + 30, 0xFFAAAAAA, true);
        } else {
            g.drawString(mc.font, target.getType().getDescription().getString(), x + 6, y + 30, 0xFFAAAAAA, true);
        }
    }
}