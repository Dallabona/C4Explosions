package br.com.playdreamcraft.c4explosions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class C4Explosions extends org.bukkit.plugin.java.JavaPlugin implements org.bukkit.event.Listener
{
	private static Map<String,Set<Entity>> bombs = new HashMap<>();

	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable()
	{
		org.bukkit.event.HandlerList.unregisterAll((Plugin) this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@org.bukkit.event.EventHandler
	public void dropItem(org.bukkit.event.player.PlayerDropItemEvent event)
	{
		Player player = event.getPlayer();
		String playerName = player.getName();
		Entity entity = event.getItemDrop();

		if(bombs.containsKey(playerName))
		{
			bombs.get(playerName).add(entity);
		}else
		{
			Set entities = new HashSet<>();
			entities.add(entity);
			bombs.put(playerName,entities );
		}			
	}

	@org.bukkit.event.EventHandler
	public void playerInteract(org.bukkit.event.player.PlayerInteractEvent event)
	{	
		String playerName = event.getPlayer().getName();
		
		if(!bombs.containsKey(playerName))
			return;
		
		Action action = event.getAction();
		ItemStack item = event.getItem();

		if(item.getType() != org.bukkit.Material.REDSTONE_TORCH_ON || item == null )
			return;

		if(action == org.bukkit.event.block.Action.PHYSICAL)
			return;
		Float power = Math.min(Math.max(item.getAmount(), 1), 5);				
		
		for(Entity entity : bombs.get(playerName))
		{
			System.out.println(entity);
			loc.getWorld().createExplosion(entity.getLocation(), power, true);
		}			
		bombs.remove(playerName);				
	}
}
