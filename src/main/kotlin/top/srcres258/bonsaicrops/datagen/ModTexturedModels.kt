package top.srcres258.bonsaicrops.datagen

import net.minecraft.client.data.models.model.TexturedModel

object ModTexturedModels {
    val BONSAI_POT: TexturedModel.Provider = TexturedModel.createDefault(ModTextureMappings::bonsaiPot,
        ModModelTemplates.BONSAI_POT)
}