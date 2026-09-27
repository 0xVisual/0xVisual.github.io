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

public class AccessoryRenderer {

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
        if (oxteam.com.client.ZeroXVisualClient.enableHalo)  renderHalo(poseStack, buffers, player, camPos);
        if (oxteam.com.client.ZeroXVisualClient.enableWings) renderWings(poseStack, buffers, player, camPos);
        if (oxteam.com.client.ZeroXVisualClient.enableHorns) renderHorns(poseStack, buffers, player, camPos);
    }

    private static void renderHalo(PoseStack poseStack, MultiBufferSource buffers, Player player, Vec3 camPos) {
        poseStack.pushPose();
        double headY = player.getY() + player.getEyeHeight() + 0.35;
        poseStack.translate(player.getX() - camPos.x, headY - camPos.y, player.getZ() - camPos.z);
        poseStack.mulPose(Axis.YP.rotationDegrees(-player.yHeadRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(90f));

        VertexConsumer vc = buffers.getBuffer(RenderType.debugQuads());
        Matrix4f mat = poseStack.last().pose();

        int r = 0xFF, g = 0xD7, b = 0x00;

        float outerR = 0.30f, innerR = 0.22f;
        int segs = 32;

        for (int i = 0; i < segs; i++) {
            float a1 = (float) (2 * Math.PI * i / segs);
            float a2 = (float) (2 * Math.PI * (i + 1) / segs);
            float ox1 = (float) Math.cos(a1) * outerR, oz1 = (float) Math.sin(a1) * outerR;
            float ox2 = (float) Math.cos(a2) * outerR, oz2 = (float) Math.sin(a2) * outerR;
            float ix1 = (float) Math.cos(a1) * innerR, iz1 = (float) Math.sin(a1) * innerR;
            float ix2 = (float) Math.cos(a2) * innerR, iz2 = (float) Math.sin(a2) * innerR;

            vc.vertex(mat, ox1, 0, oz1).color(r, g, b, 255).endVertex();
            vc.vertex(mat, ix1, 0, iz1).color(r, g, b, 255).endVertex();
            vc.vertex(mat, ix2, 0, iz2).color(r, g, b, 255).endVertex();
            vc.vertex(mat, ox2, 0, oz2).color(r, g, b, 255).endVertex();
        }
        poseStack.popPose();
    }

    private static void renderWings(PoseStack poseStack, MultiBufferSource buffers, Player player, Vec3 camPos) {
        poseStack.pushPose();
        double backY = player.getY() + 1.2;
        poseStack.translate(player.getX() - camPos.x, backY - camPos.y, player.getZ() - camPos.z);
        poseStack.mulPose(Axis.YP.rotationDegrees(-player.yHeadRot));

        VertexConsumer vc = buffers.getBuffer(RenderType.debugQuads());
        Matrix4f mat = poseStack.last().pose();
        int r = 0xFF, g = 0xFF, b = 0xFF;

        float w = 0.7f, h = 1.1f;

        vc.vertex(mat, 0, 0, 0.2f).color(r, g, b, 200).endVertex();
        vc.vertex(mat, -w, -h, 0.2f).color(r, g, b, 200).endVertex();
        vc.vertex(mat, -w, 0, 0.2f).color(r, g, b, 200).endVertex();
        vc.vertex(mat, 0, 0, 0.2f).color(r, g, b, 200).endVertex();

        vc.vertex(mat, 0, 0, 0.2f).color(r, g, b, 200).endVertex();
        vc.vertex(mat, w, 0, 0.2f).color(r, g, b, 200).endVertex();
        vc.vertex(mat, w, -h, 0.2f).color(r, g, b, 200).endVertex();
        vc.vertex(mat, 0, 0, 0.2f).color(r, g, b, 200).endVertex();

        poseStack.popPose();
    }

    private static void renderHorns(PoseStack poseStack, MultiBufferSource buffers, Player player, Vec3 camPos) {
        poseStack.pushPose();
        double headY = player.getY() + player.getEyeHeight() + 0.25;
        poseStack.translate(player.getX() - camPos.x, headY - camPos.y, player.getZ() - camPos.z);
        poseStack.mulPose(Axis.YP.rotationDegrees(-player.yHeadRot));

        VertexConsumer vc = buffers.getBuffer(RenderType.debugQuads());
        Matrix4f mat = poseStack.last().pose();
        int r = 0xFF, g = 0x33, b = 0x33;

        vc.vertex(mat, -0.15f, 0, 0).color(r, g, b, 255).endVertex();
        vc.vertex(mat, -0.25f, 0.3f, 0).color(r, g, b, 255).endVertex();
        vc.vertex(mat, -0.05f, 0.3f, 0).color(r, g, b, 255).endVertex();
        vc.vertex(mat, -0.15f, 0, 0).color(r, g, b, 255).endVertex();

        vc.vertex(mat, 0.15f, 0, 0).color(r, g, b, 255).endVertex();
        vc.vertex(mat, 0.05f, 0.3f, 0).color(r, g, b, 255).endVertex();
        vc.vertex(mat, 0.25f, 0.3f, 0).color(r, g, b, 255).endVertex();
        vc.vertex(mat, 0.15f, 0, 0).color(r, g, b, 255).endVertex();

        poseStack.popPose();
    }
}