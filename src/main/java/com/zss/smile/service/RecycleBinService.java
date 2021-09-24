package com.zss.smile.service;

import java.util.List;

/**
 * @author ZSS
 * @date 2021/9/23 17:16
 * @desc 回收站服务层接口
 */
public interface RecycleBinService {

    /**
     * 添加至回收站
     *
     * @param userId 用户Id
     * @param docId  文档Id
     * @return result
     */
    Boolean recycle(String userId, String docId);

    /**
     * 获取用户回收站中，未过期的文档
     *
     * @param userId 用户Id
     * @return docIds
     */
    List<String> unExpired(String userId);

    /**
     * 获取已经过期的回收站数据
     *
     * @return docIds
     */
    List<String> getExpired();

    /**
     * 清除所有过期的回收站记录
     */
    void cleanUpAllInExpired();

    /**
     * 删除回收站记录
     *
     * @param userId 用户Id
     * @param docId  文档Id
     */
    void deleteRecord(String userId, String docId);
}
