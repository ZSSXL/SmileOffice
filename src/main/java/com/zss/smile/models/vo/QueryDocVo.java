package com.zss.smile.models.vo;

import com.zss.smile.common.SmileConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/8 10:16
 * @desc 文档查询对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryDocVo implements Serializable {

    /**
     * 文档类型
     *
     * @see com.zss.smile.common.enums.FileTypeEnum
     */
    private String documentType;

    private Integer page = SmileConstant.DEFAULT_PAGE;

    private Integer size = SmileConstant.DEFAULT_SIZE;

}
