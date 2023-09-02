package kr.co.smh.module.weather.service;

import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.module.weather.model.WeatherVO;
import kr.co.smh.module.weather.model.WindTypeVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherService {
	@Value("${weather.secretKey}")
	private String secretKey;
	private final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
	
	private String getWindDirection(int degree) {
		  int result = (int)((degree + 22.5 * 0.5) / 22.5);
		  WindTypeVO windTypeVO = WindTypeVO.value(result);
		  return windTypeVO.getName();
		}
	
	public HttpEntity<?> getWeather() {
		  StringBuilder urlBuilder = new StringBuilder(BASE_URL);
		  try {
		    urlBuilder.append("?" + URLEncoder.encode("lat", "UTF-8") + "=35.17");
		    urlBuilder.append("&" + URLEncoder.encode("lon", "UTF-8") + "=129.07");
		    urlBuilder.append("&" + URLEncoder.encode("appid", "UTF-8") + "=" + secretKey);
		    urlBuilder.append("&" + URLEncoder.encode("lang", "UTF-8") + "=kr");
		    urlBuilder.append("&" + URLEncoder.encode("units", "UTF-8") + "=metric");

		    RestTemplate restTemplate = new RestTemplate();
		    WeatherVO weatherVO = restTemplate.getForObject(urlBuilder.toString(), WeatherVO.class);

		    // deg -> 풍향변환
		    String wd = getWindDirection(weatherVO.getWind().getDeg());
		    weatherVO.setWindType(wd);
		    System.out.println(weatherVO);
		    
			return new ResponseEntity<>(
					ResDTO.builder()
						.code(0)
						.message("날씨정보를 가져오는데 성공하였습니다.")
						.data(weatherVO)
						.build(),
						HttpStatus.OK);
		  } catch (Exception e) {
		    e.printStackTrace();
			return new ResponseEntity<>(
					ResDTO.builder()
						.code(0)
						.message("날씨정보를 가져오는데 실패하였습니다.")
						.build(),
						HttpStatus.OK);
		  }
	}
}
