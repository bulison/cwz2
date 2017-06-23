package cn.fooltech.fool_ops.domain.fiscal.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import cn.fooltech.fool_ops.domain.fiscal.entity.CarryForwardProfitLoss;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.repository.CarryForwardProfitLossRepository;
import cn.fooltech.fool_ops.domain.fiscal.vo.CarryForwardProfitLossVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.voucher.service.VoucherService;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.ErrorCode;
import cn.fooltech.fool_ops.utils.SecurityUtil;


/**
 * <p>结转损益科目网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年2月16日
 */
@Service
public class CarryForwardProfitLossService extends BaseService<CarryForwardProfitLoss, CarryForwardProfitLossVo, String>{
	
	@Autowired
	private CarryForwardProfitLossRepository profitLossRepo;
	
	/**
	 * 科目服务类
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 凭证服务类
	 */
	@Autowired
	private VoucherService voucherService;
	
	/**
	 * 财务会计期间服务类
	 */
	@Autowired
	private FiscalPeriodService periodService;
	
	/**
	 * 分页查询
	 * @param paramater
	 * @return
	 */
	public Page<CarryForwardProfitLossVo> query(PageParamater paramater, int type){
		
		String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Direction.ASC, "outSubject.code");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		
		Page<CarryForwardProfitLoss> page = profitLossRepo.findPageBy(accId, type, pageRequest);
		return getPageVos(page, pageRequest);
	}
	
	/**
	 * 新增(只有新增)
	 * @param vo
	 * @return
	 */
	public RequestResult save(CarryForwardProfitLossVo vo){
		String inSubjectId = vo.getInSubjectId();
		String outSubjectIds = vo.getOutSubjectIds();

		
		FiscalAccountingSubject inSubject = subjectService.get(inSubjectId); //转入科目
		
		if(StringUtils.isNotBlank(outSubjectIds)){
			String[] outSubjectIdArray = outSubjectIds.split(",");
			Organization org = SecurityUtil.getCurrentOrg();
			Organization dept = SecurityUtil.getCurrentDept();
			FiscalAccount acc = SecurityUtil.getFiscalAccount();
			String fiscalAccountId = acc.getFid(); //财务账套
			
			for(String outSubjectId : outSubjectIdArray){
				FiscalAccountingSubject outSubject = subjectService.get(outSubjectId); //转出科目
				Long count = profitLossRepo.countBy(fiscalAccountId, inSubject.getFid(), outSubject.getFid());
				boolean exist = false;
				if(count!=null && count>0)exist = true;
				if(outSubject.getFlag() != FiscalAccountingSubject.FLAG_PARENT && !exist){
					CarryForwardProfitLoss entity = new CarryForwardProfitLoss();
					entity.setOrg(org);
					entity.setInSubject(inSubject);
					entity.setType(vo.getType());
					entity.setOutSubject(outSubject);
					entity.setDept(dept);
					entity.setFiscalAccount(acc);
					entity.setUpdateTime(Calendar.getInstance().getTime());
					profitLossRepo.save(entity);			
				}
			}
		}
		return new RequestResult();
	}
	
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	public RequestResult delete(String fid){
		CarryForwardProfitLoss entity = profitLossRepo.findOne(fid);
		profitLossRepo.delete(entity);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 删除所有记录
	 * @return
	 */
	public RequestResult deleteAll(int type){
		String accId = SecurityUtil.getFiscalAccountId();
		List<CarryForwardProfitLoss> entities = profitLossRepo.findByTypeAndAccId(type, accId);
		profitLossRepo.delete(entities);
		return new RequestResult();
	}
	
	/**
	 * 制作凭证
	 * @param vo
	 * @param flag 是否必须结转  1 是  0 否
	 * @return
	 */
	public RequestResult makeVoucher(CarryForwardProfitLossVo vo, int flag){
		FiscalPeriod fiscalPeriod = periodService.get(vo.getFiscalPeriodId());
		Date voucherDate = DateUtilTools.string2Date(vo.getVoucherDate(), DateUtilTools.DATE_PATTERN_YYYY_MM_DD);
		if(voucherDate == null){
			return buildFailRequestResult("无效操作，凭证日期必填!");
		}
		if(fiscalPeriod == null){
			return buildFailRequestResult("无效操作，财务会计期间不存在!");
		}
		if(fiscalPeriod.getCheckoutStatus() == FiscalPeriod.UN_USED){
			return buildFailRequestResult("无效操作，财务会计期间未启用!");
		}
		if(fiscalPeriod.getCheckoutStatus() == FiscalPeriod.CHECKED){
			return buildFailRequestResult("无效操作，财务会计期间已结账!");
		}
		
		if(0 == flag){
			if(!voucherService.isAllPostedOrCanceled(fiscalPeriod.getFid())){
				return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.EXIST_UNPOSTED_UNCANCEL_VOUCHER, "存在未过账或者未作废的凭证，是否继续结转?");
			}
		}
		
		Organization dept = SecurityUtil.getCurrentDept();
		String orgId = SecurityUtil.getCurrentOrgId();
		String fiscalAccountId = SecurityUtil.getFiscalAccountId();
		String fiscalPeriodId = vo.getFiscalPeriodId();
		String voucherWordId = vo.getVoucherWordId();
		String creatorId = SecurityUtil.getCurrentUserId();
		String deptId = dept != null ? dept.getFid() : "";
		Integer type = vo.getType();
		String resume = vo.getResume();
		
		String voucherWordNumber = profitLossRepo.saveVoucher(orgId, fiscalAccountId, fiscalPeriodId, 
				voucherWordId, creatorId, deptId, type, voucherDate, resume);
		if(Strings.isNullOrEmpty(voucherWordNumber)){
			return buildFailRequestResult("无凭证生成");
		}
		return buildSuccessRequestResult(voucherWordNumber);
	}
	
	/**
	 * 单个实体转vo
	 * @param entity
	 * @return
	 */
	@Override
	public CarryForwardProfitLossVo getVo(CarryForwardProfitLoss entity){
		CarryForwardProfitLossVo vo = new CarryForwardProfitLossVo();
		vo.setFid(entity.getFid());
		vo.setType(entity.getType());
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
		
		//部门
		Organization dept = entity.getDept();
		if(dept != null){
			vo.setDeptId(dept.getFid());
		}
		//转入科目
		FiscalAccountingSubject inSubject = entity.getInSubject();
		if(inSubject != null){
			vo.setInSubjectId(inSubject.getFid());
			vo.setInSubjectCode(inSubject.getCode());
			vo.setInSubjectName(inSubject.getName());
		}
		//转出科目
		FiscalAccountingSubject outSubject = entity.getOutSubject();
		if(outSubject != null){
			vo.setOutSubjectId(outSubject.getFid());
			vo.setOutSubjectCode(outSubject.getCode());
			vo.setOutSubjectName(outSubject.getName());
		}
		return vo;
	}

	@Override
	public CrudRepository<CarryForwardProfitLoss, String> getRepository() {
		return profitLossRepo;
	}
	
	/**
	 * 制作凭证
	 * @param orgId 机构ID
	 * @param fiscalAccountId 财务账套ID
	 * @param fiscalPeriodId 会计期间ID
	 * @param voucherWordId 凭证字ID
	 * @param creatorId 创建人ID
	 * @param deptId 部门ID
	 * @param type 类型
	 * return 凭证字号
	 */
	public String saveVoucher(String orgId, String fiscalAccountId, String fiscalPeriodId, String voucherWordId, 
			String creatorId, String deptId, Integer type, Date voucherDate, String resume){
		return profitLossRepo.saveVoucher(orgId, fiscalAccountId, fiscalPeriodId, voucherWordId, creatorId, 
				deptId, type, voucherDate, resume);
	}
	
	/**
	 * 根据参数统计记录
	 * @param fiscalAccountId 财务账套ID
	 * @param inSubjectId 转入科目ID
	 * @param outSubjectId 转出科目ID
	 * @return
	 */
	public Long countBy(String fiscalAccountId, String inSubjectId, String outSubjectId){
		return profitLossRepo.countBy(fiscalAccountId, inSubjectId, outSubjectId);
	}

	/**
	 * 统计某个科目被引用的次数
	 * @param subjectId 科目ID
	 * @param type 类别:1--结转损益；2--结转制造费用
	 * @return
	 */
	public long countBySubjectIdAndType(String subjectId,Integer type){
		return profitLossRepo.countBySubjectIdAndType(subjectId, type);
	}
}
