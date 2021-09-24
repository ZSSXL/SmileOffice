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
 * @date 2021/9/23 16:00
 * @desc 回收站实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "smile_recycle_bin")
@Table(appliesTo = "smile_recycle_bin", comment = "文档")
public class RecycleBin implements Serializable {

    @Id
    @Column(name = "binId", columnDefinition = "varchar(255)")
    private String binId;

    @Column(name = "user_id", columnDefinition = "varchar(255)")
    private String userId;

    @Column(name = "doc_id", columnDefinition = "varchar(255)")
    private String docId;

    @Column(name = "create_time", columnDefinition = "varchar(20)")
    private String createTime;

}
