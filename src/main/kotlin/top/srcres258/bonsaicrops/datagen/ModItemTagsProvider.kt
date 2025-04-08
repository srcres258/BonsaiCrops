package top.srcres258.bonsaicrops.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.world.level.block.Block
import top.srcres258.bonsaicrops.BonsaiCrops
import java.util.concurrent.CompletableFuture

class ModItemTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    blockTags: CompletableFuture<TagLookup<Block>>
) : ItemTagsProvider(output, lookupProvider, blockTags, BonsaiCrops.MOD_ID) {
    override fun addTags(provider: HolderLookup.Provider) {}
}