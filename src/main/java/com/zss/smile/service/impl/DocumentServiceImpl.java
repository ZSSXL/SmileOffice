package com.zss.smile.service.impl;

import com.zss.smile.common.enums.FileTypeEnum;
import com.zss.smile.helpers.DocumentManager;
import com.zss.smile.helpers.FileUtility;
import com.zss.smile.models.entities.Document;
import com.zss.smile.models.entities.RecycleBin;
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
import com.zss.smile.repository.RecycleBinRepository;
import com.zss.smile.service.DocumentService;
import com.zss.smile.util.DateUtil;
import com.zss.smile.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.io.File;
import java.util.*;

/**
 * @author ZSS
 * @date 2021/9/6 9:48
 * @desc 服务层接口方法实现
 */
@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final RecycleBinRepository recycleBinRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, RecycleBinRepository recycleBinRepository) {
        this.documentRepository = documentRepository;
        this.recycleBinRepository = recycleBinRepository;
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
                .collect(false)
                .recycle(false)
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
                .collect(false)
                .recycle(false)
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
    public Page<DocVo> getDocByPage(String userId, QueryDocVo queryVo) {
        // 根据文档创建时间进行排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(queryVo.getPage(), queryVo.getSize(), sort);

        Page<Document> result = documentRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 是否仅查询收藏的文档
            if (queryVo.getCollect()) {
                predicates.add(cb.equal(root.get("collect").as(Boolean.class), true));
            }

            // 根据文档类型查询
            if (StringUtils.isNotEmpty(queryVo.getDocumentType())) {
                predicates.add(cb.equal(root.get("documentType").as(String.class),
                        queryVo.getDocumentType()));
            }

            // 查询未处于回收状态下的文档
            predicates.add(cb.equal(root.get("recycle").as(Boolean.class), false));

            // 查询对应用户
            predicates.add(cb.equal(root.get("userId").as(String.class), userId));

            Predicate[] p = new Predicate[predicates.size()];
            return cb.and(predicates.toArray(p));
        }, pageable);

        List<DocVo> docVoList = changePageContent(result.getContent());
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
    public Boolean deleteDocument(String userId, String docId) {
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

    @Override
    public Map<String, Integer> countCollectDocument(String userId) {
        Map<String, Integer> countResult = new HashMap<>(3);
        for (FileTypeEnum fileTypeEnum : FileTypeEnum.values()) {
            Integer count = documentRepository.countByUserIdAndDocumentTypeAndCollect(userId, fileTypeEnum.get(), true);
            countResult.put(fileTypeEnum.get(), count);
        }
        return countResult;
    }

    @Override
    public Page<DocVo> getDocInRecycle(String userId, QueryDocVo queryVo, List<String> docIds) {
        // 根据文档创建时间进行排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(queryVo.getPage(), queryVo.getSize(), sort);

        Page<Document> result = documentRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 根据文档类型查询
            if (StringUtils.isNotEmpty(queryVo.getDocumentType())) {
                predicates.add(cb.equal(root.get("documentType").as(String.class),
                        queryVo.getDocumentType()));
            }

            // 批量查询文档
            CriteriaBuilder.In<Object> docIdIn = cb.in(root.get("docId"));
            for (String docId : docIds) {
                docIdIn.value(docId);
            }
            predicates.add(docIdIn);

            // 查询处于回收状态下的文档
            predicates.add(cb.equal(root.get("recycle").as(Boolean.class), true));

            // 查询对应用户
            predicates.add(cb.equal(root.get("userId").as(String.class), userId));

            Predicate[] p = new Predicate[predicates.size()];
            return cb.and(predicates.toArray(p));
        }, pageable);

        List<DocVo> docVoList = changePageContentForRecycle(result.getContent(), userId);
        return new PageImpl<>(docVoList, pageable, result.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllExpiredDoc(List<String> docIds) {
        Integer result = documentRepository.deleteAllInBatch(docIds);
        log.info("删除过期回收站文档数量: [{}]", result);
    }

    @Override
    public List<Document> getAllDocument(List<String> docIds) {
        return documentRepository.findAllByIds(docIds);
    }

    /**
     * 修改分页返回字段
     *
     * @param content 分页内容
     * @return list
     */
    private List<DocVo> changePageContent(List<Document> content) {
        List<DocVo> docVoList = new ArrayList<>();
        for (Document document : content) {
            docVoList.add(DocVo.builder()
                    .docId(document.getDocId())
                    .documentName(document.getDocumentName())
                    .documentKey(document.getDocumentKey())
                    .documentSize(document.getDocumentSize())
                    .collect(document.getCollect())
                    .documentType(FileUtility.getFileTypeEnum(document.getDocumentName()).get())
                    .createTime(String.valueOf(30 - DateUtil.daysFromNow(document.getCreateTime())))
                    .updateTime(DateUtil.timestampToDate(document.getUpdateTime()))
                    .build());
        }
        return docVoList;
    }

    /**
     * 修改分页返回字段
     *
     * @param content 分页内容
     * @return list
     */
    private List<DocVo> changePageContentForRecycle(List<Document> content, String userId) {
        List<DocVo> docVoList = new ArrayList<>();
        RecycleBin recycleBin;
        for (Document document : content) {
            String docId = document.getDocId();
            recycleBin = recycleBinRepository.findByDocIdAndUserId(docId, userId);
            docVoList.add(DocVo.builder()
                    .docId(document.getDocId())
                    .documentName(document.getDocumentName())
                    .documentSize(document.getDocumentSize())
                    .documentType(FileUtility.getFileTypeEnum(document.getDocumentName()).get())
                    .createTime(String.valueOf(30 - DateUtil.daysFromNow(recycleBin.getCreateTime())))
                    .build());
        }
        return docVoList;
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
