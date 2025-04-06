package top.srcres258.bonsaicrops.network

import net.minecraft.core.BlockPos
import net.neoforged.neoforge.network.handling.IPayloadContext
import top.srcres258.bonsaicrops.network.custom.ClientboundBlockEntityProgressUpdatePayload
import top.srcres258.bonsaicrops.util.IProgressAccessor

object ClientboundBlockEntityProgressUpdatePayloadHandler {
    fun handleDataOnMain(data: ClientboundBlockEntityProgressUpdatePayload, context: IPayloadContext) {
        val blockPos = BlockPos(data.blockPosX, data.blockPosY, data.blockPosZ)
        val player = context.player()
        val level = player.level()
        val blockEntity = level.getBlockEntity(blockPos)
        if (blockEntity != null && blockEntity is IProgressAccessor) {
            blockEntity.progress = data.progress
            blockEntity.maxProgress = data.maxProgress
        }
    }
}