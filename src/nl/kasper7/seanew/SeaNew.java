package nl.kasper7.seanew;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import nl.kasper7.seanew.biomebrush.BiomeBrushCmd;
import nl.kasper7.seanew.generalutils.BrushListener;
import nl.kasper7.seanew.generalutils.Brushes;
import nl.kasper7.seanew.generalutils.ChunkUtils;
import nl.kasper7.seanew.help.SeaNewCmd;
import nl.kasper7.seanew.popbrush.PopBrushCmd;

public class SeaNew extends JavaPlugin {

	public SeaNew() {
		super();
		try {
			Class.forName(ChunkUtils.class.getName());
			Class.forName(Brushes.class.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Bukkit.shutdown();
		}
	}

	@Override
	public void onEnable() {
		getCommand("seanew").setExecutor(new SeaNewCmd());
		getCommand("popbrush").setExecutor(new PopBrushCmd());
		BiomeBrushCmd biomeBrushCmd = new BiomeBrushCmd();
		getCommand("biomebrush").setExecutor(biomeBrushCmd);
		getCommand("biomebrush").setTabCompleter(biomeBrushCmd);
		getServer().getPluginManager().registerEvents(new BrushListener(), this);
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "SeaNew started!");
	}

	@Override
	public void onDisable() {
		
	}

}
