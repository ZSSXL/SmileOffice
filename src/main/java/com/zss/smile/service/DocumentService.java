package com.zss.smile.service;

import com.zss.smile.models.entities.Document;
import com.zss.smile.models.entities.User;
import com.zss.smile.models.office.OfficeConfig;
import com.zss.smile.models.vo.DocVo;
import com.zss.smile.models.vo.QueryDocVo;
import com.zss.smile.models.vo.RenameVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author ZSS
 * @date 2021/9/6 9:47
 * @desc 文档信息服务层接口
 */
public interface DocumentService {

    /**
     * 构建文档配置
     *
     * @param userInfo          用户信息
     * @param newDoc            新文件名称
     * @param baseStorageFolder 基础路径
     * @return config
     */
    OfficeConfig buildOfficeConfig(User userInfo, String newDoc, String baseStorageFolder);

    /**
     * 上传文档
     *
     * @param userId           用户Id
     * @param originalFilename 文件名
     * @param fileSize         文件大小
     */
    void uploadDocument(String userId, String originalFilename, String fileSize);

    /**
     * 通过Id获取文档数据
     *
     * @param docId 文档Id
     * @return Doc
     */
    Document getDocument(String docId);

    /**
     * 预览文档
     *
     * @param user              用户
     * @param doc               文档
     * @param baseStorageFolder 基础路径
     * @return cfg
     */
    OfficeConfig reviewDoc(User user, Document doc, String baseStorageFolder);

    /**
     * 统计用户的文档数量
     *
     * @param userId 用户Id
     * @return Map
     */
    Map<String, Integer> countDoc(String userId);

    /**
     * 分页查询用户的文档
     *
     * @param userId  用户Id
     * @param query   查询条件
     * @return page
     */
    Page<DocVo> getDocByPage(String userId, QueryDocVo query);

    /**
     * 重命名文档
     *
     * @param renameVo 重命名数据
     */
    void renameDocument(RenameVo renameVo);

    /**
     * 更新文档
     *
     * @param document 更新数据
     */
    void updateDocument(Document document);

    /**
     * 通过key获取对应的文档
     *
     * @param docKey key
     * @return Doc
     */
    Document getDocumentByKey(String docKey);

    /**
     * 删除文档
     *
     * @param docId  文档Id
     * @param userId 用户Id
     * @return result
     */
    Boolean deleteDocument(String userId, String docId);

    /**
     * 检验是否有同名文件
     *
     * @param originalFilename 原文件名
     * @param userId           用户Id
     * @return docName
     */
    String checkDocumentName(String originalFilename, String userId);

    /**
     * 校验文档是否已经存在
     *
     * @param documentName 文档名称
     * @param userId       用户Id
     * @return exist
     */
    Boolean existInDbByDocName(String documentName, String userId);

    /**
     * 获取用户的收藏统计数量
     *
     * @param userId 用户Id
     * @return 统计结果
     */
    Map<String, Integer> countCollectDocument(String userId);

    /**
     * 获取回收且未过期的文档数据
     *
     * @param userId 用户Id
     * @param query  查询条件
     * @param docIds ids
     * @return page
     */
    Page<DocVo> getDocInRecycle(String userId, QueryDocVo query, List<String> docIds);

    /**
     * 删除所有回收站中过期的文档
     *
     * @param docIds 文档Id
     */
    void deleteAllExpiredDoc(List<String> docIds);

    /**
     * 通过Id获取文档信息
     *
     * @param docIds ids
     * @return docList
     */
    List<Document> getAllDocument(List<String> docIds);
}
