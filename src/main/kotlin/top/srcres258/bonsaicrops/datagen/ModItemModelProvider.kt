package top.srcres258.bonsaicrops.datagen

import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import top.srcres258.bonsaicrops.BonsaiCrops

class ModItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(output, BonsaiCrops.MOD_ID, existingFileHelper) {
    override fun registerModels() {}
}