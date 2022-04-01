package com.samyeonyiduk.service;

import com.samyeonyiduk.domain.users.Users;
import com.samyeonyiduk.domain.users.UsersRepository;
import com.samyeonyiduk.web.dto.MailDto;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailService {

    private final UsersRepository usersRepository;
    private JavaMailSender emailSender;

    public void sendCompleteMessage(Long userId) {
        SimpleMailMessage message = new SimpleMailMessage();

        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(
                "해당 유저가 없습니다. ID = " + userId));
        String address = user.getIntraId() + "@student.42seoul.kr";
        String title = user.getIntraId() + "카뎃님, 문의사항을 보내주셔서 감사합니다.";
        String content = "빠른 시일 내로 확인하여 응답하겠습니다 :) 응답이 늦는 경우 jimkwon 계정으로 slack DM 주세요!";

        message.setFrom("2kwon2lee@gmail.com");
        message.setTo(address);
        message.setSubject(title);
        message.setText(content);
        emailSender.send(message);
    }

    public void sendQuestionMessage(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("2kwon2lee@gmail.com");
        message.setTo("2kwon2lee@gmail.com");
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getContent());
        emailSender.send(message);
    }
}
