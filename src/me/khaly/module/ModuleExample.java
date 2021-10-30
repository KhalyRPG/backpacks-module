package me.khaly.module;

import me.khaly.core.module.Module;

public class ModuleExample extends Module {

	public ModuleExample() {
		super("Display Name", "module-example", 0.1F);
	}

	@Override
	public void load() {
		// Module load logic
	}

	@Override
	public void unload() {
		// Module unload logic
	}
}
