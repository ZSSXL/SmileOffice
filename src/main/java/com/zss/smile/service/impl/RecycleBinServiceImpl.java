package com.zss.smile.service.impl;

import com.zss.smile.models.entities.RecycleBin;
import com.zss.smile.repository.RecycleBinRepository;
import com.zss.smile.service.RecycleBinService;
import com.zss.smile.util.DateUtil;
import com.zss.smile.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataUnit;

import java.util.List;

/**
 * @author ZSS
 * @date 2021/9/23 17:16
 * @desc 回收站服务层接口方法实现
 */
@Slf4j
@Service
public class RecycleBinServiceImpl implements RecycleBinService {

    private final RecycleBinRepository recycleBinRepository;

    @Autowired
    public RecycleBinServiceImpl(RecycleBinRepository recycleBinRepository) {
        this.recycleBinRepository = recycleBinRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean recycle(String userId, String docId) {
        RecycleBin build = RecycleBin.builder()
                .binId(IdUtil.getId())
                .userId(userId)
                .docId(docId)
                .createTime(DateUtil.currentTimestamp())
                .build();
        try {
            recycleBinRepository.save(build);
            return true;
        } catch (Exception e) {
            log.error("回收站添加失败");
            return false;
        }
    }

    @Override
    public List<String> unExpired(String userId) {
        return recycleBinRepository.findUnExpired(userId, DateUtil.thirtyDaysAgo());
    }

    @Override
    public List<String> getExpired() {
        return recycleBinRepository.findExpired(DateUtil.thirtyDaysAgo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanUpAllInExpired() {
        Integer result = recycleBinRepository.deleteAllExpired(DateUtil.thirtyDaysAgo());
        log.info("回收站清除数量: [{}]", result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRecord(String userId, String docId) {
        Integer result = recycleBinRepository.deleteByUserIdAndDocId(userId, docId);
        log.info("回收站记录删除结果: [{}]", result == 1);
    }
}
