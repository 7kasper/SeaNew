package nl.kasper7.seanew;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import nl.kasper7.seanew.help.SeaNewCmd;
import nl.kasper7.seanew.popbrush.PopBrushCmd;
import nl.kasper7.seanew.popbrush.util.ChunkUtils;

public class SeaNew extends JavaPlugin {

	public SeaNew() {
		super();
		try {
			Class.forName(ChunkUtils.class.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Bukkit.shutdown();
		}
	}

	@Override
	public void onEnable() {
		getCommand("seanew").setExecutor(new SeaNewCmd());
		getCommand("popbrush").setExecutor(new PopBrushCmd());
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "SeaNew started!");
	}

	@Override
	public void onDisable() {
		
	}

}
