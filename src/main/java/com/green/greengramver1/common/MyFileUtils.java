package com.green.greengramver1.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component //빈등록(빈: Spring 컨테이너가 객체관리를 한다.=spring이 대리로 객체화 하고 주소값을 갖고있다가 내가 필요하면 줘라.)
public class MyFileUtils {
    private final String uploadPath;

    /*
        @Value("${file.directory}"은
        yaml 파일에 있는 file.directory 속성에 저장된 값을 생성자 호출할 때 값을 넣어준다.
     */
    public MyFileUtils(@Value("${file.directory}") String uploadPath) {
        log.info("MyFileUtils - 생성자: {}", uploadPath);
        this.uploadPath = uploadPath;
    }

    // path="ddd/aaa"
    // D:/ksj/download/greengram_ver1/ddd/aaa  이렇게 경로가 덧붙여진 파일 만들어줌.
    // 디렉토리 생성
    public String makeFolders(String path){
        File file = new File(uploadPath, path);
        file.mkdirs(); //mkdir 메소드는 파일안에 파일까지는 감지불가
        return file.getAbsolutePath();
    }
    //makeFolders 메소드 안씀.

    //파일명에서 확장자 추출
    public String getExt(String fileName){
        int lastIdx = fileName.lastIndexOf(".");
        return fileName.substring(lastIdx);
    }

    //랜덤파일명 생성
    public String makeRandomFileName(){
        return UUID.randomUUID().toString();
    }

    //랜덤파일명+확장자 생성
    public String makeRandomFileName(String originalFileName){

        return makeRandomFileName()+getExt(originalFileName);
    }

    //MultipartFile 인터페이스의 getOriginalFilename() 구현은 Spring이 알아서 해줌
    public String makeRandomFileName(MultipartFile file){
        return makeRandomFileName(file.getOriginalFilename());
    }

    //파일을 원하는 경로(path)에 저장
    public void transferTo(MultipartFile mf, String path) throws IOException {
        File file = new File(uploadPath, path);
        mf.transferTo(file);
    }
}

class Test{
    public static void main(String[] args) {
        MyFileUtils myFileUtils = new MyFileUtils("C:/temp");
        System.out.println(myFileUtils.makeRandomFileName("707211_1532672215.jpg"));
    }
}