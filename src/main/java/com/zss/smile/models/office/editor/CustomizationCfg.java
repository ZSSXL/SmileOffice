package com.zss.smile.models.office.editor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/7 11:29
 * @desc Editor Customization
 * -- 自定义编辑器界面
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomizationCfg implements Serializable {

    /**
     * 压缩页面header -- default value is false
     */
    private Boolean compactHeader;

    /**
     * 压缩工具栏 -- default value is false
     */
    private Boolean compactToolbar;

    /**
     * 隐藏右边菜单 -- default value is false
     */
    private Boolean hideRightMenu;

    /**
     * 隐藏标尺 -- default value is false
     */
    private Boolean hideRulers;

    /**
     * 拼写检查 -- default value is true
     * -- todo 晚点再来
     */
    private Boolean spellcheck;
}
