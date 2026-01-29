package com.ecotalecoins.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec.Builder;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.PageManager;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ATMInteraction extends SimpleBlockInteraction {
    @Nonnull
    public static final BuilderCodec<ATMInteraction> CODEC = ((Builder)BuilderCodec.builder(ATMInteraction.class, ATMInteraction::new).documentation("Opens the TRW/EcoTale Bank Interface")).build();
    protected void interactWithBlock(@Nonnull World world, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull InteractionType type, @Nonnull InteractionContext context, @Nullable ItemStack itemInHand, @Nonnull Vector3i targetBlock, @Nonnull CooldownHandler cooldownHandler) {
        int x = targetBlock.x;
        int y = targetBlock.y;
        int z = targetBlock.z;
        WorldChunk worldChunk = world.getChunkIfInMemory(ChunkUtil.indexChunkFromBlock(x, z));
        if (worldChunk == null) {
            context.getState().state = InteractionState.Failed;
        } else {
            Ref<EntityStore> ref = context.getEntity();
            Ref<ChunkStore> block = worldChunk.getBlockComponentEntity(x, y, z);
            if (block == null) {
                block = worldChunk.getBlockComponentEntity(targetBlock.x, targetBlock.y, targetBlock.z);
            }

            if (block == null) {
                context.getState().state = InteractionState.Failed;
            } else {
                Player player = commandBuffer.getComponent(ref, Player.getComponentType());
                if (player == null) {
                    context.getState().state = InteractionState.Failed;
                } else {
                    PlayerRef playerRef = commandBuffer.getComponent(ref, PlayerRef.getComponentType());
                    if (playerRef == null) {
                        context.getState().state = InteractionState.Failed;
                    } else {
                        PageManager pageManager = player.getPageManager();
                        if (playerRef != null) {
                            pageManager.openCustomPage(ref, world.getEntityStore().getStore(), new com.ecotalecoins.gui.BankGui(playerRef));
                        }
                    }
                }
            }
        }
    }

    protected void simulateInteractWithBlock(@Nonnull InteractionType type, @Nonnull InteractionContext context, @Nullable ItemStack itemInHand, @Nonnull World world, @Nonnull Vector3i targetBlock) {
    }
}