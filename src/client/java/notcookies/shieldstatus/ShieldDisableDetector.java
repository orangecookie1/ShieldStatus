package notcookies.shieldstatus;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

import java.util.HashMap;
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
        for (Map.Entry<UUID, Long> entry : ShieldStatusClient.SHIELD_DISABLED_START.entrySet()) {
            long currentTime = System.currentTimeMillis();
            UUID uuid = entry.getKey();
            Long startTime = entry.getValue();
            long elapsed  = currentTime-startTime;

            if(elapsed > 5000){
                ShieldStatusClient.SHIELD_DISABLED_START.remove(uuid);
                System.out.println("Shield was re-enabled!");
                SHIELD_STATE.put(uuid, true);
            }else if (elapsed < 5000){
                SHIELD_STATE.put(uuid, false);
            }
        }
    }
}
