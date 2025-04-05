package top.srcres258.bonsaicrops.block

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister
import top.srcres258.bonsaicrops.BonsaiCrops
import top.srcres258.bonsaicrops.block.custom.BonsaiPotBlock
import top.srcres258.bonsaicrops.item.ModItems

object ModBlocks {
    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(BonsaiCrops.MOD_ID)

    val BONSAI_POT: DeferredBlock<Block> = registerBlockWithItem("bonsai_pot") {
        BonsaiPotBlock(
            false,
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .sound(SoundType.STONE)
                .noOcclusion()
        )
    }
    val HOPPING_BONSAI_POT: DeferredBlock<Block> = registerBlockWithItem("hopping_bonsai_pot") {
        BonsaiPotBlock(
            true,
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .sound(SoundType.STONE)
                .noOcclusion()
        )
    }

    private fun <T : Block> registerBlock(name: String, block: () -> T) =
        BLOCKS.register(name, block)

    private fun <T : Block> registerBlockWithItem(name: String, block: () -> T) =
        BLOCKS.register(name, block).also { deferredBlock ->
            registerBlockItem(name, deferredBlock)
        }

    private fun <T : Block> registerBlockItem(name: String, block: DeferredBlock<T>) =
        ModItems.ITEMS.register(name) { ->
            BlockItem(block.get(), Item.Properties())
        }

    fun register(eventBus: IEventBus) {
        BLOCKS.register(eventBus)
    }
}