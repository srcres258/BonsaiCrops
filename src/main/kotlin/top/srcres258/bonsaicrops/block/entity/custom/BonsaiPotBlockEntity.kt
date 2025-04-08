package top.srcres258.bonsaicrops.block.entity.custom

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import top.srcres258.bonsaicrops.block.entity.ITickable
import top.srcres258.bonsaicrops.block.entity.ModBlockEntityTypes
import top.srcres258.bonsaicrops.network.custom.sendBlockEntityProgressUpdatePayloadToAllPlayers
import top.srcres258.bonsaicrops.util.IProgressAccessor
import top.srcres258.bonsaicrops.util.dropItemHandler
import top.srcres258.bonsaicrops.util.generateDropsForBlock

class BonsaiPotBlockEntity(
    private val isHopping: Boolean,
    pos: BlockPos,
    blockState: BlockState
) : BlockEntity(
    if (isHopping) {
        ModBlockEntityTypes.HOPPING_BONSAI_POT_BLOCK_ENTITY.get()
    } else {
        ModBlockEntityTypes.BONSAI_POT_BLOCK_ENTITY.get()
    },
    pos,
    blockState
), ITickable, IProgressAccessor {
    val cropInventory = BonsaiPotItemHandler(this)
    override var progress = 0
    override var maxProgress = 200
    private var outputProgressPerDropItem = mutableMapOf<Item, Double>()

    fun drops() {
        val level = level
        if (level != null) {
            dropItemHandler(level, worldPosition, cropInventory)
        }
    }

    fun clearContents() {
        cropInventory.setStackInSlot(0, ItemStack.EMPTY)
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)

        tag.put("crop_inventory", cropInventory.serializeNBT(registries))
        tag.putInt("progress", progress)
        tag.putInt("max_progress", maxProgress)
        val outputProgressTag = CompoundTag()
        for ((item, progress) in outputProgressPerDropItem) {
            val id = BuiltInRegistries.ITEM.getKey(item)
            val tagKey = "${id.namespace}:${id.path}"
            outputProgressTag.putDouble(tagKey, progress)
        }
        tag.put("output_progress", outputProgressTag)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        cropInventory.deserializeNBT(registries, tag.getCompound("crop_inventory"))
        progress = tag.getInt("progress")
        maxProgress = tag.getInt("max_progress")
        val outputProgressTag = tag.getCompound("output_progress")
        for (tagKey in outputProgressTag.allKeys) {
            val parts = tagKey.split(":")
            if (parts.size == 2) {
                val namespace = parts[0]
                val path = parts[1]
                val id = ResourceLocation.fromNamespaceAndPath(namespace, path)
                val item = BuiltInRegistries.ITEM.get(id)
                if (item.isPresent) {
                    val progress = tag.getDouble(tagKey)
                    outputProgressPerDropItem[item.get().value()] = progress
                }
            }
        }

        super.loadAdditional(tag, registries)
    }

    override fun tick(level: Level, blockPos: BlockPos, blockState: BlockState) {
        if (hasRecipe) {
            increaseCraftingProgress()
            setChanged(level, blockPos, blockState)
            sendBlockEntityProgressUpdatePayloadToAllPlayers(blockPos, progress, maxProgress)

            if (hasCraftingFinished) {
                if (craftItem(level, blockPos)) {
                    resetProgress()
                    sendBlockEntityProgressUpdatePayloadToAllPlayers(blockPos, progress, maxProgress)
                }
            }
        } else {
            decreaseCraftingProgress()
            setChanged(level, blockPos, blockState)
            sendBlockEntityProgressUpdatePayloadToAllPlayers(blockPos, progress, maxProgress)
        }
    }

    private val hasRecipe: Boolean
        get() {
            val stack = cropInventory.getStackInSlot(0)
            if (stack.isEmpty) {
                return false
            }
            val item = stack.item
            return item is BlockItem && item.block is CropBlock
        }

    val hasCraftingFinished: Boolean
        get() = progress >= maxProgress

    private fun craftItem(level: Level, blockPos: BlockPos): Boolean {
        if (!isHopping) {
            return false
        }

        if (level is ServerLevel) {
            // Generates outputs according to the loot table, and update the progress of output items.
            val outputs = harvest(level, blockPos)
            if (outputs.isEmpty()) {
                return false
            }
            for (output in outputs) {
                val progressIncrement = output.count.toDouble() / 10.0
                if (output.item in outputProgressPerDropItem.keys) {
                    outputProgressPerDropItem[output.item] = outputProgressPerDropItem[output.item]!! + progressIncrement
                } else {
                    outputProgressPerDropItem[output.item] = progressIncrement
                }
            }

            // Finds available output items and try to output them into the block below.
            val itemsAvailable = outputItemsAvailable
            if (itemsAvailable.isEmpty()) {
                return false
            }
            val resultStacks = mutableListOf<ItemStack>()
            for (item in itemsAvailable) {
                outputs.firstOrNull { output -> output.item == item }
                    ?.let { outputStack -> resultStacks.add(outputStack.copy()) }
            }
            val blockCap = level.getCapability(
                Capabilities.ItemHandler.BLOCK,
                blockPos.below(),
                Direction.UP
            ) ?: return false
            for (stack in resultStacks) {
                var insertionSucceeded = false
                for (i in 0 ..< blockCap.slots) {
                    if (!blockCap.isItemValid(i, stack)) {
                        continue
                    }
                    val stackInside = blockCap.getStackInSlot(i)
                    if (!(stackInside.isEmpty || ItemStack.isSameItemSameComponents(stackInside, stack))) {
                        continue
                    }
                    val stack1 = blockCap.insertItem(i, stack, true)
                    if (stack.count > stack1.count) {
                        blockCap.insertItem(i, stack, false)
                        insertionSucceeded = true
                        break
                    }
                }
                if (!insertionSucceeded) {
                    return false
                }
            }

            return true
        } else {
            return false
        }
    }

    private val outputItemsAvailable: List<Item>
        get() = mutableListOf<Item>()
            .also { result ->
                for ((item, progress) in outputProgressPerDropItem) {
                    if (progress >= 1.0) {
                        result.add(item)
                    }
                }
            }

    private fun increaseCraftingProgress() {
        if (progress < maxProgress) {
            progress++
        }
    }

    private fun decreaseCraftingProgress() {
        if (progress > 0) {
            progress--
        }
    }

    private fun resetProgress() {
        progress = 0
        maxProgress = 200
    }

    fun harvest(
        level: Level? = this.level,
        blockPos: BlockPos = this.blockPos,
        tool: ItemStack = ItemStack.EMPTY,
        resetProgress: Boolean = false
    ): List<ItemStack> {
        if (!hasCraftingFinished) {
            return emptyList()
        }
        if (level == null) {
            return emptyList()
        }
        if (level !is ServerLevel) {
            return emptyList()
        }

        val cropItemStack = cropInventory.getStackInSlot(0)
        val cropItem = cropItemStack.item
        if (cropItem !is BlockItem) {
            return emptyList()
        }
        val cropBlock = cropItem.block
        if (cropBlock !is CropBlock) {
            return emptyList()
        }
        val outputs = generateDropsForBlock(
            level,
            cropBlock,
            pos = blockPos,
            blockState = cropBlock.getStateForAge(cropBlock.maxAge),
            tool = tool,
            blockEntity = this
        )

        if (resetProgress) {
            this.resetProgress()
        }

        return outputs
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> =
        ClientboundBlockEntityDataPacket.create(this)

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag =
        saveWithoutMetadata(registries)
}