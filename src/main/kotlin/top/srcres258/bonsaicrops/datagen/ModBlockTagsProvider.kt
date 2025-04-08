package top.srcres258.bonsaicrops.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.BlockTagsProvider
import top.srcres258.bonsaicrops.BonsaiCrops
import java.util.concurrent.CompletableFuture

class ModBlockTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>
) : BlockTagsProvider(output, lookupProvider, BonsaiCrops.MOD_ID) {
    override fun addTags(provider: HolderLookup.Provider) {}
}