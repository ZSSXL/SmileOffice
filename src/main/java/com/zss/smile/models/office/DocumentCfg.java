package com.zss.smile.models.office;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zss.smile.models.office.doc.InfoCfg;
import com.zss.smile.models.office.doc.PermissionCfg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/7 10:46
 * @desc Doc config
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCfg implements Serializable {

    /**
     * 文档唯一Key
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String key;

    /**
     * 文件名
     */
    private String title;

    /**
     * 文件绝对路径
     */
    private String url;

    /**
     * 文件类型 -- 文件拓展名
     */
    private String fileType;

    /**
     * 文档基本信息
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private InfoCfg info;

    /**
     * 文档权限
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PermissionCfg permissions;
}
