package com.emergency.challenge.service;

import com.emergency.challenge.controller.request.LoginRequestDto;
import com.emergency.challenge.controller.request.MemberRequestDto;
import com.emergency.challenge.controller.response.MemberResponseDto;
import com.emergency.challenge.controller.response.ResponseDto;
import com.emergency.challenge.controller.response.TokenDto;
import com.emergency.challenge.domain.Friend;
import com.emergency.challenge.domain.Member;
import com.emergency.challenge.domain.RefreshToken;
import com.emergency.challenge.domain.UserDetailsImpl;
import com.emergency.challenge.error.ErrorCode;
import com.emergency.challenge.jwt.TokenProvider;
import com.emergency.challenge.repository.FriendRepository;
import com.emergency.challenge.repository.MemberRepository;
import com.emergency.challenge.repository.RefreshTokenRepository;
import com.emergency.challenge.shared.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final FriendRepository friendRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;


    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";


//=============================================??????===================================

    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {

        //?????? ????????? ?????? ??????
        if (null != isPresentMember(requestDto.getLoginId())) {
            return ResponseDto.fail(ErrorCode.ALREADY_SAVED_ID.name(),
                    ErrorCode.ALREADY_SAVED_ID.getMessage());
        }

        //???????????? ?????? ??????
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            return ResponseDto.fail(ErrorCode.PASSWORDS_NOT_MATCHED.name(),
                    ErrorCode.PASSWORDS_NOT_MATCHED.getMessage());
        }


        Member member = Member.builder()
                .loginId(requestDto.getLoginId())
                .nickName(requestDto.getNickName())
                .phoneNumber(requestDto.getPhoneNumber())
                .role(Authority.ROLE_MEMBER)
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();

        if(requestDto.isAdmin()){
            if(!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                return ResponseDto.fail("NOT_ADMIN", "ADMIN????????? ???????????? ????????????.");
            }
            Member.builder()
                    .loginId(requestDto.getLoginId())
                    .nickName(requestDto.getNickName())
                    .phoneNumber(requestDto.getPhoneNumber())
                    .password(passwordEncoder.encode(requestDto.getPassword()))
                    .role(Authority.ROLE_ADMIN)
                    .build();
        }

        memberRepository.save(member);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getMemberId())
                        .loginId(member.getLoginId())
                        .nickName(member.getNickName())
                        .phoneNumber(member.getPhoneNumber())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }

    // ?????????
    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getLoginId());
        if (null == member) {
            return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND.name(),
                    ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail(ErrorCode.INVALID_MEMBER.name(), ErrorCode.INVALID_MEMBER.getMessage());
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(requestDto.getLoginId(), requestDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getMemberId())
                        .loginId(member.getLoginId())
                        .nickName(member.getNickName())
                        .phoneNumber(member.getPhoneNumber())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }


    // ????????????
    public ResponseDto<?> logout(HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(ErrorCode.INVALID_TOKEN.name(), ErrorCode.INVALID_TOKEN.getMessage());
        }
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND.name(),
                    ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
        return tokenProvider.deleteRefreshToken(member);
    }

    //?????? ?????????
    @Transactional
    public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(ErrorCode.INVALID_TOKEN.name(), ErrorCode.INVALID_TOKEN.getMessage());
        }


        Member member = refreshTokenRepository.findByValue(request.getHeader("Refresh-Token")).get().getMember();
        RefreshToken refreshToken = tokenProvider.isPresentRefreshToken(member);



        if (!refreshToken.getValue().equals(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "refresh token is invalid");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        refreshToken.updateValue(tokenDto.getRefreshToken());
        tokenToHeaders(tokenDto, response);
        return ResponseDto.success("success");
    }

    @Transactional(readOnly = true)
    public Member isPresentMember(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(nickname);
        return optionalMember.orElse(null);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

    //?????? ???????????? ????????? ??????
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    //?????? ?????? ????????????
    @Transactional
    public ResponseDto<?> friendList(HttpServletRequest request,Long memberId){
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(ErrorCode.INVALID_TOKEN.name(), ErrorCode.INVALID_TOKEN.getMessage());
        }
        Member member= memberRepository.findByMemberId(memberId).orElseThrow(NullPointerException::new);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getMemberId())
                        .loginId(member.getLoginId())
                        .nickName(member.getNickName())
                        .phoneNumber(member.getPhoneNumber())
                        .friends(member.getFriends())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build());
    }
    @Transactional
    public ResponseDto<?> friendPlus(HttpServletRequest request,Long memberId,MemberRequestDto loginId) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(ErrorCode.INVALID_TOKEN.name(), ErrorCode.INVALID_TOKEN.getMessage());
        }

        Member member = memberRepository.findByMemberId(memberId).orElse(null);
        Member friend = memberRepository.findByLoginId(loginId.getLoginId())
                .orElseThrow(()->new NullPointerException("???????????? ?????????"));
        boolean friendPresent = friendRepository.findByMemberAndFriend(member, friend).isPresent();
        if(friendPresent){
            return ResponseDto.fail("AREADY_FRIEND", "?????? ????????? ???????????????.");
        }else{
            Friend friends=new Friend();
            friends.setMember(member);
            friends.setFriend(friend);
            friendRepository.save(friends);
            return ResponseDto.success("success");
        }
    }


//==============================User ?????? ??????======================================
    public Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByLoginId(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException("???????????? ?????? ???????????????")
        );
    }

//=================================================================================



//?????? ?????? ?????? ??????
//public class Verification{
//      public ResponseDto<Object> logout(HttpServletRequest request){
//          if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
//              return ResponseDto.fail("INVALID_TOKEN", "Token??? ???????????? ????????????.");
//          }
//          Member member = tokenProvider.getMemberFromAuthentication();
//          if (null == member) {
//              return ResponseDto.fail("MEMBER_NOT_FOUND",
//                      "???????????? ?????? ??? ????????????.");
//          }
//          return null;
//      }
//    }
}
