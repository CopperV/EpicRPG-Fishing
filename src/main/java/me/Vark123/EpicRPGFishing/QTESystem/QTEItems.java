package me.Vark123.EpicRPGFishing.QTESystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;

import lombok.Getter;
import me.Vark123.EpicRPGFishing.Tools.Pair;

public class QTEItems {

	@Getter
	protected static List<Material> materials = new ArrayList<>(Arrays.asList(
			Material.GOLDEN_SWORD,
			Material.CHAINMAIL_HELMET,
			Material.LEATHER_CHESTPLATE,
			Material.IRON_LEGGINGS,
			Material.DIAMOND_BOOTS,
			Material.BOW,
			Material.CROSSBOW,
			Material.SHIELD,
			Material.TOTEM_OF_UNDYING,
			Material.TRIDENT,
			Material.ARROW,
			Material.NETHERITE_AXE,
			Material.EMERALD,
			Material.DIAMOND,
			Material.IRON_INGOT,
			Material.GOLD_INGOT,
			Material.DIAMOND_PICKAXE,
			Material.GOLDEN_HOE,
			Material.IRON_SHOVEL));
	
	public static Pair<Material, Material> getQTEItems() {
		Random rand = new Random();
		int choice1 = rand.nextInt(materials.size());
		Material m1 = materials.get(choice1);
		int choice2;
		do {
			choice2 = rand.nextInt(materials.size());
		} while(choice1 == choice2);
		Material m2 = materials.get(choice2);
		return new Pair<>(m1, m2);
	}
	
}
