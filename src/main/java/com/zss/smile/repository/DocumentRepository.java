package com.zss.smile.repository;

import com.zss.smile.models.entities.Document;
import com.zss.smile.models.vo.DocVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author ZSS
 * @date 2021/9/6 9:45
 * @desc 文档信息持久化
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {

    /**
     * 根据文档类型和用户统计数量
     *
     * @param userId       用户Id
     * @param documentType 文档类型
     * @return 统计数量
     */
    Integer countByUserIdAndDocumentType(String userId, String documentType);

    /**
     * 分页查询文档
     *
     * @param userId       用户Id
     * @param documentType 文档类型 -- word,cell,slide
     * @param pageable     分页查询条件
     * @return page
     */
    Page<Document> findAllByUserIdAndDocumentType(String userId, String documentType, Pageable pageable);

    /**
     * 分页查询文档
     *
     * @param userId       用户Id
     * @param pageable     分页查询条件
     * @return page
     */
    Page<Document> findAllByUserId(String userId, Pageable pageable);

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
    Integer deleteByDocIdAndUserId(String docId, String userId);

    /**
     * 通过文档名称和用户id获取文档
     *
     * @param documentName 文档名称
     * @param userId       用户Id
     * @return doc
     */
    Optional<Document> findByDocumentNameAndUserId(String documentName, String userId);

}
