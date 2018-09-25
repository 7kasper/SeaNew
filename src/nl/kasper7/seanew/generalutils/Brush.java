package nl.kasper7.seanew.generalutils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Brush {

	private Material key;
	private BiFunction<Brush, Player, Boolean> rightClickFunc;
	private BiFunction<Brush, Player, Boolean> leftClickFunc;
	private BiFunction<Brush, Player, Boolean> dropFunc;
	private BiFunction<Brush, Player, Boolean> swapFunc;
	private Map<String, Object> metadata = new HashMap<>();

	public Brush(Material key, 
			Consumer<Brush> initFunc, 
			BiFunction<Brush, Player, Boolean> rightClickFunc, 
			BiFunction<Brush, Player, Boolean> leftClickFunc, 
			BiFunction<Brush, Player, Boolean> dropFunc, 
			BiFunction<Brush, Player, Boolean> swapFunc) 
	{
		this.key = key;
		this.rightClickFunc = rightClickFunc;
		this.leftClickFunc = leftClickFunc;
		this.dropFunc = dropFunc;
		this.swapFunc = swapFunc;
		if (initFunc != null) {
			initFunc.accept(this);
		}
	}

	public boolean rightClick(Player clicker) {
		if (rightClickFunc != null) {
			return rightClickFunc.apply(this, clicker);
		}
		return false;
	}

	public boolean leftClick(Player clicker) {
		if (leftClickFunc != null) {
			return leftClickFunc.apply(this, clicker);
		}
		return false;
	}

	public boolean swap(Player swapper) {
		if (swapFunc != null) {
			return swapFunc.apply(this, swapper);
		}
		return false;
	}

	public boolean drop(Player dropper) {
		if (dropFunc != null) {
			return dropFunc.apply(this, dropper);
		}
		return false;
	}

	public Material getKey() {
		return key;
	}

	public void setKey(Material key) {
		this.key = key;
	}

	public void setMeta(String key, Object value) {
		metadata.put(key, value);
	}

	public boolean hasMeta(String key) {
		return metadata.containsKey(key);
	}

	public void unsetMeta(String key) {
		metadata.remove(key);
	}

	public Object getMeta(String key) {
		return metadata.get(key);
	}

	public int getIntMeta(String key) {
		return ((AtomicInteger) getMeta(key)).get();
	}

	public void setIntMeta(String key, int value) {
		((AtomicInteger) getMeta(key)).set(value);
	}

	public BiFunction<Brush, Player, Boolean> getRightClickFunc() {
		return rightClickFunc;
	}

	public void setRightClickFunc(BiFunction<Brush, Player, Boolean> rightClickFunc) {
		this.rightClickFunc = rightClickFunc;
	}

	public BiFunction<Brush, Player, Boolean> getLeftClickFunc() {
		return leftClickFunc;
	}

	public void setLeftClickFunc(BiFunction<Brush, Player, Boolean> leftClickFunc) {
		this.leftClickFunc = leftClickFunc;
	}

	public BiFunction<Brush, Player, Boolean> getDropFunc() {
		return dropFunc;
	}

	public void setDropFunc(BiFunction<Brush, Player, Boolean> dropFunc) {
		this.dropFunc = dropFunc;
	}

	public BiFunction<Brush, Player, Boolean> getSwapFunc() {
		return swapFunc;
	}

	public void setSwapFunc(BiFunction<Brush, Player, Boolean> swapFunc) {
		this.swapFunc = swapFunc;
	}

}
