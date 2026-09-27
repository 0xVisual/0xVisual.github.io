package oxteam.com.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class ChinaHatRenderer {

    private static final float RADIUS = 0.45f;
    private static final float HEIGHT = 0.40f;
    private static final int SEGMENTS = 24;

    public static void render(PoseStack poseStack, MultiBufferSource buffers) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();

        for (Player player : mc.level.players()) {
            if (player.distanceToSqr(camPos.x, camPos.y, camPos.z) > 32 * 32) continue;
            renderOnPlayer(poseStack, buffers, player, camPos);
        }
    }

    private static void renderOnPlayer(PoseStack poseStack, MultiBufferSource buffers, Player player, Vec3 camPos) {
        poseStack.pushPose();

        double headY = player.getY() + player.getEyeHeight() + 0.05;
        poseStack.translate(player.getX() - camPos.x, headY - camPos.y, player.getZ() - camPos.z);
        poseStack.mulPose(Axis.YP.rotationDegrees(-player.yHeadRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(player.getXRot()));

        int color = getHatColor();
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int a = 200;

        // === ЦВЕТНАЯ ЗАЛИВКА БЕЗ ТЕКСТУР ===
        VertexConsumer vc = buffers.getBuffer(RenderType.debugQuads());
        Matrix4f mat = poseStack.last().pose();

        for (int i = 0; i < SEGMENTS; i++) {
            float a1 = (float) (2 * Math.PI * i / SEGMENTS);
            float a2 = (float) (2 * Math.PI * (i + 1) / SEGMENTS);

            float x1 = (float) Math.cos(a1) * RADIUS;
            float z1 = (float) Math.sin(a1) * RADIUS;
            float x2 = (float) Math.cos(a2) * RADIUS;
            float z2 = (float) Math.sin(a2) * RADIUS;

            // Боковая грань конуса
            vc.vertex(mat, 0, HEIGHT, 0).color(r, g, b, a).endVertex();
            vc.vertex(mat, x1, 0, z1).color(r, g, b, a).endVertex();
            vc.vertex(mat, x2, 0, z2).color(r, g, b, a).endVertex();
            vc.vertex(mat, 0, HEIGHT, 0).color(r, g, b, a).endVertex();

            // Основание
            vc.vertex(mat, 0, 0, 0).color(r, g, b, a).endVertex();
            vc.vertex(mat, x2, 0, z2).color(r, g, b, a).endVertex();
            vc.vertex(mat, x1, 0, z1).color(r, g, b, a).endVertex();
            vc.vertex(mat, 0, 0, 0).color(r, g, b, a).endVertex();
        }

        poseStack.popPose();
    }

    private static int getHatColor() {
        return switch (oxteam.com.client.ZeroXVisualClient.style) {
            case "Red"    -> 0xFFFF3B3B;
            case "Blue"   -> 0xFF3B7BFF;
            case "Green"  -> 0xFF3BFF5C;
            case "SE3"    -> 0xFFFFCC66;
            case "SE2"    -> 0xFF88CCFF;
            case "SE"     -> 0xFFFF6666;
            case "Fruits" -> 0xFFFF77CC;
            default       -> 0xFFFF00CC;
        };
    }
}