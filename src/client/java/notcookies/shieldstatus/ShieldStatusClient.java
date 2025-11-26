package notcookies.shieldstatus;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShieldStatusClient implements ClientModInitializer {

    public static LivingEntity currentEntity = null;
    public static Map<UUID, Long> SHIELD_DISABLED_START = new HashMap<>();

    @Override
    public void onInitializeClient() {
        ShieldDisableDetector detector = new ShieldDisableDetector();
        detector.register();
        System.out.println("Shield Color by Status Loaded!");
    }
}