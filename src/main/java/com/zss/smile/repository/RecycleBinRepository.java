package com.zss.smile.repository;

import com.zss.smile.models.entities.RecycleBin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ZSS
 * @date 2021/9/23 17:15
 * @desc 回收站数据持久化
 */
@Repository
public interface RecycleBinRepository extends JpaRepository<RecycleBin, String> {

    /**
     * 查询未过期的回收站记录
     *
     * @param userId           用户Id
     * @param recentThirtyDays 三十天前的时间戳
     * @return ids
     */
    @Query(value = "select doc_id from smile_recycle_bin where user_id = ?1 and create_time >= ?2", nativeQuery = true)
    List<String> findUnExpired(String userId, String recentThirtyDays);

    /**
     * 获取过期的回收站记录
     *
     * @param recentThirtyDays 三十天前的时间戳
     * @return ids
     */
    @Query(value = "select doc_id from smile_recycle_bin where create_time <= ?1", nativeQuery = true)
    List<String> findExpired(String recentThirtyDays);

    /**
     * 清楚过期的回收站记录
     *
     * @param thirtyDaysAgo 三十天前的时间戳
     * @return result
     */
    @Modifying
    @Query(value = "delete from smile_recycle_bin where create_time <= ?1", nativeQuery = true)
    Integer deleteAllExpired(String thirtyDaysAgo);

    /**
     * 删除回收站记录
     *
     * @param userId 用户Id
     * @param docId  文档Id
     * @return result
     */
    @Modifying
    Integer deleteByUserIdAndDocId(String userId, String docId);

    /**
     * 通过用户Id和文档Id查找回收站记录
     *
     * @param docId  文档Id
     * @param userId 用户Id
     * @return 回收站
     */
    RecycleBin findByDocIdAndUserId(String docId, String userId);
}
