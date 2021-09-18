package com.zss.smile.models.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZSS
 * @date 2021/9/8 13:10
 * @desc 重命名对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RenameVo {

    private String docId;

    /**
     * 文档名称
     */
    private String documentName;

    /**
     * 新名称
     */
    private String newDocumentName;
}
