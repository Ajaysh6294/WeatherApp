package com.app.WeatherApp.service;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class WeatherService {

	@Value("${weather.api.key}")
    private String apiKey;  // Read API Key from application.properties

    public Map<String, Object> getWeather(String city) {
        Map<String, Object> weatherData = new HashMap<>();
        
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        try {
        	// url se connection banaya
            URL url = new URL(apiUrl);  // Java ka class jo ek URL object banata hai
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //ye network call bhejta hai
            connection.setRequestMethod("GET"); 
            
            int responseCode = connection.getResponseCode(); //response code leta hai connect hua ya nahi

            InputStreamReader reader;

            if (responseCode == 200) {
                // Valid response
                reader = new InputStreamReader(connection.getInputStream());
            } else {
                // Error response (e.g., 404 city not found)
                reader = new InputStreamReader(connection.getErrorStream());
            }
            
           // InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            Scanner scanner = new Scanner(reader);
            StringBuilder responseContent = new StringBuilder();

            while (scanner.hasNext()) {
                responseContent.append(scanner.nextLine());
            }
            scanner.close();

            // Parsing JSON (Gson se google ki library)
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class); //string ko json object me convert karna
            System.out.println("json object ki value "+jsonObject);
            // Check for valid city
            if (!jsonObject.has("cod") || !jsonObject.get("cod").getAsString().equals("200")) {
                weatherData.put("error", jsonObject.has("message") ? jsonObject.get("message").getAsString() : "Invalid city");
                return weatherData;
            }

            if (!jsonObject.has("main") || !jsonObject.has("weather")) {
                weatherData.put("error", "Incomplete data received. Try a valid city.");
                return weatherData;
            }

            // Extract weather details
            long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
            // Define the date format
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd:MM:yyyy hh:mm:ss a");

            Date date = new Date(dateTimestamp);
            // Format the date
            String formattedDate = formatter.format(date);
            double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
            int temperatureCelsius = (int) (temperatureKelvin - 273.15);
            int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
            double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
            String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();

            // Store values in map
            weatherData.put("date", formattedDate);
            weatherData.put("city", city);
            weatherData.put("temperature", temperatureCelsius);
            weatherData.put("weatherCondition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windSpeed", windSpeed);
            weatherData.put("rawData", responseContent.toString());
            connection.disconnect();
        } catch (Exception e) {
            weatherData.put("error", "Failed to fetch weather data");
        }
        return weatherData;
    }
}
