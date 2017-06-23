/**
 * Description: 文件处理工具类
 */

package cn.fooltech.fool_ops.component.core.filesystem;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.nio.channels.Channel;
import java.nio.charset.Charset;
import java.util.Enumeration;

public class FileUtil {

    private static final int DEFAULT_BUFFER_SIZE = 4096;

    /**
     * 创建文件夹
     *
     * @param directory
     */
    public static FileState createDirectory(String directory) {
        File file = new File(directory);
        if (!file.exists()) {
            boolean result = file.mkdirs();
            return result ? FileState.SUCCEED : FileState.FAIL;
        } else {
            if (!file.isDirectory()) {
                return FileState.NOTDIR;
            }
        }
        return FileState.EXISTS;
    }

    /**
     * 删除文件，不包括删除文件夹
     */
    public static FileState delete(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            boolean result = file.delete();
            return result ? FileState.SUCCEED : FileState.FAIL;
        }
        return FileState.NOTEXISTS;
    }

    /**
     * 获取文件扩展名，没有扩展名时返回空字符串
     *
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1);
        } else {
            return "";
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean exists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * 将输入流转换成字节数组
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] toBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            int index = 0;
            byte[] buffer = new byte[1024 * 512]; // 512KB
            while ((index = in.read(buffer)) != -1) {
                out.write(buffer, 0, index);
            }

            out.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            closeOutputStream(out);
        }
    }

    /**
     * 将文件转换成字节数组
     * @param fileName
     * @return
     * @throws IOException
     */
    public static byte[] toBytes(File file) throws IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);

            return toBytes(in);
        } catch (IOException e) {
            throw e;
        } finally {
            closeInputStream(in);
        }
    }

    /**
     * 将文件转换成字节数组
     * @param fileName
     * @return
     * @throws IOException
     */
    public static byte[] toBytes(String fileName) throws IOException {
        return toBytes(new File(fileName));
    }

    /**
     * 判断文件夹最后一个字符是否为文件分隔符
     *
     * @param fileName
     * @return
     */
    public static boolean lastCharIsFileSeparatorChar(String fileName) {
        return (fileName.charAt(fileName.length() - 1) == File.separatorChar);
    }

    /**
     * 关闭Channel
     *
     * @param channel
     */
    public static void closeChannel(Channel channel) {
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭InputStream
     *
     * @param is
     */
    public static void closeInputStream(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭OutputStream
     *
     * @param os
     */
    public static void closeOutputStream(OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param file
     * @throws FileNotFoundException
     */
    public static void checkFileExists(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在：" + file.getName());
        }
    }

    /**
     * 删除超出指定时间的临时文件（相对系统时间）
     * @param filePath 待删除文件夹
     * @param second 秒
     */
    public static void deleteOutDateFiles(String filePath, long second) {
        File dir = new File(filePath);
        if (dir.exists() && dir.isDirectory()) {
            long currentTime = System.currentTimeMillis();
            File[] files = dir.listFiles();
            for (File file : files) {
                if ((currentTime - file.lastModified()) > second * 1000) {
                    file.delete();
                }
            }
        }
    }

    /**
     * 压缩zip文件
     * @param sourcePath 待压缩目录
     * @param zipFile 压缩后文件，不存在则自动创建
     */
    public static void zip(String sourcePath, File zipFile) {
        File file = new File(sourcePath);
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));
            zip(out, file, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 压缩算法
     *
     * @param out
     * @param file
     * @param base
     * @throws Exception
     */
    private static void zip(ZipOutputStream out, File file, String base)
            throws Exception {
        if (file.isDirectory()) {
            File[] fc = file.listFiles();
            if (base != null)
                out.putNextEntry(new ZipEntry(base + "/"));
            base = base == null ? "" : base + "/";
            for (int i = 0; i < fc.length; i++) {
                zip(out, fc[i], base + fc[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(file);
            byte buffer[] = new byte[DEFAULT_BUFFER_SIZE];
            int count = 0;
            for (int n = 0; -1 != (n = in.read(buffer)); ) {
                out.write(buffer, 0, n);
                count += n;
            }
            in.close();
        }
    }

    /**
     * 解压zip文件，出错抛出运行时异常
     *
     * @param zipFile
     *            待解压的zip文件
     * @param outputPath
     *            解压到的目录，不存在将自动创建
     * @param charset 压缩文件编码，不指定为系统编码
     * @return 返回解压后的目录，即outputPath
     */
    public static File unzip(File zipFile, String outputPath, String charset) {
        if (charset == null) {
            charset = Charset.defaultCharset().name();
        }

        File output = new File(outputPath);
        ZipFile zfile = null;
        try {
            zfile = new ZipFile(zipFile, charset);
            Enumeration zList = zfile.getEntries();
            ZipEntry ze = null;
            byte[] buf = new byte[1024];

            if (!output.exists()) {
                output.mkdirs();
            }

            while (zList.hasMoreElements()) {
                ze = (ZipEntry) zList.nextElement();
                if (ze.isDirectory()) {
                    File f2 = new File(outputPath + File.separator + ze.getName());
                    f2.mkdir();
                    continue;
                }

                OutputStream os = new BufferedOutputStream(
                        new FileOutputStream(getRealFile(outputPath,
                                ze.getName())));
                InputStream is = new BufferedInputStream(
                        zfile.getInputStream(ze));
                int readLen = 0;
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
                is.close();
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (zfile != null) {
                try {
                    zfile.close();
                } catch (IOException e) {
                }
            }
        }

        return output;
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir
     *            指定根目录
     * @param absFileName
     *            相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public static File getRealFile(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                ret = new File(ret, dirs[i]);
            }
            if (!ret.exists()) {
                ret.mkdirs();
            }
            ret = new File(ret, dirs[dirs.length - 1]);
            return ret;
        } else {
            ret = new File(ret, dirs[0]);
        }
        return ret;
    }

    /**
     * 获取文件名的后缀名，截取倒数第一个“.”
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        if (fileName != null && fileName.indexOf(".") != -1) {
            int idx = fileName.lastIndexOf(".");
            return fileName.substring(idx + 1);
        }
        return null;
    }
}
