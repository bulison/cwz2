package cn.fooltech.fool_ops.web.excelmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.core.filesystem.FileSystem;
import cn.fooltech.fool_ops.domain.excelmap.service.ExcelExceptionService;
import cn.fooltech.fool_ops.domain.excelmap.vo.ExcelExceptionVo;
import cn.fooltech.fool_ops.utils.ExcelUtils;


/**
 * 导入异常报告
 */
@Controller
@RequestMapping(value = "/ExcelExceptionController")
public class ExcelExceptionController {
	
	@Autowired
	private ExcelExceptionService exceService;
	
	/**
	 * 文件服务器
	 */
	@Autowired
	protected FileSystem fileSystem;
	/**
	 * 管理页面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(HttpServletRequest rq,String code){
		rq.setAttribute("code", code);
		return "/excelMap/excelExceptionManage";
	}
	/**
	 * 分页查询
	 * @param vo
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/query")
	@ResponseBody
	public PageJson query(ExcelExceptionVo vo, PageParamater paramater){
		Page<ExcelExceptionVo> pages = exceService.query(vo, paramater);
		PageJson pageJson = new PageJson(pages);
		return pageJson;
	}
	
	/**
	 * 删除仓库单据
	 * @param id 仓库单据ID
	 * @param buildCode
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id){
		return exceService.delete(id);
	}
	
	/**
	 * 下载excel文件
	 * @throws IOException 
	 */
	@RequestMapping("/excelfile")
	public void getExcelFile(@RequestParam String code, HttpServletResponse response) throws IOException{
		String finalPath = fileSystem.getRoot()+File.separator+ExcelUtils.EXCEL_PATH+
				File.separator+code+".xls";
		response.setContentType("application/msexcel;");                
        response.setHeader("Content-Disposition", new String(("attachment;filename="+"error.xls").getBytes("utf-8"), "UTF-8"));  
        File f = new File(finalPath);
        if(f.exists()){
        	FileInputStream in = new FileInputStream(f);  
            byte b[] = new byte[1024];  
            int i = 0;  
            ServletOutputStream out = response.getOutputStream();  
            while((i=in.read(b))!=  -1){  
                out.write(b, 0, i);  
            }  
            out.flush();  
            out.close();  
            in.close();  
        }
	}
}
