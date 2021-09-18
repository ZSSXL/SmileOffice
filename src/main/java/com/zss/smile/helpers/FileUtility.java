package com.zss.smile.helpers;

import com.zss.smile.common.enums.FileTypeEnum;

import java.net.URL;
import java.util.*;

/**
 * @author ZSS
 * @date 2021/9/3 16:35
 * @desc 文件实用工具
 */
@SuppressWarnings("unused")
public class FileUtility {

    /**
     * 获取文件类型
     *
     * @param fileName 文件名
     * @return FileTypeEnum
     */
    public static FileTypeEnum getFileTypeEnum(String fileName) {
        String ext = Objects.requireNonNull(getFileExtension(fileName))
                .toLowerCase();

        if (ExtsDocument.contains(ext)) {
            return FileTypeEnum.Word;
        }

        if (ExtsSpreadsheet.contains(ext)) {
            return FileTypeEnum.Cell;
        }

        if (ExtsPresentation.contains(ext)) {
            return FileTypeEnum.Slide;
        }

        return FileTypeEnum.Word;
    }

    /**
     * Word -- 类型
     */
    public static List<String> ExtsDocument = Arrays.asList(
            ".doc", ".docx", ".docm", ".dot", ".dotx", ".dotm", ".odt",
            ".fodt", ".ott", ".rtf", ".txt", ".html", ".htm", ".mht", ".xml",
            ".pdf", ".djvu", ".fb2", ".epub", ".xps"
    );

    /**
     * Cell -- 表格类型
     */
    public static List<String> ExtsSpreadsheet = Arrays.asList(
            ".xls", ".xlsx", ".xlsm", ".xlt", ".xltx", ".xltm",
            ".ods", ".fods", ".ots", ".csv"
    );

    /**
     * Slide -- 幻灯片类型
     */
    public static List<String> ExtsPresentation = Arrays.asList(
            ".pps", ".ppsx", ".ppsm", ".ppt", ".pptx", ".pptm",
            ".pot", ".potx", ".potm", ".odp", ".fodp", ".otp"
    );

    /**
     * 获取文件拓展名(带点)
     *
     * @param url 请求路径
     * @return 文件拓展名
     */
    public static String getFileExtension(String url) {
        String fileName = getFileName(url);
        if (fileName == null) {
            return null;
        }
        String fileExt = fileName.substring(fileName.lastIndexOf("."));
        return fileExt.toLowerCase();
    }

    /**
     * 获取文件名(带拓展名)
     *
     * @param url 文件路径
     * @return 文件名
     */
    public static String getFileName(String url) {
        if (url == null) {
            return "";
        }
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        return fileName.split("\\?")[0];
    }

    /**
     * 获取文件名(不带文件拓展名)
     *
     * @param url 文件路径
     * @return 文件名
     */
    public static String getFileNameWithoutExtension(String url) {
        String fileName = getFileName(url);
        if (fileName == null) {
            return null;
        }
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    /**
     * 获取请求参数
     *
     * @param url 请求路径
     * @return paramMap
     */
    public static Map<String, String> getUrlParams(String url) {
        try {
            String query = new URL(url).getQuery();
            String[] params = query.split("&");
            Map<String, String> map = new HashMap<>(params.length);
            for (String param : params) {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                map.put(name, value);
            }
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HashMap<>(0);
        }
    }

    /**
     * 工具测试
     */
    public static void main(String[] args) {
        String url = "http://localhost:8080/web/test/文件测试.docx";
        String fileExtension = FileUtility.getFileExtension(url);
        System.out.println(fileExtension);
        String fileName = FileUtility.getFileName(url);
        System.out.println(fileName);
        FileTypeEnum fileTypeEnum = FileUtility.getFileTypeEnum(url);
        System.out.println(fileTypeEnum.toString());
        String fileNameWithoutExtension = FileUtility.getFileNameWithoutExtension(url);
        System.out.println(fileNameWithoutExtension);
    }
}
