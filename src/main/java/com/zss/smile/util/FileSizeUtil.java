package com.zss.smile.util;

import java.text.DecimalFormat;

/**
 * @author ZSS
 * @date 2021/9/13 15:48
 * @desc 文件大小工具
 */
@SuppressWarnings("unused")
public class FileSizeUtil {

    /**
     * 定义GB的计算常量
     */
    private static final int GB = 1024 * 1024 * 1024;
    /**
     * 定义MB的计算常量
     */
    private static final int MB = 1024 * 1024;
    /**
     * 定义KB的计算常量
     */
    private static final int KB = 1024;

    /**
     * 获取文件大小
     *
     * @param size 文件大小
     * @return size
     */
    public static String getSize(int size) {
        //格式化小数
        DecimalFormat df = new DecimalFormat("0.00");
        String resultSize;
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = df.format(size / (float) GB) + " GB";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = df.format(size / (float) MB) + " MB";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = df.format(size / (float) KB) + " KB";
        } else {
            resultSize = size + " B";
        }
        return resultSize;
    }
}
