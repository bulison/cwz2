package cn.fooltech.fool_ops.web.index;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class ValidateCodeController {

	@SuppressWarnings("restriction")
	@RequestMapping("/ValidateCode")
	public void validateCode(HttpServletRequest request, HttpServletResponse response) {
		/*
		 * char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
		 * 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
		 * 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		 */

		char[] codeSequence = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		// 在内存中创建图象
		int width = 60, height = 20;

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文
		Graphics g = image.createGraphics();
		// 生成随机类
		Random random = new Random();
		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);

		// 设定字体
		g.setFont(new Font("Fixedsys", Font.ITALIC, 18));

		// 画边框
		// g.setColor(new Color());
		// g.drawRect(0,0,width-1,height-1);

		// 随机产生255条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 255; i++) {
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height);
			int x2 = random.nextInt(12);
			int y2 = random.nextInt(12);
			g.drawLine(x1, y1, x1 + x2, y1 + y2);
		}
		// 取随机产生的认证码(4位数字)
		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;		
		for (int i = 0; i < 4; i++) {
			// 得到随机产生的验证码数字。
			// String strRnd = String.valueOf(codeSequence[random.nextInt(36)]);
			// 改为只有数字
			String strRnd = String.valueOf(codeSequence[random.nextInt(10)]);			
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同
			red = 20 + random.nextInt(110);
			green = 20 + random.nextInt(110);
			blue = 20 + random.nextInt(110);
			g.setColor(new Color(red, green, blue));
			g.drawString(strRnd, 13 * i + 6, 16);
			randomCode.append(strRnd);
		}		
		//request.getSession(true).setAttribute("validateCode", randomStr);
		// 图像生效
		g.dispose();

		
		response.setHeader("Pragma", "no-cache");  
		response.setHeader("Cache-Control", "no-cache");  
		response.setDateHeader("Expires", 0);  
		response.setContentType("image/jpeg");  
		
		try{
			ImageIO.write(image,"jpeg", response.getOutputStream());
			
			HttpSession session = (HttpSession) request.getSession();
			session.setAttribute("VALIDATE_CODE", randomCode.toString());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	private Color getRandColor(int fc, int bc) { // 给定范围获得随机颜色
		Random rd = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + rd.nextInt(bc - fc);
		int g = fc + rd.nextInt(bc - fc);
		int b = fc + rd.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}
