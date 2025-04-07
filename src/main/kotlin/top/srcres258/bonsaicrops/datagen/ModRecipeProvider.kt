package top.srcres258.bonsaicrops.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import top.srcres258.bonsaicrops.BonsaiCrops
import top.srcres258.bonsaicrops.block.ModBlocks
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>
) : RecipeProvider(output, registries) {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BONSAI_POT)
            .pattern("ABA")
            .pattern(" A ")
            .define('A', Items.BRICK)
            .define('B', Blocks.DIRT)
            .unlockedBy("has_brick", has(Items.BRICK))
            .save(recipeOutput)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.HOPPING_BONSAI_POT)
            .pattern("ABA")
            .pattern(" A ")
            .pattern(" C ")
            .define('A', Items.BRICK)
            .define('B', Blocks.DIRT)
            .define('C', Blocks.HOPPER)
            .unlockedBy("has_hopper", has(Items.HOPPER))
            .save(recipeOutput)
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.HOPPING_BONSAI_POT)
            .requires(ModBlocks.BONSAI_POT)
            .requires(Blocks.HOPPER)
            .unlockedBy("has_bonsai_pot", has(ModBlocks.BONSAI_POT))
            .save(recipeOutput, "${BonsaiCrops.MOD_ID}:${ModBlocks.HOPPING_BONSAI_POT.id.path}_from_" +
                    ModBlocks.BONSAI_POT.id.path)
    }
}