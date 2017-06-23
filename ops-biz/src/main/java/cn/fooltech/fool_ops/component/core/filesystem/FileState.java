/**
 * Description: 文件处理结果状态
 * Copyright:   Copyright (c)2011
 * Company:     广东金宇恒科技有限公司
 *
 * @author: yang
 * @version: 1.0
 * Create at:   2011-10-26 下午03:59:58
 * <p>
 * Modification History:
 * Date         Author      Version     Description
 * ------------------------------------------------------------------
 * 2011-10-26      yang        1.0         1.0 Version
 */

package cn.fooltech.fool_ops.component.core.filesystem;

public enum FileState {
    SUCCEED, FAIL, EXISTS, NOTEXISTS, NOTDIR;

    public String toString() {
        switch (this) {
            case SUCCEED:
                return "成功";
            case FAIL:
                return "失败";
            case EXISTS:
                return "文件已经存在";
            case NOTEXISTS:
                return "文件不存在";
            case NOTDIR:
                return "不是文件目录";
            default:
                return "未知状态";
        }
    }
}
