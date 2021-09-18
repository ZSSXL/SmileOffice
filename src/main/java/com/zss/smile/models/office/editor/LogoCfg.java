package com.zss.smile.models.office.editor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/17 9:59
 * @desc 编辑器logo -- 社区版不生效
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoCfg implements Serializable {

    /**
     * 常态下编辑器logo
     */
    private String image;

    /**
     * 嵌入式编辑器Logo
     */
    private String imageEmbedded;

    /**
     * 点logo时跳转的界面
     */
    private String url;
}
