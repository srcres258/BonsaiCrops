package top.srcres258.bonsaicrops.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider
import top.srcres258.bonsaicrops.BonsaiCrops
import java.util.concurrent.CompletableFuture

class ModGlobalLootModifierProvider(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>
) : GlobalLootModifierProvider(output, registries, BonsaiCrops.MOD_ID) {
    override fun start() {}
}