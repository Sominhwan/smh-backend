package kr.co.smh.util.gmail.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import kr.co.smh.common.service.CertificationNumberService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GmailService {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	private final JavaMailSender javaMailSender;

    // 단일 전송
	@Async
    public void sendEmail(String userEmail, String userName, int certificationNumber) {
        String title = "비밀번호 찾기를 위한 인증 메일입니다.";	
        String content = "인증 코드는 다음과 같습니다.\r\n" + certificationNumber + "\r\n인증코드는 3분간 유효합니다.";
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
    	try {
			mimeMessageHelper.setTo(userEmail);
        	mimeMessageHelper.setSubject(title);
        	mimeMessageHelper.setText(content, true);
            javaMailSender.send(mimeMessage);

		} catch (MessagingException e) {
			e.printStackTrace();
		}     
    }
}
