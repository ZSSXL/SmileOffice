package com.zss.smile.models.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/8 16:06
 * @desc 回调函数请求参数对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallbackVo implements Serializable {

    /**
     * 定义已编辑的文档标识符 -- docId
     */
    private String key;

    /**
     * 1 - 正在编辑文档，
     * 2 - 文档已准备好保存，
     * 3 - 发生文档保存错误，
     * 4 - 文档关闭，没有任何变化，
     * 6 - 正在编辑文档，但保存当前文档状态，
     * 7 - 强制保存文档时出错。
     */
    private Integer status;

    /**
     * 定义要使用文档存储服务保存的已编辑文档的链接。仅当状态值等于2或3时才存在链接。
     */
    private String url;
}
