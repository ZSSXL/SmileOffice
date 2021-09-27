package com.zss.smile.models.office.editor;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/27 20:00
 * @desc 嵌入式预览配置
 */
@Data
@Builder
public class EmbeddedCfg implements Serializable {

    /**
     * 定义作为嵌入网页的文档的源文件的文档的绝对 URL。
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String embedUrl;

    /**
     * 定义将以全屏模式打开的文档的绝对 URL。
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fullscreenUrl;

    /**
     * 定义允许将文档保存到用户个人计算机上的绝对 URL。
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String saveUrl;

    /**
     * 定义允许其他用户共享此文档的绝对 URL。
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String shareUrl;

    /**
     * 定义嵌入查看器工具栏的位置，可以是top或bottom。
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String toolbarDocked;
}
