package cn.fooltech.fool_ops.domain.datainit;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.PageService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalAccountVo;
import cn.fooltech.fool_ops.domain.wage.service.WageService;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("ops.DataClearService")
public class DataClearService implements PageService {

	private static final String CLEAR_BILL = "classpath:datainit/clear_bill_data.sql";
	private static final String CLEAR_SYS = "classpath:datainit/clear_sys_data.sql";
	private static final String CLEAR_WAGE = "classpath:datainit/clear_wage_data.sql";


	/**
	 * 清空服务类
	 */
	@Autowired
	private SqlDataClearService sqlclearService;
	

	/**
	 * 财务-科目网页服务类 
	 */
	@Autowired
	private FiscalAccountingSubjectService fiscalAccountingSubjectWebService;
	/**
	 * 财务账套网页服务类
	 */
	@Autowired
	private FiscalAccountService fiscalAccountWebService;
	
	@Autowired
	private WageService wageService;
	
	/***
	 * 清空业务数据
	 * @param tag 1、单据格式化；2、财务格式化；3、系统格式化
	 * @param pwd 验证密码
	 * @return
	 */
	public RequestResult clear(int[] tag, String pwd){
		//验证密码，正确清空数据，否则返回错误信息
		boolean checkUserPwd = SecurityUtil.checkUserPwd(pwd);
		if(!checkUserPwd){
			return buildFailRequestResult("输入密码有误,格式化科目失败。");
		}
		System.out.println("===========数据清空开始============");
		System.out.println("===========标识:"+tag+"============");
		long currentTime1 = System.currentTimeMillis();
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		for (int i = 0; i < tag.length; i++) {
			int temp=tag[i];
			switch (temp) {
			case 1://单据格式化
				sqlclearService.clearData(orgId, accId, CLEAR_BILL);
				break;
			case 2://财务格式化
				fiscalAccountingSubjectWebService.saveInitialize(pwd,2,accId);
				//初始工资，工资明细，工资凭证，计算公式
				sqlclearService.clearData(orgId, accId, CLEAR_WAGE);
				break;
			case 3://系统格式化
				List<FiscalAccountVo> org = fiscalAccountWebService.queryByOrg();
				for (FiscalAccountVo vo : org) {
					sqlclearService.clearData(orgId, vo.getFid(), CLEAR_BILL);

					fiscalAccountingSubjectWebService.saveInitialize(pwd,1,vo.getFid());
					//初始工资，工资明细，工资凭证，计算公式
					sqlclearService.clearData(orgId, vo.getFid(), CLEAR_WAGE);
				}

				sqlclearService.clearData(orgId, accId, CLEAR_SYS);
				break;
			default:
				break;
			}
		}

		long currentTime2 = System.currentTimeMillis();
		System.out.println("===========数据清空结束============");
		System.out.println("===========用时:"+(currentTime2-currentTime1)+"ms============");
		return buildSuccessRequestResult();
	}
	
}
