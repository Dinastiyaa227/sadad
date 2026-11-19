package com.example.legit.mixin;

import com.example.legit.LegitClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	// Невидимые игроки получают контур (glow), как будто на них спектральная стрела
	@Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
	private void legit$glowInvisible(CallbackInfoReturnable<Boolean> cir) {
		Entity self = (Entity) (Object) this;
		if (LegitClient.INVIS.enabled && self instanceof PlayerEntity && self.isInvisible()) {
			cir.setReturnValue(true);
		}
	}
}
