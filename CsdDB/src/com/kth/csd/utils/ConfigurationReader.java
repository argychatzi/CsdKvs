package com.kth.csd.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConfigurationReader {
	private static final Gson gson = new Gson();
	private static final String TAG = ConfigurationReader.class.getCanonicalName();

	public static Configuration loadConfigurationFile(String fileNo) throws IOException{
		Logger.d(TAG, "loadConfigurationFile" +fileNo );
        JsonObject jsonObject = new JsonObject();
        //String path = "../configurations/configuration_" + fileNo + ".json" ;
        String path = "properties/configurations/configuration_" + fileNo + ".json";
        Logger.d(TAG, "loadConfiguration loading file from" + path);
        try {
            JsonParser parser = new JsonParser();
            //String path = "../configurations/configuration_" + fileNo + ".json" ;
            FileReader fileReader = new FileReader(path);
            JsonElement jsonElement = parser.parse(fileReader);
            Logger.d(TAG, "loadConfiguration loading file from" + path);
            jsonObject = jsonElement.getAsJsonObject();
            Logger.d(TAG, "loadConfigurationFile jsonObject" + jsonObject.toString());
         
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        	Logger.d(TAG,"exception reading file" + e);
        }
        
        return gson.fromJson(jsonObject, Configuration.class);
	}
}
