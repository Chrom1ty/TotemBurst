package totem.burst.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;

public class TotemBurstClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		TotemBurstSounds.register();

		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (world.isClientSide && player == Minecraft.getInstance().player) {
				KillTracker.onAttack(entity.getId());
			}
			return InteractionResult.PASS;
		});

	}
}
