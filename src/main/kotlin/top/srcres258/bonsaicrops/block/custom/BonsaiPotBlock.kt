package top.srcres258.bonsaicrops.block.custom

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

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
) : Block(properties) {
    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape = SHAPE
}