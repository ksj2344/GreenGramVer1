package com.green.greengramver1.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInRes {
    private long userId;
    private String pic;
    private String nickName;
    @JsonIgnore
    private String upw;
    @JsonIgnore
    private String message; //응답할 때 나오는 메세지 전달용
}
