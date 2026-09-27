package oxteam.com.client.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public class MixinSkyColor {

    @Inject(method = "getSkyColor", at = @At("RETURN"), cancellable = true)
    private void onGetSkyColor(Vec3 cameraPos, float partialTick,
                               CallbackInfoReturnable<Vec3> cir) {
        if (!oxteam.com.client.ZeroXVisualClient.skyColorEnabled) return;

        cir.setReturnValue(new Vec3(
                oxteam.com.client.ZeroXVisualClient.skyRed,
                oxteam.com.client.ZeroXVisualClient.skyGreen,
                oxteam.com.client.ZeroXVisualClient.skyBlue
        ));
    }
}