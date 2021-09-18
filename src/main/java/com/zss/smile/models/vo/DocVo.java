package com.zss.smile.models.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/8 10:00
 * @desc 文档传输对象
 */
@Data
@Builder
public class DocVo implements Serializable {

    private String docId;

    private String documentName;

    private String documentKey;

    private String documentType;

    private String documentSize;

    private String createTime;

    private String updateTime;
}
