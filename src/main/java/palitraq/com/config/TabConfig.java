package palitraq.com.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.nio.file.Files;

public class TabConfig {
    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .setLenient()
        .create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "customtab.json");
    private static final DecimalFormat TPS_FORMAT = new DecimalFormat("#.##");
    
    public boolean enabled = true;
    public String header = "§6§lCustomTAB\n§eTPS: %TPS%\n§aPlayers: %PLAYERS%";
    public String footer = "§e§lMade with §c§l❤";

    private static final String CONFIG_EXAMPLE = """
        {
            // Colors:
            // §0 - black            §8 - dark gray
            // §1 - dark blue       §9 - blue
            // §2 - dark green      §a - green
            // §3 - dark aqua       §b - aqua
            // §4 - dark red        §c - red
            // §5 - dark purple     §d - light purple
            // §6 - gold            §e - yellow
            // §7 - gray            §f - white
            //
            // Formatting:
            // §k - obfuscated      §n - underline
            // §l - bold            §o - italic
            // §m - strikethrough   §r - reset
            //
            // Variables:
            // %TPS% - shows server TPS
            // %PLAYERS% - shows online players count
            // Use \n for new line
            
            "enabled": true,
            
            // You can customize header here:
            "header": "§6§lServer Name\\n§bTPS: §a%TPS%\\n§ePlayers: §a%PLAYERS%",
            
            // You can customize footer here:
            "footer": "§d§lwww.example.com"
        }""";
    
    public static TabConfig load() {
        if (!CONFIG_FILE.exists()) {
            TabConfig config = new TabConfig();
            config.saveWithExample();
            return config;
        }
        
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            String content = Files.readString(CONFIG_FILE.toPath());
            // Удаляем комментарии перед парсингом JSON
            content = content.replaceAll("//.*\\n", "");
            return GSON.fromJson(content, TabConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new TabConfig();
        }
    }
    
    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveWithExample() {
        try {
            Files.writeString(CONFIG_FILE.toPath(), CONFIG_EXAMPLE);
        } catch (IOException e) {
            e.printStackTrace();
            save(); // Fallback to basic save if example fails
        }
    }
    
    public String formatHeader(MinecraftServer server) {
        String result = header;
        
        // Replace variables with actual values
        double meanTickTime = mean(server.lastTickLengths) * 1.0E-6D;
        double tps = Math.min(1000.0 / meanTickTime, 20.0);
        result = result.replace("%TPS%", TPS_FORMAT.format(tps));
        result = result.replace("%PLAYERS%", String.valueOf(server.getCurrentPlayerCount()));
        
        return result;
    }
    
    private static double mean(long[] values) {
        long sum = 0L;
        for (long v : values) {
            sum += v;
        }
        return (double) sum / (double) values.length;
    }
} 