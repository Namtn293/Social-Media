package com.namtn.media.controller;

import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.ResponseUtil;
import com.namtn.media.core.util.SuccessResponse;
import com.namtn.media.model.main.dto.CommentDto;
import com.namtn.media.model.main.dto.SimpleIdDto;
import com.namtn.media.model.main.vo.CommentVo;
import com.namtn.media.service.CommentService;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping("/create")
    public SuccessResponse<CommentVo> create(@RequestBody CommentDto dto)throws BusinessException {
        return ResponseUtil.ok(commentService.createComment(dto));
    }

    @PostMapping("/get")
    public SuccessResponse<List<CommentVo>> getAllCmtOfPost(@RequestBody SimpleIdDto postId) throws BusinessException{
        return ResponseUtil.ok(commentService.getAllComments(postId));
    }

    @PostMapping("/delete")
    public SuccessResponse<String> delete(@RequestBody SimpleIdDto id) throws BusinessException{
        commentService.deleteComment(id);
        return ResponseUtil.ok("Delete Success");
    }
}
