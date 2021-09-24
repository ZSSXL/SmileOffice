package com.zss.smile.schedule;

import com.zss.smile.helpers.DocumentManager;
import com.zss.smile.models.entities.Document;
import com.zss.smile.models.entities.User;
import com.zss.smile.service.DocumentService;
import com.zss.smile.service.RecycleBinService;
import com.zss.smile.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZSS
 * @date 2021/9/24 9:44
 * @desc 定时清除回收站过时数据 --
 */
@Component
public class CleanRecycleBinTask {

    @Value("${resources.files.location}")
    private String baseStorageFolder;

    private final DocumentService documentService;
    private final UserService userService;
    private final RecycleBinService recycleBinService;


    @Autowired
    public CleanRecycleBinTask(DocumentService documentService, UserService userService, RecycleBinService recycleBinService) {
        this.documentService = documentService;
        this.userService = userService;
        this.recycleBinService = recycleBinService;
    }

    /**
     * 定时删除过期的回收站数据 -- 每周日 23:50执行
     */
    @Scheduled(cron = "0 50 23 ? * 1 *")
    public void cleanUp() {
        List<String> docIds = recycleBinService.getExpired();

        // 1. 删除文件
        Map<String, String> userMap = new HashMap<>(docIds.size());
        List<Document> docList = documentService.getAllDocument(docIds);
        for (Document document : docList) {
            // 先从缓存获取
            String loginName = userMap.get(document.getUserId());
            if (StringUtils.isEmpty(loginName)) {
                User user = userService.getUserById(document.getUserId());
                // 添加至缓存
                userMap.put(user.getUserId(), user.getLoginName());
                loginName = user.getLoginName();
            }
            DocumentManager
                    .deleteDocument(loginName, document.getDocumentName(), baseStorageFolder);
        }

        // 2. 删除文档记录
        documentService.deleteAllExpiredDoc(docIds);

        // 3. 删除回收站记录
        recycleBinService.cleanUpAllInExpired();
    }
}
