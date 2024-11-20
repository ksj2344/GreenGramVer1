package com.green.greengramver1.user;


import com.green.greengramver1.common.MyFileUtils;
import com.green.greengramver1.user.model.UserInsReq;
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

    public int postSignUp(MultipartFile pic, UserInsReq p){
        //프로필 이미지 파일처리
        //String savedPicName=myFileUtils.makeRandomFileName(pic.getOriginalFilename());
        String savedPicName=(pic != null? myFileUtils.makeRandomFileName(pic):null);
        p.setPic(savedPicName);

        //비밀번호 처리
        String hashedPassword= BCrypt.hashpw(p.getUpw(), BCrypt.gensalt()); //비밀번호 암호화
        log.info("hashedPassword:{}",hashedPassword);
        p.setUpw(hashedPassword);

        int result=mapper.insUser(p); //userId에 값 보내려면 일단 여기서 먼저 insUser 호출하여야함
        if(pic ==null){ return result; } //pic이 null이라면 아래 과정 실행하지 않음.

        // user/${userId}/${savedPicName}   디렉토리/PK를 이름으로 가진 디렉토리/파일명
        long userId=p.getUserId(); //insert하며 받은 pk를 파일명으로 사용

        String middlePath=String.format("user/%d", userId); //user폴더에 userId폴더 만들기
        myFileUtils.makeFolders(middlePath);
        log.info("middlePath:{}",middlePath);

        String filePath=String.format("%s/%s", middlePath, savedPicName); //path 뒤에 파일명 붙이기
        try {
            myFileUtils.transferTo(pic,filePath); //tranferTo가 throws하였으니 예외처리
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
