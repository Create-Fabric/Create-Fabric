package com.simibubi.create.compat.rei;

import java.util.Arrays;
import java.util.stream.Stream;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;

import io.github.fabricators_of_create.porting_lib.mixin.accessors.common.accessor.ItemValueAccessor;
import io.github.fabricators_of_create.porting_lib.mixin.accessors.common.accessor.TagValueAccessor;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.Block;

public final class ToolboxColoringRecipeMaker {

	// From JEI's ShulkerBoxColoringRecipeMaker
	public static Stream<CraftingRecipe> createRecipes() {
		String group = "create.toolbox.color";
		ItemStack baseShulkerStack = AllBlocks.TOOLBOXES.get(DyeColor.BROWN)
			.asStack();
		Ingredient baseShulkerIngredient = Ingredient.of(baseShulkerStack);

		return Arrays.stream(DyeColor.values())
			.filter(dc -> dc != DyeColor.BROWN)
			.map(color -> {
				DyeItem dye = DyeItem.byColor(color);
				ItemStack dyeStack = new ItemStack(dye);
				TagKey<Item> colorTag = color.getTag();
				Ingredient.Value dyeList = ItemValueAccessor.createItemValue(dyeStack);
				Ingredient.Value colorList = TagValueAccessor.createTagValue(colorTag);
				Stream<Ingredient.Value> colorIngredientStream = Stream.of(dyeList, colorList);
				Ingredient colorIngredient = Ingredient.fromValues(colorIngredientStream);
				NonNullList<Ingredient> inputs =
					NonNullList.of(Ingredient.EMPTY, baseShulkerIngredient, colorIngredient);
				Block coloredShulkerBox = AllBlocks.TOOLBOXES.get(color)
					.get();
				ItemStack output = new ItemStack(coloredShulkerBox);
				ResourceLocation id = Create.asResource(group + "." + output.getDescriptionId());
				return new ShapelessRecipe(id, group, CraftingBookCategory.MISC, output, inputs);
			});
	}

	private ToolboxColoringRecipeMaker() {}

}
