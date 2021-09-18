package com.zss.smile.service.impl;

import com.zss.smile.common.enums.FileTypeEnum;
import com.zss.smile.helpers.DocumentManager;
import com.zss.smile.helpers.FileUtility;
import com.zss.smile.models.entities.Document;
import com.zss.smile.models.entities.User;
import com.zss.smile.models.office.DocumentCfg;
import com.zss.smile.models.office.EditorCfg;
import com.zss.smile.models.office.OfficeConfig;
import com.zss.smile.models.office.doc.InfoCfg;
import com.zss.smile.models.office.doc.PermissionCfg;
import com.zss.smile.models.office.editor.CustomizationCfg;
import com.zss.smile.models.office.editor.UserCfg;
import com.zss.smile.models.vo.DocVo;
import com.zss.smile.models.vo.QueryDocVo;
import com.zss.smile.models.vo.RenameVo;
import com.zss.smile.repository.DocumentRepository;
import com.zss.smile.service.DocumentService;
import com.zss.smile.util.DateUtil;
import com.zss.smile.util.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

/**
 * @author ZSS
 * @date 2021/9/6 9:48
 * @desc 服务层接口方法实现
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OfficeConfig buildOfficeConfig(User userInfo, String newDoc, String baseStorageFolder) {

        String docId = IdUtil.getId();
        Document document = Document.builder()
                .docId(docId)
                .userId(userInfo.getUserId())
                .documentName(newDoc)
                .documentKey(docId)
                .documentSize("0.00 KB")
                .documentType(FileUtility.getFileTypeEnum(newDoc).get())
                .createTime(DateUtil.currentTimestamp())
                .updateTime(DateUtil.currentTimestamp())
                .build();
        documentRepository.save(document);
        return buildOfficeConfig(userInfo, document.getDocumentName(), baseStorageFolder, docId);
    }

    @Override
    public void uploadDocument(String userId, String originalFilename, String fileSize) {
        String docId = IdUtil.getId();
        Document document = Document.builder()
                .docId(docId)
                .userId(userId)
                .documentName(originalFilename)
                .documentKey(docId)
                .documentSize(fileSize)
                .documentType(FileUtility.getFileTypeEnum(originalFilename).get())
                .createTime(DateUtil.currentTimestamp())
                .updateTime(DateUtil.currentTimestamp())
                .build();
        documentRepository.save(document);
    }

    @Override
    public Document getDocument(String docId) {
        return documentRepository.findById(docId).orElse(null);
    }

    @Override
    public OfficeConfig reviewDoc(User user, Document doc, String baseStorageFolder) {
        return buildOfficeConfig(user, doc.getDocumentName(), baseStorageFolder, doc.getDocumentKey());
    }

    @Override
    public Map<String, Integer> countDoc(String userId) {
        Map<String, Integer> countResult = new HashMap<>(3);
        for (FileTypeEnum fileTypeEnum : FileTypeEnum.values()) {
            Integer count = documentRepository.countByUserIdAndDocumentType(userId, fileTypeEnum.get());
            countResult.put(fileTypeEnum.get(), count);
        }
        return countResult;
    }

    @Override
    public Page<DocVo> getDocByPage(String userId, QueryDocVo query) {
        // 根据文档创建时间进行排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize(), sort);

        Page<Document> result;
        if (StringUtils.isNotEmpty(query.getDocumentType())) {
            result = documentRepository
                    .findAllByUserIdAndDocumentType(userId, query.getDocumentType(), pageable);
        } else {
            result = documentRepository.findAllByUserId(userId, pageable);
        }

        List<Document> content = result.getContent();
        List<DocVo> docVoList = new ArrayList<>();
        for (Document document : content) {
            docVoList.add(DocVo.builder()
                    .docId(document.getDocId())
                    .documentName(document.getDocumentName())
                    .documentKey(document.getDocumentKey())
                    .documentSize(document.getDocumentSize())
                    .documentType(FileUtility.getFileTypeEnum(document.getDocumentName()).get())
                    .createTime(DateUtil.timestampToDate(document.getCreateTime()))
                    .updateTime(DateUtil.timestampToDate(document.getUpdateTime()))
                    .build());
        }
        return new PageImpl<>(docVoList, pageable, result.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void renameDocument(RenameVo renameVo) {
        Document document = documentRepository.findById(renameVo.getDocId()).orElse(null);
        if (document != null) {
            document.setDocumentName(renameVo.getNewDocumentName());
            document.setUpdateTime(DateUtil.currentTimestamp());
            documentRepository.save(document);
        }
    }

    @Override
    public void updateDocument(Document document) {
        documentRepository.save(document);
    }

    @Override
    public Document getDocumentByKey(String docKey) {
        return documentRepository.findByDocumentKey(docKey).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDocument(String docId, String userId) {
        return documentRepository.deleteByDocIdAndUserId(docId, userId) == 1;
    }

    @Override
    public String checkDocumentName(String originalFilename, String userId) {
        Optional<Document> document = documentRepository.findByDocumentNameAndUserId(originalFilename, userId);
        String newDocumentName = originalFilename;
        if (document.isPresent()) {
            String fileName = FileUtility.getFileNameWithoutExtension(originalFilename);
            String fileExtension = FileUtility.getFileExtension(originalFilename);
            int index = 1;
            do {
                newDocumentName = fileName + "(" + index + ")" + fileExtension;
                document = documentRepository.findByDocumentNameAndUserId(newDocumentName, userId);
                index++;
            } while (document.isPresent());
        }
        return newDocumentName;
    }

    @Override
    public Boolean existInDbByDocName(String documentName, String userId) {
        return documentRepository.findByDocumentNameAndUserId(documentName, userId).isPresent();
    }

    /**
     * 构建OfficeConfig配置
     *
     * @param userInfo          用户信息
     * @param documentName      文档名
     * @param baseStorageFolder 基础路径
     * @param docKey            文档key
     * @return config
     */
    private OfficeConfig buildOfficeConfig(User userInfo, String documentName, String baseStorageFolder, String docKey) {
        // 构建文档配置
        DocumentCfg docConfig = DocumentCfg.builder()
                .key(docKey)
                .fileType(Objects.requireNonNull(FileUtility.getFileExtension(documentName))
                        .replace(".", ""))
                .url(DocumentManager.getServerUrlBase() +
                        File.separator + "files" +
                        File.separator + userInfo.getLoginName() +
                        File.separator + documentName)
                .title(documentName)
                .info(InfoCfg.builder()
                        .favorite(false)
                        .folder(baseStorageFolder + userInfo.getLoginName())
                        .owner(userInfo.getUsername())
                        .uploaded(DateUtil.getDateComplete())
                        .build())
                .permissions(PermissionCfg.builder()
                        .comment(true)
                        .download(true)
                        .edit(true)
                        .fillForms(true)
                        .modifyContentControl(true)
                        .modifyFilter(true)
                        .review(true)
                        .build())
                .build();
        // 构建编辑器配置
        EditorCfg editorConfig = EditorCfg.builder()
                .mode("edit")
                .lang("zh-CN")
                // 回调接口地址
                .callbackUrl(DocumentManager.getServerUrlBase() + "/doc/callback")
                .customization(CustomizationCfg.builder()
                        .compactHeader(false)
                        .compactToolbar(false)
                        .hideRightMenu(false)
                        .hideRulers(false)
                        .spellcheck(false)
                        .build())
                .recent(null)
                .user(UserCfg.builder()
                        .id(userInfo.getUserId())
                        .name(userInfo.getUsername())
                        .build())
                .build();

        return OfficeConfig.builder()
                .type("desktop")
                .mode("edit")
                .documentType(FileUtility.getFileTypeEnum(documentName).get())
                .height("100%")
                .width("100%")
                .document(docConfig)
                .editorConfig(editorConfig)
                .build();
    }
}
