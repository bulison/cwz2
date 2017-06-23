package cn.fooltech.fool_ops.component.core.filesystem;

import com.sun.imageio.plugins.bmp.BMPImageReader;

import javax.imageio.*;
import javax.imageio.plugins.bmp.BMPImageWriteParam;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.util.Iterator;

public class ImageCompress implements FileCompress {

    /**
     * 压缩图片
     *
     * @param image     图片对象
     * @param extension 图片扩展名
     * @return 新图片数据
     * @throws IOException
     */
    public byte[] compress(File image) throws IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(image);

            return compress(in);
        } catch (IOException e) {
            throw e;
        } finally {
            FileUtil.closeInputStream(in);
        }
    }

    /**
     * 压缩图片
     *
     * @param is
     * @return
     * @throws IOException
     */
    public byte[] compress(InputStream is) throws IOException {
        return compress(FileUtil.toBytes(is));
    }

    /**
     * 压缩图片
     *
     * @param imageData 源图片数据
     * @param extension 图片扩展名
     * @return 新图片数据
     * @throws IOException
     */
    public byte[] compress(byte[] imageData) throws IOException {
        ByteArrayInputStream is = null;
        ByteArrayOutputStream out = null;
        try {
            is = new ByteArrayInputStream(imageData);
            BufferedImage src = ImageIO.read(is);
            out = new ByteArrayOutputStream(imageData.length);
            buildImageWrite(out, src, getImageReader(imageData));
            out.flush();

            return out.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            FileUtil.closeInputStream(is);
            FileUtil.closeOutputStream(out);
        }
    }

    /**
     * 将新Image输出到输出流中
     *
     * @param out
     * @param src
     * @param extension
     * @throws IOException
     */
    private void buildImageWrite(OutputStream out, BufferedImage src, ImageReader reader) throws IOException {
        ImageWriter imgWrier = ImageIO.getImageWriter(reader);
        //ImageWriter imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
        imgWrier.reset();
        imgWrier.setOutput(ImageIO.createImageOutputStream(out));
        imgWrier.write(null, new IIOImage(src, null, null), buildImageWriteParam(reader));
    }

    /**
     * 新Image参数
     *
     * @return
     */
    private ImageWriteParam buildImageWriteParam(ImageReader reader) {

        ImageWriteParam imgWriteParams = null;
        if (reader instanceof BMPImageReader) {
            imgWriteParams = new BMPImageWriteParam(null);
        } else {
            imgWriteParams = new JPEGImageWriteParam(null);
        }

        imgWriteParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imgWriteParams.setCompressionQuality((float) 0.1);
        imgWriteParams.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
        ColorModel colorModel = ColorModel.getRGBdefault();
        imgWriteParams.setDestinationType(new ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));

        return imgWriteParams;
    }

    private ImageReader getImageReader(byte[] imageData) throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(imageData));
        Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
        return iter.next();
    }
}