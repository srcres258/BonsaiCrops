package top.srcres258.bonsaicrops.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.DataMapProvider
import java.util.concurrent.CompletableFuture

class ModDataMapProvider(
    packOutput: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>
) : DataMapProvider(packOutput, lookupProvider) {
    override fun gather(provider: HolderLookup.Provider) {}
}