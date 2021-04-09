package com.simibubi.create.foundation.command;

import java.util.function.Supplier;

import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.networking.SimplePacketBase;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class HighlightPacket extends SimplePacketBase {

	private final BlockPos pos;

	public HighlightPacket(BlockPos pos) {
		this.pos = pos;
	}

	public HighlightPacket(PacketBuffer buffer) {
		this.pos = BlockPos.fromLong(buffer.readLong());
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeLong(pos.toLong());
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get()
			.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(EnvType.CLIENT, () -> () -> {
				performHighlight(pos);
			}));

		ctx.get()
			.setPacketHandled(true);
	}

	@Environment(EnvType.CLIENT)
	public static void performHighlight(BlockPos pos) {
		if (Minecraft.getInstance().world == null || !Minecraft.getInstance().world.isBlockPresent(pos))
			return;

		CreateClient.outliner.showAABB("highlightCommand", VoxelShapes.fullCube()
			.getBoundingBox()
			.offset(pos), 200)
			.lineWidth(1 / 32f)
			.colored(0xEeEeEe)
			// .colored(0x243B50)
			.withFaceTexture(AllSpecialTextures.SELECTION);

	}
}
