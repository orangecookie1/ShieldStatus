package notcookies.shieldstatus.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import notcookies.shieldstatus.ShieldStatusClient;
import notcookies.shieldstatus.TintedVertexConsumer;
import notcookies.shieldstatus.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.UUID;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {

    @ModifyVariable(
            method = "render",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private VertexConsumerProvider applyShieldColorTint(
            VertexConsumerProvider original,
            ItemStack stack,
            ModelTransformationMode mode,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay
    ) {
        // Only apply to shields
        if (!stack.isOf(Items.SHIELD)) {
            return original;
        }

        // Don't tint in GUI/inventory
        if (mode == ModelTransformationMode.GUI) {
            return original;
        }

        if (!ConfigManager.CONFIG.showShieldDisableOverlay) {
            return original;
        }

        LivingEntity entity = ShieldStatusClient.currentEntity;
        float[] color = new float[]{1.0f, 1.0f, 1.0f, 0.7f};

        if (entity != null) {
            UUID playerUUID = entity.getUuid();
            Boolean shieldUsable = notcookies.shieldstatus.ShieldDisableDetector.SHIELD_STATE.get(playerUUID);

            if (shieldUsable != null) {
                if (shieldUsable) {
                    // Enabled - GREEN
                    color = new float[]{0.0f, 1.0f, 0.0f, 0.7f};
                } else {
                    // Disabled
                    if (ConfigManager.CONFIG.showShieldInterpolation) {
                        color = calculateInterpolatedColor(playerUUID);
                    } else {
                        // No interpolation - RED
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

        float red = 1.0f - progress;
        float green = progress * 0.75f;
        float blue = 0.0f;
        float alpha = 1.0f;

        return new float[]{red, green, blue, alpha};
    }
}