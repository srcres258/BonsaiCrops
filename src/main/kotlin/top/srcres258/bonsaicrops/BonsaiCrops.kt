package top.srcres258.bonsaicrops

import net.neoforged.fml.common.Mod
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import top.srcres258.bonsaicrops.block.ModBlocks
import top.srcres258.bonsaicrops.item.ModCreativeModeTabs
import top.srcres258.bonsaicrops.item.ModItems

@Mod(BonsaiCrops.MOD_ID)
object BonsaiCrops {
    const val MOD_ID = "bonsaicrops"

    val LOGGER: Logger = LogManager.getLogger(MOD_ID)

    init {
        LOGGER.log(Level.INFO, "Loading $MOD_ID...")

        ModCreativeModeTabs.register(MOD_BUS)
        ModBlocks.register(MOD_BUS)
        ModItems.register(MOD_BUS)
    }
}