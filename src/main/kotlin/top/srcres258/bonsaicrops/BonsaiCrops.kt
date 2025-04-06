package top.srcres258.bonsaicrops

import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import top.srcres258.bonsaicrops.block.ModBlocks
import top.srcres258.bonsaicrops.block.entity.ModBlockEntityTypes
import top.srcres258.bonsaicrops.block.entity.renderer.BonsaiPotBlockEntityRenderer
import top.srcres258.bonsaicrops.item.ModCreativeModeTabs
import top.srcres258.bonsaicrops.item.ModItems
import top.srcres258.bonsaicrops.network.ModNetworks

@Mod(BonsaiCrops.MOD_ID)
object BonsaiCrops {
    const val MOD_ID = "bonsaicrops"

    val LOGGER: Logger = LogManager.getLogger(MOD_ID)

    init {
        LOGGER.log(Level.INFO, "Loading $MOD_ID...")

        MOD_BUS.addListener(::registerPayloadHandlers)

        ModCreativeModeTabs.register(MOD_BUS)
        ModBlocks.register(MOD_BUS)
        ModItems.register(MOD_BUS)
        ModBlockEntityTypes.register(MOD_BUS)
    }

    fun registerPayloadHandlers(event: RegisterPayloadHandlersEvent) {
        ModNetworks.registerPayloadHandlers(event.registrar("1"))
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
    object ClientModEvents {
        @SubscribeEvent
        fun onRegisterRenderers(event: EntityRenderersEvent.RegisterRenderers) {
            event.registerBlockEntityRenderer(ModBlockEntityTypes.BONSAI_POT_BLOCK_ENTITY.get(),
                ::BonsaiPotBlockEntityRenderer)
            event.registerBlockEntityRenderer(ModBlockEntityTypes.HOPPING_BONSAI_POT_BLOCK_ENTITY.get(),
                ::BonsaiPotBlockEntityRenderer)
        }
    }
}