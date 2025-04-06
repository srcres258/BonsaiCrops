package top.srcres258.bonsaicrops.network

import net.neoforged.neoforge.network.registration.PayloadRegistrar
import top.srcres258.bonsaicrops.network.custom.ClientboundBlockEntityProgressUpdatePayload

object ModNetworks {
    fun registerPayloadHandlers(registrar: PayloadRegistrar) {
        registrar.playToClient(
            ClientboundBlockEntityProgressUpdatePayload.TYPE,
            ClientboundBlockEntityProgressUpdatePayload.STREAM_CODEC,
            ClientboundBlockEntityProgressUpdatePayloadHandler::handleDataOnMain
        )
    }
}