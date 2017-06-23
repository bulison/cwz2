package cn.fooltech.fool_ops.domain.fiscal.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.entity.PrepaidExpenses;
import cn.fooltech.fool_ops.domain.fiscal.entity.PrepaidExpensesDetail;
import cn.fooltech.fool_ops.domain.fiscal.repository.PrepaidExpensesRepository;
import cn.fooltech.fool_ops.domain.fiscal.vo.PrepaidExpensesVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.OrganizationRepository;
import cn.fooltech.fool_ops.domain.voucher.service.VoucherBillService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>待摊费用网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-04-14 09:16:27
 */
@Service
public class PrepaidExpensesService extends BaseService<PrepaidExpenses,PrepaidExpensesVo,String> {
	@Autowired  
    private JdbcTemplate jdbcTemplate;  
	@Autowired
	private OrganizationRepository orgRepo;
	/**
	 * 待摊费用持久类
	 */
	@Autowired
	private PrepaidExpensesRepository expensesRepository;
	
	/**
	 * 科目服务类
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
//	/**
//	 * 待摊费用明细持久类
//	 */
//	@Autowired
//	private PrepaidExpensesDetailRepository expensesDetailRepository;
	
	/**
	 * 待摊费用明细网页服务类
	 */
	@Autowired
	private PrepaidExpensesDetailService detailService;
	
	/**
	 * 会计期间服务类
	 */
	@Autowired
	private FiscalPeriodService periodService;
	
	/**
	 * 凭证、单据关联服务类
	 */
	@Autowired
	private VoucherBillService voucherBillService;
	
	
	/**
	 * 查询待摊费用列表信息，按照待摊费用主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<PrepaidExpenses> query(PrepaidExpensesVo prepaidExpensesVo,PageParamater pageParamater){
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest request = getPageRequest(pageParamater,sort);
		return expensesRepository.query(prepaidExpensesVo, request);
	}
	
	
	/**
	 * 单个待摊费用实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public PrepaidExpensesVo getVo(PrepaidExpenses entity){
		if(entity == null)
			return null;
		PrepaidExpensesVo vo = new PrepaidExpensesVo();
		vo.setExpensesCode(entity.getExpensesCode());
		vo.setExpensesName(entity.getExpensesName());
		vo.setExpensesAmount(entity.getExpensesAmount());
		if (entity.getDiscountPeriod()!=null) {
			vo.setDiscountPeriod(new BigDecimal(entity.getDiscountPeriod()));
		}
		vo.setShareDate(DateUtilTools.date2String(entity.getShareDate(),DATE));
		vo.setRemark(entity.getRemark());
		vo.setRecordStatus(entity.getRecordStatus());
		vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime(),DATE_TIME));
		vo.setAuditTime(DateUtilTools.date2String(entity.getAuditTime(),DATE_TIME));
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(),DATE_TIME));
		vo.setFid(entity.getFid());
		
		Organization dept = entity.getDept();
		if(dept!=null){
			vo.setDeptId(dept.getFid());
			vo.setDeptName(dept.getOrgName());
		}
		
		FiscalAccountingSubject debitSubject = entity.getDebitSubject();
		if(debitSubject!=null){
			vo.setDebitSubjectId(debitSubject.getFid());
			vo.setDebitSubjectName(debitSubject.getName());
		}
		
		FiscalAccountingSubject creditSubject = entity.getCreditSubject();
		if(creditSubject!=null){
			vo.setCreditSubjectId(creditSubject.getFid());
			vo.setCreditSubjectName(creditSubject.getName());
		}
		
		User creator = entity.getCreator();
		if(creator!=null){
			vo.setCreatorId(creator.getFid());
			vo.setCreatorName(creator.getUserName());
		}
		
		User auditor = entity.getAuditor();
		if(auditor!=null){
			vo.setAuditorId(auditor.getFid());
			vo.setAuditorName(auditor.getUserName());
		}
		
		return vo;
	}
	
	/**
	 * 判断能否修改科目
	 * @return
	 */
	public boolean isEnableEdit(String assetId){
		String accId = SecurityUtil.getFiscalAccountId();
		
		PrepaidExpenses prepaid = expensesRepository.findOne(assetId);
		List<String> detailIds = Lists.newArrayList();
		
		for(PrepaidExpensesDetail detail:prepaid.getDetails()){
			detailIds.add(detail.getFid());
		}
		
		//如果单据之前已经生成过凭证，则不能再次生成
		if(detailIds.size()>0 && voucherBillService.countVoucher(detailIds, WarehouseBuilderCodeHelper.dtfy, accId)>0){
			return false;
		}
		return true;
	}
	
	/**
	 * 删除待摊费用<br>
	 */
	public RequestResult delete(String fid){
		PrepaidExpenses entity = expensesRepository.findOne(fid);
		if(entity==null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "记录不存在，可能已被删除，请刷新再试");
		}
		if(entity.getRecordStatus()==PrepaidExpenses.STATUS_AUDIT){
			return new RequestResult(RequestResult.RETURN_FAILURE, "已审核记录不能删除");
		}
		detailService.deleteByExpensesId(fid);
		expensesRepository.delete(fid);
		return new RequestResult();
	}
	
	/**
	 * 获取待摊费用信息
	 * @param fid 待摊费用ID
	 * @return
	 */
	public PrepaidExpensesVo getByFid(String fid) {
		return getVo(expensesRepository.findOne(fid));
	}
	

	/**
	 * 新增/编辑待摊费用
	 * @param vo
	 */
	public RequestResult save(PrepaidExpensesVo vo) {
		
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
		}
		
		if(!Strings.isNullOrEmpty(vo.getDebitSubjectId()) && !Strings.isNullOrEmpty(vo.getCreditSubjectId()) && vo.getDebitSubjectId().equals(vo.getCreditSubjectId())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "借方科目不能与贷方科目相同");
		}
		
		String fid = vo.getFid();
		String accId = SecurityUtil.getFiscalAccountId();
		if(expensesRepository.countByExpensesCode(fid, vo.getExpensesCode(), accId)>0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "费用编号重复");
		}
		Date shareDate = DateUtilTools.string2Date(vo.getShareDate(), DATE);
		FiscalPeriod period = periodService.getLastCheckedPeriod(accId);
		if(period!=null){
			if(shareDate.compareTo(period.getEndDate())<=0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "计提日期不能小于已结账的财务会计期间的日期");
			}
		}
		
		PrepaidExpenses entity = null;
		if(StringUtils.isBlank(fid)){
			entity = new PrepaidExpenses();
			entity.setCreateTime(Calendar.getInstance().getTime());
			entity.setUpdateTime(Calendar.getInstance().getTime());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setRecordStatus(PrepaidExpenses.STATUS_UN_AUDIT);
		}else {
			entity = expensesRepository.findOne(vo.getFid());
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
			if(!checkUpdateTime(vo.getUpdateTime(), entity.getUpdateTime())){
				return new RequestResult(RequestResult.RETURN_FAILURE, "数据已被其他用户修改，请刷新再试");
			}
			if(!isEnableEdit(vo.getFid())){
				return new RequestResult(RequestResult.RETURN_FAILURE, "待摊费用已生成凭证，不允许修改!");
			}
		}
		
		if(Strings.isNullOrEmpty(vo.getFid()) || entity.getRecordStatus()==PrepaidExpenses.STATUS_UN_AUDIT){
			entity.setExpensesCode(vo.getExpensesCode());
			entity.setExpensesName(vo.getExpensesName());
			entity.setExpensesAmount(vo.getExpensesAmount());
			if (vo.getDiscountPeriod()!=null) {
				entity.setDiscountPeriod(vo.getDiscountPeriod().intValue());
			}
			entity.setShareDate(shareDate);
			entity.setRemark(vo.getRemark());
			
			if(!Strings.isNullOrEmpty(vo.getDeptId())){
				Organization dept = orgRepo.findOne(vo.getDeptId());
				entity.setDept(dept);
			}
		}
		
		if(!Strings.isNullOrEmpty(vo.getDebitSubjectId())){
			FiscalAccountingSubject debitSubject = subjectService.get(vo.getDebitSubjectId());
			entity.setDebitSubject(debitSubject);
		}
		
		if(!Strings.isNullOrEmpty(vo.getCreditSubjectId())){
			FiscalAccountingSubject creditSubject = subjectService.get(vo.getCreditSubjectId());
			entity.setCreditSubject(creditSubject);
		}
		
		expensesRepository.save(entity);
		
		return new RequestResult();
	}
	
	/**
	 * 通过审核
	 * @param fid
	 * @return
	 */
	public RequestResult savePassAudit(String fid){
		PrepaidExpenses entity = expensesRepository.findOne(fid);
		if(entity==null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "记录不存在，可能已被删除，请刷新再试");
		}
		if(entity.getRecordStatus()==PrepaidExpenses.STATUS_AUDIT){
			return new RequestResult(RequestResult.RETURN_FAILURE, "记录已被其他用户审核");
		}
		if(entity.getShareDate()==null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "计提日期不能为空");
		}
		//审核后写入待摊费用计提表(tbd_prepaid_expenses_detail)，写入计提期数的记录，最后一打记录写入余数金额；
		FiscalPeriod period = periodService.getPeriod(entity.getShareDate(), SecurityUtil.getFiscalAccountId());

		Date firstDate = null;
		if(period==null){
			firstDate = entity.getShareDate();
		}else{
			firstDate = period.getEndDate();
		}
		
		entity.setRecordStatus(PrepaidExpenses.STATUS_AUDIT);
		entity.setAuditor(SecurityUtil.getCurrentUser());
		entity.setAuditTime(Calendar.getInstance().getTime());
		expensesRepository.save(entity);
		
		detailService.saveByExpenses(entity, firstDate);
		
		return new RequestResult();
	}
	
	/**
	 * 反审核
	 * @param fid
	 * @return
	 */
	public RequestResult saveCancleAudit(String fid){
		PrepaidExpenses entity = expensesRepository.findOne(fid);
		if(entity==null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "记录不存在，可能已被删除，请刷新再试");
		}
		if(entity.getRecordStatus()==PrepaidExpenses.STATUS_UN_AUDIT){
			return new RequestResult(RequestResult.RETURN_FAILURE, "记录已被其他用户反审核");
		}
	
		if(hasMakeVoucher(entity.getFid())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "已生成凭证的待摊费用单据，不允许反审核!");
		}
		
		entity.setRecordStatus(PrepaidExpenses.STATUS_UN_AUDIT);
		entity.setAuditor(null);
		entity.setAuditTime(null);
		
		//反审后删除待摊费用计提表的记录
		detailService.deleteByExpensesId(fid);
		
		expensesRepository.save(entity);
		
		return new RequestResult();
	}


	@Override
	public CrudRepository<PrepaidExpenses, String> getRepository() {
		return expensesRepository;
	}
	/**
	 * 判断待摊费用是否已经生成过凭证
	 * @param fid 待摊费用单据ID
	 * @return
	 */
	public boolean hasMakeVoucher(String fid){
		String sql = "select count(*) as count from tbd_prepaid_expenses_detail detail " +
					"inner join tbd_voucher_bill relation on relation.fbill_id = detail.fid " + 
					"where relation.fbill_type =? and detail.fexpenses_id = ?";
		
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{WarehouseBuilderCodeHelper.dtfy,fid} );
		if(!map.isEmpty()){
			Integer count = map.get("count")==null?0:Integer.parseInt(map.get("count").toString());
			return count.intValue() > 0;
		}
		return false;
	}
}
