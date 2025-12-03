package notcookies.shieldstatus.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Inject(
            method = "playSound(Lnet/minecraft/entity/Entity;DDDLnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/sound/SoundCategory;FFJ)V",
            at = @At("HEAD")
    )
    private void onPlaySound(
            @Nullable Entity entity,
            double x,
            double y,
            double z,
            RegistryEntry<SoundEvent> sound,
            SoundCategory category,
            float volume,
            float pitch,
            long seed,
            CallbackInfo ci
    ) {
        if (sound.matchesId(SoundEvents.ITEM_SHIELD_BREAK.value().id())) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && client.world != null) {
                // Find the closest player to the sound coordinates
                PlayerEntity closestPlayer = null;
                double closestDistance = Double.MAX_VALUE;

                for (PlayerEntity player : client.world.getPlayers()) {
                    // Calculate distance from player to sound location
                    double dx = player.getX() - x;
                    double dy = player.getY() - y;
                    double dz = player.getZ() - z;
                    double distanceSquared = dx * dx + dy * dy + dz * dz;

                    // Check if this player is closer and within reasonable range (5 blocks)
                    if (distanceSquared < closestDistance && distanceSquared < 25.0) {
                        closestDistance = distanceSquared;
                        closestPlayer = player;
                    }
                }

                if (closestPlayer != null) {
                    UUID id = closestPlayer.getUuid();
                    //client.player.sendMessage(
                    //        Text.literal("§cShield disabled for: " + closestPlayer.getName().getString()),
                    //        false
                    //);
                    //System.out.println("Shield disabled for: " + closestPlayer.getName().getString() + " at distance: " + Math.sqrt(closestDistance));

                    // Record the disable time
                    notcookies.shieldstatus.ShieldStatusClient.SHIELD_DISABLED_START.putIfAbsent(id, System.currentTimeMillis());
                } else {
                    //client.player.sendMessage(
                    //        Text.literal("§eShield break sound detected but no player nearby"),
                    //        false
                    //);
                }
            }
        }
    }
}