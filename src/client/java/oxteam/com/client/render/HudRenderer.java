package oxteam.com.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class HudRenderer {

    private static final int LOGO_RED = 0xFFFF2222;

    // ============ WATERMARK ============
    public static void renderWatermark(GuiGraphics g) {
        if (!oxteam.com.client.ZeroXVisualClient.enableWatermark) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        int x = 6;
        int y = 6;
        int p = 1;

        drawPixelZero(g, x, y, p);
        drawPixelX(g, x + 9 * p, y, p);

        long t = System.currentTimeMillis();
        float pulse = (float) ((Math.sin(t / 400.0) + 1) / 2.0);
        int dotAlpha = (int) (120 + pulse * 135);
        int dotColor = (dotAlpha << 24) | (LOGO_RED & 0xFFFFFF);
        int dotX = x + 18 * p;
        g.fill(dotX, y + 3 * p, dotX + 2 * p, y + 5 * p, dotColor);
    }

    private static void drawPixelZero(GuiGraphics g, int x, int y, int p) {
        g.fill(x + 1 * p, y + 0 * p, x + 6 * p, y + 1 * p, LOGO_RED);
        g.fill(x + 1 * p, y + 7 * p, x + 6 * p, y + 8 * p, LOGO_RED);
        g.fill(x + 0 * p, y + 1 * p, x + 1 * p, y + 7 * p, LOGO_RED);
        g.fill(x + 6 * p, y + 1 * p, x + 7 * p, y + 7 * p, LOGO_RED);

        for (int i = 0; i < 6; i++) {
            g.fill(x + (5 - i) * p, y + (1 + i) * p, x + (6 - i) * p, y + (2 + i) * p, LOGO_RED);
        }
    }

    private static void drawPixelX(GuiGraphics g, int x, int y, int p) {
        for (int i = 0; i < 7; i++) {
            g.fill(x + i * p, y + i * p, x + (i + 1) * p, y + (i + 1) * p, LOGO_RED);
        }
        for (int i = 0; i < 7; i++) {
            g.fill(x + (6 - i) * p, y + i * p, x + (7 - i) * p, y + (i + 1) * p, LOGO_RED);
        }
    }

    // ============ ARRAYLIST ============
    public static void renderArrayList(GuiGraphics g) {
        if (!oxteam.com.client.ZeroXVisualClient.enableArrayList) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        List<String> enabled = new ArrayList<>();

        if (oxteam.com.client.ZeroXVisualClient.enableWatermark) enabled.add("Watermark");
        if (oxteam.com.client.ZeroXVisualClient.enableArrayList) enabled.add("ArrayList");
        if (oxteam.com.client.ZeroXVisualClient.enableInterface) enabled.add("Interface");
        if (oxteam.com.client.ZeroXVisualClient.enableParticles) enabled.add("Particles");
        if (oxteam.com.client.ZeroXVisualClient.enableTrails) enabled.add("Trails");
        if (oxteam.com.client.ZeroXVisualClient.enableJumpCircle) enabled.add("JumpCircle");
        if (oxteam.com.client.ZeroXVisualClient.enableFlyingParticles) enabled.add("FlyingPart");
        if (oxteam.com.client.ZeroXVisualClient.enableFogColor) enabled.add("FogColor");
        if (oxteam.com.client.ZeroXVisualClient.enableHitParticles) enabled.add("HitPart");
        if (oxteam.com.client.ZeroXVisualClient.enableNameColor) enabled.add("NameColor");
        if (oxteam.com.client.ZeroXVisualClient.skyColorEnabled) enabled.add("SkyColor");
        if (oxteam.com.client.ZeroXVisualClient.timeChangerEnabled) enabled.add("TimeChanger");
        if (oxteam.com.client.ZeroXVisualClient.enableNeonParticles) enabled.add("Neon");

        enabled.sort((a, b) -> Integer.compare(mc.font.width(b), mc.font.width(a)));

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int y = 6;
        long t = System.currentTimeMillis();

        for (String name : enabled) {
            int textWidth = mc.font.width(name);
            int boxWidth = textWidth + 12;
            int x = screenWidth - boxWidth - 4;

            float k = (float) ((Math.sin((t / 600.0) + name.hashCode()) + 1) / 2.0);
            int r = 0xFF;
            int g2 = (int) (0x22 + k * 0x33);
            int b = (int) (0x22 + k * 0x33);
            int textColor = 0xFF000000 | (r << 16) | (g2 << 8) | b;

            g.fill(x, y, x + 3, y + 10, 0xFFFF3333);
            g.fill(x + 3, y, x + boxWidth, y + 10, 0x80000000);
            g.drawString(mc.font, name, x + 6, y + 1, textColor, false);

            y += 12;
        }
    }
}