package notcookies.shieldstatus;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import notcookies.shieldstatus.ShieldStatusClient;

public class ShieldDisableDetector {
    private final Map<UUID, Boolean> lastCooldownValues = new HashMap<>();
    public static Map<UUID, Boolean> SHIELD_STATE = new HashMap<>();

    public void register() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (client.world == null) return;

        long currentTime = System.currentTimeMillis();

        Iterator<Map.Entry<UUID, Long>> iterator = ShieldStatusClient.SHIELD_DISABLED_START.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, Long> entry = iterator.next();
            UUID uuid = entry.getKey();
            Long startTime = entry.getValue();
            long elapsed = currentTime - startTime;

            if (elapsed >= 5000) {
                iterator.remove();
                SHIELD_STATE.put(uuid, true);
            } else {
                SHIELD_STATE.put(uuid, false);
            }
        }
    }
}