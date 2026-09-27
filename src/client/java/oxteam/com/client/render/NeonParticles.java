package oxteam.com.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import org.joml.Vector3f;

public class NeonParticles {

    private static final Vector3f[] NEON_COLORS = {
            new Vector3f(1.0f, 0.0f, 0.0f),
            new Vector3f(0.0f, 1.0f, 0.0f),
            new Vector3f(0.0f, 0.5f, 1.0f),
            new Vector3f(1.0f, 0.0f, 1.0f),
            new Vector3f(1.0f, 1.0f, 0.0f),
            new Vector3f(0.0f, 1.0f, 1.0f),
    };

    private static Vector3f getColorForStyle(String style) {
        return switch (style) {
            case "Red"    -> new Vector3f(1.0f, 0.1f, 0.1f);
            case "Blue"   -> new Vector3f(0.1f, 0.5f, 1.0f);
            case "Green"  -> new Vector3f(0.1f, 1.0f, 0.3f);
            case "SE3"    -> new Vector3f(1.0f, 0.8f, 0.4f);
            case "SE2"    -> new Vector3f(0.5f, 0.8f, 1.0f);
            case "SE"     -> new Vector3f(1.0f, 0.4f, 0.4f);
            case "Fruits" -> new Vector3f(1.0f, 0.5f, 0.8f);
            default       -> new Vector3f(1.0f, 0.0f, 1.0f);
        };
    }

    public static void spawnNeon(Minecraft client, double x, double y, double z) {
        if (client.level == null) return;

        Vector3f color = getColorForStyle(oxteam.com.client.ZeroXVisualClient.style);
        DustParticleOptions dust = new DustParticleOptions(color, 1.5f);
        client.level.addParticle(dust, x, y, z, 0, 0, 0);
    }

    public static void spawnRainbow(Minecraft client, double x, double y, double z) {
        if (client.level == null) return;

        long t = System.currentTimeMillis();
        int index = (int) ((t / 200) % NEON_COLORS.length);
        Vector3f color = NEON_COLORS[index];

        DustParticleOptions dust = new DustParticleOptions(color, 1.5f);
        client.level.addParticle(dust, x, y, z, 0, 0, 0);
    }

    public static void spawnBig(Minecraft client, double x, double y, double z, float size) {
        if (client.level == null) return;

        Vector3f color = getColorForStyle(oxteam.com.client.ZeroXVisualClient.style);
        DustParticleOptions dust = new DustParticleOptions(color, size);
        client.level.addParticle(dust, x, y, z, 0, 0, 0);
    }
}