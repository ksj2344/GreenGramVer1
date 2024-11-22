package com.green.greengramver1.user;


import com.green.greengramver1.common.model.ResultResponse;
import com.green.greengramver1.user.model.UserInsReq;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final UserService service;

    /*
        파일(MultipartFile)+Data
        파일 업로드 시에는 RequestBody를 사용할 수 없음. RequestPart 에노테이션을 사용하여야함.
        pic 파일 여러개 받으려면 List<MultipartFile>써야함
    */
    @PostMapping("sign-up")
    @Operation(summary = "회원 가입")
    public ResultResponse<Integer> signUp(@RequestPart UserInsReq p
                                         ,@RequestPart(required = false) MultipartFile pic){
        log.info("UserInsReq:{}, file:{}", p, pic !=null? pic.getOriginalFilename():null);
        int result= service.postSignUp(pic,p);
        return ResultResponse.<Integer>builder()
                .resultMessage("회원가입 완료")
                .resultData(result)
                .build();
    }
}
