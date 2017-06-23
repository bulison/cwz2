/**
 * Description: 本地文件系统实现
 * Copyright:   Copyright (c)2011
 * Company:     广东金宇恒科技有限公司
 *
 * @author: yang
 * @version: 1.0
 * Create at:   2011-10-14 上午08:48:51
 * <p>
 * Modification History:
 * Date         Author      Version     Description
 * ------------------------------------------------------------------
 * 2011-10-14      yang        1.0         1.0 Version
 */

package cn.fooltech.fool_ops.component.core.filesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LocalFileSystem implements FileSystem {
    private static Logger logger = LoggerFactory.getLogger(LocalFileSystem.class);

    // 默认为512KB
    private int blockSize = 512 * 1024;

    // 文件系统根目录
    private String root;

    // 保存已创建的文件夹名称，避免每次创建文件时都进行文件夹是否存在的判断
    private Map<String, Boolean> existsDirectorys = Collections.synchronizedMap(new HashMap<String, Boolean>(2));

    private FileCompress imageCompress;

    /**
     * 使用文件系统根目录初始化<br>
     * 校验文件系统根目录是否为空<br>
     * 检验文件系统根目录是否存在<br>
     * 检验文件系统根目录是否为有效目录<br>
     * 检验文件系统根目录是否有写入权限<br>
     * @param root 文件系统根目录
     */
    public LocalFileSystem(String root) {
        this.root = root;

        if (this.root == null || "".equals(this.root)) {
            throw new IllegalArgumentException("未设置有效的文件系统根目录：" + this.root);
        }

        if (!FileUtil.lastCharIsFileSeparatorChar(this.root)) {
            this.root += File.separatorChar;
        }

        File fileRoot = new File(this.root);
        if (!fileRoot.exists()) {
            throw new IllegalArgumentException("文件系统根目录不存在：" + this.root);
        }

        if (!fileRoot.isDirectory()) {
            throw new IllegalArgumentException("文件系统根目录不是有效的目录：" + this.root);
        }

        if (!fileRoot.canWrite()) {
            throw new RuntimeException("没有权限写文件系统根目录：" + this.root);
        }

        logger.info("init file io:{}", root);
    }

    /**
     * 图片压缩器
     * @param compress
     */
    public void setImageCompress(FileCompress compress) {
        this.imageCompress = compress;
    }

    /**
     * 获取文件系统根目录
     */
    public String getRoot() {
        return this.root;
    }

    /**
     * 获取读/写块的大小（默认为512KB）
     * @return
     */
    public int getBlockSize() {
        return this.blockSize;
    }

    /**
     * 设置读/写块的大小
     * @param size 字节数
     */
    public void setBlockSize(int size) {
        this.blockSize = size;
    }

    /**
     * 将文件写入文件系统根目录对应的文件夹中
     * @param file 写入文件对象
     * @param fileName 写入文件名
     * @param folder 写入文件对应根文件系统根目录的文件夹中
     * @exception IOException 写入文件失败
     */
    public void write(File file, String fileName, String folder) throws IOException {
        FileChannel srcChannel = null;
        FileChannel distChannel = null;
        try {
            srcChannel = new FileInputStream(file).getChannel();
            distChannel = new FileOutputStream(buildFullName(fileName, folder)).getChannel();
            distChannel.transferFrom(srcChannel, 0, srcChannel.size());

            // 不要使用，会导致文件句柄未释放而不能对源文件进行操作（如删除）
            //MappedByteBuffer buf = srcChannel.map(FileChannel.MapMode.READ_ONLY, 0, srcChannel.size());
            //distChannel.write(buf);
        } catch (IOException e) {
            throw e;
        } finally {
            FileUtil.closeChannel(srcChannel);
            FileUtil.closeChannel(distChannel);
        }
    }

    /**
     * 将文件写入文件系统根目录对应的文件夹中（适合小文件，建议不超过10M）
     * @param bytes 写入内容
     * @param fileName 写入文件名
     * @param folder 写入文件对应根文件系统根目录的文件夹中
     * @exception IOException 写入文件失败
     */
    public void write(byte[] bytes, String fileName, String folder) throws IOException {
        FileChannel distChannel = null;
        try {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            distChannel = new FileOutputStream(buildFullName(fileName, folder)).getChannel();
            distChannel.write(buffer);
        } catch (IOException e) {
            throw e;
        } finally {
            FileUtil.closeChannel(distChannel);
        }
    }

    /**
     * 将输入流写入文件系统根目录对应的文件夹中（适合于大文件，需自行关闭InputStream）
     * @param is 写入内容
     * @param fileName 写入文件名
     * @param folder 写入文件对应根文件系统根目录的文件夹中
     * @exception IOException 写入文件失败
     */
    public void write(InputStream is, String fileName, String folder) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(buildFullName(fileName, folder));

            byte[] buffer = new byte[blockSize];
            int index = 0;
            while ((index = is.read(buffer)) != -1) {
                out.write(buffer, 0, index);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            FileUtil.closeOutputStream(out);
        }
    }

    /**
     * 将图片写入文件系统根目录对应的文件夹中
     * @param file 写入文件对象
     * @param fileName 写入文件名
     * @param folder 写入文件对应根文件系统根目录的文件夹中
     * @param compress 是否压缩图片
     * @exception 写文件异常
     */
    public void writeImage(File file, String fileName, String folder, boolean compress) throws IOException {
        if (compress && imageCompress != null) {
            try {
                byte[] compressData = imageCompress.compress(file);
                write(compressData, fileName, folder);
            } catch (Exception e) {
                write(file, fileName, folder);
            }
        } else {
            write(file, fileName, folder);
        }
    }

    /**
     * 将图片写入文件系统根目录对应的文件夹中
     * @param bytes 写入内容
     * @param fileName 写入文件名
     * @param folder 写入文件对应根文件系统根目录的文件夹中
     * @param compress 是否压缩图片
     * @exception 写文件异常
     */
    public void writeImage(byte[] data, String fileName, String folder, boolean compress) throws IOException {
        if (compress && imageCompress != null) {
            try {
                byte[] compressData = imageCompress.compress(data);
                write(compressData, fileName, folder);
            } catch (Exception e) {
                write(data, fileName, folder);
            }
        } else {
            write(data, fileName, folder);
        }
    }

    /**
     * 将图片输入流写入文件系统根目录对应的文件夹中（需自行关闭InputStream）
     * @param is 写入内容
     * @param fileName 写入文件名
     * @param folder 写入文件对应根文件系统根目录的文件夹中
     * @param compress 是否压缩图片
     * @exception 写文件异常
     */
    public void writeImage(InputStream is, String fileName, String folder, boolean compress) throws IOException {
        if (compress && imageCompress != null) {
            try {
                byte[] compressData = imageCompress.compress(is);
                write(compressData, fileName, folder);
            } catch (Exception e) {
                write(is, fileName, folder);
            }
        } else {
            write(is, fileName, folder);
        }
    }

    /**
     * 读取文件系统根目录对应文件夹下指定的文件（适合小文件，建议不超过10M）
     * @param fileName 读取文件名
     * @param folder 读取文件所在的目录
     * @return 文件内容
     * @exception IOException 文件不存在
     */
    public byte[] read(String fileName, String folder) throws IOException {
        File file = getFile(fileName, folder);

        FileChannel srcChannel = null;
        WritableByteChannel distChannel = null;
        try {
            srcChannel = new FileInputStream(file).getChannel();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            distChannel = Channels.newChannel(out);
            ByteBuffer buffer = ByteBuffer.allocate(blockSize);

            while (true) {
                int i = srcChannel.read(buffer);
                if (i == -1) {
                    break;
                }

                buffer.flip();
                distChannel.write(buffer);
                buffer.clear();
            }

            return out.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            FileUtil.closeChannel(srcChannel);
            FileUtil.closeChannel(distChannel);
        }
    }

    /**
     * 读取文件系统根目录对应文件夹下指定的文件（适合于大文件，需自行关闭InputStream）
     * @param fileName 读取文件名
     * @param folder 读取文件所在的目录
     * @return 文件内容
     * @exception IOException 文件不存在
     */
    public InputStream inputStream(String fileName, String folder) throws IOException {
        File file = getFile(fileName, folder);
        return new FileInputStream(file);
    }

    /**
     * 复制文件
     * @param srcFileName 源文件名
     * @param srcFolder 源文件所在目录
     * @param distFileName 目标文件名
     * @param distFolder 目标文件所在目录
     * @exception FileNotFoundException 文件不存在
     * @exception IOException 复制文件异常
     */
    public void copy(String srcFileName, String srcFolder, String distFileName, String distFolder) throws IOException {
        File file = getFile(srcFileName, srcFolder);
        this.copy(file, distFileName, distFolder);
    }

    /**
     * 复制文件
     * @param srcFile 源文件
     * @param distFileName 目标文件名
     * @param distFolder 目标文件所在目录
     * @throws IOException
     */
    public void copy(File srcFile, String distFileName, String distFolder) throws IOException {
        this.write(srcFile, distFileName, distFolder);
    }

    /**
     * 删除文件系统根目录对应文件夹下指定的文件
     * @param fileName 删除文件名
     * @param folder 删除文件所在的目录
     * @return 结果标识
     */
    public FileState delete(String fileName, String folder) throws IOException {
        return FileUtil.delete(buildFullName(fileName, folder));
    }

    /**
     * 构建文件夹路径
     * @param folder 文件夹
     * @return
     */
    public String buildDirectory(String folder) throws IOException {
        StringBuffer dir = new StringBuffer(this.root);
        if (folder != null) {
            dir.append(folder);

            if (!FileUtil.lastCharIsFileSeparatorChar(folder)) {
                dir.append(File.separatorChar);
            }
        }

        String saveDir = dir.toString();
        createDirecotry(saveDir);

        return saveDir;
    }

    /**
     * 构建完整路径名
     * @param fileName 文件名
     * @param folder 文件夹
     * @return
     */
    public String buildFullName(String fileName, String folder) throws IOException {
        return new StringBuffer().append(buildDirectory(folder)).append(fileName).toString();
    }

    /**
     * 创建文件夹
     * @param directory 文件夹全名称
     */
    public void createDirecotry(String directory) throws IOException {
        if (!existsDirectorys.containsKey(directory)) {
            FileState fileState = FileUtil.createDirectory(directory);
            if (FileState.SUCCEED == fileState || FileState.EXISTS == fileState) {
                existsDirectorys.put(directory, true);
            } else if (FileState.NOTDIR == fileState) {
                throw new IOException("不是有效的目录：" + directory);
            } else if (FileState.FAIL == fileState) {
                throw new IOException("无法创建目录：" + directory);
            }
        }
    }

    /**
     * 获取文件系统目录下的文件对象
     * @param fileName 文件名
     * @param folder 所属文件夹
     * @return 文件对象
     * @throws IOException 文件不存在
     */
    public File getFile(String fileName, String folder) throws IOException {
        File file = new File(buildFullName(fileName, folder));
        FileUtil.checkFileExists(file);

        return file;
    }
}
