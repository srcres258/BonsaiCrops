package top.srcres258.bonsaicrops.datagen

import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper
import top.srcres258.bonsaicrops.BonsaiCrops
import top.srcres258.bonsaicrops.block.ModBlocks

class ModBlockStateProvider(
    output: PackOutput,
    exFileHelper: ExistingFileHelper
) : BlockStateProvider(output, BonsaiCrops.MOD_ID, exFileHelper) {
    override fun registerStatesAndModels() {
        blockWithItem(ModBlocks.BONSAI_POT.get(),
            models().getExistingFile(modLoc("block/bonsai_pot")))
        blockWithItem(
            ModBlocks.HOPPING_BONSAI_POT.get(),
            models().withExistingParent("hopping_bonsai_pot", modLoc("block/bonsai_pot"))
                .texture("0", modLoc("block/hopping_bonsai_pot"))
        )
    }

    private fun blockWithItem(block: Block, model: ModelFile = cubeAll(block)) {
        simpleBlockWithItem(block, model)
    }
}