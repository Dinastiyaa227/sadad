package com.example.legit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Random;

public class LegitClient implements ClientModInitializer {
	public static final Module TRIGGER = new Module("Triggerbot");
	public static final Module INVIS = new Module("Invisible ESP");
	public static final List<Module> MODULES = List.of(TRIGGER, INVIS);

	private static final Random RNG = new Random();
	private static boolean guiWasDown = false;
	private static int delay = 0;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(LegitClient::tick);
	}

	private static void tick(MinecraftClient mc) {
		if (mc.player == null || mc.world == null) return;
		long w = mc.getWindow().getHandle();

		// Click GUI на Right Shift
		boolean gui = GLFW.glfwGetKey(w, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
		if (gui && !guiWasDown && mc.currentScreen == null) mc.setScreen(new ClickGui());
		guiWasDown = gui;

		// Бинды модулей (работают только когда нет открытого экрана)
		if (mc.currentScreen == null) {
			for (Module m : MODULES) {
				boolean down = m.key != -1 && GLFW.glfwGetKey(w, m.key) == GLFW.GLFW_PRESS;
				if (down && !m.wasDown) m.enabled = !m.enabled;
				m.wasDown = down;
			}
			if (TRIGGER.enabled) triggerbot(mc);
		}
	}

	private static void triggerbot(MinecraftClient mc) {
		// crosshairTarget считается по хитбоксу, поэтому бьём только когда прицел реально на модели
		if (mc.crosshairTarget instanceof EntityHitResult hit
				&& hit.getEntity() instanceof LivingEntity target
				&& target != mc.player && target.isAlive()
				&& mc.player.getAttackCooldownProgress(0f) >= 0.95f) {
			if (delay-- <= 0) {
				mc.interactionManager.attackEntity(mc.player, target);
				mc.player.swingHand(Hand.MAIN_HAND);
				delay = RNG.nextInt(3); // небольшая случайная задержка 0-2 тика
			}
		}
	}
}
