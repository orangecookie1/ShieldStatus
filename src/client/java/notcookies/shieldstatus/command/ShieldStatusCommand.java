package notcookies.shieldstatus.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import notcookies.shieldstatus.config.ConfigManager;

public class ShieldStatusCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        // Main command: /shieldstatus
        dispatcher.register(ClientCommandManager.literal("shieldstatus")
                .then(ClientCommandManager.literal("toggleoverlay")
                        .executes(ShieldStatusCommand::toggleOverlay))
                .then(ClientCommandManager.literal("toggleinterpolation")
                        .executes(ShieldStatusCommand::toggleInterpolation))
        );

        // Alias: /ss
        dispatcher.register(ClientCommandManager.literal("ss")
                .then(ClientCommandManager.literal("toggleoverlay")
                        .executes(ShieldStatusCommand::toggleOverlay))
                .then(ClientCommandManager.literal("toggleinterpolation")
                        .executes(ShieldStatusCommand::toggleInterpolation))
        );
    }

    private static int toggleOverlay(CommandContext<FabricClientCommandSource> context) {
        ConfigManager.CONFIG.showShieldDisableOverlay = !ConfigManager.CONFIG.showShieldDisableOverlay;
        ConfigManager.save();

        String status = ConfigManager.CONFIG.showShieldDisableOverlay ? "enabled" : "disabled";
        context.getSource().sendFeedback(
                Text.literal("§aShield Disable Overlay: §f" + status)
        );
        return 1;
    }

    private static int toggleInterpolation(CommandContext<FabricClientCommandSource> context) {
        ConfigManager.CONFIG.showShieldInterpolation = !ConfigManager.CONFIG.showShieldInterpolation;
        ConfigManager.save();

        String status = ConfigManager.CONFIG.showShieldInterpolation ? "enabled" : "disabled";
        context.getSource().sendFeedback(
                Text.literal("§aShield Interpolation: §f" + status)
        );
        return 1;
    }
}