package net.justmili.trueend.config.serializer;

import java.util.Map;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;

import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class GsonSerializer extends ConfigSerializer {
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static final String extension = "json";

	public GsonSerializer(String configFileName) {
		super(configFileName, extension);
	}

	@Override
	public void serialize(Map<String, Object> entries) {
		try (FileWriter writer = new FileWriter(this.getConfigFile())) {
			gson.toJson(entries, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> deserialize() {
		try (FileReader reader = new FileReader(this.getConfigFile())) {
			return gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
			}.getType());
		} catch (IOException e) {
			this.createConfigFile();
			return null;
		}
	}
}
