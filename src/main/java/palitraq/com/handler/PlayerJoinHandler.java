package palitraq.com.handler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import palitraq.com.CustomTAB;
import palitraq.com.config.TabConfig;

public class PlayerJoinHandler {
    private static MinecraftServer server;
    
    public static void init() {
        // Сохраняем ссылку на сервер при его запуске
        ServerLifecycleEvents.SERVER_STARTED.register(serverInstance -> {
            server = serverInstance;
        });
        
        // Регистрируем обработчик входа игрока
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (TabConfig.load().enabled) {
                sendTabUpdate(handler.player);
            }
        });
        
        // Обновляем таб каждые 20 тиков (1 секунда)
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Thread updateThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000); // 1 секунда
                        if (server.getPlayerManager() != null) {
                            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                                if (TabConfig.load().enabled) {
                                    sendTabUpdate(player);
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
            updateThread.setDaemon(true);
            updateThread.start();
        });
    }
    
    public static void sendTabUpdate(ServerPlayerEntity player) {
        if (server == null) return;
        
        TabConfig config = TabConfig.load();
        PlayerListHeaderS2CPacket packet = new PlayerListHeaderS2CPacket(
            Text.literal(config.formatHeader(server)),
            Text.literal(config.footer)
        );
        player.networkHandler.sendPacket(packet);
    }
} 