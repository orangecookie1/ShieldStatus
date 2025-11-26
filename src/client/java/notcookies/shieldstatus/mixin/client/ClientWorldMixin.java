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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import notcookies.shieldstatus.ShieldStatusClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    private final Map<UUID, Boolean> lastCooldownValues = new HashMap<>();
    public static Map<UUID, Boolean> SHIELD_STATE = new HashMap<>();


    @Inject(method = "playSound(Lnet/minecraft/entity/Entity;DDDLnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/sound/SoundCategory;FFJ)V", at = @At("HEAD"))
    private void onPlaySound(@Nullable Entity entity, double x, double y, double z, RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch, long seed, CallbackInfo ci) {
        // Compare registry entries using matchesId or by checking the sound's identifier
        if (sound.matchesId(SoundEvents.ITEM_SHIELD_BREAK.value().id())){
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                client.player.sendMessage(Text.literal("§cShield break sound detected at " + String.format("%.1f, %.1f, %.1f", x, y, z)), false);
                System.out.println("Shield break sound at " + x + ", " + y + ", " + z);

                if (entity != null) {
                    System.out.println("Entity: " + entity.getName().getString());
                    UUID id = entity.getUuid();
                    ShieldStatusClient.SHIELD_DISABLED_START.putIfAbsent(id, System.currentTimeMillis());
                }
            }
        }
    }
}