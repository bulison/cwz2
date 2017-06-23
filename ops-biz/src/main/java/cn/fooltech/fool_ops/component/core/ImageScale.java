package cn.fooltech.fool_ops.component.core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>图片缩放(JPG,PNG,BMP)</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年7月21日
 */
public class ImageScale {

    public static final int SIZE_8 = 1;
    public static final int SIZE_16 = 1 << 1;
    public static final int SIZE_32 = 1 << 2;
    public static final int SIZE_64 = 1 << 3;
    public static final int SIZE_128 = 1 << 4;
    public static final int SIZE_256 = 1 << 5;
    public static final int SIZE_512 = 1 << 6;
    public static final int SIZE_512X256 = 1 << 7;
    public static final int SIZE_70X70 = 1 << 8;
    /**
     * 图片类型文件后缀
     */
    public final static List<String> SupportImageType = new ArrayList<String>(Arrays.asList("jpeg", "jpg", "png", "bmp"));
    double support = (double) 3.0;
    double PI = (double) 3.14159265358978;
    double[] contrib;
    double[] normContrib;
    double[] tmpContrib;
    int startContrib, stopContrib;
    int nDots;
    int nHalfDots;
    String fileType;
    private int width;
    private int height;
    private int scaleWidth;

	/*public static void main(String[] args) {
		ImageScale is = new ImageScale();
		try {
			is.scaleByFormat("D:/kola.jpg", "D:/kola.jpg", SIZE_128, true, 30);
			is.scaleByFormat("D:/wing.png", "D:/wing.png", SIZE_128, true, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

    public static boolean supportType(String filetype) {
        return SupportImageType.contains(filetype.toLowerCase());
    }

    /**
     * 以图片宽度为基础，对图片进行等比缩放并保存
     *
     * @param fromFileStr   原图片地址
     * @param saveToFileStr 生成缩略图地址(文件名自动加上尺寸)
     * @param format        例：ImageScale.SIZE_8 | ImageScale.SIZE_16 表示同时生成宽度为8、16的图片
     */
    public void scaleByFormat(String fromFileStr, String saveToFileStr, int format, boolean autoFileName) throws Exception {
        scaleByFormat(fromFileStr, saveToFileStr, format, autoFileName, 0);
    }

    /**
     * 以图片宽度为基础，对图片进行等比缩放并保存
     *
     * @param fromFileStr   原图片地址
     * @param saveToFileStr 生成缩略图地址(文件名自动加上尺寸)
     * @param format        例：ImageScale.SIZE_8 | ImageScale.SIZE_16 表示同时生成宽度为8、16的图片
     * @param roundedCorner 圆角的半径，为0则不切圆角
     */
    public void scaleByFormat(String fromFileStr, String saveToFileStr, int format, boolean autoFileName, int roundedCorner) throws Exception {

        if ((ImageScale.SIZE_8 & format) > 0) {
            scale(fromFileStr, saveToFileStr, 8, autoFileName, roundedCorner);
        }
        if ((ImageScale.SIZE_16 & format) > 0) {
            scale(fromFileStr, saveToFileStr, 16, autoFileName, roundedCorner);
        }
        if ((ImageScale.SIZE_32 & format) > 0) {
            scale(fromFileStr, saveToFileStr, 32, autoFileName, roundedCorner);
        }
        if ((ImageScale.SIZE_64 & format) > 0) {
            scale(fromFileStr, saveToFileStr, 64, autoFileName, roundedCorner);
        }
        if ((ImageScale.SIZE_128 & format) > 0) {
            scale(fromFileStr, saveToFileStr, 128, autoFileName, roundedCorner);
        }
        if ((ImageScale.SIZE_256 & format) > 0) {
            scale(fromFileStr, saveToFileStr, 256, autoFileName, roundedCorner);
        }
        if ((ImageScale.SIZE_512 & format) > 0) {
            scale(fromFileStr, saveToFileStr, 512, autoFileName, roundedCorner);
        }
        if ((ImageScale.SIZE_512X256 & format) > 0) {
            scale(fromFileStr, saveToFileStr, 512, 256, autoFileName, roundedCorner);
        }
        if ((ImageScale.SIZE_70X70 & format) > 0) {
            scale(fromFileStr, saveToFileStr, 70, 70, autoFileName, roundedCorner);
        }
    }

    /**
     * 以图片宽度为基础，对图片进行等比缩放并保存
     *
     * @param fromFileStr   原图片地址
     * @param saveToFileStr 生成缩略图地址
     * @param formatWideth  生成图片宽度
     */
    public void scale(String fromFileStr, String saveToFileStr, int formatWidth, boolean autoFileName, int roundedCorner) throws Exception {

        BufferedImage srcImage;
        File fromFile = new File(fromFileStr);
        srcImage = javax.imageio.ImageIO.read(fromFile); // construct image

        int imageWidth = srcImage.getWidth(null);
        int imageHeight = srcImage.getHeight(null);

        int formatHeight = (imageHeight * formatWidth) / imageWidth;

        scale(fromFileStr, saveToFileStr, formatWidth, formatHeight, autoFileName, roundedCorner);
    }

    /**
     * 缩放图片至指定宽度、高度
     *
     * @param fromFileStr   原图片地址
     * @param saveToFileStr 生成缩略图地址
     * @param formatWideth  生成图片宽度
     * @param formatHeight  生成图片高度
     */
    public void scale(String fromFileStr, String saveToFileStr,
                      int formatWidth, int formatHeight, boolean autoFileName, int roundedCorner) throws Exception {

        fileType = getFileType(fromFileStr);
        BufferedImage srcImage;
        File fromFile = new File(fromFileStr);
        srcImage = javax.imageio.ImageIO.read(fromFile); // construct image

        int imageWidth = srcImage.getWidth(null);

        //自动加命名
        String newsaveToFileStr = "";
        if (autoFileName) {
            String left = fromFileStr.substring(0, fromFileStr.indexOf('.'));
            String right = fromFileStr.substring(fromFileStr.indexOf('.'));
            newsaveToFileStr = left + "_" + formatWidth + "x" + formatHeight + right;
        } else {
            newsaveToFileStr = saveToFileStr;
        }
        File saveFile = new File(newsaveToFileStr);

        BufferedImage newImage = null;

        if (formatWidth > imageWidth) {
            //放大
            newImage = imageZoom(srcImage, formatWidth, formatHeight);
        } else if (formatWidth < imageWidth) {
            //缩小
            newImage = imageZoom(srcImage, formatWidth, formatHeight);
        } else {
            newImage = srcImage;
        }
        if (roundedCorner > 0) {
            newImage = makeRoundedCorner(newImage, roundedCorner);
        }

        ImageIO.write(newImage, "png", saveFile);
    }

    private String getFileType(String path) {
        String right = path.substring(path.indexOf('.') + 1);
        if (!ImageScale.supportType(right)) {
            throw new RuntimeException("unsupport image type!");
        }
        return right;
    }

    /**
     * 对图片进行缩小（使用Lanczos算法）(PNG格式下有点问题，暂时没有解决办法，建议直接使用JDK的缩放)
     *
     * @param srcBufferImage 原始图片Buffer
     * @param w              缩小后图片的宽度
     * @param times          缩小后图片的高度
     */
    public BufferedImage imageZoomOut(BufferedImage srcBufferImage, int w, int h) {
        width = srcBufferImage.getWidth();
        height = srcBufferImage.getHeight();
        scaleWidth = w;

        if (DetermineResultSize(w, h) == 1) {
            return srcBufferImage;
        }
        CalContrib();
        BufferedImage pbOut = HorizontalFiltering(srcBufferImage, w);
        BufferedImage pbFinalOut = VerticalFiltering(pbOut, h);
        return pbFinalOut;
    }

    /**
     * 对图片进行放大（使用JDK直接放大）
     *
     * @param srcBufferImage 原始图片Buffer
     * @param w              放大后图片的宽度
     * @param times          放大后图片的高度
     */
    public BufferedImage imageZoom(BufferedImage srcBufferImage, int w, int h) {

        BufferedImage newImage = new BufferedImage(w, h, srcBufferImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(srcBufferImage, 0, 0, w, h, null);
        g.dispose();

        return newImage;
        //boolean preserveAlpha = "png".equals(fileType) ? false : true;
        //System.out.println("preserveAlpha="+preserveAlpha);
        //return createResizedCopy(srcBufferImage, w, h, preserveAlpha);

    }

    /**
     * 决定图像尺寸
     */
    private int DetermineResultSize(int w, int h) {
        double scaleH, scaleV;
        scaleH = (double) w / (double) width;
        scaleV = (double) h / (double) height;
        // 需要判断一下scaleH，scaleV，不做放大操作
        if (scaleH >= 1.0 && scaleV >= 1.0) {
            return 1;
        }
        return 0;

    } // end of DetermineResultSize()

    private double Lanczos(int i, int inWidth, int outWidth, double Support) {
        double x;
        x = (double) i * (double) outWidth / (double) inWidth;
        return Math.sin(x * PI) / (x * PI) * Math.sin(x * PI / Support)
                / (x * PI / Support);
    }

    private void CalContrib() {
        nHalfDots = (int) ((double) width * support / (double) scaleWidth);
        nDots = nHalfDots * 2 + 1;
        try {
            contrib = new double[nDots];
            normContrib = new double[nDots];
            tmpContrib = new double[nDots];
        } catch (Exception e) {

        }

        int center = nHalfDots;
        contrib[center] = 1.0;

        double weight = 0.0;
        int i = 0;
        for (i = 1; i <= center; i++) {
            contrib[center + i] = Lanczos(i, width, scaleWidth, support);
            weight += contrib[center + i];
        }

        for (i = center - 1; i >= 0; i--) {
            contrib[i] = contrib[center * 2 - i];
        }

        weight = weight * 2 + 1.0;

        for (i = 0; i <= center; i++) {
            normContrib[i] = contrib[i] / weight;
        }

        for (i = center + 1; i < nDots; i++) {
            normContrib[i] = normContrib[center * 2 - i];
        }
    } // end of CalContrib()

    // 处理边缘
    private void CalTempContrib(int start, int stop) {
        double weight = 0;

        int i = 0;
        for (i = start; i <= stop; i++) {
            weight += contrib[i];
        }

        for (i = start; i <= stop; i++) {
            tmpContrib[i] = contrib[i] / weight;
        }

    } // end of CalTempContrib()

    private int GetRedValue(int rgbValue) {
        int temp = rgbValue & 0x00ff0000;
        return temp >> 16;
    }

    private int GetGreenValue(int rgbValue) {
        int temp = rgbValue & 0x0000ff00;
        return temp >> 8;
    }

    private int GetBlueValue(int rgbValue) {
        return rgbValue & 0x000000ff;
    }


    private int ComRGB(int redValue, int greenValue, int blueValue) {
        return (redValue << 16) + (greenValue << 8) + blueValue;
    }

    // 行水平滤波
    private int HorizontalFilter(BufferedImage bufImg, int startX, int stopX,
                                 int start, int stop, int y, double[] pContrib) {
        double valueRed = 0.0;
        double valueGreen = 0.0;
        double valueBlue = 0.0;
        int valueRGB = 0;
        int i, j;

        for (i = startX, j = start; i <= stopX; i++, j++) {
            valueRGB = bufImg.getRGB(i, y);

            valueRed += GetRedValue(valueRGB) * pContrib[j];
            valueGreen += GetGreenValue(valueRGB) * pContrib[j];
            valueBlue += GetBlueValue(valueRGB) * pContrib[j];
        }

        valueRGB = ComRGB(Clip((int) valueRed), Clip((int) valueGreen),
                Clip((int) valueBlue));
        return valueRGB;

    } // end of HorizontalFilter()

    // 图片水平滤波
    private BufferedImage HorizontalFiltering(BufferedImage bufImage, int iOutW) {
        int dwInW = bufImage.getWidth();
        int dwInH = bufImage.getHeight();
        int value = 0;

        int colorType = "png".equals(fileType) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage pbOut = new BufferedImage(iOutW, dwInH, colorType);

        for (int x = 0; x < iOutW; x++) {

            int startX;
            int start;
            int X = (int) (((double) x) * ((double) dwInW) / ((double) iOutW) + 0.5);
            int y = 0;

            startX = X - nHalfDots;
            if (startX < 0) {
                startX = 0;
                start = nHalfDots - X;
            } else {
                start = 0;
            }

            int stop;
            int stopX = X + nHalfDots;
            if (stopX > (dwInW - 1)) {
                stopX = dwInW - 1;
                stop = nHalfDots + (dwInW - 1 - X);
            } else {
                stop = nHalfDots * 2;
            }

            if (start > 0 || stop < nDots - 1) {
                CalTempContrib(start, stop);
                for (y = 0; y < dwInH; y++) {
                    value = HorizontalFilter(bufImage, startX, stopX, start,
                            stop, y, tmpContrib);
                    pbOut.setRGB(x, y, value);
                }
            } else {
                for (y = 0; y < dwInH; y++) {
                    value = HorizontalFilter(bufImage, startX, stopX, start,
                            stop, y, normContrib);
                    pbOut.setRGB(x, y, value);
                }
            }
        }

        return pbOut;

    } // end of HorizontalFiltering()

    private int VerticalFilter(BufferedImage pbInImage, int startY, int stopY,
                               int start, int stop, int x, double[] pContrib) {
        double valueRed = 0.0;
        double valueGreen = 0.0;
        double valueBlue = 0.0;
        int valueRGB = 0;
        int i, j;

        for (i = startY, j = start; i <= stopY; i++, j++) {
            valueRGB = pbInImage.getRGB(x, i);

            valueRed += GetRedValue(valueRGB) * pContrib[j];
            valueGreen += GetGreenValue(valueRGB) * pContrib[j];
            valueBlue += GetBlueValue(valueRGB) * pContrib[j];
        }

        valueRGB = ComRGB(Clip((int) valueRed), Clip((int) valueGreen),
                Clip((int) valueBlue));
        return valueRGB;

    } // end of VerticalFilter()

    private BufferedImage VerticalFiltering(BufferedImage pbImage, int iOutH) {
        int iW = pbImage.getWidth();
        int iH = pbImage.getHeight();
        int value = 0;

        int colorType = "png".equals(fileType) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage pbOut = new BufferedImage(iW, iOutH, colorType);

        for (int y = 0; y < iOutH; y++) {

            int startY;
            int start;
            int Y = (int) (((double) y) * ((double) iH) / ((double) iOutH) + 0.5);

            startY = Y - nHalfDots;
            if (startY < 0) {
                startY = 0;
                start = nHalfDots - Y;
            } else {
                start = 0;
            }

            int stop;
            int stopY = Y + nHalfDots;
            if (stopY > (int) (iH - 1)) {
                stopY = iH - 1;
                stop = nHalfDots + (iH - 1 - Y);
            } else {
                stop = nHalfDots * 2;
            }

            if (start > 0 || stop < nDots - 1) {
                CalTempContrib(start, stop);
                for (int x = 0; x < iW; x++) {
                    value = VerticalFilter(pbImage, startY, stopY, start, stop,
                            x, tmpContrib);
                    pbOut.setRGB(x, y, value);
                }
            } else {
                for (int x = 0; x < iW; x++) {
                    value = VerticalFilter(pbImage, startY, stopY, start, stop,
                            x, normContrib);
                    pbOut.setRGB(x, y, value);
                }
            }

        }

        return pbOut;

    } // end of VerticalFiltering()

    private int Clip(int x) {
        if (x < 0)
            return 0;
        if (x > 255)
            return 255;
        return x;
    }

    public BufferedImage makeRoundedCorner(BufferedImage image,
                                           int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();

        //int colorType = "png".equals(fileType) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        int colorType = BufferedImage.TYPE_INT_ARGB;
        BufferedImage output = new BufferedImage(w, h, colorType);

        Graphics2D g2 = output.createGraphics();

        // This is what we want, but it only does hard-clipping, i.e. aliasing
        // g2.setClip(new RoundRectangle2D ...)

        // so instead fake soft-clipping by first drawing the desired clip shape
        // in fully opaque white with antialiasing enabled...
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius,
                cornerRadius));

        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }

    public BufferedImage createResizedCopy(Image originalImage, int scaledWidth,
                                           int scaledHeight, boolean preserveAlpha) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB
                : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight,
                imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }
}
