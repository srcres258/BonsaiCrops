package top.srcres258.bonsaicrops.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import top.srcres258.bonsaicrops.block.ModBlocks

class ModBlockLootTableProvider(
    registries: HolderLookup.Provider
) : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), registries) {
    override fun generate() {
        dropSelf(ModBlocks.BONSAI_POT.get())
        dropSelf(ModBlocks.HOPPING_BONSAI_POT.get())
    }

    override fun getKnownBlocks(): Iterable<Block> = Iterable {
        ModBlocks.BLOCKS.entries.map { it.value() }.iterator()
    }
}