package com.kth.csd.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConfigurationReader {
	private static final Gson gson = new Gson();

	public static Configuration loadConfigurationFile(){
        JsonObject jsonObject = new JsonObject();
        
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader("./configuration.json"));
            jsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
        
        return gson.fromJson(jsonObject, Configuration.class);
	}
	
}
