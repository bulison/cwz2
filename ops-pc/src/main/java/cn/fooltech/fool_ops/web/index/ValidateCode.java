//package cn.fooltech.fool_ops.web.index;
//
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Graphics;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.util.Random;
//
//import javax.servlet.ServletException;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
//
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
//
///**
// * tomcat5下jsp出现getOutputStream() has already been called for this
// * response异常的原因和解决方法
// * 在tomcat5下jsp中出现此错误一般都是在jsp中使用了输出流（如输出图片验证码，文件下载等），没有妥善处理好的原因。
// * 具体的原因就是在tomcat中jsp编译成servlet之后在函数_jspService(HttpServletRequest request,
// * HttpServletResponse response)的最后有一段这样的代码 finally { if (_jspxFactory != null)
// * _jspxFactory.releasePageContext(_jspx_page_context); }
// * 这里是在释放在jsp中使用的对象，会调用response.getWriter(),因为这个方法是和
// * response.getOutputStream()相冲突的！所以会出现以上这个异常。
// *
// * 然后当然是要提出解决的办法，其实挺简单的（并不是和某些朋友说的那样-- 将jsp内的所有空格和回车符号所有都删除掉），
// *
// * 在使用完输出流以后调用以下两行代码即可： out.clear(); out = pageContext.pushBody();
// *
// * @author liubo
// *
// */
//@SuppressWarnings("restriction")
//public class ValidateCode {
//	public String validateCreate(HttpServletResponse response)
//			throws ServletException, IOException {
//
//		/*
//		 * char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
//		 * 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
//		 * 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
//		 */
//
//		char[] codeSequence = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
//				'9' };
//		// 在内存中创建图象
//		int width = 60, height = 20;
//
//		BufferedImage image = new BufferedImage(width, height,
//				BufferedImage.TYPE_INT_RGB);
//
//		// 获取图形上下文
//		Graphics g = image.createGraphics();
//		// 生成随机类
//		Random random = new Random();
//		// 设定背景色
//		g.setColor(getRandColor(200, 250));
//		g.fillRect(0, 0, width, height);
//
//		// 设定字体
//		g.setFont(new Font("Fixedsys", Font.ITALIC, 18));
//
//		// 画边框
//		// g.setColor(new Color());
//		// g.drawRect(0,0,width-1,height-1);
//
//		// 随机产生255条干扰线，使图象中的认证码不易被其它程序探测到
//		g.setColor(getRandColor(160, 200));
//		for (int i = 0; i < 255; i++) {
//			int x1 = random.nextInt(width);
//			int y1 = random.nextInt(height);
//			int x2 = random.nextInt(12);
//			int y2 = random.nextInt(12);
//			g.drawLine(x1, y1, x1 + x2, y1 + y2);
//		}
//		// 取随机产生的认证码(4位数字)
//		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
//		StringBuffer randomCode = new StringBuffer();
//		int red = 0, green = 0, blue = 0;
//
//		for (int i = 0; i < 4; i++) {
//			// 得到随机产生的验证码数字。
//			// String strRnd = String.valueOf(codeSequence[random.nextInt(36)]);
//			// 改为只有数字
//			String strRnd = String.valueOf(codeSequence[random.nextInt(10)]);
//			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同
//			red = 20 + random.nextInt(110);
//			green = 20 + random.nextInt(110);
//			blue = 20 + random.nextInt(110);
//			g.setColor(new Color(red, green, blue));
//			g.drawString(strRnd, 13 * i + 6, 16);
//			randomCode.append(strRnd);
//		}
//		// 图像生效
//		g.dispose();
//		// 输出图象到页面
//		ServletOutputStream out;
//
//		out = response.getOutputStream();
////		try {
////			ImageIO.write(image, "PNG", out);
////		} catch (javax.imageio.IIOException e) {
////
////		}
//		try{
//			//ImageIO.write(image,"PNG",out);
//			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
//			encoder.encode(image);
//		}catch(javax.imageio.IIOException e) {
//			System.out.println("--------------------------------------");
//			e.printStackTrace();
//		}
//		out.flush();
//		out.close();
//		return randomCode.toString();
//
//	}
//
//	private Color getRandColor(int fc, int bc) { // 给定范围获得随机颜色
//		Random rd = new Random();
//		if (fc > 255)
//			fc = 255;
//		if (bc > 255)
//			bc = 255;
//		int r = fc + rd.nextInt(bc - fc);
//		int g = fc + rd.nextInt(bc - fc);
//		int b = fc + rd.nextInt(bc - fc);
//		return new Color(r, g, b);
//	}
//}
