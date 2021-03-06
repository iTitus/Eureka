package eureka.network;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;


import eureka.Eureka;
import eureka.api.EurekaKnowledge;
import eureka.api.EurekaRegistry;
import io.netty.buffer.ByteBuf;

/**
 * Copyright (c) 2014, AEnterprise
 * http://buildcraftadditions.wordpress.com/
 * Eureka is distributed under the terms of LGPLv3
 * Please check the contents of the license located in
 * http://buildcraftadditions.wordpress.com/wiki/licensing-stuff/
 */
public class MessageEngineeringDiary implements IMessage, IMessageHandler<MessageEngineeringDiary, IMessage> {
	EntityPlayer player;
	int[] progress = new int[100];
	boolean[] finished = new boolean[100];

	public MessageEngineeringDiary() {
	}

	public MessageEngineeringDiary(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		ArrayList<String> keys = EurekaRegistry.getKeys();
		for (int t = 0; t < keys.size(); t++) {
			progress[t] = buf.readInt();
			finished[t] = buf.readBoolean();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ArrayList<String> keys = EurekaRegistry.getKeys();
		for (int t = 0; t < keys.size(); t++) {
			buf.writeInt(EurekaKnowledge.getProgress(player, keys.get(t)));
			buf.writeBoolean(EurekaKnowledge.isFinished(player, keys.get(t)));
		}
	}


	@Override
	public IMessage onMessage(MessageEngineeringDiary message, MessageContext ctx) {
		ArrayList<String> keys = EurekaRegistry.getKeys();
		NBTTagCompound info = EurekaKnowledge.getTag(Eureka.proxy.getPlayer());
		for (int t = 0; t < keys.size(); t++) {
			info.setInteger(keys.get(t) + "Progress", message.progress[t]);
			info.setBoolean(keys.get(t) + "Finished", message.finished[t]);
		}
		return null;
	}
}
