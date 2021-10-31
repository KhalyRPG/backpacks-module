package me.khaly.module.backpacks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.khaly.core.api.events.UserJoinEvent;
import me.khaly.core.module.Module;
import me.khaly.core.user.User;
import me.khaly.module.backpacks.backpack.Backpack;

public class BackpacksModule extends Module implements Listener, CommandClass {
	
	private Map<UUID, Backpack> backpacks;
	
	public BackpacksModule() {
		super("Backpacks", "backpacks", 0.1F);
		
		this.setAuthor("Khaly");
		this.setPersistent(true);
	}

	@Override
	public void load() {
		this.backpacks = new HashMap<>();
	}
	
	@Command(names = "backpack", desc = "Abrir mochila personal")
	public void backpackCommand(@Sender Player player) {
		UUID uuid = player.getUniqueId();
		
		if(backpacks.containsKey(uuid)) {
			Backpack backpack = backpacks.get(uuid);
			
			backpack.open();
			player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.MASTER, 1, 1);
			
		} else {
			player.sendMessage("§cAlgo salió mal...");
		}
	}
	
	@EventHandler
	public void onJoin(UserJoinEvent event) {
		User user = event.getUser();
		UUID uuid = user.getBukkitPlayer().getUniqueId();
		Backpack backpack = new Backpack(user.getBukkitPlayer(), "Mochila", 2);
		
		backpacks.put(uuid, backpack);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if(backpacks.containsKey(uuid)) {
			Backpack backpack = backpacks.remove(uuid);
			backpack.save();
		}
	}
	
	public Map<UUID, Backpack> getBackpacks() {
		return backpacks;
	}
}
