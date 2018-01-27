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
