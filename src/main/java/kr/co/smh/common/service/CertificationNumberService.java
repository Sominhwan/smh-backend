package kr.co.smh.common.service;

import java.security.SecureRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CertificationNumberService {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	
	public int createCertificationNumber() {
		SecureRandom random = new SecureRandom();
		int certificationNumber = random.nextInt(888888) + 111111;
		log.info("생성된 인증번호 --> " + certificationNumber);
		return certificationNumber;
	}
}
