package palitraq.com.handler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.text.LiteralText;
import palitraq.com.config.TabConfig;

public class PlayerJoinHandler {
    private static MinecraftServer server;
    private static final long UPDATE_INTERVAL = 1000L;
    private static Thread updateThread;
    
    public static void init() {
        // Регистрируем обработчик входа игрока
        ServerPlayConnectionEvents.JOIN.register((handler, sender, serverInstance) -> {
            server = serverInstance;
            if (updateThread == null || !updateThread.isAlive()) {
                startUpdateThread();
            }
            updatePlayer(handler.player);
        });
    }

    private static void startUpdateThread() {
        updateThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (server != null && !server.isStopped()) {
                        updateAllPlayers();
                    }
                    Thread.sleep(UPDATE_INTERVAL);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "TAB-Update-Thread");
        updateThread.setDaemon(true);
        updateThread.start();
    }

    private static void updateAllPlayers() {
        if (server == null || server.getPlayerManager() == null) return;
        
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            updatePlayer(player);
        }
    }
    
    private static void updatePlayer(ServerPlayerEntity player) {
        if (player == null || player.networkHandler == null || server == null) return;
        
        try {
            TabConfig config = TabConfig.load();
            if (!config.enabled) return;

            // Получаем актуальные значения
            String players = String.valueOf(server.getCurrentPlayerCount());
            String tps = String.format("%.1f", calculateTPS());

            // Заменяем плейсхолдеры на значения с сохранением форматирования
            String headerText = config.header
                .replace("%PLAYERS%", players)
                .replace("%TPS%", tps);
                
            String footerText = config.footer
                .replace("%PLAYERS%", players)
                .replace("%TPS%", tps);

            // Создаем компоненты текста с поддержкой цветовых кодов
            MutableText header = new LiteralText(headerText);
            MutableText footer = new LiteralText(footerText);

            // Отправляем пакет
            PlayerListHeaderS2CPacket packet = new PlayerListHeaderS2CPacket(header, footer);
            player.networkHandler.sendPacket(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double calculateTPS() {
        if (server == null) return 20.0;
        
        try {
            double mspt = getMSPT();
            return Math.min(20.0, 1000.0 / Math.max(mspt, 50.0));
        } catch (Exception e) {
            return 20.0;
        }
    }

    private static double getMSPT() {
        long[] tickTimes = server.lastTickLengths;
        if (tickTimes == null || tickTimes.length == 0) return 50.0;

        long sum = 0L;
        for (long value : tickTimes) {
            sum += value;
        }
        return (sum / tickTimes.length) * 1.0E-6D;
    }
} 