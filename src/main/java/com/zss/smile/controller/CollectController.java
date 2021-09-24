package com.zss.smile.controller;

import com.zss.smile.common.SmileConstant;
import com.zss.smile.common.anno.RequiredPermission;
import com.zss.smile.common.response.ServerResponse;
import com.zss.smile.models.entities.Document;
import com.zss.smile.models.vo.DocVo;
import com.zss.smile.models.vo.QueryDocVo;
import com.zss.smile.service.DocumentService;
import com.zss.smile.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ZSS
 * @date 2021/9/23 13:29
 * @desc 文档收藏控制器
 */
@Slf4j
@RestController
@RequestMapping("/collect")
public class CollectController {

    private final DocumentService documentService;

    @Autowired
    public CollectController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * 添加收藏
     *
     * @param docId 文档Id
     * @return result
     */
    @GetMapping("/add/{docId}")
    @RequiredPermission
    public ServerResponse<String> collect(@PathVariable("docId") String docId) {
        Document document = documentService.getDocument(docId);
        if (document == null) {
            return ServerResponse.error("该文档不存在, 你收藏个der啊, 文档Id[" + docId + "]");
        } else {
            if (!document.getCollect()) {
                document.setCollect(true);
                documentService.updateDocument(document);
            }
            return ServerResponse.success("收藏成功");
        }
    }

    /**
     * 取消收藏
     *
     * @param docId 文档Id
     * @return result
     */
    @GetMapping("/cancel/{docId}")
    @RequiredPermission
    public ServerResponse<String> cancel(@PathVariable("docId") String docId) {
        Document document = documentService.getDocument(docId);
        if (document == null) {
            return ServerResponse.error("该文档不存在, 你取消个der啊, 文档Id[" + docId + "]");
        } else {
            if (document.getCollect()) {
                document.setCollect(false);
                documentService.updateDocument(document);
            }
            return ServerResponse.success("取消收藏成功");
        }
    }

    /**
     * 获取收藏统计
     *
     * @param token 用户Token
     * @return 统计结果
     */
    @GetMapping("/count")
    @RequiredPermission
    public ServerResponse<Map<String, Integer>> count(@RequestHeader(SmileConstant.SMILE_TOKEN) String token) {
        String userId = TokenUtil.getClaim(token, "userId").asString();
        Map<String, Integer> collectCount = documentService.countCollectDocument(userId);
        return ServerResponse.success(collectCount);
    }
}
