package com.zss.smile.controller;

import com.zss.smile.helpers.FileUtility;
import com.zss.smile.models.office.DocumentCfg;
import com.zss.smile.models.office.EditorCfg;
import com.zss.smile.models.office.OfficeConfig;
import com.zss.smile.models.office.editor.EmbeddedCfg;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

/**
 * @author ZSS
 * @date 2021/9/27 20:11
 * @desc 公共预览
 */
@Controller
@CrossOrigin
@RequestMapping("/preview")
public class PreviewController {

    @GetMapping
    public String preview(@RequestParam("url") String url, Model model) {
        OfficeConfig officeConfig = buildPreviewConfig(url);
        model.addAttribute("officeConfig", officeConfig);
        return "preview";
    }

    /**
     * 构建预览配置
     *
     * @param url 地址
     * @return 配置
     */
    private OfficeConfig buildPreviewConfig(String url) {
        String fileName = FileUtility.getFileName(url);

        DocumentCfg documentCfg = DocumentCfg.builder()
                .url(url)
                .title(fileName)
                .fileType(Objects.requireNonNull(FileUtility.getFileExtension(fileName))
                        .replace(".", ""))
                .build();

        EmbeddedCfg embeddedCfg = EmbeddedCfg.builder()
                .embedUrl(url)
                .fullscreenUrl(url)
                .saveUrl(url)
                .shareUrl(url)
                .toolbarDocked("top")
                .build();

        EditorCfg editorCfg = EditorCfg.builder()
                .mode("view")
                .lang("zh-CN")
                .embedded(embeddedCfg)
                .build();

        return OfficeConfig.builder()
                .height("100%")
                .width("100%")
                .type("embedded")
                .mode("view")
                .documentType(FileUtility.getFileTypeEnum(url).get())
                .document(documentCfg)
                .editorConfig(editorCfg)
                .build();
    }
}
