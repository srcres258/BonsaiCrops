package top.srcres258.bonsaicrops.item

import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import top.srcres258.bonsaicrops.BonsaiCrops
import top.srcres258.bonsaicrops.block.ModBlocks

object ModCreativeModeTabs {
    val CREATIVE_MODE_TABS: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BonsaiCrops.MOD_ID)

    val BONSAI_CROPS: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        CREATIVE_MODE_TABS.register("bonsai_crops_tab") { ->
            CreativeModeTab.builder()
                .title(Component.translatable("creativetab.bonsaicrops.bonsai_crops_tab"))
                .icon { ItemStack(ModBlocks.BONSAI_POT.get()) }
                .displayItems { parameters, output ->
                    output.accept(ModBlocks.BONSAI_POT)
                    output.accept(ModBlocks.HOPPING_BONSAI_POT)
                }
                .build()
        }

    fun register(eventBus: IEventBus) {
        CREATIVE_MODE_TABS.register(eventBus)
    }
}