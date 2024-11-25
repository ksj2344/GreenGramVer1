package com.green.greengramver1.user;


import com.green.greengramver1.common.model.ResultResponse;
import com.green.greengramver1.user.model.UserSignInRes;
import com.green.greengramver1.user.model.UserSignUpReq;
import com.green.greengramver1.user.model.UserSignInReq;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService service;

    /*
        파일(MultipartFile)+Data
        파일 업로드 시에는 RequestBody를 사용할 수 없음.
        RequestPart는 파일을 받을 때 사용하는 에노테이션. <-얘를 써야함.
        pic 파일 여러개 받으려면 List<MultipartFile>써야함

        RequestPart, RequestParam은 파라미터명이 중요하고(어디에 뭐가 들어가는지가 중함)
        RequestBody만 파라미터명이 중요하지 않다(JSON으로 받기때문)
    */
    @PostMapping("sign-up")
    @Operation(summary = "회원 가입")
    public ResultResponse<Integer> signUp(@RequestPart UserSignUpReq p
                                         ,@RequestPart(required = false) MultipartFile pic){
                                        //required는 기본값이 true이다. false로 설정하면 필수로 받지 않아도 된다.
        log.info("UserInsReq:{}, file:{}", p, pic !=null? pic.getOriginalFilename():null);
        //pic이 레퍼런스 변수라서 null.getOriginalFilename()이 된다면 오류가 발생한다. 그래서 null check한 것.
        int result= service.postSignUp(pic,p);
        return ResultResponse.<Integer>builder()
                .resultMessage("회원가입 완료")
                .resultData(result)
                .build();
    }

    @PostMapping("sign-in")
    @Operation(summary = "로그인")
    public ResultResponse<UserSignInRes> signIn(@RequestBody UserSignInReq p){
        UserSignInRes res = service.postSignIn(p);
        return ResultResponse.<UserSignInRes>builder()
                .resultMessage(res.getMessage())
                .resultData(res)
                .build();
    }
}
