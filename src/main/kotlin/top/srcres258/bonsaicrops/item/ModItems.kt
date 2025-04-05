package top.srcres258.bonsaicrops.item

import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import top.srcres258.bonsaicrops.BonsaiCrops

object ModItems {
    val ITEMS: DeferredRegister.Items = DeferredRegister.createItems(BonsaiCrops.MOD_ID)

    fun register(eventBus: IEventBus) {
        ITEMS.register(eventBus)
    }
}