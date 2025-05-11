package com.namtn.media.controller;


import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.ResponseUtil;
import com.namtn.media.core.util.SuccessResponse;
import com.namtn.media.model.main.dto.ReportDto;
import com.namtn.media.model.main.dto.SimpleIdDto;
import com.namtn.media.model.main.vo.Feed;
import com.namtn.media.model.main.vo.PostDetailVo;
import com.namtn.media.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }
    @PostMapping("/create")
    public SuccessResponse<PostDetailVo> create(@RequestParam(value = "image", required = false) MultipartFile[] files,
                                                @RequestParam(value = "content", required = false) String content)
            throws IOException, BusinessException {
        PostDetailVo result = postService.createPost(files, content);
        return ResponseUtil.ok(result);
    }

    @PostMapping("/update")
    public SuccessResponse<String> update(@RequestParam(value = "id",required = false) Long id,
                                          @RequestParam(value = "content",required = false) String content,
                                          @RequestParam(value = "files",required = false) MultipartFile[] files)
            throws IOException, BusinessException{
        postService.updatePost(id,content,files);
        return ResponseUtil.ok("Update success");
    }

    @PostMapping("/get")
    public SuccessResponse<PostDetailVo> getId(@RequestBody SimpleIdDto dto)throws BusinessException{
        return ResponseUtil.ok(postService.getPostDetail(dto.getId()));
    }

    @PostMapping("/like")
    public SuccessResponse<String> like(@RequestBody SimpleIdDto id){
        postService.likePost(id.getId());
        return ResponseUtil.ok("Success");
    }

    @PostMapping("/delete")
    public SuccessResponse<String> delete(@RequestBody SimpleIdDto id) throws BusinessException{
        postService.deletePost(id.getId());
        return ResponseUtil.ok("Delete success");
    }

    @GetMapping("/new-feed")
    public SuccessResponse<List<Feed>> getNewFeed() throws BusinessException{
        return ResponseUtil.ok(postService.getNewsFeed());
    }

    @GetMapping("/{email}")
    public SuccessResponse<List<Feed>> getByEmail(@PathVariable("email") String email)throws BusinessException{
        return ResponseUtil.ok(postService.getMyPosts(email));
    }

    @PostMapping("/report")
    public SuccessResponse<String> report(@RequestBody ReportDto dto) throws BusinessException{
        postService.reportPost(dto);
        return ResponseUtil.ok("Report success");
    }

    @GetMapping("/trending")
    public SuccessResponse<List<Feed>> getTrending(){
        return ResponseUtil.ok(postService.getTrending());
    }

}

