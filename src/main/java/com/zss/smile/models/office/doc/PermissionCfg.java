package com.zss.smile.models.office.doc;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/7 11:08
 * @desc Document Permissions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionCfg implements Serializable {

    /**
     * 是否可以评论文档 -- default value is true
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean comment;
    /**
     * 下载还是只能在线查看或编辑 -- default value is true
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean download;
    /**
     * 编辑还是只能查看 -- default value is true
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean edit;
    /**
     * 是否可以填写表单 -- default value is true
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean fillForms;
    /**
     * 是否可以更改内容控制设置 -- default value is true
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean modifyFilter;
    /**
     * 是否可以更改内容控制设置 -- default value is true
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean modifyContentControl;
    /**
     * 是否可以查看文档 -- default value is true
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean review;
}
