package br.com.playdreamcraft.c4explosions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class C4Explosions extends JavaPlugin implements Listener
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
		HandlerList.unregisterAll((Plugin) this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@EventHandler
	public void dropItem(PlayerDropItemEvent event)
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

	@EventHandler
	public void playerInteract(PlayerInteractEvent event)
	{	
		String playerName = event.getPlayer().getName();
		
		if(!bombs.containsKey(playerName))
			return;
		
		
		Action action = event.getAction();
		ItemStack item = event.getItem();

		if(item.getType() != Material.REDSTONE_TORCH_ON || item == null )
			return;

		if(action == Action.PHYSICAL)
			return;
		Float power = null;
		if(item.getAmount() > 0 && item.getAmount() <= 5)
		{
			power = (float) item.getAmount();
		}else if(item.getAmount() > 0)
		{
			power = 5F;
		}				
		
		for(Entity entity : bombs.get(playerName))
		{
			System.out.println(entity);
			createExplosion(entity.getLocation(), power);
		}			
			bombs.remove(playerName);				
	}

	public void createExplosion(Location loc, float power)
	{
		loc.getWorld().createExplosion(loc, power, true);
	}

}
