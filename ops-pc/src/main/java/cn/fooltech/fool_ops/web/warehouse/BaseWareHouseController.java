package cn.fooltech.fool_ops.web.warehouse;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.fooltech.fool_ops.domain.transport.service.TransportBilldetailService;
import cn.fooltech.fool_ops.domain.transport.vo.TransportBilldetailVo;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillDetailService;
import cn.fooltech.fool_ops.utils.JsonUtil;
import cn.fooltech.fool_ops.utils.NumberUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.BillRuleService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentBillVo;
import cn.fooltech.fool_ops.domain.print.entity.PrintTemp;
import cn.fooltech.fool_ops.domain.print.service.PrintTempService;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.sysman.service.UserAttrService;
import cn.fooltech.fool_ops.domain.warehouse.builder.IWarehouseWebServiceBuilder;
import cn.fooltech.fool_ops.domain.warehouse.builder.impl.WareHouseWebServiceFactory;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;
import cn.fooltech.fool_ops.domain.warehouse.service.bill.IWareHouseWebService;
import cn.fooltech.fool_ops.domain.warehouse.service.bill.ProductionPlanWebService;
import cn.fooltech.fool_ops.domain.warehouse.service.bill.PurchaseRequisitionWebService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.domain.warehouse.vo.StockLockingVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillDetailVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillVo;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import net.sf.json.JSONObject;

/**
 * <p>仓库单据网页控制器基类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年9月21日
 * @see WarehouseBuilderCode
 */
@Controller
@RequestMapping("/warehouse/{buildCode}/")
public class BaseWareHouseController {
	/**
	 * 收付款单网页服务类
	 */
	@Autowired
	private PaymentBillService paymentBillWebService;
	@Autowired
	private PurchaseRequisitionWebService purchaseRequisitionWebService;

	@Autowired
	private TransportBilldetailService transportBilldetailService;

	/**
	 * 仓库单据网页服务基类
	 */
	@Resource(name = "ops.WarehouseBillService")
	protected WarehouseBillService defaultService;

	@Autowired
	private WarehouseBillDetailService billdetailService;
	
	/**
	 * 仓库单据网页服务工厂类
	 */
	@Autowired
	private WareHouseWebServiceFactory serviceFactory;
	
	/**
	 * 单据单号生成规则网页服务类
	 */
	@Autowired
	private BillRuleService billRuleService;
	
	/**
	 * 机构网页服务类
	 */
	@Autowired
	private OrgService orgService;
	
	/**
	 * 打印模板配置服务表
	 */
	@Autowired
	private PrintTempService printTempService;
	
	/**
	 * 用户属性服务类
	 */
	@Autowired
	private UserAttrService userAttrService;
	
	@Autowired
	private ProductionPlanWebService productionPlanWebService;
	
	/**
	 * 获取仓库单据网页服务类
	 * @param buildCode
	 * @return
	 */
	protected IWareHouseWebService getWebService(WarehouseBuilderCode buildCode){
		IWarehouseWebServiceBuilder builder = serviceFactory.getHandleBuilder(buildCode);
		if(builder == null){
			return defaultService;
		}
		return (IWareHouseWebService) builder;
	}
	
	/**
	 * 初始化仓库单据类型
	 * @param buildCode
	 */
	public void initBillType(WarehouseBillVo vo, WarehouseBuilderCode buildCode){
		Integer billType = WarehouseBuilderCodeHelper.getBillType(buildCode);
		if(billType != null){
			vo.setBillType(billType);
		}
	}
	
	/**
	 * 获取仓库单据分页列表信息
	 * @param vo
	 * @param paramater
	 * @param buildCode
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageJson list(WarehouseBillVo vo, PageParamater paramater, @PathVariable("buildCode") WarehouseBuilderCode buildCode){
		initBillType(vo, buildCode);
		Page<WarehouseBillVo> page = getWebService(buildCode).query(vo, paramater);
		return new PageJson(page);
	}
	
	/**
	 * 新增、编辑仓库单据
	 * @param vo
	 * @param buildCode
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public RequestResult save(WarehouseBillVo vo, @PathVariable("buildCode") WarehouseBuilderCode buildCode){
		initBillType(vo, buildCode);
		return getWebService(buildCode).save(vo);
	}
	
	/**
	 * 删除仓库单据
	 * @param id 仓库单据ID
	 * @param buildCode
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id, @PathVariable("buildCode") WarehouseBuilderCode buildCode){
		return getWebService(buildCode).delete(id);
	}
	
	/**
	 * 管理页面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(@PathVariable("buildCode") WarehouseBuilderCode buildCode) {
		return "/warehourse/" + buildCode + "/"+buildCode+"Manage";
	}

	/**
	 * 添加页面
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(Model model, @PathVariable("buildCode") WarehouseBuilderCode buildCode){
		model.addAttribute("code", getNewCode(buildCode));
		return "/warehourse/" + buildCode + "/add";
	}
	
	/**
	 * 新增、编辑页面
	 * @param id 仓库单据ID
	 * @param mark 0 编辑 、新增   1 复制
	 * @param model
	 * @param buildCode
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(String id, @RequestParam(value = "mark", defaultValue = "0") Integer mark, 
			Model model, @PathVariable("buildCode") WarehouseBuilderCode buildCode){
		if(StringUtils.isBlank(id)){
			model.addAttribute("code", getNewCode(buildCode));
		}
		else{
			WarehouseBillVo bill = getWebService(buildCode).getById(id, true);
			//lzf
			if(mark == 1) {
				model.addAttribute("code", getNewCode(buildCode));
				model.addAttribute("mark", mark);
				bill.setRecordStatus(null);
				bill.setFreeAmount(null);
				bill.setProductionStatus(0);
			}
			model.addAttribute("obj", bill);
			//lzf
		}
		model.addAttribute("billName",WarehouseBuilderCodeHelper.getBillName(buildCode));
		
		String localCache = userAttrService.getLocalCache();
		model.addAttribute("localCache", localCache);
		return "/warehourse/" + buildCode + "/"+buildCode+"Edit";  
	}
	
	
	/**
	 * 仓库单据明细
	 * @param id 仓库单据ID
	 * @param model
	 * @param buildCode
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail(String id, Model model, @PathVariable("buildCode") WarehouseBuilderCode buildCode){
		WarehouseBillVo bill = getWebService(buildCode).getById(id);
		model.addAttribute("obj", bill);
		return "/warehourse/" + buildCode + "/detail";
	}
	
	
	/**
	 * 审核仓库单据
	 * @param id 仓库单据ID
	 * @param vo
	 * @param buildCode
	 * @return
	 */
	@RequestMapping("/passAudit")
	@ResponseBody
	public RequestResult passAudit(String id, StockLockingVo vo, @PathVariable("buildCode") WarehouseBuilderCode buildCode){
		RequestResult result = getWebService(buildCode).passAudit(id, vo);
		return result;
	}
	
	/**
	 * 作废仓库单据
	 * @param id 仓库单据ID
	 * @param buildCode
	 * @return
	 */
	@RequestMapping("/cancel")
	@ResponseBody
	public RequestResult cancel(String id, @PathVariable("buildCode") WarehouseBuilderCode buildCode){
		
		RequestResult result = getWebService(buildCode).cancel(id);
		return result;
	}
	
	
	/**
	 * 获取新单号
	 * @param buildCode
	 * @return
	 */
	protected String getNewCode(WarehouseBuilderCode buildCode){
		int billType = WarehouseBuilderCodeHelper.getBillType(buildCode);
		return billRuleService.getNewCode(billType);
	}
	
	/**
	 * 导出，需要在cfg_excel_map表配置对应字段
	 * @param vo
	 * @param paramater
	 * @param buildCode
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/export")
	public void export(WarehouseBillVo vo, PageParamater paramater, @PathVariable("buildCode") WarehouseBuilderCode buildCode,HttpServletResponse response) throws Exception{
		initBillType(vo, buildCode);
		Page<WarehouseBillVo> page = getWebService(buildCode).query(vo, paramater);
		List<WarehouseBillVo> vos = page.getContent();
		ExcelUtils.exportExcel(WarehouseBillVo.class,buildCode.parseInt(),vos,"export.xls",response);
	}
	
	/**
	 * 打印
	 * @param id 仓库单据ID
	 * @param model
	 * @param buildCode
	 * @return
	 */
	@RequestMapping("/print")
	public String print(String id,String code, Model model, @PathVariable("buildCode") WarehouseBuilderCode buildCode){
		model.addAttribute("org", orgService.getOrg());
		model.addAttribute("data", JsonUtil.toJsonString(defaultService.getById(id, true)));
		PrintTemp  temp =  printTempService.getByOrgId(SecurityUtil.getCurrentOrgId(), code);
		if (temp != null) {
			String printUrl = temp.getPrintTempUrl();
			if (temp.getPageRow() != null) {
				model.addAttribute("pageRow", temp.getPageRow());
			}
			if (StringUtils.isNotBlank(printUrl)) {
				return printUrl;
			}
			
		}
		return "/warehourse/" + buildCode + "/print";
	}


	/**
	 * 获取单据明细
	 * @param billId 单据Id,多个用逗号隔开
	 * @return
	 */
	@RequestMapping("/billDetails")
	@ResponseBody
	public List<WarehouseBillDetailVo> billDetails(String billId){
		WarehouseBill bill = defaultService.get(billId);
		List<WarehouseBillDetail> details = bill.getDetails();
		return billdetailService.getVos(details);
	}
	
	/**
	 * 多单合并
	 * @param billIds 单据IDs,多个用逗号隔开
	 * @param merge 是否合并明细 1：合并；0不合并
	 * @param onlyMaterial 是否只取材料 1：只取材料 0：取所有
	 * @return
	 */
	@RequestMapping("/mergeBillDetails")
	@ResponseBody
	public List<WarehouseBillDetailVo> mergeBillDetails(String billIds, 
			@RequestParam(defaultValue="0",required=false) Integer merge,
			@RequestParam(defaultValue="0",required=false) Integer onlyMaterial){
		return defaultService.mergeBillDetails(billIds, merge, onlyMaterial);
	}

	/**
	 * 获取收货单明细表2
	 * @param billId 收货单主表ID
	 * @return
	 */
	@RequestMapping("/transportBillDetails")
	@ResponseBody
	public List<TransportBilldetailVo> transportBillDetails(@RequestParam String billId){
		return transportBilldetailService.getTransportBillDetailsVo(billId);
	}
	
	/**
	 * 生成采购单
	 * @return
	 * 
	 */
	@RequestMapping(value = "/generats")
	public String generats(String billIds, Model model,@PathVariable("buildCode") WarehouseBuilderCode buildCode){
		WarehouseBillVo bill = purchaseRequisitionWebService.generats(billIds);
		model.addAttribute("code", getNewCode(buildCode));
		model.addAttribute("obj", bill);
		model.addAttribute("billName",WarehouseBuilderCodeHelper.getBillName(buildCode));
		String userId = SecurityUtil.getCurrentUserId();
		String localCache =  userAttrService.getLocalCache();
		model.addAttribute("localCache", localCache);
		return "/warehourse/cgdd/cgddEdit";
	}
	
    /**
     * 修改生产状态
     * @param id 
     * @param productionStatus 生产状况
     * @param buildCode
     * @return
     */
	@RequestMapping(value = "/proStatus", method = RequestMethod.POST)
	@ResponseBody
	public RequestResult proStatus(String id,Integer productionStatus, @PathVariable("buildCode") WarehouseBuilderCode buildCode){
		return productionPlanWebService.proStatus(id, productionStatus);
	}
	
	/**
	 * 从Bom表查找物料组成，如果有相同货品，自动合并
	 * @param vo  表单传输对象- 仓库单据记录明细
	 * @return
	 */
	@RequestMapping(value = "/calculation")
	@ResponseBody
	public List<WarehouseBillDetailVo> calculation(WarehouseBillVo vo){
		return productionPlanWebService.calculationResult(vo);
	}
	
	/**
	 * 根据单据ID获取单据成本金额
	 * @return
	 */
//	@RequestMapping(value = "/getBillSourceAmount")
//	@ResponseBody
//	public String getBillSourceAmount(@RequestParam String billId, @RequestParam String planId, @RequestParam Integer billType){
//		BigDecimal amount = defaultService.getBillSourceAmount(billId, planId, billType);
//		return NumberUtil.bigDecimalToStr(amount, 4);
//	}
	/**
	 * 获取收/负款单号 和 收/负款金额	
	 * @param model
	 * @param type 单据类型（收款单：51；付款的52；）
	 * @param fid  单据id
	 * @param caleType  计算类型(1.整数运算;2.负数运算)
	 * @return
	 */
	@RequestMapping(value = "/getCodeAndAmount")
	@ResponseBody
	public RequestResult getCodeAndAmount(ModelMap model,Integer type,String fid,Integer caleType){
		String code="";
		if (type!=null&&type==51) {
			code=paymentBillWebService.getNewCode(type);
		}
		else if(type!=null&&type==52){
			code=paymentBillWebService.getNewCode(type);
		}
		model.put("code", code);
		
		if(Strings.isNullOrEmpty(fid)) return new RequestResult(RequestResult.RETURN_FAILURE,"编号不能为空！");
		WarehouseBillVo obj=defaultService.getById(fid);
		//合计金额
		BigDecimal totalAmount = Strings.isNullOrEmpty(obj.getTotalAmount())?BigDecimal.ZERO:BigDecimal.valueOf(Double.valueOf(obj.getTotalAmount()));
		totalAmount = NumberUtil.subtract(totalAmount, obj.getDeductionAmount());

		//累计收付款金额
		BigDecimal totalPayAmount =Strings.isNullOrEmpty(obj.getTotalPayAmount())?BigDecimal.ZERO:BigDecimal.valueOf(Double.valueOf(obj.getTotalPayAmount()));
		//免单金额
		BigDecimal freeAmount =Strings.isNullOrEmpty(obj.getFreeAmount())?BigDecimal.ZERO:BigDecimal.valueOf(Double.valueOf(obj.getFreeAmount()));
		//采购入库单、采购发票、 销售出货单、销售发票 ，累计未收/付款金额
		if(caleType!=null&&caleType==1)model.put("amount", totalAmount.subtract(freeAmount).subtract(totalPayAmount));
		//采购退货单、销售退货单，累计未收/付款金额
		if(caleType!=null&&caleType==2)model.put("amount", totalPayAmount.subtract(totalAmount.subtract(freeAmount)));
		
		model.put("totalPayAmount", totalPayAmount);
		model.put("freeAmount", freeAmount);
//		return new RequestResult(RequestResult.RETURN_SUCCESS,model);
		return new RequestResult(RequestResult.RETURN_SUCCESS, "操作成功!", model);
	}
	/**
	 * 保存收付款单和勾对表记录
	 * @param vo			表单传输对象 - 收付款单
	 * @param ckBillType    勾对单据类型：11.采购入库;12.采购退货;15.采购发票;41.销售出货;42.销售退货;44.销售发票.
	 * @param billId  		单据id
	 * @param caleType  	计算类型(1.整数运算;2.负数运算)
	 * @return
	 */
	@RequestMapping(value="/savePaymentReceived")
	@ResponseBody
	public RequestResult savePaymentReceived (PaymentBillVo vo,Integer ckBillType,String billId,Integer caleType){
		return defaultService.savePaymentReceived(vo,ckBillType,billId,caleType);
	}
	
	/**
	 * 弹出收款、付款弹出框
	 * @return
	 */
	@RequestMapping(value="/billCreater")
	public String billCreater (String billId,String billType){
		return "/warehourse/billCheck/createBill";
	}
	
	/**
	 * 绑定日期转换
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);// 严格限制日期输入
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

	/**
	 * 判断箱号和封号是否存在引用的发货单中，如果不存在提示用户是否审核，但不限制审核（不限制原因怕发货单输入错误）；
	 * 判断货品数量是否大于发货单的货品数量（如果一张发货单对应多张收货单，要合并判断），如果大于提示用户是否审核，但不限制审核；
	 * 判断运输数量是否大于发货单的运输数量（如果一张发货单对应多张收货单，要合并判断），如果大于提示用户是否审核，但不限制审核；
	 * @return
	 */
	@RequestMapping(value="/checkShd")
	@ResponseBody
	public RequestResult checkShd(String id){
		return defaultService.checkShd(id);
	}
}
