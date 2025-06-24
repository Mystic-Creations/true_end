package net.justmili.trueend.config.serializer;

import net.minecraftforge.fml.loading.FMLPaths;

import java.util.Map;

import java.io.IOException;
import java.io.File;

public abstract class ConfigSerializer {
	private final File configFile;

    public ConfigSerializer(String configFileName, String extension) {
        extension = "." + extension;
		this.configFile = FMLPaths.CONFIGDIR.get().resolve(configFileName.endsWith(extension) ? configFileName : configFileName + extension).toFile();
	}

	protected File getConfigFile() {
		return this.configFile;
	}

	abstract public void serialize(Map<String, Object> entries);

	abstract public Map<String, Object> deserialize();

	protected void createConfigFile() {
		try {
			this.getConfigFile().createNewFile();
		} catch (IOException ignored) {
		}
	}
}
