package notcookies.shieldstatus.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import notcookies.shieldstatus.ShieldStatusClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> {
    @Inject(method = "updateRenderState", at = @At("HEAD"))
    private void captureEntity(T entity, S state, float tickDelta, CallbackInfo ci) {
        if (entity instanceof PlayerEntity) {
            //ShieldStatusClient.currentEntity = entity;
            //System.out.println("Capturing player: " + entity.getName().getString());
        }
    }
}
