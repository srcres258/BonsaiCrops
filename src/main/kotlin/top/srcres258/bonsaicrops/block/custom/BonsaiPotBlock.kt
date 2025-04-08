package top.srcres258.bonsaicrops.block.custom

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.Containers
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import top.srcres258.bonsaicrops.block.entity.ModBlockEntityTypes
import top.srcres258.bonsaicrops.block.entity.custom.BonsaiPotBlockEntity

private val SHAPE: VoxelShape = Shapes.join(
    Shapes.or(
        Block.box(2.0, 0.0, 2.0, 14.0, 1.0, 14.0),
        Block.box(1.0, 1.0, 1.0, 15.0, 3.0, 15.0)
    ),
    Block.box(2.0, 1.0, 2.0, 14.0, 3.0, 14.0),
    BooleanOp.ONLY_FIRST
)

class BonsaiPotBlock(
    val isHopping: Boolean,
    properties: Properties
) : BaseEntityBlock(properties) {
    companion object {
        val CODEC: MapCodec<BonsaiPotBlock> = simpleCodec { properties -> BonsaiPotBlock(false, properties) }
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = CODEC

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape = SHAPE

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        BonsaiPotBlockEntity(isHopping, pos, state)

    // NOTE to override this method and return this value,
    // otherwise this block will **NOT** be rendered by the game and consequently
    // become invisible!!!
    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean
    ) {
        if (state.block != newState.block) {
            val blockEntity = level.getBlockEntity(pos)
            if (blockEntity is BonsaiPotBlockEntity) {
                blockEntity.drops()
                level.updateNeighbourForOutputSignal(pos, this)
            }

            // From NeoForge's official documentation:
            //
            // To make sure that caches can correctly update their stored capability, modders must call
            // level.invalidateCapabilities(pos) whenever a capability changes, appears, or disappears.
            level.invalidateCapabilities(pos)
        }

        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): InteractionResult {
        val blockEntity = level.getBlockEntity(pos)
        if (blockEntity is BonsaiPotBlockEntity) {
            val item = stack.item
            if (item is HoeItem) {
                // If the player is holding a hoe, try to harvest the crop.
                if (blockEntity.hasCraftingFinished) {
                    val outputs = blockEntity.harvest(level, pos, stack, true)
                    if (outputs.isNotEmpty()) {
                        val center = Vec3.atCenterOf(pos)
                        for (output in outputs) {
                            Containers.dropItemStack(level, center.x, center.y, center.z, output)
                        }
                        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand))
                    }
                }
            } else {
                val cropInventory = blockEntity.cropInventory
                if (cropInventory.getStackInSlot(0).isEmpty && !stack.isEmpty) {
                    if (item is BlockItem && item.block is CropBlock) {
                        // If the crop inventory is empty and the player is holding an crop item,
                        // add the item to the crop inventory and decrease the player's stack size.
                        cropInventory.setStackInSlot(0, stack.split(1))
                        level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1F, 2F)
                    }
                } else if (stack.isEmpty) {
                    // If the player is not holding an item,
                    // remove the first item from the crop inventory and give it to the player.
                    val stackExtracted = cropInventory.extractItem(0, 1, false)
                    player.setItemInHand(hand, stackExtracted)
                    blockEntity.clearContents()
                    level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1F, 1F)
                }
            }

            return InteractionResult.SUCCESS
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? =
        if (level.isClientSide) {
            null
        } else {
            createTickerHelper(
                blockEntityType,
                if (isHopping) {
                    ModBlockEntityTypes.HOPPING_BONSAI_POT_BLOCK_ENTITY.get()
                } else {
                    ModBlockEntityTypes.BONSAI_POT_BLOCK_ENTITY.get()
                }
            ) { level1, blockPos, blockState1, blockEntity ->
                blockEntity.tick(level1, blockPos, blockState1)
            }
        }
}