package cn.fooltech.fool_ops.web.excelmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.excelmap.service.ExcelMapService;
import cn.fooltech.fool_ops.domain.excelmap.vo.ExcelMapVo;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * <p>导入导出控制类</p>
 * @author lzf
 * @version 1.0
 * @date 2015年10月26日
 */
@Controller
@RequestMapping(value = "/ExcelMapController")
public class ExcelMapController extends BaseController{
	
	private final String TEMPLATE_PATH = "classpath:xlstemplate/";
	
	@Autowired
	private ExcelMapService excelMapWebService;
	
	/**
	 * 管理页面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/excelMap/excelManage";
	}
	
	/**
	 * 导入页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/importer")
	public String importer(String action,String downHref,String fn,HttpServletRequest rq){
		
		rq.setAttribute("action", action);
		rq.setAttribute("downHref", downHref);
		rq.setAttribute("fn", fn);
		return "/excelMap/import";
	}
	
	/**
	 * 下载导入模板
	 * @param downFile
	 * @param reponse
	 * @throws Exception 
	 */
	@RequestMapping(value = "/downTemplate")
	public void downTemplate(@RequestParam String downFile, HttpServletResponse response) throws Exception{
		
		String downDecode = URLDecoder.decode(downFile, "utf-8");
		File file = ResourceUtils.getFile(TEMPLATE_PATH+downDecode);
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
	
	/**
	 * 列表
	 * @return 
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageJson list(ExcelMapVo vo,PageParamater paramater){
		Page<ExcelMapVo> page = excelMapWebService.query(vo, paramater);
		return new PageJson(page);
	}
	
	/**
	 * 保存
	 * @return 
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public RequestResult save(ExcelMapVo vo){
		return excelMapWebService.save(vo);
	}
	
	/**
	 * 保存
	 * @return 
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public RequestResult delete(ExcelMapVo vo){
		return excelMapWebService.delete(vo.getFid());
	}

}
