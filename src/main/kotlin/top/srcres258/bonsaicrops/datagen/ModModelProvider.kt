package top.srcres258.bonsaicrops.datagen

import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.core.Holder
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import top.srcres258.bonsaicrops.BonsaiCrops
import top.srcres258.bonsaicrops.block.ModBlocks
import java.util.stream.Stream

class ModModelProvider(
    output: PackOutput
) : ModelProvider(output, BonsaiCrops.MOD_ID) {
    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        registerBlockModels(blockModels)
        registerItemModels(itemModels)
    }

    override fun getKnownBlocks(): Stream<out Holder<Block>> =
        listOf(
            ModBlocks.BONSAI_POT,
            ModBlocks.HOPPING_BONSAI_POT
        ).stream()

    override fun getKnownItems(): Stream<out Holder<Item>> =
        emptyList<Holder<Item>>().stream()

    private fun registerBlockModels(blockModels: BlockModelGenerators) {
        bonsaiPotBlockWithItem(blockModels, ModBlocks.BONSAI_POT)
        bonsaiPotBlockWithItem(blockModels, ModBlocks.HOPPING_BONSAI_POT)
    }

    private fun registerItemModels(blockModels: ItemModelGenerators) {}

    private fun simpleBlockWithItem(blockModels: BlockModelGenerators, block: DeferredBlock<out Block>) {
        blockModels.createTrivialCube(block.get())
        blockModels.registerSimpleItemModel(block.get(),
            ResourceLocation.fromNamespaceAndPath(block.id.namespace, "block/${block.id.path}"))
    }

    private fun bonsaiPotBlockWithItem(blockModels: BlockModelGenerators, block: DeferredBlock<out Block>) {
        blockModels.createTrivialBlock(block.get(), ModTexturedModels.BONSAI_POT)
        blockModels.registerSimpleItemModel(block.get(),
            ResourceLocation.fromNamespaceAndPath(block.id.namespace, "block/${block.id.path}"))
    }
}