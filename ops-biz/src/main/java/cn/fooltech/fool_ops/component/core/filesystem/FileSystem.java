package cn.fooltech.fool_ops.component.core.filesystem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileSystem {

    /**
     * 获取文件系统根目录
     */
    public String getRoot();

    /**
     * 将文件写入文件系统根目录对应的文件夹中
     *
     * @param file     写入文件对象
     * @param fileName 写入文件名
     * @param folder   写入文件对应根文件系统根目录的文件夹中
     * @throws IOException 写文件异常
     */
    public void write(File file, String fileName, String folder) throws IOException;

    /**
     * 将数据写入文件系统根目录对应的文件夹中（适合小文件，建议不超过10M）
     *
     * @param bytes    写入内容
     * @param fileName 写入文件名
     * @param folder   写入文件对应根文件系统根目录的文件夹中
     * @throws IOException 写文件异常
     */
    public void write(byte[] bytes, String fileName, String folder) throws IOException;

    /**
     * 将输入流写入文件系统根目录对应的文件夹中（适合于大文件，需自行关闭InputStream）
     *
     * @param is       写入内容
     * @param fileName 写入文件名
     * @param folder   写入文件对应根文件系统根目录的文件夹中
     * @throws IOException 写文件异常
     */
    public void write(InputStream is, String fileName, String folder) throws IOException;

    /**
     * 将图片写入文件系统根目录对应的文件夹中
     *
     * @param file     写入文件对象
     * @param fileName 写入文件名
     * @param folder   写入文件对应根文件系统根目录的文件夹中
     * @param compress 是否压缩图片
     * @throws IOException 写文件异常
     */
    public void writeImage(File file, String fileName, String folder, boolean compress) throws IOException;

    /**
     * 将图片写入文件系统根目录对应的文件夹中
     *
     * @param bytes    写入内容
     * @param fileName 写入文件名
     * @param folder   写入文件对应根文件系统根目录的文件夹中
     * @param compress 是否压缩图片
     * @throws IOException 写文件异常
     */
    public void writeImage(byte[] bytes, String fileName, String folder, boolean compress) throws IOException;

    /**
     * 将图片输入流写入文件系统根目录对应的文件夹中（需自行关闭InputStream）
     *
     * @param is       写入内容
     * @param fileName 写入文件名
     * @param folder   写入文件对应根文件系统根目录的文件夹中
     * @param compress 是否压缩图片
     * @throws IOException 写文件异常
     */
    public void writeImage(InputStream is, String fileName, String folder, boolean compress) throws IOException;

    /**
     * 读取文件系统根目录对应文件夹下指定的文件（适合小文件，建议不超过10M）
     *
     * @param fileName 读取文件名
     * @param folder   读取文件所在的目录
     * @return 文件内容
     * @throws IOException 文件不存在
     */
    public byte[] read(String fileName, String folder) throws IOException;

    /**
     * 读取文件系统根目录对应文件夹下指定的文件（适合于大文件，需自行关闭InputStream）
     *
     * @param fileName 读取文件名
     * @param folder   读取文件所在的目录
     * @return 文件内容
     * @throws IOException 文件不存在
     */
    public InputStream inputStream(String fileName, String folder) throws IOException;

    /**
     * 复制文件
     *
     * @param srcFileName  源文件名
     * @param srcFolder    源文件所在目录
     * @param distFileName 目标文件名
     * @param distFolder   目标文件所在目录
     * @throws IOException 文件不存在
     */
    public void copy(String srcFileName, String srcFolder, String distFileName, String distFolder) throws IOException;

    /**
     * 删除文件系统根目录对应文件夹下指定的文件
     *
     * @param fileName 删除文件名
     * @param folder   删除文件所在的目录
     * @return 结果标识
     */
    public FileState delete(String fileName, String folder) throws IOException;

    /**
     * 创建文件夹
     *
     * @param directory 文件夹全名称
     */
    public void createDirecotry(String directory) throws IOException;
}
