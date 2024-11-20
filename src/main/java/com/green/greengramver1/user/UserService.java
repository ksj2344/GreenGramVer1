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
        String savedPicName=myFileUtils.makeRandomFileName(pic);
        String hashedPassword= BCrypt.hashpw(p.getUpw(), BCrypt.gensalt()); //비밀번호 암호화
        log.info("hashedPassword:{}",hashedPassword);
        p.setUpw(hashedPassword);
        p.setPic(savedPicName);
        int result=mapper.insUser(p); //userId에 값 보내려면 일단 여기서 먼저 insUser 호출하여야함
        long userId=p.getUserId(); //userId는 insert 후에 얻을 수 있다.

        // user/${userId}/${savedPicName}
        String middlePath=String.format("user/%d", userId); //user폴더에 userId폴더 만들기
        myFileUtils.makeFolders(middlePath);
        log.info("middlePath:{}",middlePath);
        String filePath=String.format("%s/%s", middlePath, savedPicName); //path 뒤에 파일명 붙이기
        try {
            myFileUtils.transferTo(pic,filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
