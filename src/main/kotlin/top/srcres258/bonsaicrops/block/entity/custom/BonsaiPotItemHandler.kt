package top.srcres258.bonsaicrops.block.entity.custom

import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.items.ItemStackHandler

class BonsaiPotItemHandler(
    private val blockEntity: BonsaiPotBlockEntity,
    private val size: Int = 1
) : ItemStackHandler(size) {
    override fun getStackLimit(slot: Int, stack: ItemStack): Int = size

    override fun onContentsChanged(slot: Int) {
        blockEntity.setChanged()

        val level = blockEntity.level
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(blockEntity.blockPos, blockEntity.blockState, blockEntity.blockState, Block.UPDATE_ALL)
        }
    }
}