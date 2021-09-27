package com.zss.smile.models.office.editor;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/7 11:19
 * @desc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCfg implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

}
