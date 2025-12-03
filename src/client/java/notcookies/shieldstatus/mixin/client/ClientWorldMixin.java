package notcookies.shieldstatus.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Inject(
            method = "playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZJ)V",
            at = @At("HEAD")
    )
    private void onPlaySound(
            double x,
            double y,
            double z,
            SoundEvent event,
            SoundCategory category,
            float volume,
            float pitch,
            boolean useDistance,
            long seed,
            CallbackInfo ci
    ) {
        if (event.id().equals(SoundEvents.ITEM_SHIELD_BREAK.id())) {
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

                    // If the closest player is the client player (you), check if your shield is actually on cooldown
                    if (closestPlayer.getUuid().equals(client.player.getUuid())) {
                        float shieldCooldown = client.player.getItemCooldownManager().getCooldownProgress(new ItemStack(Items.SHIELD), 0.0f);
                        if (shieldCooldown <= 0.0f) {
                            // Your shield is NOT on cooldown, so this is a false detection
                            //System.out.println("Ignoring shield break for " + closestPlayer.getName().getString() + " - shield not on cooldown");
                            return;
                        }
                    }

                    //client.player.sendMessage(
                    //        Text.literal("§cShield disabled for: " + closestPlayer.getName().getString()),
                    //        false
                    //);
                    //System.out.println("Shield disabled for: " + closestPlayer.getName().getString() + " at distance: " + Math.sqrt(closestDistance));
                    //System.out.println("Shield disabled for: " + closestPlayer.getName().getString() + " at X: " + x + " Y: " + y + " Z: " + z);

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