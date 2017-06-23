package cn.fooltech.fool_ops.web.basedata;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsPriceService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsVo;
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.domain.excelmap.service.ExcelExceptionService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.ExcelUtils.ImportType;
import cn.fooltech.fool_ops.utils.WebResponseUtils;
import net.sf.json.JSONObject;

/**
 * <p>货品网页控制器类</p>
 * @author rqh
 * @version 1.0
 * @date 2014年12月23日
 */
@Controller
@RequestMapping(value = "/goods")
public class GoodsController {
	
	/**
	 * 货品服务类
	 */
	@Autowired
	private GoodsService goodsService;
	
	/**
	 * 货品定价服务类
	 */
	@Autowired
	private GoodsPriceService priceService;
	
	@Autowired
	private ExcelExceptionService execService;
	
	/**
	 * 客户弹出窗口
	 * @return
	 */
	@RequestMapping(value = "/window")
	public String window(){
		return "/basedata/goodWindow";
	}
	
	/**
	 * 管理页面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/basedata/goodsManage";
	}
	
	/**
	 * 条码窗口
	 * @return
	 */
	@RequestMapping(value = "/barGrid")
	public String barGrid(){
		return "/basedata/barBox";
	}
	
	/**
	 * 获取企业的所有货品
	 * @return
	 */
	@RequestMapping("/getAll")
	@ResponseBody
	public List<CommonTreeVo> getAll(){
		return goodsService.getAll();
	}
	
	/**
	 * 获取企业的全部有效的货品
	 * @return
	 */
	@RequestMapping(value = "/getTree")
	@ResponseBody
	public List<CommonTreeVo> getTree(){
		return goodsService.getTree();
	}
	
	/**
	 * 获取货品组下的有效的货品
	 * @param groupId 货品组ID
	 * @return
	 */
	@RequestMapping("/getPartTree")
	@ResponseBody
	public List<CommonTreeVo> getPartTree(String groupId){
		return goodsService.getPartTree(groupId);
	}
	
	/**
	 * 获取所有标识为货品的记录
	 * @return
	 */
	@RequestMapping(value = "/getChilds")
	@ResponseBody
	public PageJson getChilds(GoodsVo vo, PageParamater paramater){
		Page<GoodsVo> vos = goodsService.getChilds(vo, paramater);
		return new PageJson(vos);
	}
	
	/**
	 * 获取所有标识为货品的记录
	 * @return
	 */
	@RequestMapping(value = "/comboData")
	@ResponseBody
	public Object comboData(GoodsVo vo, PageParamater paramater){
		Page<GoodsVo> vos = goodsService.getChilds(vo, paramater);
		return new PageJson(vos).getRows();
	}
	
	/**
	 * 获取某张单据下的所有货品
	 * @param billId 仓库单据ID
	 * @param paramater
	 * @return
	 */
	@RequestMapping("/getGoods")
	@ResponseBody
	public PageJson getGoods(GoodsVo vo, PageParamater paramater){
		return new PageJson(goodsService.getGoods(vo, paramater));
	}
	
	/**
	 * 模糊匹配查找货品(货品条形码，货品编号，货品名称)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/vagueSearch")
	public List<GoodsVo> vagueSearch(GoodsVo vo){
		return goodsService.vagueSearch(vo);
	}
	
	/**
	 * 添加货品页面
	 * @param parentId 上级货品ID
	 * @param model
	 * @return
	 */
	@RequestMapping("/add")
	public String add(){
		return "/basedata/addGoods";
	}
	
	/**
	 * 编辑货品页面
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model){
		GoodsVo goodsVo = goodsService.getById(id);
		model.put("goods",goodsVo);
		return "/basedata/addGoods";
	}
	
	/**
	 * 新增/编辑货品
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public RequestResult save(GoodsVo vo){
		return goodsService.save(vo);
	}
	
	/**
	 * 删除货品
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody 
	public RequestResult delete(String id){
		return goodsService.delete(id);
	}
	@RequestMapping(value="/details")
	public String detailsGoods(HttpServletRequest request,String id){
		if(StringUtils.isNotBlank(id)){
			GoodsVo goods = goodsService.getById(id);
			request.setAttribute("goods", goods);
		}
		return "/basedata/detailsGoods";
	}
	/**
	 * 判断编号是否有效
	 * @param code 编号
	 * @return
	 */
	@RequestMapping("/isCodeValid")
	@ResponseBody
	public RequestResult isCodeValid(GoodsVo vo){
		return goodsService.isCodeValid(vo);
	}
	
	/**
	 * 获取货品的最低销售价、参考单价、成本价
	 * @param vo
	 * @return
	 * 必填字段: billType fid(货品ID) unitId
	 * 可省字段: customerId supplierId goodsSpecId 
	 */
	@RequestMapping("/getOtherPrice")
	@ResponseBody
	public JSONObject getOtherPrice(GoodsVo vo){
		String customerId = vo.getCustomerId();
		String supplierId = vo.getSupplierId();
		String goodsId = vo.getFid();
		String unitId = vo.getUnitId();
		String goodsSpecId = vo.getGoodsSpecId();
		
		JSONObject result = new JSONObject();
		if(vo.getBillType() == WarehouseBuilderCode.xsth || vo.getBillType() == WarehouseBuilderCode.sctl){
			//成本价
			BigDecimal costPrice = goodsService.getCostPrice(customerId, goodsId, unitId, goodsSpecId);
			result.accumulate("costPrice", costPrice);
		}
		else{
			//最低价
			BigDecimal lowestPrice = priceService.getLowestPrice(goodsId, unitId, goodsSpecId);
			result.accumulate("lowestPrice", lowestPrice.toString());
		}
		//参考单价
		BigDecimal referencePrice = goodsService.getReferenceUnitPrice(vo.getBillType(), customerId, supplierId, goodsId, unitId, goodsSpecId);
		result.accumulate("referencePrice", referencePrice.toString());
		return result;
	}
	
	/**
	 * 导出
	 * @param vo
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/export")
	public void export(GoodsVo vo,HttpServletResponse response) throws Exception{
		PageParamater paramater = new PageParamater();
		paramater.setRows(Integer.MAX_VALUE);
		paramater.setStart(0);
		Page<GoodsVo> data = goodsService.getChilds(vo, paramater);
		List<GoodsVo> vos = data.getContent();
		ExcelUtils.exportExcel(GoodsVo.class, vos, "货品.xls", response);
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
		RequestResult result = ExcelUtils.importExcel(GoodsVo.class, request, ImportType.SEQUNENCE, voBeans);
		
		int success = 0,fail = 0;
		
		//判断是否转换成功
		if(result.isSuccess()){
			
			//逐个vo保存，统计成功、失败个数
			for(ImportVoBean voBean:voBeans){
				if(voBean.getVaild()){
					GoodsVo goodsVo = (GoodsVo)voBean.getVo();
					goodsVo.setFlag(Goods.FLAG_GOODS);//只导入货品s
					RequestResult cur = goodsService.save(goodsVo);
					if(cur.getReturnCode() == RequestResult.RETURN_SUCCESS){
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
			
			ExcelUtils.processResultVos(voBeans, (Workbook) result.getData(), code);
			
			String successTag = "导入成功%d个；导入失败%d个；";
            successTag = String.format(successTag, success, fail);

            int returnCode = fail>0?RequestResult.RETURN_FAILURE:RequestResult.RETURN_SUCCESS;

            result = new RequestResult(returnCode, successTag, code);
		}
		WebResponseUtils.writeJsonToHtml(response, result);
	}
	
	/**
	 * 获取某张单据下的所有货品
	 * @param fid 货品ID
	 * @param goodsName 货品名称
	 * @param paramater
	 * @return
	 */
	@RequestMapping("/checkGoodsName")
	@ResponseBody
	public RequestResult checkGoodsName(String fid, String goodsName){
		return goodsService.checkGoodsName(fid, goodsName);
	}
	/**
	 * 通过fid查询货品
	 */
	@RequestMapping("/getGoodsById")
	@ResponseBody
	public GoodsVo findGoodsById(String fid){
		return goodsService.findGoodsById(fid);
	}
}
