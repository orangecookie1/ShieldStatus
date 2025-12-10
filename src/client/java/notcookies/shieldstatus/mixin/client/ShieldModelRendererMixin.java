package notcookies.shieldstatus.mixin.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.MovingBlockRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.command.RenderCommandQueue;
import net.minecraft.client.render.entity.state.EntityHitboxAndView;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.special.ShieldModelRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import notcookies.shieldstatus.ShieldStatusClient;
import notcookies.shieldstatus.config.ConfigManager;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.UUID;

@Mixin(ShieldModelRenderer.class)
public class ShieldModelRendererMixin {

    @ModifyVariable(
            method = "render",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private OrderedRenderCommandQueue applyColorTint(
            OrderedRenderCommandQueue original,
            @Nullable ComponentMap componentMap,
            ItemDisplayContext itemDisplayContext,
            MatrixStack matrixStack,
            OrderedRenderCommandQueue orderedRenderCommandQueue,
            int light,
            int overlay,
            boolean glint,
            int seed
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

        final int colorInt = getColorInt(color[0], color[1], color[2], color[3]);

        // Return a wrapped OrderedRenderCommandQueue that modifies the color parameter
        return new OrderedRenderCommandQueue() {
            @Override
            public void submitModelPart(ModelPart part, MatrixStack matrices, RenderLayer renderLayer, int light, int overlay, @Nullable Sprite sprite, boolean sheeted, boolean hasGlint, int tintedColor, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay, int i) {
                // Override the color parameter with our custom color
                original.submitModelPart(part, matrices, renderLayer, light, overlay, sprite, sheeted, hasGlint, colorInt, crumblingOverlay, i);
            }

            @Override
            public RenderCommandQueue getBatchingQueue(int batchSize) {
                return original.getBatchingQueue(batchSize);
            }

            @Override
            public void submitDebugHitbox(MatrixStack matrices, EntityRenderState renderState, EntityHitboxAndView debugHitbox) {
                original.submitDebugHitbox(matrices, renderState, debugHitbox);
            }

            @Override
            public void submitShadowPieces(MatrixStack matrices, float shadowRadius, List<EntityRenderState.ShadowPiece> shadowPieces) {
                original.submitShadowPieces(matrices, shadowRadius, shadowPieces);
            }

            @Override
            public void submitLabel(MatrixStack matrices, @Nullable Vec3d nameLabelPos, int y, Text label, boolean notSneaking, int light, double squaredDistanceToCamera, CameraRenderState cameraState) {
                original.submitLabel(matrices, nameLabelPos, y, label, notSneaking, light, squaredDistanceToCamera, cameraState);
            }

            @Override
            public void submitText(MatrixStack matrices, float x, float y, OrderedText text, boolean dropShadow, TextRenderer.TextLayerType layerType, int light, int color, int backgroundColor, int outlineColor) {
                original.submitText(matrices, x, y, text, dropShadow, layerType, light, color, backgroundColor, outlineColor);
            }

            @Override
            public void submitFire(MatrixStack matrices, EntityRenderState renderState, Quaternionf rotation) {
                original.submitFire(matrices, renderState, rotation);
            }

            @Override
            public void submitLeash(MatrixStack matrices, EntityRenderState.LeashData leashData) {
                original.submitLeash(matrices, leashData);
            }

            @Override
            public <S> void submitModel(Model<? super S> model, S state, MatrixStack matrices, RenderLayer renderLayer, int light, int overlay, int tintedColor, @Nullable Sprite sprite, int outlineColor, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
                original.submitModel(model, state, matrices, renderLayer, light, overlay, tintedColor, sprite, outlineColor, crumblingOverlay);
            }

            @Override
            public void submitBlock(MatrixStack matrices, BlockState state, int light, int overlay, int outlineColor) {
                original.submitBlock(matrices, state, light, overlay, outlineColor);
            }

            @Override
            public void submitMovingBlock(MatrixStack matrices, MovingBlockRenderState state) {
                original.submitMovingBlock(matrices, state);
            }

            @Override
            public void submitBlockStateModel(MatrixStack matrices, RenderLayer renderLayer, BlockStateModel model, float r, float g, float b, int light, int overlay, int outlineColor) {
                original.submitBlockStateModel(matrices, renderLayer, model, r, g, b, light, overlay, outlineColor);
            }

            @Override
            public void submitItem(MatrixStack matrices, ItemDisplayContext displayContext, int light, int overlay, int outlineColors, int[] tintLayers, List<BakedQuad> quads, RenderLayer renderLayer, ItemRenderState.Glint glintType) {
                original.submitItem(matrices, displayContext, light, overlay, outlineColors, tintLayers, quads, renderLayer, glintType);
            }

            @Override
            public void submitCustom(MatrixStack matrices, RenderLayer renderLayer, Custom customRenderer) {
                original.submitCustom(matrices, renderLayer, customRenderer);
            }

            @Override
            public void submitCustom(LayeredCustom customRenderer) {
                original.submitCustom(customRenderer);
            }
        };
    }

    @Unique
    private int getColorInt(float red, float green, float blue, float alpha) {
        int r = (int)(red * 255.0f) & 0xFF;
        int g = (int)(green * 255.0f) & 0xFF;
        int b = (int)(blue * 255.0f) & 0xFF;
        int a = (int)(alpha * 255.0f) & 0xFF;
        return (a << 24) | (r << 16) | (g << 8) | b;
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
        float alpha = 0.7f;

        return new float[]{red, green, blue, alpha};
    }
}