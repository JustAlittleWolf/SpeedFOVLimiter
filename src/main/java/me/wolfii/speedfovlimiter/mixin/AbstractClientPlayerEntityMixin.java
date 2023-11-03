package me.wolfii.speedfovlimiter.mixin;

import me.wolfii.speedfovlimiter.Config;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {

    @Redirect(method = "getFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"))
    private float modifyGetAttributeValue(float delta, float start, float end) {
        if (Config.getConfig().modEnabled) end = Math.min(Math.max(end, Config.getConfig().lowestFovMultiplier), Config.getConfig().highestFovMultiplier);
        return MathHelper.lerp(delta, start, end);
    }
}
