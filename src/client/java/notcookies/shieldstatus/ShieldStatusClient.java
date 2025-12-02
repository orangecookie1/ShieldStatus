package notcookies.shieldstatus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.entity.LivingEntity;
import notcookies.shieldstatus.command.ShieldStatusCommand;
import notcookies.shieldstatus.config.ConfigManager;

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
        ConfigManager.load();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            ShieldStatusCommand.register(dispatcher);
        });
        System.out.println("[ShieldStatus] Shield Status Mod Loaded!");
    }

    public static boolean isInterpolationEnabled() {
        return ConfigManager.CONFIG.showShieldInterpolation;
    }

    public static boolean isOverlayEnabled() {
        return ConfigManager.CONFIG.showShieldDisableOverlay;
    }
}