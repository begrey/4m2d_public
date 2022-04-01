package com.samyeonyiduk.web;

import com.samyeonyiduk.service.MailService;
import com.samyeonyiduk.web.dto.MailDto;
import com.samyeonyiduk.web.dto.posts.PostsResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Api(tags = {"MAIL API"})
@RestController
public class MailController {

    private  final MailService mailService;

    @ApiOperation(value = "mail전송", notes = "문의사항 관리")
    @PostMapping(value = "api/mail/{userId}")
    public void sendMail(@PathVariable("userId") Long userId, @RequestBody MailDto mailDto) {
        mailService.sendCompleteMessage(userId);
        mailService.sendQuestionMessage(mailDto);
        System.out.println("메일 전송 완료");
    }


}
