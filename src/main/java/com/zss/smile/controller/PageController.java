package com.zss.smile.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;

/**
 * @author ZSS
 * @date 2021/9/3 11:06
 * @desc 页面跳转控制器
 */
@Slf4j
@Controller
public class PageController {

    private final static String FAVICON = "favicon.ico";
    private final static List<String> FAVICONS = Arrays.asList("favicon");
    private final static String INDEX = "index";

    /**
     * 页面跳转
     *
     * @param page page
     * @return page
     */
    @GetMapping("/{page}")
    public String helloHtml(@PathVariable String page) {
        if (!StringUtils.equals(FAVICON, page)) {
            return page;
        } else {
            return INDEX;
        }
    }

}
