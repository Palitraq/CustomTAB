package palitraq.com;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import palitraq.com.config.TabConfig;
import palitraq.com.handler.PlayerJoinHandler;

public class CustomTAB implements ModInitializer {
	public static final String MOD_ID = "customtab";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Identifier PLAYER_JOIN_PACKET_ID = new Identifier(MOD_ID, "player_join");
	public static final TabConfig CONFIG = TabConfig.load();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("CustomTAB initialized!");
		LOGGER.info("Configuration loaded: enabled={}, header={}, footer={}", 
			CONFIG.enabled, CONFIG.header, CONFIG.footer);
		
		// Загружаем конфигурацию при старте сервера
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			TabConfig.load();
		});
		
		PlayerJoinHandler.init();
	}
}