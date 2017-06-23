package cn.fooltech.fool_ops.web.cashier;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.cashier.entity.BankBill;
import cn.fooltech.fool_ops.domain.cashier.service.BankBillService;
import cn.fooltech.fool_ops.domain.cashier.service.BankInitService;
import cn.fooltech.fool_ops.domain.cashier.vo.BankBillVo;
import cn.fooltech.fool_ops.domain.cashier.vo.BankInitVo;
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.excelmap.service.ExcelExceptionService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalConfig;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalConfigService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalAccountingSubjectVo;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentBillVo;
import cn.fooltech.fool_ops.domain.print.entity.PrintTemp;
import cn.fooltech.fool_ops.domain.print.service.PrintTempService;
import cn.fooltech.fool_ops.utils.*;
import cn.fooltech.fool_ops.utils.ExcelUtils.ImportType;
import cn.fooltech.fool_ops.web.base.BaseController;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.json.JSONArray;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 出纳-银行单据记录网页Controller
 * @author lgk
 * @date 2015年12月16日下午1:36:37
 * @version V1.0
 */

@Controller
@RequestMapping(value = "/cashierBankBillController")
public class BankBillController extends BaseController{
    @Autowired
	private BankBillService bankBillService;
    
    @Autowired
	private FiscalAccountingSubjectService fiscalAccountingSubjectService;
    
    @Autowired
    private BankInitService bankInitService;
    
	// 财务参数设置服务类
	@Autowired
	private FiscalConfigService configService;
	
	@Autowired
	private ExcelExceptionService exExceptionWebService;

	@Autowired
    private PrintTempService printTempWebService;

    /**
	 * 银行没达页面
	 * 银行对账单管理页面
	 * 银行存款对账页面
	 */
	@RequestMapping(value="/manage")
	public String manage(int type){
		if(type==4){
			return "/cashier/balance/manage";
		}else if(type==3){
			return "/cashier/daily/manage";
		}else if(type==5){
			return "/cashier/accountChecking/manage";
		}else if(type==6){
			return "/cashier/cashJournal/manage";
		}else{
			return null;
		}
	}
	
	/**
	 * 余额调节页面
	 * @return
	 */
	@RequestMapping(value="/balanceAdj")
	public String balanceAdj(){
		return "/cashier/balanceAdjustment/balanceAdjustment";
	}
	
	/**
	 * 自动对账设置界面
	 * @return
	 */
	@RequestMapping(value = "/autoCheck")
	public String window(){
		return "/cashier/accountChecking/autoCheck";
	}
	
	/**
	 * 新增银行对账单页面
	 * @return
	 */
	@RequestMapping(value="/add")
	public String add(int type,String subjectId,HttpServletRequest request){
		String fiscalAccountId = SecurityUtil.getFiscalAccountId(); //财务账套
		FiscalConfig mustLeaf=configService.getConfig(fiscalAccountId, "F04");
		if(StringUtils.isNotBlank(subjectId)){
			FiscalAccountingSubjectVo vo=fiscalAccountingSubjectService.getById(subjectId);
			request.setAttribute("subject", vo);
		}
		request.setAttribute("mustLeaf", mustLeaf);
		if(type==4){
			return "/cashier/balance/edit";
		}else if(type==3){
			return "/cashier/daily/edit";
		}else if(type==6){
			return "/cashier/cashJournal/edit";
		}else{
			return null;
		}
	}

    /**
	 * 银行没达
	 * @return
	 */
	@RequestMapping(value="/goBankBill")
	public String goBankBill(Model model,String bid,String type){
		BankInitVo bankInitVo = bankInitService.getById(bid);
		model.addAttribute("type", type);
		model.addAttribute("bankInitVo", bankInitVo);
		return "/cashier/initBank/bankBill";
	}

	/**
	 * 企业没达页面
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String addFirmNotReached(Model model,BankBillVo vo,String view){
		if(StringUtils.isNotBlank(vo.getFid())){
			BankBillVo bankInitVo = bankBillService.getById(vo.getFid());
			model.addAttribute("bankBillVo", bankInitVo);
			model.addAttribute("view", view);
		}
		if (vo.getType() == 1 || vo.getType() == 2) {
			return "/cashier/initBank/bankBillEdit";
		} else if (vo.getType() == 3) {
			return "/cashier/daily/edit";
		} else if (vo.getType() == 4) {
			return "/cashier/balance/edit";
		}
        return "";
	}
	
	/**
	 * 银行账单修改页面
	 * @return
	 */
	@RequestMapping(value="/editBankBill")
	public String editBankBill(HttpServletRequest request,BankBillVo vo){
		BankBillVo billVo = bankBillService.getByFid(vo.getFid());
		request.setAttribute("bankBill", billVo);
		if(vo.getType()==3){
			return "/cashier/daily/edit";
		}else if(vo.getType()==4){
			return "/cashier/balance/edit";
		}else if (vo.getType() == 6) {
			return "/cashier/cashJournal/edit";
		}else{
			return null;
		}
	}

	/**
	 * 银行单据列表
	 * @param bankBillVo
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value = "/listBankBill")
	@ResponseBody
	public PageJson queryBankBill(BankBillVo bankBillVo,PageParamater paramater){
		Page<BankBill> page = bankBillService.query(bankBillVo, paramater);
		PageJson pageJson = new PageJson();
		List<BankBillVo> bills = bankBillService.getVos(page.getContent());
		pageJson.setRows(bankBillService.getVo(bills));
		pageJson.setTotal(page.getTotalElements());
		return pageJson;
 	}

	/**
	 * 银行单据删除
	 * @param bankBillVo
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public RequestResult delete(BankBillVo bankBillVo){
		return bankBillService.delete(bankBillVo);
	}
	
	/**
	 * 银行单据保存
	 * @param bankBillVo
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public RequestResult save(BankBillVo bankBillVo){
		return bankBillService.save(bankBillVo);
	}
	
	/**
	 * 手动对账
	 * @param upId 上表的ID
	 * @param downId 下表的ID
	 * @param checkDateStr 对账日期
	 * @return
	 */
	@RequestMapping(value = "/saveByCheck")
	@ResponseBody
	public RequestResult saveByCheck(String upId, String downId, String checkDateStr){
		return bankBillService.saveByCheck(upId, downId, checkDateStr);
	}
	
	/**
	 * 自动对账
	 * @param checkDateStr 对账日期
	 * @param limitDateStr 截止日期
	 * @param checkDay 相隔日期是否勾选 0：不勾选 1：勾选
	 * @param days 相隔多少日期（当checkDay=1时有效）
	 * @param settlementType 结算方式相同是否勾选 0：不勾选 1：勾选
	 * @param settlementNo 结算号相同是否勾选 0：不勾选 1：勾选
	 * @param settlementDate 结算日期相同是否勾选 0：不勾选 1：勾选
	 * @param subjectId 科目id
	 * @return
	 */
	@RequestMapping(value = "/saveByAutoCheck")
	@ResponseBody
	public RequestResult saveByAutoCheck(String checkDateStr, String limitDateStr, 
			int checkDay, int days, 
			int settlementType, int settlementNo, Integer settlementDate,String subjectId){
		return bankBillService.saveByAutoCheck(checkDateStr, limitDateStr, checkDay,
				days, settlementType, settlementNo, settlementDate,subjectId);
	}
	
	/**
	 * 取消对账
	 * @param ids 多个ID用逗号隔开
	 * @return
	 */
	@RequestMapping(value = "/deleteCheck")
	@ResponseBody
	public RequestResult deleteCheck(@RequestParam String ids){
		return bankBillService.deleteCheck(ids);
	}
	
	/**
	 * 上传文件并导入
	 * @author xjh
	 * @param type 	 类型：
	 *  1--期初企业未到账
	 *	2--期初银行未到账
	 *	3--银行日记账
	 *	4--银行对账单
	 *  5--现金日记账
	 * @date 2015年9月23日
	 */

	@RequestMapping(value="/import", method=RequestMethod.POST)
	@ResponseBody
	public void importExcel(HttpServletRequest request, HttpServletResponse response,Integer type){
		
		List<ImportVoBean> voBeans = Lists.newArrayList();
		//把excel转换成VO对象
		RequestResult result = new RequestResult();
		//现金日记账需要把type参数传入查询cfg_excel_map数据表
		if (type!=null) {
			result = ExcelUtils.importExcelByType(BankBillVo.class, request, ImportType.SEQUNENCE, voBeans, type);
		}
		int success = 0,fail = 0;
		
		//判断是否转换成功
		if(result.getReturnCode() == RequestResult.RETURN_SUCCESS){
			
			//逐个vo保存，统计成功、失败个数
			for(ImportVoBean voBean:voBeans){
				if(voBean.getVaild()){
					BankBillVo newdata = (BankBillVo)voBean.getVo();
					//设置单据类型
					newdata.setType(type);
					RequestResult cur = bankBillService.save(newdata);
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
			String code = DateUtil.format(Calendar.getInstance().getTime(), DateUtil.dateTime);
			exExceptionWebService.save(voBeans, code);
			
			ExcelUtils.processResultVos(voBeans, (Workbook) result.getData(), code);
			
			String successTag = "导入成功%d个；导入失败%d个；";
			successTag = String.format(successTag, success, fail);

            int returnCode = fail>0?RequestResult.RETURN_FAILURE:RequestResult.RETURN_SUCCESS;

            result = new RequestResult(returnCode, successTag, code);
		}
		WebUtils.writeJsonToHtml(response, result);
	}

	/**
	 * 银行对账单打印（支持合并打印, 不允许余额方向相反的一起打印）
	 * @param code
	 * @param billIds 单据ID
	 * @param model
	 * @return
	 */
	@RequestMapping("/print/batch/{code}")
	public String printBatch(@PathVariable(value="code") String code,
							 @RequestParam String billIds,
							 @RequestParam(defaultValue = "1") Integer mergeTotal,
							 @RequestParam(defaultValue = "5") Integer mergeSize, Model model){
	    Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
	    List<String> ids = splitter.splitToList(billIds);
        List<BankBill> bankBills = bankBillService.getByIds(ids);

        List<BankBillVo> mergeBankBills = mergeBills(bankBills);

        if(mergeTotal!=null && mergeTotal==1){
			sumBills(mergeBankBills);
		}else{
			mergeBankBills = sumBills(mergeBankBills, mergeSize);
		}

        model.addAttribute("orgName", SecurityUtil.getCurrentOrg().getOrgName());
        model.addAttribute("data", JSONArray.fromObject(mergeBankBills));
        model.addAttribute("code", code);

        String orgFid = SecurityUtil.getCurrentOrgId();
        PrintTemp temp = printTempWebService.getByOrgCode(orgFid, code);

        if (temp!=null) {
            String printUrl = temp.getPrintTempUrl();
            if (temp.getPageRow() != null) {
                model.addAttribute("pageRow", temp.getPageRow());
            }
            if (StringUtils.isNotBlank(printUrl)) {
                return printUrl;
            }
        }
		return "/cashier/balance/print";//返回模板的路径
    }

	/**
	 * 合计
	 * @param bills
	 * @return
	 */
	private void sumBills(List<BankBillVo> bills){

		BigDecimal credit = BigDecimal.ZERO;
		BigDecimal debit = BigDecimal.ZERO;
		for(BankBillVo bill:bills){
			credit = NumberUtil.add(bill.getCredit(), credit);
			debit = NumberUtil.add(bill.getDebit(), debit);
		}
		BankBillVo newdata = new BankBillVo();
		newdata.setResume("合计");
		newdata.setCredit(credit);
		newdata.setDebit(debit);
		bills.add(newdata);
	}

	/**
	 * 合计
	 * @param bills
	 * @return
	 */
	private List<BankBillVo> sumBills(List<BankBillVo> bills, int mergeSize){

		BigDecimal credit = BigDecimal.ZERO;
		BigDecimal debit = BigDecimal.ZERO;

		List<BankBillVo> billTemp = Lists.newArrayList();

		for(int i=0;i<bills.size();i++){
			BankBillVo bill = bills.get(i);
			credit = NumberUtil.add(bill.getCredit(), credit);
			debit = NumberUtil.add(bill.getDebit(), debit);
			billTemp.add(bill);

			if(((i+1)%mergeSize)==0 || i==bills.size()-1){
				BankBillVo newdata = new BankBillVo();
				newdata.setResume("合计");
				newdata.setCredit(credit);
				newdata.setDebit(debit);
				billTemp.add(newdata);

				credit = BigDecimal.ZERO;
				debit = BigDecimal.ZERO;
			}
		}
		return billTemp;
	}

    /**
     * 合并打印单据
     * 根据科目 摘要 借贷分开合并
     * @param bills
     * @return List<BankBill>
     */
    private List<BankBillVo> mergeBills(List<BankBill> bills) {
        Map<String, BankBillVo> map = Maps.newLinkedHashMap();
        StringBuilder key = new StringBuilder();

        for (BankBill bill : bills) {
        	key.delete(0, key.length());
            //key.append(bill.getFid());
            key.append(bill.getSubject().getFid());
            key.append("#");
            key.append(bill.getResume());

            if (bill.getDebit().compareTo(BigDecimal.ZERO)!=0) {
                key.append("@");
            } else if (bill.getCredit().compareTo(BigDecimal.ZERO)!=0) {
                key.append("$");
            }

            if (map.containsKey(key.toString())) {
                BankBillVo exist = map.get(key.toString());
                boolean debit = key.indexOf("@")>-1?true:false;

                BigDecimal val1 = debit?exist.getDebit():exist.getCredit();
                BigDecimal val2 = debit?bill.getDebit():bill.getCredit();
                if (debit) {
                    exist.setDebit(NumberUtil.add(val1, val2));
                } else {
                    exist.setCredit(NumberUtil.add(val1, val2));
                }
                map.put(key.toString(), exist);
            } else {
                map.put(key.toString(), bankBillService.getVo(bill));
            }
        }
        return Lists.newArrayList(map.values());
    }
	/**
	 * 导出
	 * @param bankBillVo 页面参数
	 */
	@RequestMapping(value = "/export")
	public void export(BankBillVo bankBillVo, HttpServletResponse response) throws Exception {
		
		PageParamater paramater = new PageParamater();
		paramater.setStart(0);
		paramater.setRows(Integer.MAX_VALUE);
		
		Page<BankBill> page = bankBillService.query(bankBillVo, paramater);
		List<BankBillVo> bills = bankBillService.getVos(page.getContent());
		List<BankBillVo> list = bankBillService.getVo(bills);
		Integer type = bankBillVo.getType();
		String exportName="";
		if(type==3){
			exportName="银行日记账.xls";
		}else if(type==4){
			exportName="银行对账单.xls";
		}else if(type==5){
			exportName="现金日记账.xls";
		}
		ExcelUtils.exportExcel(BankBillVo.class,bankBillVo.getType(),list,exportName,response);
	}

}
