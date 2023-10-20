package kr.co.smh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync // 비동기 처리 가능
@SpringBootApplication
public class SmhBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmhBackendApplication.class, args);
	}

}
