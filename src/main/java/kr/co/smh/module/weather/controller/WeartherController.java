package kr.co.smh.module.weather.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smh.module.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/weather")
public class WeartherController {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	private final WeatherService weatherService;
	
	@GetMapping(value="/get")
	public HttpEntity<?> getWeather() {
		return weatherService.getWeather();
	}
}
