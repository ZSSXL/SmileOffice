package com.zss.smile.controller;

import com.zss.smile.common.response.ServerResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ZSS
 * @date 2022/6/29 14:57
 * @desc minio前端控制器
 */
@Slf4j
@RestController
@RequestMapping("/minio")
public class MinioController {

    private final MinioClient minioClient;

    @Autowired
    public MinioController(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @PostMapping("/upload")
    public ServerResponse<String> uploadFileToMinio(HttpServletRequest request) {

        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile document = multipartHttpServletRequest.getFile("file");
        if (document == null) {
            return ServerResponse.error("上传文件不能为空");
        }
        String fileName = document.getOriginalFilename();

        try {
            PutObjectArgs build = PutObjectArgs.builder().bucket("my-buck").object("dir/" + fileName)
                    .stream(document.getInputStream(), document.getSize(), -1)
                    .contentType(document.getContentType())
                    .build();
            minioClient.putObject(build);
            return ServerResponse.success("上传文件成功");
        } catch (Exception e) {
            log.error("上传文件失败: [{}]", e.getMessage());
            return ServerResponse.error("上传文件失败");
        }
    }
}
