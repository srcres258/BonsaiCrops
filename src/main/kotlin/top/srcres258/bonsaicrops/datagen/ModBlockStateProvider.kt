package top.srcres258.bonsaicrops.datagen

import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import top.srcres258.bonsaicrops.BonsaiCrops

class ModBlockStateProvider(
    output: PackOutput,
    exFileHelper: ExistingFileHelper
) : BlockStateProvider(output, BonsaiCrops.MOD_ID, exFileHelper) {
    override fun registerStatesAndModels() {}
}