package top.srcres258.bonsaicrops.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import top.srcres258.bonsaicrops.BonsaiCrops
import top.srcres258.bonsaicrops.block.ModBlocks
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    registries: HolderLookup.Provider,
    output: RecipeOutput
) : RecipeProvider(registries, output) {
    class Runner(
        packOutput: PackOutput,
        registries: CompletableFuture<HolderLookup.Provider>
    ) : RecipeProvider.Runner(packOutput, registries) {
        override fun createRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput): RecipeProvider =
            ModRecipeProvider(registries, output)

        override fun getName(): String = "${BonsaiCrops.MOD_ID} recipe provider"
    }

    override fun buildRecipes() {
        shaped(RecipeCategory.MISC, ModBlocks.BONSAI_POT)
            .pattern("ABA")
            .pattern(" A ")
            .define('A', Items.BRICK)
            .define('B', Blocks.DIRT)
            .unlockedBy("has_brick", has(Items.BRICK))
            .save(output)
        shaped(RecipeCategory.MISC, ModBlocks.HOPPING_BONSAI_POT)
            .pattern("ABA")
            .pattern(" A ")
            .pattern(" C ")
            .define('A', Items.BRICK)
            .define('B', Blocks.DIRT)
            .define('C', Blocks.HOPPER)
            .unlockedBy("has_hopper", has(Items.HOPPER))
            .save(output)
        shapeless(RecipeCategory.MISC, ModBlocks.HOPPING_BONSAI_POT)
            .requires(ModBlocks.BONSAI_POT)
            .requires(Blocks.HOPPER)
            .unlockedBy("has_bonsai_pot", has(ModBlocks.BONSAI_POT))
            .save(output, "${BonsaiCrops.MOD_ID}:${ModBlocks.HOPPING_BONSAI_POT.id.path}_from_" +
                    ModBlocks.BONSAI_POT.id.path)
    }
}