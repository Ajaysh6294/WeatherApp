//package com.app.WeatherApp.restController;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.app.WeatherApp.service.WeatherService;
//
//@RestController
//@RequestMapping("/weather")
//public class WeatherController {
//
//	@Autowired
//	 private final WeatherService weatherService;
//
//	    public WeatherController(WeatherService weatherService) {
//	    	System.out.println("restcontroller : "+weatherService);
//	    	
//	        this.weatherService = weatherService;
//	    }
//
//	    @GetMapping("/{city}")
//	    public Map<String, Object> getWeather(@PathVariable String city,Model model) {
//	    	
//	    	System.out.println("restcontroller get mapping : "+city);
//	        return weatherService.getWeather(city,model);
//	    }
//}
