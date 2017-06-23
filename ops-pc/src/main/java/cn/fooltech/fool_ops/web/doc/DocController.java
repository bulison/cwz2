package cn.fooltech.fool_ops.web.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/doc")
public class DocController {

	private final String DOC_PATH = "classpath:doc/";
	
	/**
	 * 下载导入模板
	 * @param downFile
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/downDoc")
	public void downTemplate(@RequestParam String downFile, HttpServletResponse response) throws Exception{
		
		String downDecode = URLDecoder.decode(downFile, "utf-8");
		File file = ResourceUtils.getFile(DOC_PATH+downDecode);
		if(!file.exists() || !file.canRead())return;
		OutputStream os = null;
		InputStream is = null;
		try {
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename="+downFile);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			os = response.getOutputStream();
			is = new FileInputStream(file);
			
			byte[] bytes = new byte[1024];
			while(is.read(bytes)!=-1){
				os.write(bytes);
			}
			os.flush();
		
			is.close();
			os.close();
		} catch (Exception e) {
		} finally{
			if(is!=null){
				is.close();
			}
			if(os!=null){
				os.close();
			}
		}
	}
}
