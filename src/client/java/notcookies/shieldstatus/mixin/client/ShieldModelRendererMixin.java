package notcookies.shieldstatus.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.model.special.ShieldModelRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import notcookies.shieldstatus.ShieldStatusClient;
import notcookies.shieldstatus.TintedVertexConsumer;
import notcookies.shieldstatus.config.ConfigManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.UUID;

@Mixin(ShieldModelRenderer.class)
public class ShieldModelRendererMixin {

    @ModifyVariable(
            method = "render",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private VertexConsumerProvider applyColorTint(
            VertexConsumerProvider original,
            @Nullable ComponentMap componentMap,
            ItemDisplayContext itemDisplayContext,
            MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider,
            int light,
            int overlay,
            boolean glint
    ) {
        LivingEntity entity = ShieldStatusClient.currentEntity;

        if (itemDisplayContext == ItemDisplayContext.GUI) {
            return original;
        }

        if(!ConfigManager.CONFIG.showShieldDisableOverlay){
            return original;
        }

        float[] color = new float[]{1.0f, 1.0f, 1.0f, 0.7f};

        if (entity != null) {
            UUID playerUUID = entity.getUuid();

            Boolean shieldUsable = notcookies.shieldstatus.ShieldDisableDetector.SHIELD_STATE.get(playerUUID);

            if (shieldUsable != null) {
                if (shieldUsable) {
                    // Enabled - GREEN
                    color = new float[]{0.0f, 1.0f, 0.0f, 0.7f};
                } else {
                    // Disabled - check if interpolation is enabled
                    if (ConfigManager.CONFIG.showShieldInterpolation) {
                        color = calculateInterpolatedColor(playerUUID);
                    } else {
                        // No interpolation - just RED
                        color = new float[]{1.0f, 0.0f, 0.0f, 0.7f};
                    }
                }
            } else {
                color = new float[]{0.0f, 1.0f, 0.0f, 0.7f};
            }
        }

        final float red = color[0];
        final float green = color[1];
        final float blue = color[2];
        final float alpha = color[3];

        return renderType -> new TintedVertexConsumer(
                original.getBuffer(renderType),
                red, green, blue, alpha
        );
    }

    @Unique
    private float[] calculateInterpolatedColor(UUID playerUUID) {
        Long startTime = ShieldStatusClient.SHIELD_DISABLED_START.get(playerUUID);

        if (startTime == null) {
            return new float[]{1.0f, 0.0f, 0.0f, 0.7f};
        }

        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - startTime;

        float progress = Math.min(elapsed / 5000.0f, 1.0f);

        float red = 1.0f - (progress * 0.65f);
        float green = (progress * 0.85f);
        float blue = 0.0f;
        float alpha = 0.7f;

        return new float[]{red, green, blue, alpha};
    }
}