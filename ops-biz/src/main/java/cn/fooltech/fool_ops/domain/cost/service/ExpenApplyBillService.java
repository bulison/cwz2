package cn.fooltech.fool_ops.domain.cost.service;

import java.util.Calendar;
import java.util.Date;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.cost.entity.ExpenApplyBill;
import cn.fooltech.fool_ops.domain.cost.repository.ExpenApplyBillRepository;
import cn.fooltech.fool_ops.domain.cost.vo.ExpenApplyBillVo;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>费用申请单网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-05-03 15:32:22
 */
@Service
public class ExpenApplyBillService extends BaseService<ExpenApplyBill, ExpenApplyBillVo, String> {
	
	/**
	 * 费用申请单服务类
	 */
	@Autowired
	private ExpenApplyBillRepository expenApplyBillRepo;
	
	/**
	 * 会计期间服务类
	 */
	@Autowired
	private StockPeriodService stockPeriodService;
	
	/**
	 * 人员服务类
	 */
	@Autowired
	private MemberService memberService;
	
	/**
	 * 机构服务类
	 */
	@Autowired
	private OrgService orgService;
	
	
	/**
	 * 查询费用申请单列表信息，按照费用申请单主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<ExpenApplyBillVo> query(ExpenApplyBillVo vo,PageParamater pageParamater){
		
		String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Direction.DESC, "code");
		PageRequest pageRequest = getPageRequest(pageParamater, sort);
		Date startDate = vo.getStartDate();
		Date endDate = vo.getEndDate();
		String deptId = vo.getDeptId();
		String memberId = vo.getMemberId();
		
		Page<ExpenApplyBill> page = expenApplyBillRepo.findPageBy(accId, startDate, endDate, deptId, memberId, pageRequest);
		return getPageVos(page, pageRequest);
	}
	
	
	/**
	 * 单个费用申请单实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public ExpenApplyBillVo getVo(ExpenApplyBill entity){
		if(entity == null)
			return null;
		ExpenApplyBillVo vo = new ExpenApplyBillVo();
		vo.setCode(entity.getCode());
		vo.setVoucherCode(entity.getVoucherCode());
		vo.setBillType(entity.getBillType());
		vo.setDate(DateUtilTools.date2String(entity.getDate(), DATE));
		vo.setPlanEnd(DateUtilTools.date2String(entity.getPlanEnd(), DATE));
		vo.setAmount(NumberUtil.scale(entity.getAmount(), NumberUtil.PRECISION_SCALE_2));
		vo.setDescribe(entity.getDescribe());
		vo.setAuditTime(DateUtilTools.date2String(entity.getAuditTime(), DATE_TIME));
		vo.setCancelTime(DateUtilTools.date2String(entity.getCancelTime(), DATE_TIME));
		vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime(), DATE_TIME));
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setRecordStatus(entity.getRecordStatus());
		vo.setFid(entity.getFid());
		
		Organization dept = entity.getDept();
		if(dept!=null){
			vo.setDeptId(dept.getFid());
			vo.setDeptName(dept.getOrgName());
		}
		
		Member member = entity.getMember();
		if(member!=null){
			vo.setMemberId(member.getFid());
			vo.setMemberName(member.getUsername());
		}
		
		StockPeriod period = entity.getPeriod();
		vo.setPeriodId(period.getFid());
		vo.setPeriodName(period.getPeriod());
		
		User auditor = entity.getAuditor();
		User cancle = entity.getCancelor();
		User creator = entity.getCreator();
		
		vo.setCreatorName(creator.getUserName());
		
		if(auditor!=null){
			vo.setAuditorName(auditor.getUserName());
		}
		if(cancle!=null){
			vo.setCancelName(cancle.getUserName());		
		}
		
		return vo;
	}
	
	/**
	 * 删除费用申请单<br>
	 */
	public RequestResult delete(String fid){
		ExpenApplyBill entity = expenApplyBillRepo.findOne(fid);
		
		//会计期间
		StockPeriod stockPeriod = entity.getPeriod();
		if(stockPeriod.getCheckoutStatus() == StockPeriod.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "会计期间已结账");
		}
		
		if(entity.getRecordStatus()!=ExpenApplyBill.STATUS_UNAUDITED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "非待审核状态不能删除");
		}
		
		expenApplyBillRepo.delete(fid);
		return new RequestResult();
	}
	

	/**
	 * 新增/编辑费用申请单
	 * @param vo
	 */
	public RequestResult save(ExpenApplyBillVo vo) {
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		Date billDate = DateUtilTools.string2Date(vo.getDate(), DATE);

		boolean exist = this.existTodayVoucherCode(vo.getVoucherCode(), vo.getFid(), billDate);
		if(exist)return buildFailRequestResult("原始单号同一日内重复");
		
		String accId = SecurityUtil.getFiscalAccountId();
		//会计期间
		StockPeriod stockPeriod = stockPeriodService.getPeriod(billDate, accId);
		if(stockPeriod == null){
//			return new RequestResult(RequestResult.RETURN_FAILURE,105, "单据日期没有对应会计期间");
			return new RequestResult(RequestResult.RETURN_FAILURE, "单据日期没有对应会计期间");
		}
		if(stockPeriod.getCheckoutStatus() == StockPeriod.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "会计期间已结账");
		}
		if(stockPeriod.getCheckoutStatus() == StockPeriod.UN_USED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "会计期间未启用");
		}
		
		Member applyer = memberService.get(vo.getMemberId());
		Organization dept = orgService.get(vo.getDeptId());
		
		ExpenApplyBill entity = null;
		if(StringUtils.isBlank(vo.getFid())){
			entity = new ExpenApplyBill();
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setCreateTime(new Date());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		}else {
			entity = expenApplyBillRepo.findOne(vo.getFid());
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
			if(!checkUpdateTime(vo.getUpdateTime(), entity.getUpdateTime())){
				return new RequestResult(RequestResult.RETURN_FAILURE, "数据已被其他用户修改，请刷新再试");
			}
		}
		
		entity.setCode(vo.getCode());
		entity.setVoucherCode(vo.getVoucherCode());
		entity.setBillType(WarehouseBuilderCodeHelper.fysqd);
		entity.setDate(billDate);
		entity.setPlanEnd(DateUtilTools.string2Date(vo.getPlanEnd(), DATE));
		entity.setAmount(vo.getAmount());
		entity.setDescribe(vo.getDescribe());
		entity.setPeriod(stockPeriod);
		entity.setMember(applyer);
		entity.setDept(dept);
		entity.setUpdateTime(DateUtilTools.now());
		
		expenApplyBillRepo.save(entity);
		
		return new RequestResult();
	}

	/**
	 * 判断是否存在原始单号
	 * @param voucherCode
	 * @param excludeId
	 * @param billDate
	 * @return
	 */
	public boolean existTodayVoucherCode(String voucherCode, String excludeId, Date billDate){
		if(Strings.isNullOrEmpty(voucherCode))return false;
		Long count = 0L;
		String accId = SecurityUtil.getFiscalAccountId();
		if(Strings.isNullOrEmpty(excludeId)) {
			count = expenApplyBillRepo.countTodayVoucherCode(accId, voucherCode, billDate);
		}else{
			count = expenApplyBillRepo.countTodayVoucherCode(accId, voucherCode, billDate, excludeId);
		}
		if(count>0)return true;
		else return false;
	}
	
	/**
	 * 审核
	 * @return
	 */
	public RequestResult passAudit(String id){
		
		ExpenApplyBill entity = expenApplyBillRepo.findOne(id);
		if(entity.getRecordStatus()!=ExpenApplyBill.STATUS_UNAUDITED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "非待审核状态");
		}

		//会计期间
		StockPeriod stockPeriod = entity.getPeriod();
		if(stockPeriod.getCheckoutStatus() == StockPeriod.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "会计期间已结账");
		}
		
		entity.setAuditor(SecurityUtil.getCurrentUser());
		entity.setAuditTime(Calendar.getInstance().getTime());
		entity.setRecordStatus(ExpenApplyBill.STATUS_AUDITED);
		
		expenApplyBillRepo.save(entity);
		
		return new RequestResult();
	}
	
	/**
	 * 作废
	 * @return
	 */
	public RequestResult cancle(String id){
		
		ExpenApplyBill entity = expenApplyBillRepo.findOne(id);

		//会计期间
		StockPeriod stockPeriod = entity.getPeriod();
		if(stockPeriod.getCheckoutStatus() == StockPeriod.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "会计期间已结账");
		}
		
		entity.setCancelor(SecurityUtil.getCurrentUser());
		entity.setCancelTime(Calendar.getInstance().getTime());
		entity.setRecordStatus(ExpenApplyBill.STATUS_CANCELED);
		
		expenApplyBillRepo.save(entity);
		
		return new RequestResult();
	}


	@Override
	public CrudRepository<ExpenApplyBill, String> getRepository() {
		return expenApplyBillRepo;
	}
}
