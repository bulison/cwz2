package cn.fooltech.fool_ops.domain.warehouse.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.cashier.entity.BankCheckdate;
import cn.fooltech.fool_ops.domain.cashier.service.BankCheckdateService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.warehouse.entity.CashCheck;
import cn.fooltech.fool_ops.domain.warehouse.repository.CashCheckRepository;
import cn.fooltech.fool_ops.domain.warehouse.vo.CashCheckVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>
 * 现金盘点单服务类
 * </p>
 * 
 * @author cwz
 * @version 1.0
 * @date 2016年9月18日
 */
@Service
public class CashCheckService extends BaseService<CashCheck,CashCheckVo,String>  {
	/**
	 * 轧账日期网页服务类
	 */
	@Autowired
	private BankCheckdateService bankCheckdateService;
	
	/**
	 *  现金盘点单持久层
	 */
	@Autowired
	private CashCheckRepository cashCheckRepository;
	
	
	/**
	 * 科目服务类
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;

	/**
	 * 获取现金盘点单
	 * 
	 * @param id
	 *         
	 * @return
	 */
	public CashCheckVo getById(String id) {
		CashCheck entity = cashCheckRepository.findOne(id);
		return getVo(entity);
	}
	/**
	 * 读取上一次盘点数
	 * @return
	 */
	public CashCheckVo queryLastNumber(String subjectId) {
		String accountId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Direction.DESC, "date");
		PageRequest request = getPageRequest(new PageParamater(1, 1, 0), sort);
		CashCheck cashCheck = cashCheckRepository.queryLastNumber(SecurityUtil.getCurrentOrgId(),accountId,subjectId,request);
		if (cashCheck == null)return new CashCheckVo();
		return getVo(cashCheck);
	}

	/**
	 * 实体类转换为VO
	 * 
	 * @param entity
	 * @return
	 */
	public CashCheckVo getVo(CashCheck entity) {
		CashCheckVo vo = new CashCheckVo();
		vo.setFid(entity.getFid());
		vo.setAmount(entity.getAmount());
		vo.setCreateTime(DateUtils.getTimeString(entity.getCreateTime()));
		vo.setDate(DateUtils.getDateString(entity.getDate()));
		vo.setF001(entity.getF001() != null ? entity.getF001().longValue() : null);
		vo.setF002(entity.getF002() != null ? entity.getF002().longValue() : null);
		vo.setF005(entity.getF005() != null ? entity.getF005().longValue() : null);
		vo.setF010(entity.getF010() != null ? entity.getF010().longValue() : null);
		vo.setF020(entity.getF020() != null ? entity.getF020().longValue() : null);
		vo.setF050(entity.getF050() != null ? entity.getF050().longValue() : null);
		vo.setF100(entity.getF100() != null ? entity.getF100().longValue() : null);
		vo.setF_01(entity.getF_01() != null ? entity.getF_01().longValue() : null);
		vo.setF_02(entity.getF_02() != null ? entity.getF_02().longValue() : null);
		vo.setF_05(entity.getF_05() != null ? entity.getF_05().longValue() : null);
		vo.setF_10(entity.getF_10() != null ? entity.getF_10().longValue() : null);
		vo.setF_20(entity.getF_20() != null ? entity.getF_20().longValue() : null);
		vo.setF_50(entity.getF_50() != null ? entity.getF_50().longValue() : null);
		vo.setResume(entity.getResume());
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
		// 创建人
		User creator = entity.getCreator();
		if (creator != null) {
			vo.setCreatorName(creator.getUserName());
			vo.setCreatorId(creator.getFid());
		}
		Organization org = entity.getOrg();
		if(org !=null){
			vo.setDeptId(org.getFid());
			vo.setDeptName(org.getOrgName());
		}
		FiscalAccountingSubject subject = entity.getSubject();
		if (subject != null) {
			vo.setSubjectId(subject.getFid());
			vo.setSubjectName(subject.getName());
			vo.setSubjectCode(subject.getCode());
		}
		return vo;

	}

	/**
	 * 计算总金额
	 * 
	 * @param vo
	 * @return
	 */
	public static BigDecimal totalFamount(CashCheckVo vo) {
		// 1元总金额
		BigDecimal f001 = vo.getF001() == null ? BigDecimal.ZERO : new BigDecimal(vo.getF001());
		// 2元总金额
		BigDecimal f002 = vo.getF002() == null ? BigDecimal.ZERO : new BigDecimal(vo.getF002() * 2);
		// 5元总金额
		BigDecimal f005 = vo.getF005() == null ? BigDecimal.ZERO : new BigDecimal(vo.getF005() * 5);
		// 10元总金额
		BigDecimal f010 = vo.getF010() == null ? BigDecimal.ZERO : new BigDecimal(vo.getF010() * 10);
		// 20元总金额
		BigDecimal f020 = vo.getF020() == null ? BigDecimal.ZERO : new BigDecimal(vo.getF020() * 20);
		// 50元总金额
		BigDecimal f050 = vo.getF050() == null ? BigDecimal.ZERO : new BigDecimal(vo.getF050() * 50);
		// 100元总金额
		BigDecimal f100 = vo.getF100() == null ? BigDecimal.ZERO : new BigDecimal(vo.getF100() * 100);
		// 5角总金额
		BigDecimal f_50 = vo.getF_50() == null ? BigDecimal.ZERO : new BigDecimal(vo.getF_50() * 0.5);
		// 2角总金额
		BigDecimal f_20 = vo.getF_20() == null ? BigDecimal.ZERO : new BigDecimal(vo.getF_20() * 0.2);
		// 1角总金额
		BigDecimal f_10 = vo.getF_10() == null ? BigDecimal.ZERO : new BigDecimal(vo.getF_10() * 0.1);
		// 5分总金额
		BigDecimal f_05 = vo.getF_05() == null ? BigDecimal.ZERO : new BigDecimal(vo.getF_05() * 0.05);
		// 2分总金额
		BigDecimal f_02 = vo.getF_02() == null ? BigDecimal.ZERO : new BigDecimal(vo.getF_02() * 0.02);
		// 1分总金额
		BigDecimal f_01 = vo.getF_01() == null ? BigDecimal.ZERO : new BigDecimal(vo.getF_01() * 0.01);
		BigDecimal totalFamount = BigDecimal.ZERO;
		totalFamount = totalFamount.add(f001).add(f002).add(f005).add(f010).add(f020).add(f050).add(f100).add(f_50)
				.add(f_20).add(f_10).add(f_05).add(f_02).add(f_01);
		return totalFamount.setScale(2, BigDecimal.ROUND_HALF_UP);

	}
	/**
	 * 新增/编辑现金盘点单
	 * @param vo
	 * @return
	 */
	public RequestResult save(CashCheckVo vo){
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return new RequestResult(RequestResult.RETURN_FAILURE,inValid);
		}
		//现金科目盘点，一天只能一次
		CashCheck cashCheck = cashCheckRepository.countByDate(vo.getSubjectId(), DateUtils.getDateFromString(vo.getDate()));
		if (cashCheck!=null&&!vo.getFid().equals(cashCheck.getFid())) {
			return new RequestResult(RequestResult.RETURN_FAILURE,"现金科目一天只能盘点一次!");
		}
		//盘点金额
		BigDecimal totalFamount = totalFamount(vo);
		if(totalFamount.compareTo(BigDecimal.ZERO)==0){
			return new RequestResult(RequestResult.RETURN_FAILURE,"盘点金额不能为0");
		}
//		Organization dept = orgService.get(vo.getDeptId());
		FiscalAccountingSubject subject = null;
		
		if(StringUtils.isNotBlank(vo.getSubjectId())){
			subject = subjectService.get(vo.getSubjectId());
			if(subject==null){
				return new RequestResult(RequestResult.RETURN_FAILURE,"找不到该科目！");
			}
			Short cashSubject = subject.getCashSubject();
			if(cashSubject!=1) return new RequestResult(RequestResult.RETURN_FAILURE,"请选择现金科目！");
		}
		
		CashCheck entity =null;
		if(Strings.isNullOrEmpty(vo.getFid())){
			entity = new CashCheck();
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setCreateTime(new Date());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setDate(DateUtils.getDateFromString(vo.getDate()));
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		}else{
			entity= cashCheckRepository.findOne(vo.getFid());
			if(entity==null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
			//获取最大日期扎帐，出纳结账就不能修改
			BankCheckdate checkdate = bankCheckdateService.getMaxCheckDate();
			if(checkdate!=null){
				//判断账单日期是否大于扎帐日期，小于的就不能修改
				if(entity.getDate().compareTo(checkdate.getCheckDate())<0){
					return new RequestResult(RequestResult.RETURN_FAILURE,"已经出纳结账，不能修改数据。");
				}
			}
			if(!checkUpdateTime(vo.getUpdateTime(), entity.getUpdateTime())){
				return new RequestResult(RequestResult.RETURN_FAILURE, "数据已被其他用户修改，请刷新再试");
			}
			entity.setUpdateTime(new Date());
			
		}
		entity.setAmount(totalFamount);
		entity.setResume(vo.getResume());
		entity.setSubject(subject);
		entity.setF001(vo.getF001()==null?0:vo.getF001().intValue());
		entity.setF002(vo.getF002()==null?0:vo.getF002().intValue());
		entity.setF005(vo.getF005()==null?0:vo.getF005().intValue());
		entity.setF010(vo.getF010()==null?0:vo.getF010().intValue());
		entity.setF020(vo.getF020()==null?0:vo.getF020().intValue());
		entity.setF050(vo.getF050()==null?0:vo.getF050().intValue());
		entity.setF100(vo.getF100()==null?0:vo.getF100().intValue());
		entity.setF_01(vo.getF_01()==null?0:vo.getF_01().intValue());
		entity.setF_02(vo.getF_02()==null?0:vo.getF_02().intValue());
		entity.setF_05(vo.getF_05()==null?0:vo.getF_05().intValue());
		entity.setF_10(vo.getF_10()==null?0:vo.getF_10().intValue());
		entity.setF_20(vo.getF_20()==null?0:vo.getF_20().intValue());
		entity.setF_50(vo.getF_50()==null?0:vo.getF_50().intValue());
		try {
			cashCheckRepository.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return new RequestResult(RequestResult.RETURN_FAILURE,"保存现金盘点单有误");
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 删除现金盘点单<br>
	 */
	public RequestResult delete(String fid){
		CashCheck cashCheck = cashCheckRepository.findOne(fid);
		try {
			if (cashCheck!=null) {
				//获取最大日期扎帐，出纳结账就不能修改
				BankCheckdate checkdate = bankCheckdateService.getMaxCheckDate();
				if(checkdate!=null){
					//判断账单日期是否大于扎帐日期，小于的就不能修改
					if(cashCheck.getDate().compareTo(checkdate.getCheckDate())<0){
						return new RequestResult(RequestResult.RETURN_FAILURE,"已经出纳结账，不能删除数据。");
					}
				}
				cashCheckRepository.delete(fid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new RequestResult(RequestResult.RETURN_FAILURE, "删除失败,请联系管理员。");
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 检查现金盘点单是否当前日期，否则不能修改已经过去日期的盘点单
	 * @param fid
	 * @return
	 */
	public RequestResult checkDate(String fid){
		CashCheck cashCheck = cashCheckRepository.findOne(fid);
		try {
			if (cashCheck!=null) {
				String cashDate = DateUtils.getDateString(cashCheck.getDate());
				String date = DateUtils.getDateString(new Date());
				if(!cashDate.equals(date))return new RequestResult(RequestResult.RETURN_FAILURE, "只能修改当天现金盘点单!");
			}else{
				return new RequestResult(RequestResult.RETURN_FAILURE, "找不到现金盘点单!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new RequestResult(RequestResult.RETURN_FAILURE, "现金盘点单有误！");
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 分页查询现金盘点单
	 * @param checkVo
	 * @param pageParamater
	 * @return
	 */
	public Page<CashCheck> query(CashCheckVo checkVo,PageParamater pageParamater){
		Sort sort = new Sort(Direction.DESC, "createTime"); 
		PageRequest request = getPageRequest(pageParamater,sort);
		return cashCheckRepository.query(checkVo, request);
	}
	@Override
	public CrudRepository<CashCheck, String> getRepository() {
		return cashCheckRepository;
	}
	/**
	 * 检查修改时间戳；相同返回true，否则返回false
	 * @param updateTimeStr
	 * @param updateTime
	 * @return
	 */
	public boolean checkUpdateTime(String updateTimeStr, Date updateTime){
		String updateTimeStr2 = DateUtilTools.date2String(updateTime, 
				DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS);
		if(updateTimeStr.equals(updateTimeStr2))return true;
		return false;
	}
}
