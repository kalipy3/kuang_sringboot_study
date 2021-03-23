package com.example.demo;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    JavaMailSenderImpl mailSender;

	@Test
	void contextLoads() {
        //一个简单的邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setSubject("kalipy你好");
        mailMessage.setText("thanks for your ps4~");

        mailMessage.setTo("3069087972@qq.com");
        mailMessage.setFrom("3069087972@qq.com");

        mailSender.send(mailMessage);
	}

	@Test
	void contextLoads2() throws MessagingException {
        //一个复杂的邮件
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //组装
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setSubject("kalipy你好22222");
        helper.setText("<p style='color: red'>thanks for your mac~</p>", true);//是否启用html

        //附件
        helper.addAttachment("3.jpg", new File("/home/kalipy/桌面/壁纸/3.jpg"));

        helper.setTo("3069087972@qq.com");
        helper.setFrom("3069087972@qq.com");

        mailSender.send(mimeMessage);
	}
}
