package notcookies.shieldstatus;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShieldDisableDetector {
    private final Map<UUID, Boolean> lastCooldownValues = new HashMap<>();
    public static Map<UUID, Boolean> SHIELD_STATE = new HashMap<>();

    public void register() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (client.world == null) return;
        long currentTime = System.currentTimeMillis();
        /*for (PlayerEntity player : client.world.getPlayers()) {
            UUID id = player.getUuid();

            boolean isCooldown = player.getItemCooldownManager().isCoolingDown(Items.SHIELD.getDefaultStack());
            boolean usable = !isCooldown;

            SHIELD_STATE.put(id, usable);

            boolean previous = lastCooldownValues.getOrDefault(id, false);

            // Optional logging per player
            if (isCooldown && !previous)
                System.out.println(player.getName().getString() + "'s shield was disabled!");
            if (!isCooldown && previous)
                System.out.println(player.getName().getString() + "'s shield was re-enabled!");

            lastCooldownValues.put(id, isCooldown);
        }*/
    }
}
