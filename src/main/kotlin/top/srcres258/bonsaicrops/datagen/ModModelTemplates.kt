package top.srcres258.bonsaicrops.datagen

import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.ModelTemplates
import top.srcres258.bonsaicrops.BonsaiCrops

object ModModelTemplates {
    val BONSAI_POT: ModelTemplate = ModelTemplates.create("${BonsaiCrops.MOD_ID}:bonsai_pot_base",
        ModTextureSlots.TEXTURE_0)
}