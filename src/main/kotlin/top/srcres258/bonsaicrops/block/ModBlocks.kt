package top.srcres258.bonsaicrops.block

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister
import top.srcres258.bonsaicrops.BonsaiCrops
import top.srcres258.bonsaicrops.block.custom.BonsaiPotBlock
import top.srcres258.bonsaicrops.item.ModItems

object ModBlocks {
    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(BonsaiCrops.MOD_ID)

    val BONSAI_POT: DeferredBlock<Block> = registerBlockWithItem(
        "bonsai_pot",
        { props -> BonsaiPotBlock(false, props) },
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .instrument(NoteBlockInstrument.BASS)
            .strength(0.5F)
            .sound(SoundType.STONE)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
    )
    val HOPPING_BONSAI_POT: DeferredBlock<Block> = registerBlockWithItem(
        "hopping_bonsai_pot",
        { props -> BonsaiPotBlock(true, props) },
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.STONE)
            .noOcclusion()
    )

    private fun <T : Block> registerBlock(
        name: String,
        blockFunc: (BlockBehaviour.Properties) -> T,
        blockProps: BlockBehaviour.Properties
    ) = BLOCKS.registerBlock(name, blockFunc, blockProps)

    private fun <T : Block> registerBlockWithItem(
        name: String,
        blockFunc: (BlockBehaviour.Properties) -> T,
        blockProps: BlockBehaviour.Properties
    ) = BLOCKS.registerBlock(name, blockFunc, blockProps).also { deferredBlock ->
            registerBlockItem(name, deferredBlock)
        }

    private fun <T : Block> registerBlockItem(name: String, block: DeferredBlock<T>) =
        ModItems.ITEMS.registerItem(
            name,
            { props -> BlockItem(block.get(), props) },
            Item.Properties().useBlockDescriptionPrefix()
        )

    fun register(eventBus: IEventBus) {
        BLOCKS.register(eventBus)
    }
}