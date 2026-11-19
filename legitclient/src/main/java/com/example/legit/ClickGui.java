package com.example.legit;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class ClickGui extends Screen {
	private final Map<Module, ButtonWidget> toggles = new HashMap<>();
	private final Map<Module, ButtonWidget> binds = new HashMap<>();
	private Module listening = null;
	private boolean rshiftWasDown = true; // открыли по Right Shift, ждём отпускания

	public ClickGui() { super(Text.literal("LegitClient")); }

	@Override
	protected void init() {
		int cx = this.width / 2, y = this.height / 2 - 40;
		for (Module m : LegitClient.MODULES) {
			ButtonWidget t = ButtonWidget.builder(Text.literal(""), b -> { m.enabled = !m.enabled; refresh(); })
					.dimensions(cx - 105, y, 120, 20).build();
			ButtonWidget b = ButtonWidget.builder(Text.literal(""), btn -> { listening = m; refresh(); })
					.dimensions(cx + 20, y, 85, 20).build();
			toggles.put(m, t);
			binds.put(m, b);
			addDrawableChild(t);
			addDrawableChild(b);
			y += 24;
		}
		refresh();
	}

	private void refresh() {
		for (Module m : LegitClient.MODULES) {
			toggles.get(m).setMessage(Text.literal(m.name + ": " + (m.enabled ? "ON" : "OFF")));
			String k = listening == m ? "..." : keyName(m.key);
			binds.get(m).setMessage(Text.literal("Bind: " + k));
		}
	}

	private static String keyName(int key) {
		if (key == -1) return "NONE";
		String n = GLFW.glfwGetKeyName(key, 0);
		return n != null ? n.toUpperCase() : "KEY_" + key;
	}

	@Override
	public void tick() {
		long w = client.getWindow().getHandle();

		boolean rs = GLFW.glfwGetKey(w, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
		if (rs && !rshiftWasDown && listening == null) { close(); return; }
		rshiftWasDown = rs;

		if (listening != null) {
			for (int k = GLFW.GLFW_KEY_SPACE; k <= GLFW.GLFW_KEY_LAST; k++) {
				if (k == GLFW.GLFW_KEY_RIGHT_SHIFT) continue;
				if (GLFW.glfwGetKey(w, k) == GLFW.GLFW_PRESS) {
					if (k == GLFW.GLFW_KEY_ESCAPE) { /* отмена */ }
					else if (k == GLFW.GLFW_KEY_BACKSPACE) listening.key = -1;
					else listening.key = k;
					listening.wasDown = true; // чтобы не сработал сразу
					listening = null;
					refresh();
					break;
				}
			}
		}
	}

	@Override
	public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
		super.render(ctx, mouseX, mouseY, delta);
		ctx.drawCenteredTextWithShadow(textRenderer, "LegitClient  |  Right Shift - закрыть  |  Backspace - снять бинд",
				width / 2, height / 2 - 65, 0xFFFFFF);
	}

	@Override
	public boolean shouldPause() { return false; }
}
