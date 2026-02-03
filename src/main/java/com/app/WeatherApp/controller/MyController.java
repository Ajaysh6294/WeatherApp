package com.app.WeatherApp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.WeatherApp.service.WeatherService;

@Controller
public class MyController {

	@Autowired
	private WeatherService weatherService;
	
	@GetMapping("/")
	public String home() {
		return "index";
	}

	@PostMapping("/weather")
	public String getWeather(@RequestParam String city, Model model) {
		
		// Call the service method
        Map<String, Object> weatherData = weatherService.getWeather(city);

        // Add data to the model for rendering in thymeleaf
        model.addAllAttributes(weatherData);
        System.out.println("map value in post controller : "+weatherData);
		return "demo"; // This will forward the data to index.jsp
	}
}