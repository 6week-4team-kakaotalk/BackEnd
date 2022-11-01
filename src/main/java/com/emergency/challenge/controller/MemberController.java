package com.emergency.challenge.controller;

import com.emergency.challenge.controller.request.LoginRequestDto;
import com.emergency.challenge.controller.request.MemberRequestDto;
import com.emergency.challenge.controller.response.ResponseDto;
import com.emergency.challenge.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@CustomBaseControllerAnnotation
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping(value = "/member/signup")
    public ResponseDto<?> signup(@Valid @RequestBody MemberRequestDto requestDto) {

        return memberService.createMember(requestDto);
    }


    // 로그인
    @PostMapping(value = "/member/login")
    public ResponseDto<?> login(@RequestBody LoginRequestDto requestDto,
                                HttpServletResponse response
    ) {
        return memberService.login(requestDto, response);
    }


    // 로그아웃
    @PostMapping(value = "/auth/member/logout")
    public ResponseDto<?> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }

    //토큰 재발급
    @RequestMapping(value = "/member/reissue", method = RequestMethod.POST)
    public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        return memberService.reissue(request, response);
    }

}
