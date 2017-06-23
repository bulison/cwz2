/**
 * Description: 文件压缩接口
 * Copyright:   Copyright (c)2011
 * Company:     广东金宇恒科技有限公司
 *
 * @author: yang
 * @version: 1.0
 * Create at:   2011-10-26 下午02:39:15
 * <p>
 * Modification History:
 * Date         Author      Version     Description
 * ------------------------------------------------------------------
 * 2011-10-26      yang        1.0         1.0 Version
 */

package cn.fooltech.fool_ops.component.core.filesystem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileCompress {

    /**
     * 压缩文件
     * @param is
     * @return
     * @throws IOException
     */
    public byte[] compress(InputStream is) throws IOException;

    /**
     * 压缩文件
     * @param file
     * @return
     * @throws IOException
     */
    public byte[] compress(File file) throws IOException;

    /**
     * 压缩文件
     * @param datas
     * @return
     * @throws IOException
     */
    public byte[] compress(byte[] datas) throws IOException;
}
