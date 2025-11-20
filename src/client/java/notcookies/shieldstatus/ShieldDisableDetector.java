package notcookies.shieldstatus;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShieldDisableDetector {
    private final Map<UUID, Boolean> wasOnCooldown = new HashMap<>();

    public void register() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(MinecraftClient client) {
        if (client.world == null) return;

        for (PlayerEntity player : client.world.getPlayers()) {
            UUID id = player.getUuid();

            boolean isOnCooldown = player.getItemCooldownManager().isCoolingDown(Items.SHIELD.getDefaultStack());
            boolean wasOnCooldownBefore = wasOnCooldown.getOrDefault(id, false);

            // Cooldown started
            if (isOnCooldown && !wasOnCooldownBefore) {
                System.out.println(player.getName().getString() + "'s shield was disabled!");
            }

            // Cooldown just ended → shield re-enabled
            if (!isOnCooldown && wasOnCooldownBefore) {
                System.out.println(player.getName().getString() + "'s shield is usable again!");
            }

            wasOnCooldown.put(id, isOnCooldown);
        }
    }
}
