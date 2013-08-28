package net.mcft.copy.vanilladj.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockFenceRenderingHandler implements ISimpleBlockRenderingHandler {
	
	public static final BlockFenceRenderingHandler instance = new BlockFenceRenderingHandler();
	
	public final int renderId;
	
	public BlockFenceRenderingHandler() {
		renderId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(this);
	}
	
	@Override
	public int getRenderId() { return renderId; }
	@Override
	public boolean shouldRender3DInInventory() { return true; }
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		for (int i = 0; i < 4; i++) {
			double f2;
			switch (i) {
				case 0: renderer.setRenderBounds(0.375, 0.0, 0.0, 0.625, 1.0, 0.25); break;
				case 1: renderer.setRenderBounds(0.375, 0.0, 0.75, 0.625, 1.0, 1.0); break;
				case 2: renderer.setRenderBounds(0.4375, 0.8125, -0.125, 0.5625, 0.9375, 1.125); break;
				case 3: renderer.setRenderBounds(0.4375, 0.3125, -0.125, 0.5625, 0.4375, 1.125); break;
			}
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderer.renderFaceYNeg(block, 0.0, 0.0, 0.0, block.getIcon(0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderer.renderFaceYPos(block, 0.0, 0.0, 0.0, block.getIcon(1, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderer.renderFaceZNeg(block, 0.0, 0.0, 0.0, block.getIcon(2, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderer.renderFaceZPos(block, 0.0, 0.0, 0.0, block.getIcon(3, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderer.renderFaceXNeg(block, 0.0, 0.0, 0.0, block.getIcon(4, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderer.renderFaceXPos(block, 0.0, 0.0, 0.0, block.getIcon(5, metadata));
			tessellator.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}
		renderer.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		renderer.renderBlockFence((BlockFence)block, x, y, z);
		return true;
	}
	
}
