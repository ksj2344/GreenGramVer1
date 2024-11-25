package com.green.greengramver1.feed;

import com.green.greengramver1.feed.model.FeedGetReq;
import com.green.greengramver1.feed.model.FeedGetRes;
import com.green.greengramver1.feed.model.FeedPicDto;
import com.green.greengramver1.feed.model.FeedPostReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedMapper {
    int insFeed(FeedPostReq p);
    int insFeedPic(FeedPicDto p);
    //현재로서는 반환타입 int 안해도 되어서 void해도 상관은 없으나 추후 null처리가 필요해서 int로하는것이 좋다.

    List<FeedGetRes> selFeedList(FeedGetReq p);
    List<String> selFeedPicList(long p);  //현시점에선 feedId 정수만 받으면 되어서 굳이 FeedGetReq 필요x
}

