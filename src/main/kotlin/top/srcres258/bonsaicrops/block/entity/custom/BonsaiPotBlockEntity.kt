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
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
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

class BonsaiPotBlockEntity(
    isHopping: Boolean,
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
                val progress = tag.getDouble(tagKey)
                outputProgressPerDropItem[item] = progress
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

    private val hasCraftingFinished: Boolean
        get() = progress >= maxProgress

    private fun craftItem(level: Level, blockPos: BlockPos): Boolean {
        val blockCap = level.getCapability(
            Capabilities.ItemHandler.BLOCK,
            blockPos.below(),
            Direction.UP
        ) ?: return false
        val resultStacks = listOf(ItemStack(Items.STICK, 2))
        for (stack in resultStacks) {
            val stack1 = blockCap.insertItem(0, stack, true)
            if (stack.count == stack1.count) {
                return false
            } else {
                blockCap.insertItem(0, stack, false)
            }
        }
        return true
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

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> =
        ClientboundBlockEntityDataPacket.create(this)

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag =
        saveWithoutMetadata(registries)
}