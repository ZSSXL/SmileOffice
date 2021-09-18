package com.zss.smile.models.office;

import com.zss.smile.models.office.editor.CustomizationCfg;
import com.zss.smile.models.office.editor.RecentCfg;
import com.zss.smile.models.office.editor.UserCfg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZSS
 * @date 2021/9/7 11:17
 * @desc 编辑器配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditorCfg {

    /**
     * edit / view
     * 编辑或查看
     */
    private String mode;

    /**
     * 回调地址 -- 必须
     */
    private String callbackUrl;

    /**
     * 定义编辑器界面语言 -- 英语:en  中文:ch
     */
    private String lang;

    /**
     * 文档的用户信息
     */
    private UserCfg user;

    /**
     * 最近文件
     */
    private List<RecentCfg> recent;

    /**
     * 自定义编辑器界面配置
     */
    private CustomizationCfg customization;
}
