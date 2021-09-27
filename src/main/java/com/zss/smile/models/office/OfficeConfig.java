package com.zss.smile.models.office;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/7 10:43
 * @desc 文件对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficeConfig implements Serializable {

    /**
     * 定义文档高度
     * -- 100% by default
     */
    private String height;

    /**
     * 定义文档宽度
     * -- 100% by default
     */
    private String width;

    /**
     * 显示模式
     * desktop -- 桌面端
     * mobile -- 移动端
     * embedded -- 嵌入式端
     */
    private String type = "desktop";

    /**
     * 打开模式
     * edit -- 编辑
     * view -- 查看
     */
    private String mode = "edit";

    /**
     * 定义要打开的文档类型
     */
    private String documentType;

    /**
     * 文档部分允许更改与文档有关的所有参数
     */
    private DocumentCfg document;

    /**
     * 允许更改与编辑器界面相关的参数
     */
    private EditorCfg editorConfig;
}
