package com.green.greengramver1.feed;

import com.green.greengramver1.common.MyFileUtils;
import com.green.greengramver1.feed.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedMapper mapper;  //DI받음
    private final MyFileUtils myFileUtils;  //DI받음

    public FeedPostRes postFeed(List<MultipartFile> pics, FeedPostReq p){
        mapper.insFeed(p);
        //파일 저장
        //위치: feed/${feedPK}/
        String middlePath = String.format("feed/%d",p.getFeedId());
        //폴더 만들기
        myFileUtils.makeFolders(middlePath);
        //파일 저장하기
        FeedPicDto feedPicDto = new FeedPicDto();   //DTO: DATA Transfer Object
        feedPicDto.setFeedId(p.getFeedId());

        List<String> picture=new ArrayList<>();
        for(MultipartFile pic : pics){
            String savedPicName=myFileUtils.makeRandomFileName(pic);
            String filePaths=String.format("%s/%s",middlePath,savedPicName);
            try{
                myFileUtils.transferTo(pic,filePaths);
            }catch(IOException e){
                e.printStackTrace();
            }
            feedPicDto.setPic(savedPicName);
            mapper.insFeedPic(feedPicDto); //두개의 테이블에 저장해야해서 매퍼가 두번 필요

            picture.add(savedPicName);
        }
        FeedPostRes res = new FeedPostRes();
        res.setFeedId(p.getFeedId());
        res.setPics(picture);
        return res;
    }

    public List<FeedGetRes> getFeedList(FeedGetReq p){
        List<FeedGetRes> list= mapper.selFeedList(p);

        for(FeedGetRes res : list){
            //DB에서 각 피드에 맞는 사진 정보를 가져온다.
            List<String> picList=mapper.selFeedPicList(res.getFeedId());
            res.setPics(picList);
        } //mapper메소드(select로 가져온 pic들)를 res(각 FeedGetRes의 주소를 복사한 레퍼런스변수)의 Pics 멤버필드에 저장.
        return list;
    }
    /*
        여기서 getFeedList는 몇번 셀렉트 하게 될까? 최초의 셀렉 한번+피드의 갯수, 즉 1+N이슈가 발생하여 성능이 저하된다.
        최대한 셀렉트를 줄이는 것이 개발자가 해결할 과제이다.
     */
}
