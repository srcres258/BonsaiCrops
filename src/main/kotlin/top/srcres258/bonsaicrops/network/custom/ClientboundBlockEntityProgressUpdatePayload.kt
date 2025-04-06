package top.srcres258.bonsaicrops.network.custom

import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.PacketDistributor
import top.srcres258.bonsaicrops.BonsaiCrops

data class ClientboundBlockEntityProgressUpdatePayload(
    val blockPosX: Int,
    val blockPosY: Int,
    val blockPosZ: Int,
    val progress: Int,
    val maxProgress: Int
) : CustomPacketPayload {
    companion object {
        val TYPE = CustomPacketPayload.Type<ClientboundBlockEntityProgressUpdatePayload>(
            ResourceLocation.fromNamespaceAndPath(BonsaiCrops.MOD_ID, "block_entity_progress_update")
        )

        val STREAM_CODEC: StreamCodec<ByteBuf, ClientboundBlockEntityProgressUpdatePayload> =
            StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                { it.blockPosX },
                ByteBufCodecs.VAR_INT,
                { it.blockPosY },
                ByteBufCodecs.VAR_INT,
                { it.blockPosZ },
                ByteBufCodecs.VAR_INT,
                { it.progress },
                ByteBufCodecs.VAR_INT,
                { it.maxProgress },
                ::ClientboundBlockEntityProgressUpdatePayload
            )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}

fun sendBlockEntityProgressUpdatePayloadToAllPlayers(blockPos: BlockPos, progress: Int, maxProgress: Int) {
    PacketDistributor.sendToAllPlayers(ClientboundBlockEntityProgressUpdatePayload(
        blockPos.x, blockPos.y, blockPos.z, progress, maxProgress
    ))
}