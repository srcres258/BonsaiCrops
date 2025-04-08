package top.srcres258.bonsaicrops.datagen

import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block

object ModTextureMappings {
    fun bonsaiPot(block: Block): TextureMapping =
        bonsaiPot(TextureMapping.getBlockTexture(block))

    fun bonsaiPot(texture: ResourceLocation): TextureMapping =
        TextureMapping().put(ModTextureSlots.TEXTURE_0, texture)
}