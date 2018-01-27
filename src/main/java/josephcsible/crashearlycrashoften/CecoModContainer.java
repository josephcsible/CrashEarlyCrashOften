/*
CrashEarlyCrashOften Minecraft Mod
Copyright (C) 2018  Joseph C. Sible

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/

package josephcsible.crashearlycrashoften;

import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class CecoModContainer extends DummyModContainer {
	public CecoModContainer() {
		super(new ModMetadata());
		ModMetadata metadata = getMetadata();
		metadata.modId = "crashearlycrashoften";
		metadata.version = "0.1.0";
		metadata.name = "CrashEarlyCrashOften";
		metadata.description = "Makes the game crash with a useful crash report when it detects something has gone wrong, instead of crashing later with a useless crash report";
		metadata.url = "https://github.com/josephcsible/CrashEarlyCrashOften";
		metadata.authorList.add("Joseph C. Sible");
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return true;
	}
}
