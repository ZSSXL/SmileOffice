package com.zss.smile.models.office.editor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/7 11:23
 * @desc 最近文件
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentCfg implements Serializable {

    /**
     * 文件存储路径，可以为空
     */
    private String folder;

    /**
     * 文件命
     */
    private String title;

    /**
     * 文件绝对路径
     */
    private String url;
}
