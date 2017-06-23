package cn.fooltech.fool_ops.web.basedata;

import java.net.URLDecoder;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsPrice;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsPriceService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.service.UnitService;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsPriceVo;
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.excelmap.service.ExcelExceptionService;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.WebResponseUtils;
import cn.fooltech.fool_ops.utils.ExcelUtils.ImportType;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>货品定价管理控制器类</p>
 * @author lzf
 * @version 1.0
 * @date 2015年9月8日
 */
@Controller
@RequestMapping(value = "/goodsPriceController")
public class GoodsPriceController extends BaseController{
	
	/**
	 * 货品定价网页服务类
	 */
	@Autowired
	private GoodsPriceService priceService;
	
	@Autowired
	private UnitService unitService;
	
	@Autowired
	private ExcelExceptionService execService;
	
	@Autowired
	private GoodsService goodsService;
	
	/**
	 * 客户弹出窗口
	 * @return
	 */
	@RequestMapping(value = "/window")
	public String window(){
		return "/goodsPrice/goodsPriceWindow";
	}
	
	@RequestMapping(value="/goodsPriceManager")
	public String goodsPriceManager(){
		return "/goodsPrice/goodsPrice";
	}
	
	@RequestMapping(value="/addGoodsPrice")
	public String addGoodsPrice(){
		return "/goodsPrice/addGoodsPrice";
	}
	
	@RequestMapping(value="/editGoodsPrice")
	public String editGoodsPrice(String fid,HttpServletRequest request){
		GoodsPriceVo entity = priceService.getById(fid);
		String goodsId = entity.getGoodsId();
		Unit unitGroup = unitService.getByGoodsId(goodsId);
		String goodsSpecGroupId=goodsService.getById(goodsId).getGoodsSpecGroupId();
		request.setAttribute("entity", entity);
		request.setAttribute("unitGroupId", unitGroup.getFid());
		request.setAttribute("goodsSpecGroupId", goodsSpecGroupId);
		return "/goodsPrice/addGoodsPrice"; 
	}

	@RequestMapping(value="/list")
	@ResponseBody
	public PageJson list(GoodsPriceVo goodsPriceVo, PageParamater pageParamater){
		return new PageJson(priceService.query(goodsPriceVo, pageParamater));
	}
	
	@RequestMapping(value="/save")
	@ResponseBody
	public RequestResult save(GoodsPriceVo vo){
		return priceService.save(vo);
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public RequestResult delete(GoodsPriceVo vo){
		return priceService.delete(vo.getFid());
	}
	
	/**
	 * 导出
	 * @param vo
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/export")
	public void export(GoodsPriceVo vo,HttpServletResponse response) throws Exception{
		
		String goodsName = vo.getGoodsName();
		goodsName = URLDecoder.decode(goodsName,"utf-8");
		vo.setGoodsName(goodsName);
		
		PageParamater pageParamater = new PageParamater();
		pageParamater.setRows(Integer.MAX_VALUE);
		Page<GoodsPriceVo> data = priceService.query(vo, pageParamater);  
		List<GoodsPriceVo> vos = data.getContent();
		ExcelUtils.exportExcel(GoodsPriceVo.class, vos, "货品定价.xls", response);
	}
	
	/**
	 * 上传文件并导入
	 * @author xjh
	 * @date 2015年9月23日
	 */
	@RequestMapping(value="/import", method=RequestMethod.POST)
	@ResponseBody
	public void importExcel(HttpServletRequest request, HttpServletResponse response){
		
		List<ImportVoBean> voBeans = Lists.newArrayList();
		
		//把excel转换成VO对象
		RequestResult result = ExcelUtils.importExcel(GoodsPriceVo.class, request, ImportType.SEQUNENCE, voBeans);
		
		int success = 0,fail = 0;
		
		//判断是否转换成功
		if(result.isSuccess()){
			
			//逐个vo保存，统计成功、失败个数
			for(ImportVoBean voBean:voBeans){
				if(voBean.getVaild()){
					RequestResult cur = priceService.save((GoodsPriceVo)voBean.getVo());
					if(cur.isSuccess()){
						success++;
					}else{
						voBean.setMsg(cur.getMessage());
						voBean.setVaild(false);
						fail++;
					}
				}else{
					fail++;
				}
			}
			
			//使用时间作为流水号，保存异常信息
			String code = DateUtilTools.date2String(Calendar.getInstance().getTime(), 
					DateUtilTools.DATE_PATTERN_YYYYMMDDHHMMssSSS);
			execService.save(voBeans, code);
			
			Object workbook = result.getData();
			ExcelUtils.processResultVos(voBeans, (Workbook) workbook, code);
			
			String successTag = "导入成功%d个；导入失败%d个；";
			successTag = String.format(successTag, success, fail);

            int returnCode = fail>0?RequestResult.RETURN_FAILURE:RequestResult.RETURN_SUCCESS;

            result = new RequestResult(returnCode, successTag, code);
		}
		WebResponseUtils.writeJsonToHtml(response, result);
	}
}
