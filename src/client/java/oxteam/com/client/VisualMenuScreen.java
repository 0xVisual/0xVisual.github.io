package oxteam.com.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import oxteam.com.client.render.MusicPlayer;

public class VisualMenuScreen extends Screen {

    private static final int BORDER       = 0xFF2A2A2A;
    private static final int RED          = 0xFFFF3B3B;
    private static final int WHITE        = 0xFFFFFFFF;
    private static final int GRAY         = 0xFFAAAAAA;
    private static final int DARK         = 0xFF666666;
    private static final int CHECK_BG     = 0xFF0F0F0F;
    private static final int COLUMN_BG    = 0xCC0E0E0E;
    private static final int COLUMN_HEAD  = 0xFF080808;
    private static final int HOVER        = 0x33FFFFFF;
    private static final int TOP_BAR_BG   = 0xFF0A0A0A;

    private static final int COL_W    = 105;
    private static final int COL_GAP  = 6;
    private static final int HEAD_H   = 16;
    private static final int ROW_H    = 14;
    private static final int TOP_BAR_H = 24;

    private static int menuX = -1;
    private static int menuY = -1;

    private boolean dragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public VisualMenuScreen() {
        super(Component.literal("0xVisual"));
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        int totalWidth = COL_W * 5 + COL_GAP * 4;
        int totalHeight = TOP_BAR_H + HEAD_H + 12 * ROW_H + 20;
        if (menuX == -1) menuX = (this.width - totalWidth) / 2;
        if (menuY == -1) menuY = (this.height - totalHeight) / 2;

        g.fill(0, 0, this.width, this.height, 0x80000000);

        long t = System.currentTimeMillis();
        float pulse = (float) ((Math.sin(t / 900.0) + 1) / 2.0);
        int base = (int) (8 + pulse * 10);

        for (int i = 0; i < totalHeight; i += 4) {
            float k = (float) i / totalHeight;
            int r  = (int) (base * (1 - k * 0.3));
            int gr = (int) (base * (1 - k * 0.5));
            int b  = (int) (base * (1 - k * 0.7));
            int color = 0xF0000000 | (r << 16) | (gr << 8) | b;
            g.fill(menuX, menuY + i, menuX + totalWidth, menuY + i + 4, color);
        }

        g.fill(menuX, menuY, menuX + totalWidth, menuY + TOP_BAR_H, TOP_BAR_BG);
        g.fill(menuX, menuY + TOP_BAR_H - 1, menuX + totalWidth, menuY + TOP_BAR_H, BORDER);

        g.drawString(this.font, "0xVisual", menuX + 8, menuY + 7, RED, true);
        g.drawString(this.font, "v1.6", menuX + 76, menuY + 7, DARK, false);

        int sw = 50, sh = 14;
        int sx = menuX + totalWidth - sw - 8;
        int sy = menuY + 5;
        boolean sHover = isHovered(mouseX, mouseY, sx, sy, sw, sh);
        g.fill(sx, sy, sx + sw, sy + sh, sHover ? 0xFF222222 : 0xFF151515);
        drawBorder(g, sx, sy, sw, sh);
        g.drawString(this.font, "Save", sx + sw / 2 - this.font.width("Save") / 2, sy + 3, WHITE, false);

        int startX = menuX;
        int startY = menuY + TOP_BAR_H;

        int c1 = startX;
        int c2 = startX + COL_W + COL_GAP;
        int c3 = startX + (COL_W + COL_GAP) * 2;
        int c4 = startX + (COL_W + COL_GAP) * 3;
        int c5 = startX + (COL_W + COL_GAP) * 4;

        // CLIENT
        drawColumn(g, c1, startY, "CLIENT");
        int y = startY + HEAD_H + 4;
        y = drawRow(g, c1, y, "ClickGUI",    ZeroXVisualClient.enableMenu,        mouseX, mouseY);
        y = drawRow(g, c1, y, "Interface",   ZeroXVisualClient.enableInterface,   mouseX, mouseY);
        y = drawRow(g, c1, y, "Watermark",   ZeroXVisualClient.enableWatermark,   mouseX, mouseY);
        y = drawRow(g, c1, y, "ArrayList",   ZeroXVisualClient.enableArrayList,   mouseX, mouseY);
        y = drawRow(g, c1, y, "ClickSound",  ZeroXVisualClient.enableClickSound,  mouseX, mouseY);

        // DIFFERENT
        drawColumn(g, c2, startY, "DIFFERENT");
        y = startY + HEAD_H + 4;
        y = drawRow(g, c2, y, "FogColor",     ZeroXVisualClient.enableFogColor,    mouseX, mouseY);
        y = drawRow(g, c2, y, "JumpCircle",   ZeroXVisualClient.enableJumpCircle,  mouseX, mouseY);
        y = drawRow(g, c2, y, "ChinaHat",     ZeroXVisualClient.enableChinaHat,    mouseX, mouseY);
        y = drawRow(g, c2, y, "HandProgress", ZeroXVisualClient.enableHandProgress,mouseX, mouseY);
        y = drawRow(g, c2, y, "TimeChanger",  ZeroXVisualClient.timeChangerEnabled,mouseX, mouseY);
        y = drawRow(g, c2, y, "SkyColor",     ZeroXVisualClient.skyColorEnabled,   mouseX, mouseY);

        // GRAPHICS
        drawColumn(g, c3, startY, "GRAPHICS");
        y = startY + HEAD_H + 4;
        y = drawRow(g, c3, y, "Particles",    ZeroXVisualClient.enableParticles,   mouseX, mouseY);
        y = drawRow(g, c3, y, "Trails",       ZeroXVisualClient.enableTrails,      mouseX, mouseY);
        y = drawRow(g, c3, y, "FlyingPart",   ZeroXVisualClient.enableFlyingParticles, mouseX, mouseY);
        y = drawRow(g, c3, y, "HitPart",      ZeroXVisualClient.enableHitParticles,mouseX, mouseY);
        y = drawRow(g, c3, y, "NameColor",    ZeroXVisualClient.enableNameColor,   mouseX, mouseY);
        y = drawRow(g, c3, y, "NeonPart",     ZeroXVisualClient.enableNeonParticles, mouseX, mouseY);
        y = drawRow(g, c3, y, "TargetHUD",    ZeroXVisualClient.enableTargetHud, mouseX, mouseY);

        // STYLES
        drawColumn(g, c4, startY, "STYLES");
        y = startY + HEAD_H + 4;
        y = drawStyle(g, c4, y, "Default", 0xFFFFFFFF, ZeroXVisualClient.style.equals("Default"), mouseX, mouseY);
        y = drawStyle(g, c4, y, "SE 3",    0xFFFFCC66, ZeroXVisualClient.style.equals("SE3"),     mouseX, mouseY);
        y = drawStyle(g, c4, y, "SE 2",    0xFF88CCFF, ZeroXVisualClient.style.equals("SE2"),     mouseX, mouseY);
        y = drawStyle(g, c4, y, "SE",      0xFFFF6666, ZeroXVisualClient.style.equals("SE"),      mouseX, mouseY);
        y = drawStyle(g, c4, y, "Red",     0xFFFF3B3B, ZeroXVisualClient.style.equals("Red"),     mouseX, mouseY);
        y = drawStyle(g, c4, y, "Green",   0xFF3BFF5C, ZeroXVisualClient.style.equals("Green"),   mouseX, mouseY);
        y = drawStyle(g, c4, y, "Blue",    0xFF3B7BFF, ZeroXVisualClient.style.equals("Blue"),    mouseX, mouseY);
        y = drawStyle(g, c4, y, "Fruits",  0xFFFF77CC, ZeroXVisualClient.style.equals("Fruits"),  mouseX, mouseY);

        // MUSIC
        drawColumn(g, c5, startY, "MUSIC");
        y = startY + HEAD_H + 4;
        drawRow(g, c5, y, "Play/Pause", MusicPlayer.isPlaying(), mouseX, mouseY); y += ROW_H;
        drawRow(g, c5, y, "Next",       false, mouseX, mouseY); y += ROW_H;
        drawRow(g, c5, y, "Prev",       false, mouseX, mouseY); y += ROW_H;
        drawRow(g, c5, y, "Reload",     false, mouseX, mouseY); y += ROW_H;
        y += 4;
        String track = MusicPlayer.getCurrentTrackName();
        if (track.length() > 13) track = track.substring(0, 13) + "…";
        g.drawString(this.font, track, c5 + 4, y, GRAY, false);
        y += ROW_H;
        g.drawString(this.font, "Vol", c5 + 4, y + 3, GRAY, false);
        drawSlider(g, c5 + 28, y, 70, 10, MusicPlayer.getVolume(), mouseX, mouseY);
    }

    private void drawColumn(GuiGraphics g, int x, int y, String title) {
        int h = HEAD_H + 15 * ROW_H + 8;
        g.fill(x, y, x + COL_W, y + h, COLUMN_BG);
        g.fill(x, y, x + COL_W, y + HEAD_H, COLUMN_HEAD);
        drawBorder(g, x, y, COL_W, h);

        int tw = this.font.width(title);
        g.drawString(this.font, title, x + COL_W / 2 - tw / 2, y + 4, GRAY, false);
    }

    private int drawRow(GuiGraphics g, int x, int y, String name, boolean on, int mx, int my) {
        boolean hovered = isHovered(mx, my, x + 2, y, COL_W - 4, ROW_H);
        if (hovered) g.fill(x + 2, y, x + COL_W - 2, y + ROW_H, HOVER);

        int bx = x + 4;
        int by = y + (ROW_H - 8) / 2;
        g.fill(bx, by, bx + 8, by + 8, CHECK_BG);
        drawBorder(g, bx, by, 8, 8);

        if (on) {
            g.fill(bx + 1, by + 4, bx + 3, by + 6, RED);
            g.fill(bx + 3, by + 5, bx + 5, by + 7, RED);
            g.fill(bx + 4, by + 3, bx + 6, by + 5, RED);
            g.fill(bx + 5, by + 1, bx + 7, by + 3, RED);
        }

        g.drawString(this.font, name, x + 16, y + 4, on ? WHITE : DARK, false);
        return y + ROW_H;
    }

    private int drawStyle(GuiGraphics g, int x, int y, String name, int color, boolean active, int mx, int my) {
        boolean hovered = isHovered(mx, my, x + 2, y, COL_W - 4, ROW_H);
        if (hovered) g.fill(x + 2, y, x + COL_W - 2, y + ROW_H, HOVER);

        int bx = x + 4;
        int by = y + (ROW_H - 8) / 2;
        g.fill(bx, by, bx + 8, by + 8, color);
        drawBorder(g, bx, by, 8, 8);

        g.drawString(this.font, name, x + 16, y + 4, active ? WHITE : GRAY, false);
        if (active) g.fill(x + 2, y, x + 4, y + ROW_H, RED);
        return y + ROW_H;
    }

    private void drawSlider(GuiGraphics g, int x, int y, int w, int h, double value, int mx, int my) {
        g.fill(x, y + h / 2 - 2, x + w, y + h / 2 + 2, 0xFF1E1E1E);
        int fillW = (int) (w * value);
        g.fill(x, y + h / 2 - 2, x + fillW, y + h / 2 + 2, RED);
        int knobX = x + fillW - 4;
        g.fill(knobX, y, knobX + 8, y + h, WHITE);
        g.fill(knobX, y, knobX + 8, y + 1, 0xFF000000);
        g.fill(knobX, y + h - 1, knobX + 8, y + h, 0xFF000000);
    }

    private void drawBorder(GuiGraphics g, int x, int y, int w, int h) {
        g.fill(x, y, x + w, y + 1, BORDER);
        g.fill(x, y + h - 1, x + w, y + h, BORDER);
        g.fill(x, y, x + 1, y + h, BORDER);
        g.fill(x + w - 1, y, x + w, y + h, BORDER);
    }

    private boolean isHovered(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button != 0) return super.mouseClicked(mx, my, button);
        int imx = (int) mx, imy = (int) my;

        int totalWidth = COL_W * 5 + COL_GAP * 4;

        if (isHovered(imx, imy, menuX, menuY, totalWidth, TOP_BAR_H)) {
            int sw = 50, sh = 14;
            int sx = menuX + totalWidth - sw - 8;
            int sy = menuY + 5;
            if (isHovered(imx, imy, sx, sy, sw, sh)) {
                this.onClose();
                return true;
            }
            dragging = true;
            dragOffsetX = imx - menuX;
            dragOffsetY = imy - menuY;
            return true;
        }

        int startX = menuX;
        int startY = menuY + TOP_BAR_H;

        int c1 = startX;
        int c2 = startX + COL_W + COL_GAP;
        int c3 = startX + (COL_W + COL_GAP) * 2;
        int c4 = startX + (COL_W + COL_GAP) * 3;
        int c5 = startX + (COL_W + COL_GAP) * 4;
        int y0 = startY + HEAD_H + 4;

        // CLIENT
        if (rowHit(imx, imy, c1, y0, 0)) ZeroXVisualClient.enableMenu = !ZeroXVisualClient.enableMenu;
        else if (rowHit(imx, imy, c1, y0, 1)) ZeroXVisualClient.enableInterface = !ZeroXVisualClient.enableInterface;
        else if (rowHit(imx, imy, c1, y0, 2)) ZeroXVisualClient.enableWatermark = !ZeroXVisualClient.enableWatermark;
        else if (rowHit(imx, imy, c1, y0, 3)) ZeroXVisualClient.enableArrayList = !ZeroXVisualClient.enableArrayList;
        else if (rowHit(imx, imy, c1, y0, 4)) ZeroXVisualClient.enableClickSound = !ZeroXVisualClient.enableClickSound;

        // DIFFERENT
        else if (rowHit(imx, imy, c2, y0, 0)) ZeroXVisualClient.enableFogColor = !ZeroXVisualClient.enableFogColor;
        else if (rowHit(imx, imy, c2, y0, 1)) ZeroXVisualClient.enableJumpCircle = !ZeroXVisualClient.enableJumpCircle;
        else if (rowHit(imx, imy, c2, y0, 2)) ZeroXVisualClient.enableChinaHat = !ZeroXVisualClient.enableChinaHat;
        else if (rowHit(imx, imy, c2, y0, 3)) ZeroXVisualClient.enableHandProgress = !ZeroXVisualClient.enableHandProgress;
        else if (rowHit(imx, imy, c2, y0, 4)) ZeroXVisualClient.timeChangerEnabled = !ZeroXVisualClient.timeChangerEnabled;
        else if (rowHit(imx, imy, c2, y0, 5)) ZeroXVisualClient.skyColorEnabled = !ZeroXVisualClient.skyColorEnabled;

        // GRAPHICS
        else if (rowHit(imx, imy, c3, y0, 0)) ZeroXVisualClient.enableParticles = !ZeroXVisualClient.enableParticles;
        else if (rowHit(imx, imy, c3, y0, 1)) ZeroXVisualClient.enableTrails = !ZeroXVisualClient.enableTrails;
        else if (rowHit(imx, imy, c3, y0, 2)) ZeroXVisualClient.enableFlyingParticles = !ZeroXVisualClient.enableFlyingParticles;
        else if (rowHit(imx, imy, c3, y0, 3)) ZeroXVisualClient.enableHitParticles = !ZeroXVisualClient.enableHitParticles;
        else if (rowHit(imx, imy, c3, y0, 4)) ZeroXVisualClient.enableNameColor = !ZeroXVisualClient.enableNameColor;
        else if (rowHit(imx, imy, c3, y0, 5)) ZeroXVisualClient.enableNeonParticles = !ZeroXVisualClient.enableNeonParticles;
        else if (rowHit(imx, imy, c3, y0, 6)) ZeroXVisualClient.enableTargetHud = !ZeroXVisualClient.enableTargetHud;

        // STYLES
        else if (rowHit(imx, imy, c4, y0, 0)) ZeroXVisualClient.style = "Default";
        else if (rowHit(imx, imy, c4, y0, 1)) ZeroXVisualClient.style = "SE3";
        else if (rowHit(imx, imy, c4, y0, 2)) ZeroXVisualClient.style = "SE2";
        else if (rowHit(imx, imy, c4, y0, 3)) ZeroXVisualClient.style = "SE";
        else if (rowHit(imx, imy, c4, y0, 4)) ZeroXVisualClient.style = "Red";
        else if (rowHit(imx, imy, c4, y0, 5)) ZeroXVisualClient.style = "Green";
        else if (rowHit(imx, imy, c4, y0, 6)) ZeroXVisualClient.style = "Blue";
        else if (rowHit(imx, imy, c4, y0, 7)) ZeroXVisualClient.style = "Fruits";

        // MUSIC
        else if (rowHit(imx, imy, c5, y0, 0)) MusicPlayer.toggle();
        else if (rowHit(imx, imy, c5, y0, 1)) MusicPlayer.next();
        else if (rowHit(imx, imy, c5, y0, 2)) MusicPlayer.prev();
        else if (rowHit(imx, imy, c5, y0, 3)) MusicPlayer.loadPlaylist();
        else if (isHovered(imx, imy, c5 + 28, y0 + 4 * ROW_H + 4, 70, 12)) {
            double v = Math.max(0, Math.min(1, (mx - c5 - 28) / 70.0));
            MusicPlayer.setVolume((float) v);
        }

        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (dragging) {
            menuX = (int) mx - dragOffsetX;
            menuY = (int) my - dragOffsetY;

            int totalWidth = COL_W * 5 + COL_GAP * 4;
            menuX = Math.max(-totalWidth + 50, Math.min(this.width - 50, menuX));
            menuY = Math.max(0, Math.min(this.height - TOP_BAR_H, menuY));
            return true;
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        dragging = false;
        return super.mouseReleased(mx, my, button);
    }

    private boolean rowHit(int mx, int my, int colX, int y0, int index) {
        return isHovered(mx, my, colX + 2, y0 + index * ROW_H, COL_W - 4, ROW_H);
    }

    @Override
    public boolean isPauseScreen() { return false; }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) { this.onClose(); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}