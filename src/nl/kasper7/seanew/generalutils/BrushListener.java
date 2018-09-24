package nl.kasper7.seanew.generalutils;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class BrushListener implements Listener {

	@EventHandler(ignoreCancelled=false, priority=EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		AtomicBoolean cancelled = new AtomicBoolean(false); //Interact acts weird with cancelled :/
		Brushes.get(event.getPlayer()).stream().filter(brush -> brush.getKey() == event.getMaterial())
		.forEach(brush -> {
			if (!cancelled.get() && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
				cancelled.set(brush.leftClick(event.getPlayer()));
			} else if (!cancelled.get() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				cancelled.set(brush.rightClick(event.getPlayer()));
			}
		});
		event.setCancelled(cancelled.get() && (event.getAction() != Action.LEFT_CLICK_AIR || event.getAction() != Action.RIGHT_CLICK_AIR));
	}

	@EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
	public void onPlayerSwapItem(PlayerSwapHandItemsEvent event) {
		Brushes.get(event.getPlayer()).stream().filter(brush -> brush.getKey() == event.getOffHandItem().getType())
		.forEach(brush -> {
			if (!event.isCancelled()) {
				event.setCancelled(brush.swap(event.getPlayer()));
			}
		});
	}

	@EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Brushes.get(event.getPlayer()).stream().filter(brush -> brush.getKey() == event.getItemDrop().getItemStack().getType())
		.forEach(brush -> {
			if (!event.isCancelled()) {
				event.setCancelled(brush.drop(event.getPlayer()));
			}
		});
	}

}
