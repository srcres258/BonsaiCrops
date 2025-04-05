package top.srcres258.bonsaicrops.block

import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import top.srcres258.bonsaicrops.BonsaiCrops

object ModBlocks {
    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(BonsaiCrops.MOD_ID)

    fun register(eventBus: IEventBus) {
        BLOCKS.register(eventBus)
    }
}