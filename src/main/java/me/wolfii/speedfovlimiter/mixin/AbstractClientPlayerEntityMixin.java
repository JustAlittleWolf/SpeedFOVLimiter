package me.wolfii.speedfovlimiter.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.wolfii.speedfovlimiter.Config;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerEntityMixin {
    // See net.minecraft.world.effect.MobEffects
    @Unique
    private static final Identifier SLOWNESS_ID = Identifier.withDefaultNamespace("effect.slowness");
    @Unique
    private static final Identifier SPEED_ID = Identifier.withDefaultNamespace("effect.speed");
    // See net.minecraft.world.item.enchantment.Enchantments
    @Unique
    private static final Identifier SOUL_SPEED_ID = Identifier.withDefaultNamespace("enchantment.soul_speed/feet");

    @Unique
    private AttributeInstance dummyInstance;

    @WrapOperation(method = "getFieldOfViewModifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;lerp(FFF)F"))
    private float modifyOverallFOVEffect(float alpha1, float p0, float p1, Operation<Float> original) {
        if (!Config.getConfig().modEnabled || !Config.getConfig().limitAllFov) {
            return original.call(alpha1, p0, p1);
        }
        return Mth.lerp(alpha1, p0, Math.clamp(p1, Config.getConfig().lowestFovMultiplier, Config.getConfig().highestFovMultiplier));
    }

    @WrapOperation(method = "getFieldOfViewModifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
    private double modifySpeedEffectFOVEffect(AbstractClientPlayer instance, Holder<Attribute> holder, Operation<Double> original) {
        double rawAttributeValue = original.call(instance, holder);
        AttributeInstance attributeInstance = instance.getAttribute(holder);
        if (attributeInstance == null || !Config.getConfig().modEnabled || Config.getConfig().limitAllFov) {
            return rawAttributeValue;
        }

        if (this.dummyInstance == null || this.dummyInstance.getAttribute() != holder) {
            this.dummyInstance = new AttributeInstance(holder, _ -> {
            });
        }
        this.dummyInstance.replaceFrom(attributeInstance);
        this.dummyInstance.removeModifier(SLOWNESS_ID);
        this.dummyInstance.removeModifier(SPEED_ID);
        if (Config.getConfig().limitSoulSpeed) {
            this.dummyInstance.removeModifier(SOUL_SPEED_ID);
        }

        return Math.clamp(
            rawAttributeValue,
            (2.0 * Config.getConfig().lowestFovMultiplier - 1.0) * this.dummyInstance.getValue(),
            (2.0 * Config.getConfig().highestFovMultiplier - 1.0) * this.dummyInstance.getValue()
        );
    }
}
