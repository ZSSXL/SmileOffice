package com.zss.smile.helpers;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.InetAddress;
import java.nio.channels.FileChannel;
import java.util.Objects;

/**
 * @author ZSS
 * @date 2021/9/6 17:02
 * @desc 文档管理助手
 */
@Slf4j
@SuppressWarnings("unused")
public class DocumentManager {

    private static HttpServletRequest request;

    public static void init(HttpServletRequest req) {
        request = req;
    }

    /**
     * 当前用户的O
     *
     * @return ip
     */
    public static String curUserHostAddress() {
        String userAddress;
        try {
            userAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ex) {
            userAddress = "";
        }
        return userAddress.replaceAll("[^0-9a-zA-Z.=]", "_");
    }

    /**
     * 获取当前用户的路径
     *
     * @param loginName 登录名
     * @return dir
     */
    public static String curUserFilePath(String loginName, String storageFolder) {
        String directory = storageFolder + loginName + File.separator;
        File file = new File(directory);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                return storageFolder;
            }
        }
        return directory;
    }

    /**
     * 获取文件服务路径
     *
     * @return http://localhost:8080/path
     */
    public static String getServerUrlContext() {
        return request.getScheme() + "://" +
                request.getServerName() + ":" +
                request.getServerPort() +
                request.getContextPath();
    }

    /**
     * 获取文件服务路径
     *
     * @return http://localhost:8080/path
     */
    public static String getServerUrlBase() {
        return request.getScheme() + "://" +
                request.getServerName() + ":" +
                request.getServerPort();
    }

    /**
     * 创建新的文档
     *
     * @param loginName 当前登录用户的登录名
     * @param fileExts  文件后缀
     * @return 文件名
     */
    public static String createNewDoc(String loginName, String fileExts, String storageFolder) {
        String basePath = curUserFilePath(loginName, storageFolder);
        String newFileName = "new." + fileExts;
        File file = new File(basePath + newFileName);
        // 检测是否存在
        int index = 1;
        while (file.exists()) {
            newFileName = "new(" + index + ")." + fileExts;
            index++;
            file = new File(basePath + newFileName);
        }

        // 开始复制文件
        InputStream stream = DocumentManager.class.getResourceAsStream("/assets/new/new." + fileExts);
        if (stream != null) {
            try (FileOutputStream out = new FileOutputStream(file)) {
                int read;
                final byte[] bytes = new byte[1024];
                while ((read = stream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
                log.info("创建新文件[{}]成功", newFileName);
            } catch (Exception e) {
                log.error("创建新文件[{}]失败: [{}]", newFileName, e.getMessage());
            }
        } else {
            log.info("创建新文件[{}]失败", newFileName);
        }
        return newFileName;
    }

    /**
     * 文档重命名
     *
     * @param loginName     登录名
     * @param docName       文档名称
     * @param newDocName    新文档名
     * @param storageFolder 基础路径
     * @return 新文档名
     */
    public static Boolean renameDocument(String loginName, String docName,
                                         String newDocName, String storageFolder) {
        String basePath = curUserFilePath(loginName, storageFolder);
        String docFilePath = basePath + docName;
        String newDocFilePath = basePath + newDocName;
        // 文件拷贝
        Boolean copy = copy(docFilePath, newDocFilePath);
        if (copy) {
            // 删除旧文件
            delete(new File(docFilePath));
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param file 文件
     */
    private static void delete(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                for (File c : Objects.requireNonNull(file.listFiles())) {
                    delete(c);
                }
            }
            if (!file.delete()) {
                log.error("删除文件失败: [{}]", file);
            }
        }
    }

    /**
     * 文件快速copy
     *
     * @param src:    源文件
     * @param target: 目标文件
     */
    public static Boolean copy(String src, String target) {
        FileChannel inputChannel = null;
        FileChannel outChannel = null;

        try {
            inputChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(target).getChannel();

            outChannel.transferFrom(inputChannel, 0, inputChannel.size());
            log.info("文件拷贝成功: [{}] -> [{}]", FileUtility.getFileName(src)
                    , FileUtility.getFileName(target));
            return true;
        } catch (FileNotFoundException e) {
            log.error("查找文件[{}]失败: [{}]", FileUtility.getFileName(src), e.getMessage());
        } catch (IOException e) {
            log.error("Copy File Has IOException: [{}]", e.getMessage());
        } finally {
            closeChannel(inputChannel);
            closeChannel(outChannel);
        }
        return false;
    }

    /**
     * 更新修改后的文件
     *
     * @param documentName  文档名称
     * @param inputStream   新文件流
     * @param storageFolder 基础路径
     * @param loginName     登录名
     */
    public static void updateDoc(String documentName, InputStream inputStream,
                                 String storageFolder, String loginName) {
        String basePath = curUserFilePath(loginName, storageFolder);
        String srcDocPath = basePath + documentName;
        outputFile(inputStream, srcDocPath);
    }

    /**
     * 关闭管道
     *
     * @param channel ch
     */
    private static void closeChannel(FileChannel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                log.error("关闭管道失败: [{}]", e.getMessage());
            }
        }
    }

    /**
     * 删除文档
     *
     * @param loginName         登录名
     * @param documentName      文档名
     * @param baseStorageFolder 基础路径
     */
    public static void deleteDocument(String loginName, String documentName, String baseStorageFolder) {
        String basePath = curUserFilePath(loginName, baseStorageFolder);
        delete(new File(basePath + documentName));
    }

    public static void saveDocument(InputStream inputStream, String documentName, String loginName, String baseStorageFolder) {
        String basePath = curUserFilePath(loginName, baseStorageFolder);
        String srcDocPath = basePath + documentName;
        outputFile(inputStream, srcDocPath);
    }

    /**
     * 输出文件
     *
     * @param inputStream 输入流
     * @param srcDocPath  存储路径
     */
    private static void outputFile(InputStream inputStream, String srcDocPath) {
        try {
            FileOutputStream out = new FileOutputStream(srcDocPath);
            int read;
            final byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            inputStream.close();
            out.close();
            log.info("文件[{}]保存成功", srcDocPath);
        } catch (Exception e) {
            log.error("保存文件[{}]失败: [{}]", srcDocPath, e.getMessage());
        }
    }
}
