package cn.fooltech.fool_ops.web.print;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.exception.ThrowException;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.vo.AuxiliaryAttrVo;
import cn.fooltech.fool_ops.domain.print.entity.PrintTemp;
import cn.fooltech.fool_ops.domain.print.service.PrintTempService;
import cn.fooltech.fool_ops.domain.print.vo.PrintTempVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.WebUtils;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * 
 * <p></p>
 * @author lgk
 * @date 2016年3月22日下午05:18:59
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/printTempController")
public class PrintTempController extends BaseController{
	@Autowired
	private PrintTempService printTempService;
	@Autowired
	private AuxiliaryAttrService auxiliaryAttrWebService;
	@RequestMapping("/manage")
	public String manage(){
		return "/print/manage";
	}
	
	@RequestMapping("/add")
	public String add(){
		return "/print/edit";
	}
	
	@RequestMapping("/edit")
	public String edit(String fid,HttpServletRequest request){
		PrintTempVo vo=printTempService.getById(fid, ThrowException.Throw);
		request.setAttribute("vo",vo);
		return "/print/edit";
	}
	
//	@ResponseBody
//	@RequestMapping(value = "/save")  
//    public RequestResult save(HttpServletRequest request,HttpServletResponse response,
//    		PrintTempVo printTempVo,PageParamater pageParamater, MultipartFile file) { 
//		String path = request.getSession().getServletContext().getRealPath(PrintTempVo.PATH);
//		if(StringUtils.isNotBlank(file.getOriginalFilename())){
//			String curOrgId = SecurityUtil.getCurrentOrgId();
//			String orgId = printTempVo.getOrgId()==null?curOrgId:printTempVo.getOrgId();
//			String fileName = file.getOriginalFilename();
//			if(!fileName.substring(fileName.indexOf('.')).equalsIgnoreCase(PrintTempVo.SUFFIX)){
//				new RequestResult(RequestResult.RETURN_FAILURE, "后缀名必须为.jsp");
//			}
//			AuxiliaryAttrVo auxiliaryAttrVo =auxiliaryAttrWebService.getByFid(printTempVo.getType());
//			File targetFile = new File(path+File.separator+auxiliaryAttrVo.getCode()+File.separator+orgId,fileName);
//			if(!targetFile.exists()){
//				targetFile.mkdirs();
//			}
//			try {
//				file.transferTo(targetFile);
//			} catch (IllegalStateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			printTempVo.setPrintTempUrl("/print/"+auxiliaryAttrVo.getCode()+"/"+orgId+"/"+fileName.substring(0,fileName.length()-4));
//		}
//		return printTempService.save(printTempVo);
//	}
	@RequestMapping(value = "/save")  
    public void save(
    		HttpServletRequest request,HttpServletResponse response,PrintTempVo printTempVo) { 
		String path = request.getSession().getServletContext().getRealPath(PrintTempVo.PATH);
		List<MultipartFile> files = getFiles(request);
		if(files.size()>0){
			MultipartFile file=files.get(0);
			if(StringUtils.isNotBlank(file.getOriginalFilename())){
				String orgId = SecurityUtil.getCurrentOrgId();;
				String fileName = file.getOriginalFilename();
				if(!fileName.substring(fileName.indexOf('.')).equalsIgnoreCase(PrintTempVo.SUFFIX)){
					new RequestResult(RequestResult.RETURN_FAILURE, "后缀名必须为.jsp");
				}
				AuxiliaryAttrVo auxiliaryAttrVo =auxiliaryAttrWebService.getByFid(printTempVo.getType());
				System.out.println(path);
				File targetFile = new File(path+File.separator+auxiliaryAttrVo.getCode()+File.separator+orgId);
				try {
				if(!targetFile.exists()){
					targetFile.mkdirs();
					File targetFile2 = new File(path+File.separator+auxiliaryAttrVo.getCode()+File.separator+orgId+File.separator+fileName);
					file.transferTo(targetFile2);
//					targetFile.createNewFile();
				}
				
					
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				printTempVo.setPrintTempUrl("/print/"+auxiliaryAttrVo.getCode()+"/"+orgId+"/"+fileName.substring(0,fileName.length()-4));
			}
		}
		RequestResult result = printTempService.save(printTempVo);
		WebUtils.writeJsonToHtml(response, result);
	}
	/**
	 * 获取客户端上传的文件
	 * @param request
	 * @return
	 */
	private List<MultipartFile> getFiles(HttpServletRequest request){
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		
		HttpSession session = request.getSession();
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(session.getServletContext());
		if(resolver.isMultipart(request)){
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iterator = multiRequest.getFileNames();
			while(iterator.hasNext()){
				MultipartFile file = multiRequest.getFile(iterator.next());
				files.add(file);
			}
		}
		return files;
	}

	/**
	 * 获取打印模板分页列表信息
	 * @param printTempVo
	 * @param paramater
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageJson list(PrintTempVo printTempVo, PageParamater paramater){
		Page<PrintTempVo> page = printTempService.query(printTempVo, paramater);
		return new PageJson(page);
	}
	
	
	/**
	 * 删除模板
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id,HttpServletRequest request){
		String path = request.getSession().getServletContext().getRealPath(PrintTempVo.PATH);
		PrintTemp printTemp = printTempService.findOne(id);
		File targetFile = new File(path+printTemp.getPrintTempUrl()+PrintTempVo.SUFFIX);
		if(!targetFile.exists()){
			targetFile.delete();
		}
		return printTempService.delete(id);
	}
	
	/**
	 * 下载模板文件
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/download")
    public void download(String id, HttpServletRequest request,
            HttpServletResponse response) throws IOException{
		String path = request.getSession().getServletContext().getRealPath(PrintTempVo.DOWM_PATH);
		PrintTempVo temp =  printTempService.getById(id, ThrowException.Throw);
		File file = new File(path + File.separator + temp.getPrintTempUrl()+".jsp");
		    response.setCharacterEncoding("utf-8");
		    response.setContentType("application/x-msdownload;");
	        response.setHeader("Content-Disposition", "attachment;fileName="
	                + "print.jsp");
	        response.setHeader("Content-Length", String.valueOf(file.length()));  
	        try {
	            InputStream inputStream = new FileInputStream(file);
	 
	            OutputStream os = response.getOutputStream();
	            byte[] b = new byte[2048];
	            int length;
	            while ((length = inputStream.read(b)) > 0) {
	                os.write(b, 0, length);
	            }
	            os.flush();
	             // 这里主要关闭。
	            os.close();
	            inputStream.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    }
	
}
