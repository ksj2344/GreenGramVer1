package com.green.greengramver1.user;


import com.green.greengramver1.common.MyFileUtils;
import com.green.greengramver1.user.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final MyFileUtils myFileUtils;

    public int postSignUp(MultipartFile pic, UserSignUpReq p){
        //프로필 이미지 파일처리
        //String savedPicName=myFileUtils.makeRandomFileName(pic.getOriginalFilename());
        String savedPicName=(pic != null? myFileUtils.makeRandomFileName(pic):null);
        p.setPic(savedPicName);

        //비밀번호 처리
        String hashedPassword= BCrypt.hashpw(p.getUpw(), BCrypt.gensalt()); //비밀번호 암호화
        // gensalt, salting: 섞기. 같은 비밀번호가 입력되어도 암호화 후 다른 값이 나오기 위한 것.
        log.info("hashedPassword:{}",hashedPassword);
        p.setUpw(hashedPassword);

        int result=mapper.insUser(p); //userId에 값 보내려면 일단 여기서 먼저 insUser 호출하여야함. 리턴되는값: 영향받은행(0or1)
        if(pic ==null){ return result; } //pic이 null이라면 아래 과정 실행하지 않음.

        // 저장위치/파일명 만들기
        // user/${userId}/${savedPicName}   디렉토리/PK를 이름으로 가진 디렉토리/파일명
        long userId=p.getUserId(); //insert하며 받은 pk를 파일명으로 사용
        String middlePath=String.format("user/%d", userId); //user폴더에 userId폴더 만들기
        myFileUtils.makeFolders(middlePath);
        log.info("middlePath:{}",middlePath);

        String filePath=String.format("%s/%s", middlePath, savedPicName); //path 뒤에 파일명 붙이기
        try {
            myFileUtils.transferTo(pic,filePath); //tranferTo가 throws하였으니 예외처리
            //transferTo(저장할 파일, 저장할 절대경로) 저장하는 메소드.
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public UserSignInRes postSignIn(UserSignInReq p){
        UserSignInRes res = mapper.selUserForSignIn(p);
        if(res==null){ //Req의 uid가 테이블에 있는 uid와 일치하는 것이 없음(=id 틀렸음)
            res= new UserSignInRes();
            res.setMessage("아이디를 확인 해 주세요.");
            return res;
        } else if(!BCrypt.checkpw(p.getUpw(), res.getUpw())){ //비밀번호가 다를시.
        //BCrypt.checkpw(암호화 전 비밀번호, 받은 비밀번호) 체크 후 boolean 반환하는 메소드.
            res= new UserSignInRes();
            res.setMessage("비밀번호를 확인 해 주세요.");
            return res;
        }
        res.setMessage("로그인 성공");
        return res;
    }
}
