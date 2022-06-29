package com.zss.smile.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/3 17:18
 * @desc 文档数据实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "smile_document")
@Table(appliesTo = "smile_document", comment = "文档")
public class Document implements Serializable {

    /**
     * 用户信息
     */
    @Id
    @Column(name = "doc_id", columnDefinition = "varchar(255)")
    private String docId;

    /**
     * 用户信息
     */
    @Column(name = "user_id", columnDefinition = "varchar(255)")
    private String userId;

    /**
     * 文档类型： word/cell/slide
     */
    @Column(name = "document_type", columnDefinition = "varchar(10)")
    private String documentType;

    /**
     * 文档大小
     */
    @Column(name = "document_size", columnDefinition = "varchar(50)")
    private String documentSize;

    /**
     * 识别文档的唯一文档标识符, 每次编辑和保存文档时，都必须重新生成密钥
     */
    @Column(name = "document_key", columnDefinition = "varchar(255)")
    private String documentKey;

    /**
     * 文档名称
     */
    @Column(name = "document_name", columnDefinition = "varchar(100)")
    private String documentName;

    /**
     * 是否收藏 -- 默认false
     */
    @Column(name = "collect", columnDefinition = "boolean")
    private Boolean collect;

    /**
     * 受否处于回收状态
     */
    @Column(name = "recycle", columnDefinition = "boolean")
    private Boolean recycle;

    /**
     * 创建时间 -- 时间戳
     */
    @Column(name = "create_time", columnDefinition = "varchar(20)")
    private String createTime;

    /**
     * 更新时间 -- 时间戳
     */
    @Column(name = "update_time", columnDefinition = "varchar(20)")
    private String updateTime;
}
