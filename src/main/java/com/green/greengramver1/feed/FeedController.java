package com.green.greengramver1.feed;

import com.green.greengramver1.feed.model.FeedGetReq;
import com.green.greengramver1.feed.model.FeedGetRes;
import com.green.greengramver1.feed.model.FeedPostReq;
import com.green.greengramver1.feed.model.FeedPostRes;
import com.green.greengramver1.common.model.ResultResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("feed")
@Tag(name="2. 피드", description = "피드 관리")
public class FeedController {
    private final FeedService service;

    @PostMapping
    public ResultResponse<FeedPostRes> postFeed(@RequestPart List<MultipartFile> pics
                                              , @RequestPart FeedPostReq p) {
        FeedPostRes res = service.postFeed(pics, p);
        return ResultResponse.<FeedPostRes>builder()
                .resultMessage("피드 등록 완료")
                .resultData(res)
                .build();
    }
    //QueryString: URL?KEY=Value
    @GetMapping
    public ResultResponse<List<FeedGetRes>> getFeedList(@ParameterObject @ModelAttribute FeedGetReq p) {
        //@ParameterObject: Swagger에서 쓰는 @RequestParam
        //쿼리스트링 쓰려면 @ModelAttribute, 그러나 이건 생략가능.
        log.info("p:{}",p);

        List<FeedGetRes> list=service.getFeedList(p);
        return ResultResponse.<List<FeedGetRes>>builder()
                .resultMessage(String.format("%d개의 결과, %d 페이지", list.size(),p.getPage()))
                .resultData(list)
                .build();
        // 원래 new로 객체생성하여 return해도 됨. 이건 취향껏.
        // build 패턴은 멤버필드가 많고 이뮤터블 한 객체를 일부 멤버필드만 지정하여 생성하고 싶을 때 사용함.(나머지 멤버필드는 디폴트)
    }
}
