package com.kth.csd.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConfigurationReader {
	private static final Gson gson = new Gson();
	private static final String TAG = ConfigurationReader.class.getCanonicalName();

	public static Configuration loadConfigurationFile() throws IOException{
        JsonObject jsonObject = new JsonObject();
        
        try {
        	File f = new File(".");
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader("../configuration.json"));
            jsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
        
        return gson.fromJson(jsonObject, Configuration.class);
	}
	
}
