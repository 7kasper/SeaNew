package nl.kasper7.seanew.generalutils;

import org.bukkit.Bukkit;

public class ReflectUtils {

	private static String craftVersion;
	static {
		try {
			craftVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		} catch (Exception e) {
			craftVersion = "ohCrap"; 
		}
	}

	public static Class<?> getNMSClazz(String name) throws ClassNotFoundException {
		return Class.forName("net.minecraft.server." + craftVersion + "." + name);
	}

	public static Class<?> getCraftBukkitClazz(String name) throws ClassNotFoundException {
		return Class.forName("org.bukkit.craftbukkit." + craftVersion + "." + name);
	}
}
