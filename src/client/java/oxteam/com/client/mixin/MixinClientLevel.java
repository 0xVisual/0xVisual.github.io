package oxteam.com.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class MixinClientLevel {

    @Inject(method = "getDayTime", at = @At("RETURN"), cancellable = true)
    private void onGetDayTime(CallbackInfoReturnable<Long> cir) {
        if (!oxteam.com.client.ZeroXVisualClient.timeChangerEnabled) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.level == null) return;
        if (!(mc.level instanceof ClientLevel)) return;
        cir.setReturnValue(oxteam.com.client.ZeroXVisualClient.clientTime);
    }

    @Inject(method = "getSkyDarken", at = @At("RETURN"), cancellable = true)
    private void onGetSkyDarken(CallbackInfoReturnable<Float> cir) {
        if (!oxteam.com.client.ZeroXVisualClient.enableGamma) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.level == null) return;
        if (!(mc.level instanceof ClientLevel)) return;
        cir.setReturnValue(0.0f);
    }
}