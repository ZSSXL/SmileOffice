package com.zss.smile.common.enums;

/**
 * @author ZSS
 * @date 2021/9/1 14:28
 * @desc 文件类型 -- 枚举
 */
public enum FileTypeEnum {
    /**
     * 文档，表格， 演示
     */
    Word("word"),
    Cell("cell"),
    Slide("slide");

    private final String desc;

    FileTypeEnum(String desc) {
        this.desc = desc;
    }

    public String get() {
        return desc;
    }
}
