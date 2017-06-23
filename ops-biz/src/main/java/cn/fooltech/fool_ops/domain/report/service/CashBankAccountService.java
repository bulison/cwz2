package cn.fooltech.fool_ops.domain.report.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.report.repository.CashBankAccountRepository;
import cn.fooltech.fool_ops.domain.report.vo.CashBankAccountVo;
import cn.fooltech.fool_ops.utils.DateUtil;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * @author tjr
 */
@Service("ops.CashBankAccountWebService")
public class CashBankAccountService {

	@Autowired
	private CashBankAccountRepository cbaRepo;

	/**
	 * 获取分页
	 * @param vo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public PageJson getPage(CashBankAccountVo vo, PageParamater paramater) throws Exception {

		String start,end,d_f = "yyyy-MM-dd";
		
		try {
			start = DateUtilTools.date2String(vo.getStartTime(), d_f);
			end = DateUtilTools.date2String(vo.getEndTime(), d_f);
		} catch (Exception e) {
			return new PageJson();
		}

		// 分页
		int pageSize = paramater.getRows();
		int pageNo = paramater.getPage();
		int first = (pageNo - 1) * pageSize;
		
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();

		List list = cbaRepo.getList(vo.getBankId(),
				orgId, accId, start, end, vo.getSort(), vo.getOrderInt(), first,
				pageSize);
		long count = cbaRepo.countList(vo.getBankId(),
				orgId, accId, start, end, vo.getSort(), vo.getOrderInt());
		
		PageJson pageJson = new PageJson();
		if(null!=list&&list.size()>0){
			pageJson.setTotal(count);
			pageJson.setRows(getVos(list));
		}else{
			pageJson.setTotal(0L);
			pageJson.setRows(new ArrayList<CashBankAccountVo>());
		}
		
		return pageJson;
	}

	/**
	 * 获取列表
	 * @param vo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public List<CashBankAccountVo> getList(CashBankAccountVo vo, PageParamater paramater) throws Exception {

		String start,end,d_f = "yyyy-MM-dd";
		
		try {
			start = DateUtilTools.date2String(vo.getStartTime(), d_f);
			end = DateUtilTools.date2String(vo.getEndTime(), d_f);
		} catch (Exception e) {
			return Collections.EMPTY_LIST;
		}

		// 分页
		int pageSize = paramater.getRows();
		int pageNo = paramater.getPage();
		int first = (pageNo - 1) * pageSize;
		
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();

		List list = cbaRepo.getList(vo.getBankId(),
				orgId, accId, start, end, vo.getSort(), vo.getOrderInt(), first,
				pageSize);
		
		if(null!=list&&list.size()>0){
			return getVos(list);
		}else{
			return Collections.EMPTY_LIST;
		}
	}

	public List<CashBankAccountVo> getVos(List<Object[]> list) {
		if (null == list || 0 == list.size())
			return null;
		List<CashBankAccountVo> _l = new ArrayList<CashBankAccountVo>();
		for (Object[] objects : list) {
			_l.add(getVo(objects));
		}
		return _l;
	}

	public CashBankAccountVo getVo(Object[] obj) {
		if (obj == null)
			return null;
		CashBankAccountVo cashBankAccount = new CashBankAccountVo();
		try {
			cashBankAccount.setTime(obj[0] == null ? null : DateUtilTools.string2Date(obj[0].toString()));
			cashBankAccount.setCode(obj[1] == null ? "" : obj[1].toString());
			cashBankAccount.setVoucherCode(obj[2] == null ? "" : obj[2].toString());
			cashBankAccount.setBillType(obj[3] == null ? null : Integer.parseInt(obj[3].toString()));
			cashBankAccount.setIncome(obj[4] == null ? null : obj[4].toString());
			cashBankAccount.setExpend(obj[5] == null ? null : obj[5].toString());
			cashBankAccount.setBalanceAmount(obj[6] == null ? null : obj[6].toString());
			cashBankAccount.setRemark(obj[7] == null ? "" : obj[7].toString());
			cashBankAccount.setAbst(obj[8] == null ? "" : obj[8].toString());
			cashBankAccount.setAbstName(obj[9]==null?"".intern():obj[9].toString());
			cashBankAccount.setBillTypeName(obj[10]==null?"".intern():obj[10].toString());
			cashBankAccount.setContactUnit(obj[11]==null?"".intern():obj[11].toString());
			return cashBankAccount;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
