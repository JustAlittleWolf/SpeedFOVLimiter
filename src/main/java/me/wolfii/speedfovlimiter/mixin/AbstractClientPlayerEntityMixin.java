package me.wolfii.speedfovlimiter.mixin;

import me.wolfii.speedfovlimiter.Config;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerEntityMixin {

    @Redirect(method = "getFieldOfViewModifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;lerp(FFF)F"))
    private float modifyGetAttributeValue(float delta, float start, float end) {
        if (Config.getConfig().modEnabled) end = Math.min(Math.max(end, Config.getConfig().lowestFovMultiplier), Config.getConfig().highestFovMultiplier);
        return Mth.lerp(delta, start, end);
    }
}
