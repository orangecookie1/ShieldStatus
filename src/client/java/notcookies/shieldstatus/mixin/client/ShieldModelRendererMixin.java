package notcookies.shieldstatus.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.model.special.ShieldModelRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import notcookies.shieldstatus.ShieldStatusClient;
import notcookies.shieldstatus.TintedVertexConsumer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
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

        float[] color = new float[]{1.0f, 1.0f, 1.0f, 0.7f}; // Default white

        if (entity != null) {
            UUID playerUUID = entity.getUuid();

            // Check shield state from ShieldDisableDetector
            Boolean shieldUsable = notcookies.shieldstatus.ShieldDisableDetector.SHIELD_STATE.get(playerUUID);

            if (shieldUsable != null) {
                if (shieldUsable) {
                    // Shield is OFF cooldown (enabled) - GREEN
                    color = new float[]{0.0f, 1.0f, 0.0f, 0.7f};
                    //System.out.println("Rendering GREEN shield for " + entity.getName().getString() + " (enabled)");
                } else {
                    // Shield is ON cooldown (disabled) - RED
                    color = new float[]{1.0f, 0.0f, 0.0f, 0.7f};
                    //System.out.println("Rendering RED shield for " + entity.getName().getString() + " (disabled)");
                }
            } else {
                // Player not tracked yet - white/default
                //System.out.println("Player " + entity.getName().getString() + " not in SHIELD_STATE map");
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
}