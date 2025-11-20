package notcookies.shieldstatus;

import net.fabricmc.api.ClientModInitializer;

public class ShieldStatusClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
        new ShieldDisableDetector().register();
        System.out.println("[Shield Status] Mod Enabled");
	}
}