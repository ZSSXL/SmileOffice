package com.zss.smile.repository;

import com.zss.smile.models.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author ZSS
 * @date 2021/9/6 9:45
 * @desc 文档信息持久化
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, String>,
        JpaSpecificationExecutor<Document> {

    /**
     * 根据文档类型和用户统计数量
     *
     * @param userId       用户Id
     * @param documentType 文档类型
     * @return 统计数量
     */
    Integer countByUserIdAndDocumentType(String userId, String documentType);

    /**
     * 统计收藏的数量
     *
     * @param userId       用户Id
     * @param documentType 文档类型
     * @param collect      是否收藏
     * @return 统计数量
     */
    Integer countByUserIdAndDocumentTypeAndCollect(String userId, String documentType, Boolean collect);

    /**
     * 通过documentKey获取doc
     *
     * @param docKey key
     * @return doc
     */
    Optional<Document> findByDocumentKey(String docKey);

    /**
     * 删除文档
     *
     * @param docId  文档Id
     * @param userId 用户id
     * @return result
     */
    @Modifying
    Integer deleteByDocIdAndUserId(String docId, String userId);

    /**
     * 通过文档名称和用户id获取文档
     *
     * @param documentName 文档名称
     * @param userId       用户Id
     * @return doc
     */
    Optional<Document> findByDocumentNameAndUserId(String documentName, String userId);

    /**
     * 批量删除文档
     *
     * @param docIds 文档Id
     * @return result
     */
    @Modifying
    @Query(value = "delete from smile_document where doc_id in ?1", nativeQuery = true)
    Integer deleteAllInBatch(List<String> docIds);

    /**
     * 批量获取文档
     *
     * @param docIds 文档Id
     * @return docList
     */
    @Query(value = "select * from smile_document where doc_id in ?1", nativeQuery = true)
    List<Document> findAllByIds(List<String> docIds);
}
