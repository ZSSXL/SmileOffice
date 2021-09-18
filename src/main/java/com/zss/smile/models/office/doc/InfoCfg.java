package com.zss.smile.models.office.doc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/7 11:05
 * @desc DocumentInfo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoCfg implements Serializable {

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 上传时间 -- 年月日时分秒
     */
    private String uploaded;

    /**
     * 收藏夹图标的突出显示状态 -- default value is true
     */
    private Boolean favorite;

    /**
     * 文件路径
     */
    private String folder;
}
