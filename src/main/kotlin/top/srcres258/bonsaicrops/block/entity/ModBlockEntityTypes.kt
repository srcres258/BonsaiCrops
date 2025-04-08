package top.srcres258.bonsaicrops.block.entity

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import top.srcres258.bonsaicrops.BonsaiCrops
import top.srcres258.bonsaicrops.block.ModBlocks
import top.srcres258.bonsaicrops.block.entity.custom.BonsaiPotBlockEntity

object ModBlockEntityTypes {
    val BLOCK_ENTITY_TYPES: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, BonsaiCrops.MOD_ID)

    val BONSAI_POT_BLOCK_ENTITY: DeferredHolder<BlockEntityType<*>, BlockEntityType<BonsaiPotBlockEntity>> =
        BLOCK_ENTITY_TYPES.register("bonsai_pot_block_entity") { ->
            BlockEntityType(
                { pos, state -> BonsaiPotBlockEntity(false, pos, state) },
                ModBlocks.BONSAI_POT.get()
            )
        }
    val HOPPING_BONSAI_POT_BLOCK_ENTITY: DeferredHolder<BlockEntityType<*>, BlockEntityType<BonsaiPotBlockEntity>> =
        BLOCK_ENTITY_TYPES.register("hopping_bonsai_pot_block_entity") { ->
            BlockEntityType(
                { pos, state -> BonsaiPotBlockEntity(true, pos, state) },
                ModBlocks.HOPPING_BONSAI_POT.get()
            )
        }

    fun register(eventBus: IEventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus)
    }
}