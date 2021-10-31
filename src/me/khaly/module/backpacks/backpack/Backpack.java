package me.khaly.module.backpacks.backpack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.khaly.core.KhalyCore;
import me.khaly.core.builder.ItemBuilder;
import me.khaly.core.libraries.YamlFile;
import me.khaly.core.user.User;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;

public class Backpack {
	
	private UUID uuid;
	private String title;
	private int rows;
	private Map<Integer, ItemStack> items;
	
	private String path = "backpack";
	
	public Backpack(Player player, String title, int rows) {
		this.uuid = player.getUniqueId();
		this.title = title;
		this.rows = rows;
		
		load();
	}
	
	public Backpack(Player player, String title) {
		this(player, title, 1);
	}
		
	public String getTitle() {
		return title;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getSlots() {
		return (rows * 9);
	}
	
	public Player getOwner() {
		return Bukkit.getPlayer(uuid);
	}
	
	public Map<Integer, ItemStack> getItems() {
		return items;
	}
	
	public void open() {
		GUIBuilder builder = GUIBuilder.builder("§7" + title, rows);
		Player player = getOwner();
		
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i) == null || items.get(i).getType() == Material.AIR) {
				continue;
			}
			
			builder.addItem(ItemClickable.builder()
					.setItemStack(items.get(i))
					
					.build(), i);
		}
		
		builder.closeAction((event) -> {
			Inventory inventory = event.getInventory();
			
			if(!items.isEmpty()) {
				items.clear();
			}
			
			for(int i = 0; i < inventory.getSize(); i++) {
				ItemStack item = inventory.getItem(i);
				if(item == null || item.getType() == Material.AIR) {
					continue;
				}

				items.put(i, item);
			}
		});
		
		player.openInventory(builder.build());
	}
	
	public void save() {
		Player player = getOwner();
		User user = KhalyCore.getInstance().getUser(player);
		YamlFile file = user.getFile();
		
		for(Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
			int slot = entry.getKey();
			ItemStack item = entry.getValue();
			
			if(item == null || item.getType() == Material.AIR) {
				continue;
			}
			
			file.set(path + "." + slot, ItemBuilder.toBase64(item));
		}
		
		file.save();
	}
	
	public void load() {
		Player player = getOwner();
		User user = KhalyCore.getInstance().getUser(player);
		YamlFile file = user.getFile();
		
		this.items = new ConcurrentHashMap<>();
		
		if(file.contains(path)) {
			for(String slot : file.getConfigurationSection(path).getKeys(false)) {
				String item = file.getString(path + "." + slot);
				this.items.put(Integer.valueOf(slot), ItemBuilder.fromBase64(item));
			}
		}
	}
	
}
