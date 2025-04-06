package top.srcres258.bonsaicrops.block.entity.renderer

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.BlockPos
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.CropBlock
import thedarkcolour.kotlinforforge.neoforge.forge.use
import top.srcres258.bonsaicrops.block.entity.custom.BonsaiPotBlockEntity

class BonsaiPotBlockEntityRenderer(
    private val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<BonsaiPotBlockEntity> {
    override fun render(
        blockEntity: BonsaiPotBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val itemRenderer = context.itemRenderer
        val blockRenderer = context.blockRenderDispatcher
        val stack = blockEntity.cropInventory.getStackInSlot(0)

        val level = blockEntity.level
        if (level != null && !stack.isEmpty) {
            val item = stack.item
            if (item is BlockItem && item.block is CropBlock) {
                val block = item.block as CropBlock
                val scalingFactor = blockEntity.progress.toFloat() / blockEntity.maxProgress.toFloat() * (12F / 16F)

                poseStack.use {
                    poseStack.translate(0.5F, 0.0F, 0.5F)
                    poseStack.scale(scalingFactor, scalingFactor, scalingFactor)
                    poseStack.translate(0.0F, 0.15F / scalingFactor, 0.0F)
                    poseStack.translate(-0.5F, 0.0F, -0.5F)

                    val maxAgeState = block.getStateForAge(block.maxAge)
                    blockRenderer.renderBatched(
                        maxAgeState,
                        blockEntity.blockPos,
                        level,
                        poseStack,
                        bufferSource.getBuffer(RenderType.CUTOUT),
                        false,
                        level.random
                    )
                }
            } else {
                poseStack.use {
                    poseStack.translate(0.5F, 0.15F, 0.5F)
                    poseStack.scale(0.5F, 0.5F, 0.5F)

                    itemRenderer.renderStatic(
                        stack,
                        ItemDisplayContext.FIXED,
                        calculateLightLevel(level, blockEntity.blockPos),
                        OverlayTexture.NO_OVERLAY,
                        poseStack,
                        bufferSource,
                        level,
                        1
                    )
                }
            }
        }
    }
}

private fun calculateLightLevel(level: BlockAndTintGetter, pos: BlockPos): Int {
    val blockLight = level.getBrightness(LightLayer.BLOCK, pos)
    val skyLight = level.getBrightness(LightLayer.SKY, pos)
    return LightTexture.pack(blockLight, skyLight)
}