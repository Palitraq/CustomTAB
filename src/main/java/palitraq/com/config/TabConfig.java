package palitraq.com.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TabConfig {
    private static final String CONFIG_FILE = "customtab.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    public boolean enabled = true;
    public String header = "§6§lCustomTAB\n§eTPS: %TPS%\n§aPlayers: %PLAYERS%";
    public String footer = "§e§lMade with §c§l❤";

    public static TabConfig load() {
        File file = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE).toFile();
        TabConfig config;

        if (file.exists()) {
            try (Reader reader = new FileReader(file, StandardCharsets.UTF_8)) {
                config = GSON.fromJson(reader, TabConfig.class);
            } catch (Exception e) {
                System.err.println("Error loading config: " + e.getMessage());
                config = new TabConfig();
                save(config);
            }
        } else {
            config = new TabConfig();
            save(config);
        }

        return config;
    }

    private static void save(TabConfig config) {
        File file = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE).toFile();
        
        try (Writer writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            GSON.toJson(config, writer);
        } catch (Exception e) {
            System.err.println("Error saving config: " + e.getMessage());
        }
    }
} 