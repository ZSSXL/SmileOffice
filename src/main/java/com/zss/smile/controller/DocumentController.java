package com.zss.smile.controller;

import com.alibaba.fastjson.JSONObject;
import com.zss.smile.common.SmileConstant;
import com.zss.smile.common.anno.RequiredPermission;
import com.zss.smile.common.response.ServerResponse;
import com.zss.smile.helpers.DocumentManager;
import com.zss.smile.models.entities.Document;
import com.zss.smile.models.entities.User;
import com.zss.smile.models.office.OfficeConfig;
import com.zss.smile.models.vo.DocVo;
import com.zss.smile.models.vo.QueryDocVo;
import com.zss.smile.models.vo.RenameVo;
import com.zss.smile.service.DocumentService;
import com.zss.smile.service.RecycleBinService;
import com.zss.smile.service.UserService;
import com.zss.smile.util.DateUtil;
import com.zss.smile.util.FileSizeUtil;
import com.zss.smile.util.IdUtil;
import com.zss.smile.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author ZSS
 * @date 2021/9/6 13:38
 * @desc 文件操作
 */
@Slf4j
@RestController
@RequestMapping("/doc")
public class DocumentController {

    @Value("${resources.files.location}")
    private String baseStorageFolder;

    private final DocumentService documentService;
    private final UserService userService;
    private final RecycleBinService recycleBinService;

    @Autowired
    public DocumentController(DocumentService documentService, UserService userService, RecycleBinService recycleBinService) {
        this.documentService = documentService;
        this.userService = userService;
        this.recycleBinService = recycleBinService;
    }

    /**
     * 创建空文件
     *
     * @return string
     */
    @GetMapping("/create")
    @RequiredPermission
    public ServerResponse<OfficeConfig> createDoc(@RequestHeader(SmileConstant.SMILE_TOKEN) String token,
                                                  @RequestParam("fileExts") String fileExts) {
        String userId = TokenUtil.getClaim(token, "userId").asString();
        User userInfo = userService.getUserById(userId);
        // xxx.xxx eg: example.docx
        String document = DocumentManager
                .createNewDoc(userInfo.getLoginName(), fileExts, baseStorageFolder);
        OfficeConfig officeCfg = documentService
                .buildOfficeConfig(userInfo, document, baseStorageFolder);
        return ServerResponse.success(officeCfg);
    }

    /**
     * 上传文档
     *
     * @param token   用户token
     * @param request request
     * @return String
     */
    @PostMapping("/upload")
    @RequiredPermission
    public ServerResponse<String> uploadDoc(@RequestHeader(SmileConstant.SMILE_TOKEN) String token,
                                            HttpServletRequest request) {
        String loginName = TokenUtil.getClaim(token, "loginName").asString();
        String userId = TokenUtil.getClaim(token, "userId").asString();
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile document = multipartHttpServletRequest.getFile("document");
        if (document != null) {
            String originalFilename = document.getOriginalFilename();
            // 校验是否有同名文件
            String documentName = documentService.checkDocumentName(originalFilename, userId);

            try {
                DocumentManager.saveDocument(document.getInputStream(), documentName, loginName, baseStorageFolder);
            } catch (IOException e) {
                log.error("上传文件，获取输入流失败: [{}]", e.getMessage());
            }
            int size = (int) document.getSize();
            documentService.uploadDocument(userId, documentName, FileSizeUtil.getSize(size));
            return ServerResponse.success("上传文档[" + documentName + "]成功");
        } else {
            return ServerResponse.error("上传文件不能为空");
        }
    }

    /**
     * 统计用户的文档
     *
     * @param token 用户token
     * @return e
     */
    @GetMapping("/count")
    @RequiredPermission
    public ServerResponse<Map<String, Integer>> countDoc(@RequestHeader(SmileConstant.SMILE_TOKEN) String token) {
        String userId = TokenUtil.getClaim(token, "userId").asString();
        Map<String, Integer> countResult = documentService.countDoc(userId);
        return ServerResponse.success(countResult);
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
        Page<DocVo> page = documentService.getDocByPage(userId, query);
        return ServerResponse.success(page);
    }

    /**
     * 预览文件
     *
     * @return string
     */
    @GetMapping("/review/{docId}")
    @RequiredPermission
    public ServerResponse<OfficeConfig> reviewDoc(@PathVariable("docId") String docId) {
        Document doc = documentService.getDocument(docId);
        if (doc == null) {
            return ServerResponse.error("Id为[" + docId + "]的文档数据不存在");
        } else {
            User user = userService.getUserById(doc.getUserId());
            OfficeConfig officeCfg = documentService
                    .reviewDoc(user, doc, baseStorageFolder);
            return ServerResponse.success(officeCfg);
        }
    }

    /**
     * 重命名文件
     *
     * @return 重命名结果
     */
    @PostMapping("/rename")
    @RequiredPermission
    public ServerResponse<String> renameDocument(@RequestHeader(SmileConstant.SMILE_TOKEN) String token,
                                                 @RequestBody RenameVo renameVo) {
        String loginName = TokenUtil.getClaim(token, "loginName").asString();
        String userId = TokenUtil.getClaim(token, "userId").asString();
        String newDocumentName = renameVo.getNewDocumentName();
        Boolean result = documentService.existInDbByDocName(newDocumentName, userId);
        if (StringUtils.equals(renameVo.getDocumentName(), newDocumentName)) {
            return ServerResponse.error("新文档名称不能与旧名称同名");
        } else if (result) {
            return ServerResponse.error("文档名[" + newDocumentName + "]已存在，请重新编辑");
        } else {
            Boolean renameResult = DocumentManager.renameDocument(loginName, renameVo.getDocumentName()
                    , newDocumentName, baseStorageFolder);
            if (renameResult) {
                documentService.renameDocument(renameVo);
                return ServerResponse.success("重命名成功", newDocumentName);
            } else {
                return ServerResponse.error("重命名失败");
            }
        }
    }

    /**
     * 删除文档
     *
     * @param docId 文档Id
     * @param token token
     * @return String
     */
    @GetMapping("/delete/{docId}")
    @RequiredPermission
    public ServerResponse<Void> deleteDocument(@PathVariable("docId") String docId,
                                               @RequestHeader(SmileConstant.SMILE_TOKEN) String token) {
        String userId = TokenUtil.getClaim(token, "userId").asString();

        Document document = documentService.getDocument(docId);
        // 删除目录下的文件
        // DocumentManager
        //         .deleteDocument(loginName, document.getDocumentName(), baseStorageFolder)
        // 加入回收站
        // Boolean result = documentService.deleteDocument(docId, userId)
        // 将文档的状态设为回收
        document.setRecycle(true);
        documentService.updateDocument(document);
        // 创建回收记录
        Boolean recycle = recycleBinService.recycle(userId, docId);

        if (recycle) {
            return ServerResponse.success("文件[" + document.getDocumentName() + "]已添加至回收站");
        } else {
            return ServerResponse.error("删除文件[" + document.getDocumentName() + "]失败");
        }
    }

    /**
     * office回调方法 -- 只关注:status == 2
     *
     * @param callbackVo req
     */
    @PostMapping("/callback")
    public void callback(@RequestBody JSONObject callbackVo, HttpServletResponse response) {
        log.info("回调接口请求参数: [{}]", JSONObject.toJSONString(callbackVo));

        String status = callbackVo.get("status").toString();
        if (String.valueOf(SmileConstant.CallbackStatus.READY_FOR_SAVE).equals(status)) {
            String downloadUrl = (String) callbackVo.get("url");
            InputStream inputStream = null;
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                inputStream = connection.getInputStream();
            } catch (Exception e) {
                log.error("下载修改文件失败,URL:[{}], Error: [{}]", downloadUrl, e.getMessage());
            }

            String docKey = (String) callbackVo.get("key");
            Document document = documentService.getDocumentByKey(docKey);
            User userInfo = userService.getUserById(document.getUserId());

            if (inputStream != null) {
                try {
                    document.setDocumentSize(FileSizeUtil.getSize(inputStream.available()));
                } catch (IOException e) {
                    log.error("计算文件大小失败: [{}]", e.getMessage());
                }
                // 更新documentKey -- 每次修改，都要重新生成
                document.setDocumentKey(IdUtil.getId());
                document.setUpdateTime(DateUtil.currentTimestamp());

                // 更新文件
                DocumentManager.updateDoc(document.getDocumentName(), inputStream,
                        baseStorageFolder, userInfo.getLoginName());

                documentService.updateDocument(document);
            }
        }
        try {
            PrintWriter writer = response.getWriter();
            writer.write("{\"error\":" + "0" + "}");
        } catch (IOException e) {
            log.error("Get PrintWriter Error: [{}]", e.getMessage());
        }
    }
}
