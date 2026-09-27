package oxteam.com.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oxteam.com.client.render.ChinaHatRenderer;
import oxteam.com.client.render.HudRenderer;
import oxteam.com.client.render.MusicPlayer;
import oxteam.com.client.render.NeonParticles;
import oxteam.com.client.render.TargetHud;

import java.util.Random;

public class ZeroXVisualClient implements ClientModInitializer {
    public static final String MOD_ID = "zeroxvisual";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static KeyMapping openMenuKey;

    // === CLIENT ===
    public static boolean enableMenu = true;
    public static boolean enableInterface = true;
    public static boolean enableWatermark = true;
    public static boolean enableClickSound = false;
    public static boolean enableArrayList = true;

        // === ACCESSORIES ===
    public static boolean enableHalo = false;
    public static boolean enableWings = false;
    public static boolean enableHorns = false;

    // === GAMMA ===
    public static boolean enableGamma = false;

        // === TARGET HUD ===
    public static boolean enableTargetHud = true;

    // === DIFFERENT ===
    public static boolean enableFogColor = false;
    public static boolean enableJumpCircle = true;
    public static boolean enableChinaHat = false;
    public static boolean enableHandProgress = false;

    // === TIME CHANGER ===
    public static boolean timeChangerEnabled = false;
    public static long clientTime = 6000L;

    // === SKY COLOR ===
    public static boolean skyColorEnabled = false;
    public static double skyRed = 0.5;
    public static double skyGreen = 0.5;
    public static double skyBlue = 1.0;

    // === GRAPHICS ===
    public static boolean enableParticles = true;
    public static boolean enableTrails = true;
    public static boolean enableFlyingParticles = false;
    public static boolean enableHitParticles = true;
    public static boolean enableNameColor = false;

    // === NEON ===
    public static boolean enableNeonParticles = false;

    // === Слайдеры ===
    public static double particleRadius = 2.0;
    public static double particleMultiplier = 4.0;

    // === Стиль ===
    public static String style = "Default";

    private final Random random = new Random();

    @Override
    public void onInitializeClient() {
        LOGGER.info("0xVisual loaded!");

                // === TARGET HUD ===
        HudRenderCallback.EVENT.register((g, delta) -> {
            TargetHud.render(g);
        });

        MusicPlayer.loadPlaylist();

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.zeroxvisual.open_menu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.zeroxvisual"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.consumeClick()) {
                if (client.screen == null) client.setScreen(new VisualMenuScreen());
            }
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            Minecraft client = Minecraft.getInstance();
            if (client.player == null || client.level == null) return;

            Vec3 pos = client.player.position();

            if (enableParticles) {
                for (int i = 0; i < (int) particleMultiplier; i++) {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double x = pos.x + Math.cos(angle) * particleRadius;
                    double z = pos.z + Math.sin(angle) * particleRadius;
                    double y = pos.y + random.nextDouble() * 2;
                    spawnStyled(client, x, y, z);
                }
            }

            if (enableJumpCircle && !client.player.onGround()) {
                for (int i = 0; i < 16; i++) {
                    double angle = (i / 16.0) * Math.PI * 2;
                    double x = pos.x + Math.cos(angle) * 0.8;
                    double z = pos.z + Math.sin(angle) * 0.8;
                    spawnStyled(client, x, pos.y - 0.1, z);
                }
            }

            if (enableFlyingParticles) {
                for (int i = 0; i < 2; i++) {
                    spawnStyled(client,
                            pos.x + (random.nextDouble() - 0.5),
                            pos.y + random.nextDouble() * 2,
                            pos.z + (random.nextDouble() - 0.5));
                }
            }

            if (enableTrails) {
                AABB area = client.player.getBoundingBox().inflate(32);
                for (Projectile p : client.level.getEntitiesOfClass(Projectile.class, area)) {
                    spawnStyled(client, p.getX(), p.getY(), p.getZ());
                }
            }

            if (enableChinaHat) {
                PoseStack poseStack = context.matrixStack();
                MultiBufferSource buffers = context.consumers();
                if (poseStack != null && buffers != null) {
                    ChinaHatRenderer.render(poseStack, buffers);
                }
            }
        });

        HudRenderCallback.EVENT.register((g, delta) -> {
            HudRenderer.renderWatermark(g);
        });

        HudRenderCallback.EVENT.register((g, delta) -> {
            HudRenderer.renderArrayList(g);
        });

        HudRenderCallback.EVENT.register((g, delta) -> {
            if (!enableInterface) return;
            Minecraft client = Minecraft.getInstance();
            if (client.player == null) return;

            int x = 5, y = 20, color = 0xFFFFFF;
            g.drawString(client.font, "FPS: " + client.getFps(), x, y, color, true);
            y += 12;
            g.drawString(client.font, String.format("XYZ: %.1f / %.1f / %.1f",
                    client.player.getX(), client.player.getY(), client.player.getZ()), x, y, color, true);
            y += 12;
            g.drawString(client.font, "Style: " + style, x, y, 0xFFFF5555, true);
        });
    }

    private void spawnStyled(Minecraft client, double x, double y, double z) {
        if (enableNeonParticles) {
            NeonParticles.spawnNeon(client, x, y, z);
            return;
        }

        switch (style) {
            case "Red"    -> client.level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
            case "Blue"   -> client.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0, 0, 0);
            case "Green"  -> client.level.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
            case "SE3"    -> client.level.addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0, 0);
            case "SE2"    -> client.level.addParticle(ParticleTypes.SOUL, x, y, z, 0, 0, 0);
            case "SE"     -> client.level.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0, 0, 0);
            case "Fruits" -> client.level.addParticle(ParticleTypes.CHERRY_LEAVES, x, y, z, 0, 0, 0);
            default       -> client.level.addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0, 0);
        }
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}