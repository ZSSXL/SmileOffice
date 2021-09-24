package com.zss.smile.controller;

import com.zss.smile.common.SmileConstant;
import com.zss.smile.common.anno.RequiredPermission;
import com.zss.smile.common.response.ServerResponse;
import com.zss.smile.helpers.DocumentManager;
import com.zss.smile.models.entities.Document;
import com.zss.smile.models.vo.DocVo;
import com.zss.smile.models.vo.QueryDocVo;
import com.zss.smile.service.DocumentService;
import com.zss.smile.service.RecycleBinService;
import com.zss.smile.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZSS
 * @date 2021/9/23 17:05
 * @desc 回收站操作
 */
@Slf4j
@RestController
@RequestMapping("/recycle")
public class RecycleBinController {

    @Value("${resources.files.location}")
    private String baseStorageFolder;

    private final DocumentService documentService;
    private final RecycleBinService recycleBinService;

    @Autowired
    public RecycleBinController(DocumentService documentService, RecycleBinService recycleBinService) {
        this.documentService = documentService;
        this.recycleBinService = recycleBinService;
    }

    /**
     * 获取文档列表
     *
     * @param query 查询条件
     * @return List
     */
    @PostMapping("/page")
    @RequiredPermission
    public ServerResponse<Page<DocVo>> listDoc(@RequestHeader(SmileConstant.SMILE_TOKEN) String token,
                                               @RequestBody QueryDocVo query) {
        String userId = TokenUtil.getClaim(token, "userId").asString();
        // 1. 获取未过期的文档Id
        List<String> docIds = recycleBinService.unExpired(userId);
        // 2. 获取未过期的文档详情
        Page<DocVo> page = documentService.getDocInRecycle(userId, query, docIds);
        return ServerResponse.success(page);
    }

    /**
     * 还原
     *
     * @return result
     */
    @GetMapping("/reduce/{docId}")
    @RequiredPermission
    public ServerResponse<String> reduce(@RequestHeader(SmileConstant.SMILE_TOKEN) String token,
                                         @PathVariable("docId") String docId) {
        String userId = TokenUtil.getClaim(token, "userId").asString();
        Document document = documentService.getDocument(docId);
        document.setRecycle(false);
        documentService.updateDocument(document);

        recycleBinService.deleteRecord(userId, docId);

        return ServerResponse.success();
    }

    /**
     * 强制删除文档
     *
     * @param token 用户token
     * @param docId 文档Id
     * @return result
     */
    @GetMapping("/delete/force/{docId}")
    @RequiredPermission
    public ServerResponse<String> forceDelete(@RequestHeader(SmileConstant.SMILE_TOKEN) String token,
                                              @PathVariable("docId") String docId) {
        String loginName = TokenUtil.getClaim(token, "loginName").asString();
        String userId = TokenUtil.getClaim(token, "userId").asString();

        Document document = documentService.getDocument(docId);

        if (document == null) {
            return ServerResponse.error("文档删除失败, 没有该文档: [" + docId + "]");
        }

        // 删除文件
        DocumentManager
                .deleteDocument(loginName, document.getDocumentName(), baseStorageFolder);
        // 删除文档记录
        documentService.deleteDocument(userId, docId);
        // 删除回收站记录
        recycleBinService.deleteRecord(userId, docId);

        return ServerResponse.success("强制删除成功");
    }

}
