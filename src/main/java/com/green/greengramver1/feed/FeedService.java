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
        }
        return list;
    }
}
