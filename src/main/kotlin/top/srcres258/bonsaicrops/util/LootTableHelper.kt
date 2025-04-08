package top.srcres258.bonsaicrops.util

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.Vec3

fun generateDropsForBlock(
    level: ServerLevel,
    block: BlockBehaviour,
    pos: BlockPos,
    blockState: BlockState,
    tool: ItemStack,
    blockEntity: BlockEntity? = null
): List<ItemStack> {
    val lootTableKey = block.lootTable

    return if (lootTableKey.isEmpty) {
        listOf()
    } else {
        val params = LootParams.Builder(level)
            .also { builder ->
                builder.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                builder.withParameter(LootContextParams.BLOCK_STATE, blockState)
                builder.withParameter(LootContextParams.TOOL, tool)
                if (blockEntity != null) {
                    builder.withParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
                }
            }
            .create(LootContextParamSets.BLOCK)
        val lootTable = level.server.reloadableRegistries().getLootTable(lootTableKey.get())
        lootTable.getRandomItems(params)
    }
}