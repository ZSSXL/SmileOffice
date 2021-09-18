package com.zss.smile.common;

/**
 * @author ZSS
 * @date 2021/9/6 17:20
 * @desc 系统常量
 */
@SuppressWarnings("unused")
public class SmileConstant {

    public static final String SMILE_TOKEN = "smile_token";

    /**
     * 默认当前页
     */
    public static final Integer DEFAULT_PAGE = 0;

    /**
     * 默认每页大小
     */
    public static final Integer DEFAULT_SIZE = 10;

    /**
     * 1 - document is being edited,
     * 2 - document is ready for saving,
     * 3 - document saving error has occurred,
     * 4 - document is closed with no changes,
     * 6 - document is being edited, but the current document state is saved,
     * 7 - error has occurred while force saving the
     */
    public interface CallbackStatus {
        Integer BEING_EDITED = 1;
        Integer READY_FOR_SAVE = 2;
        Integer SAVE_HAS_ERROR = 3;
        Integer CLOSE_NO_CHANGE = 4;
        Integer SAVE_WITH_EDITING = 6;
        Integer FORCE_SAVE_HAS_ERROR = 7;
    }
}
