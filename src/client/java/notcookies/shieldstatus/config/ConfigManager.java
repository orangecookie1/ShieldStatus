package notcookies.shieldstatus.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH =
    FabricLoader.getInstance().getConfigDir().resolve("my_mod.json");

    public static Config CONFIG = new Config();

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                String json = Files.readString(PATH);
                CONFIG = GSON.fromJson(json, Config.class);
            } else {
                save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            Files.writeString(PATH, GSON.toJson(CONFIG));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

