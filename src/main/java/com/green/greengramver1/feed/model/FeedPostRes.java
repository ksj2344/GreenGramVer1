package com.green.greengramver1.feed.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FeedPostRes {
    // feed PK값과 파일이름 여러개를 리턴할 수 있어야한다.
    private long feedId;
    private List<String> pics;
}
