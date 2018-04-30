package com.power.sql.generator.window;

/**
 *  程序运行状态信息管理接口
 * @author sunyu
 *
 */
public interface IMessagesManager {
    /**
     * 状态信息等级
     */
    enum Level {
        TRACE(0), DEBUG(1), INFO(2), WARN(3), ERROR(4), FATAL(5);

        private int value;

        private Level(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    /**
     * 显示信息
     * @param level 信息等级
     * @param messages 信息内容
     * @return 是否成功
     */
    boolean showMessages(Level level, String messages);
}
