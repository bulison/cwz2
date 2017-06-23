package cn.fooltech.fool_ops.domain.wage.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.fooltech.fool_ops.domain.voucher.service.VoucherBillService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.wage.entity.Wage;
import cn.fooltech.fool_ops.domain.wage.entity.WageDetail;
import cn.fooltech.fool_ops.domain.wage.entity.WageFormula;
import cn.fooltech.fool_ops.domain.wage.repository.WageDetailRepository;
import cn.fooltech.fool_ops.domain.wage.repository.WageRepository;
import cn.fooltech.fool_ops.domain.wage.vo.WageDetailVo;
import cn.fooltech.fool_ops.domain.wage.vo.WageFormulaVo;
import cn.fooltech.fool_ops.domain.wage.vo.WageVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>
 * 工资网页服务类
 * </p>
 * 
 * @author xjh
 * @version 1.0
 * @date 2015-12-21 15:55:44
 */
@Service
public class WageService extends BaseService<Wage,WageVo, String> {
    @Autowired  
    private JdbcTemplate jdbcTemplate;  

	@Autowired
	protected OrgService orgService;
	/**
	 * 工资服务类
	 */
	@Autowired
	private WageRepository wageRepository;

	/**
	 * 工资服务类
	 */
	@Autowired
	private WageDetailService detailWebService;
	
	/**
	 * 工资明细持久层
	 */
	@Autowired
	private WageDetailRepository wageDetailRepository;

	/**
	 * 工资公式服务类
	 */
	@Autowired
	private WageFormulaService formulaService;
	
	/**
	 * 工资公式Web服务类
	 */
	@Autowired
	private WageFormulaService formulaWebService;

	/**
	 * 凭证单据服务类
	 */
	@Autowired
	private VoucherBillService voucherBillService;
	
	/**
	 * 人员服务类
	 */
	@Autowired
	private MemberService memberService;

	/**
	 * 查询工资列表信息，按照工资主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * 
	 * @param wageVo
	 * @param pageParamater
	 */
	public Page<WageVo> query(WageVo wageVo, PageParamater pageParamater) {
		Sort sort = new Sort(Direction.DESC, "wageDate");
		PageRequest request = getPageRequest(pageParamater,sort);
		Page<Wage> page = wageRepository.query(wageVo, request);
		Page<WageVo> vos = getPageVos(page, request);
		return vos;
	}

	/**
	 * 单个工资实体转换为vo
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public WageVo getVo(Wage entity) {
		if (entity == null)
			return null;
		WageVo vo = new WageVo();
		vo.setWageDate(DateUtilTools.date2String(entity.getWageDate(), MONTH));
		vo.setRemark(entity.getRemark());
		vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime(),
				DATE_TIME));
		User creator = entity.getCreator();
		
		vo.setCreatorId(creator.getFid());
		vo.setCreatorName(creator.getUserName());
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(),
				DATE_TIME));

		if (entity.getAuditorTime() != null) {
			vo.setAuditorTime(DateUtilTools.date2String(
					entity.getAuditorTime(), DATE_TIME));
		}else{
			vo.setAuditorTime("");
		}
		if (entity.getAuditor() != null) {
			vo.setAuditorId(entity.getAuditor().getFid());
			vo.setAuditorName(entity.getAuditor().getUserName());
		}else{
			vo.setAuditorId("");
		}
		
		Organization dept = entity.getDept();

		if (dept != null) {
			vo.setDeptId(dept.getFid());
			vo.setDeptName(dept.getOrgName());
		}
		vo.setFid(entity.getFid());

		return vo;
	}

	/**
	 * 删除工资<br>
	 */
	public RequestResult delete(String fid) {

		Wage wage = wageRepository.findOne(fid);
		if (wage.getAuditorTime() != null) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "工资已审核不能删除");
		}

		detailWebService.deleteByWageId(fid);
		super.delete(fid);
		return buildSuccessRequestResult();
	}

	/**
	 * 通过审核
	 * 
	 * @return
	 */
	@Transactional
	public RequestResult passAudit(String fid) {
		Wage wage = wageRepository.findOne(fid);
		if (wage.getAuditorTime() != null) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "工资已审核");
		}
		wage.setAuditor(SecurityUtil.getCurrentUser());
		wage.setAuditorTime(new Date());
		wageRepository.save(wage);
		return buildSuccessRequestResult("审核成功");
	}

	/**
	 * 反审核
	 * 
	 * @return
	 */
	@Transactional
	public RequestResult cancleAudit(String fid) {
		Wage wage = wageRepository.findOne(fid);
		if (wage.getAuditorTime() == null) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "工资非审核状态");
		}

		if (voucherBillService.countVoucher(fid)> 0) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "已生成凭证不能反审");
		}

		wage.setAuditor(null);
		wage.setAuditorTime(null);
		wageRepository.save(wage);
		return buildSuccessRequestResult();
	}

	/**
	 * 获取工资信息
	 * 
	 * @param fid
	 *            工资ID
	 * @return
	 */
	public WageVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(wageRepository.findOne(fid));
	}

	/**
	 * 新增/编辑工资
	 * 
	 * @param vo
	 */
	public RequestResult save(WageVo vo) {
		RequestResult check = checkSave(vo);
		if (check.getReturnCode() == RequestResult.RETURN_FAILURE) {
			return check;
		}

		Wage entity = new Wage();
		if (StringUtils.isBlank(vo.getFid())) {
			entity.setCreateTime(new Date());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setOrg(SecurityUtil.getCurrentOrg());
		} else {
			entity = wageRepository.findOne(vo.getFid());
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
		}

		entity.setDept(orgService.get(vo.getDeptId()));
		
		entity.setRemark(vo.getRemark());

		Date wageDate = DateUtilTools.string2Date(vo.getWageDate(), MONTH);
		entity.setWageDate(wageDate);

		wageRepository.save(entity);
		
//		List<WageFormulaVo> formulas = formulaWebService.getByAccountId(WageFormula.VIEW);
		List<WageFormulaVo> formulas = formulaWebService.getByAccountId(null);
		List<List<WageDetailVo>> detailList = vo.getDetailList(formulas);
		saveWageDetail(entity, detailList);

		return buildSuccessRequestResult();
	}

	/**
	 * 检查是否满足保存的条件
	 * @param vo
	 * @return
	 */
	private RequestResult checkSave(WageVo vo) {
		
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
		}
		
		if (StringUtils.isNotBlank(vo.getFid())) {
			Wage wage = wageRepository.findOne(vo.getFid());
			String updateTime = DateUtilTools.date2String(wage.getUpdateTime(),
					DATE_TIME);
			if (!vo.getUpdateTime().equals(updateTime)) {
				return new RequestResult(RequestResult.RETURN_FAILURE,"数据已被其他用户修改，请刷新再试");
			}
		}
		
		List<WageFormulaVo> formulas = formulaWebService.getByAccountId(null);
		if(formulas.size()==0)return new RequestResult(RequestResult.RETURN_FAILURE, "工资最少要有一项");
		
		Date wageDate = DateUtilTools.string2Date(vo.getWageDate(), MONTH);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(wageDate);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date start = calendar.getTime();

		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date end = calendar.getTime();
		String accountId=SecurityUtil.getFiscalAccountId();
		
		Long existCount = 0L;
		if(StringUtils.isBlank(vo.getFid())){
			existCount=wageRepository.queryByDeptAndAccount(vo.getDeptId(), accountId, start, end);
//			existCount = wageRepository.countByColumn(
//					Restrictions.eq("dept.fid", vo.getDeptId()),
//					Restrictions.eq("fiscalAccount.fid", accountId),
//					Restrictions.between("wageDate", start, end));
		}else{
			existCount=wageRepository.queryByDeptAndAccount(vo.getDeptId(), accountId, start, end,vo.getFid());
//			existCount = wageRepository.countByColumn(
//					Restrictions.eq("dept.fid", vo.getDeptId()),
//					Restrictions.between("wageDate", start, end),
//					Restrictions.eq("fiscalAccount.fid", accountId),
//					Restrictions.ne("fid", vo.getFid()));
		}
		

		if (existCount > 0) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "该部门本月已输入过工资");
		}
		
		List<List<WageDetailVo>> detailVos = vo.getDetailList(formulas);
		
		if(detailVos.size()==0)return new RequestResult(RequestResult.RETURN_FAILURE, "工资明细至少有一条");
		
		Set<String> checkSet = Sets.newHashSet();
		
		for(List<WageDetailVo> rowList: detailVos){
			if(rowList.size()==0)return new RequestResult(RequestResult.RETURN_FAILURE, "工资最少要有一项");
			
			WageDetailVo first = rowList.get(0);
			if(checkSet.contains(first.getMemberId())){
				Member member = memberService.get(first.getMemberId());
				return new RequestResult(RequestResult.RETURN_FAILURE, "人员重复["+member.getUsername()+"]");
			}else{
				checkSet.add(first.getMemberId());
			}
			
			for (WageDetailVo detailVo : rowList) {

				if (StringUtils.isBlank(detailVo.getFormulaId())) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "工资项目不能为空！");
				}
				if (StringUtils.isBlank(detailVo.getMemberId())) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "人员不能为空！");
				}
				if (detailVo.getValue() == null) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "数值不能为空！");
				}
			}
		}

		return buildSuccessRequestResult();
	}

	/**
	 * 保存明细
	 * 
	 * @param entity
	 * @param detailList
	 */
	private List<List<WageDetail>> saveWageDetail(Wage entity,
			List<List<WageDetailVo>> detailList) {

		List<List<WageDetail>> result = Lists.newArrayList();
		List<WageDetail> olds = wageDetailRepository.getByWageMember(entity.getFid(), null);
		for(WageDetail old:olds){
			wageDetailRepository.delete(old.getFid());
		}

		for(List<WageDetailVo> rowList:detailList){
			List<WageDetail> detailResult = detailWebService.save(rowList,entity);
			result.add(detailResult);
		}
		
		return result;
	}

	/**
	 * 引入
	 */
	@Transactional
	public RequestResult saveByImport(WageVo wageVo) {
		
		String deptId = wageVo.getDeptId();
		
		if(StringUtils.isBlank(wageVo.getWageDate())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "工资月份必填");
		}
		if(StringUtils.isBlank(deptId)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "部门必填");
		}
		
		Date wageDate = DateUtilTools.string2Date(wageVo.getWageDate(), MONTH);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(wageDate);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date start = calendar.getTime();

		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date end = calendar.getTime();

		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date preend = calendar.getTime();
		
		String accId = SecurityUtil.getFiscalAccountId();
		PageRequest request = new PageRequest(0, 1, new Sort(Direction.DESC, "wageDate"));
		Wage exist = wageRepository.getByExist(deptId,start, end, accId,request);
		Wage preexist = wageRepository.getByExist(deptId, null, preend, accId,request);
		
		List<Member> members = memberService.findNotDepartureByDeptId(deptId, wageDate);
		List<WageFormula> formulas = formulaService.getByAccountId(SecurityUtil.getFiscalAccountId(), null);
		List<WageDetail> oldDetails = null;
		List<WageDetail> preDetails = null;
		
		if(preexist!=null)preDetails = preexist.getDetails();
		
		//本月份有记录
		if(exist!=null){
			if(exist.getAuditorTime()!=null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "查询该月份已有工资单据并且已审核，不能引入");
			}
			exist.setWageDate(wageDate);
			wageRepository.save(exist);
			
			oldDetails = exist.getDetails();
			detailWebService.deleteByWageId(exist.getFid());
		//本月份没有记录
		}else{
			if(members.size()==0)return new RequestResult(RequestResult.RETURN_FAILURE, "无人员数据，不能引入");
			if(formulas.size()==0)return new RequestResult(RequestResult.RETURN_FAILURE, "无工资项，不能引入");
			exist = new Wage();
			exist.setCreateTime(new Date());
			exist.setCreator(SecurityUtil.getCurrentUser());
			exist.setFiscalAccount(SecurityUtil.getFiscalAccount());
			exist.setOrg(SecurityUtil.getCurrentOrg());
			exist.setDept(orgService.get(deptId));
			exist.setWageDate(wageDate);
			
			wageRepository.save(exist);
		}
		
		for(Member member:members){
			
			List<WageDetailVo> details = Lists.newArrayList();
			
			for(WageFormula formula:formulas){
				WageDetailVo detailVo = new WageDetailVo();
				detailVo.setFormulaId(formula.getFid());
				detailVo.setMemberId(member.getFid());
				
				if(formula.getColumnType()==WageFormula.TYPE_INPUT){
					if(preexist!=null||exist!=null){
						detailVo.setValue(createValue(preDetails, oldDetails,
								formula, member.getFid()));
					}else{
						detailVo.setValue(formula.getDefaultValue());
					}
				}else{
					detailVo.setValue(BigDecimal.ZERO);
				}
				details.add(detailVo);
			}
			detailWebService.save(details, exist);
		}
		
		return new RequestResult(RequestResult.RETURN_SUCCESS,"操作成功",  exist.getFid());
		
	}
	
	/**
	 * 从上个月的工资明细中查出工资值，查不到返回默认值/0
	 * @param preDetails
	 * @param formula
	 * @return
	 */
	private BigDecimal createValue(List<WageDetail> preDetails, List<WageDetail> oldDetails, 
			WageFormula formula, String memberId){
		
		if(oldDetails!=null){
			for(WageDetail oldDetail:oldDetails){
				if(memberId.equals(oldDetail.getMember().getFid())){
					if(oldDetail.getFormula().getFid().equals(formula.getFid())){
						return oldDetail.getValue();
					}
				}
			}
		}
		
		if(preDetails==null)return formula.getDefaultValue();
		
		boolean find = false;
		for(WageDetail detail:preDetails){
			if(memberId.equals(detail.getMember().getFid())){
				if(detail.getFormula().getFid().equals(formula.getFid())){
					return detail.getValue();
				}
				find = true;
			}
		}
		if(find)return BigDecimal.ZERO;
		return formula.getDefaultValue();
	}
	
	/**
	 * 获取存在的工资列
	 * @param fid
	 * @return
	 */
	public List<WageFormulaVo> getExistFormula(String fid) {
		List<WageFormulaVo> formulas = null;
		if(StringUtils.isNotBlank(fid)){
			Wage wage = wageRepository.findOne(fid);
			
			//已审核只查出有数据的列
			if(wage.getAuditorTime()!=null){
				List<WageFormula> formula = detailWebService.getExistFormula(fid);
				formulas = formulaWebService.getVos(formula);
			}else{
//				formulas = formulaWebService.getByAccountId(WageFormula.VIEW);
				formulas = formulaWebService.getByAccountId(null);
			}
		}else{
//			formulas = formulaWebService.getByAccountId(WageFormula.VIEW);
			formulas = formulaWebService.getByAccountId(null);
		}
		
		return formulas;
	}

	/**
	 * 获取存在的工资列（只显示需要显示的列）
	 * @param fid
	 * @return
	 */
	public List<WageFormulaVo> getExistViewFormula(String fid) {
		List<WageFormulaVo> formulas = null;
		if(StringUtils.isNotBlank(fid)){
			Wage wage = wageRepository.findOne(fid);

			//已审核只查出有数据的列
			if(wage.getAuditorTime()!=null){
				List<WageFormula> formula = detailWebService.getExistFormula(fid);
				formulas = formulaWebService.getVos(formula);
			}else{
				formulas = formulaWebService.getByAccountId(WageFormula.VIEW);
			}
		}else{
			formulas = formulaWebService.getByAccountId(WageFormula.VIEW);
		}

		return formulas;
	}

	/**
	 * 查找所有明细
	 * @param wageVo
	 */
	public List<List<List<WageDetailVo>>> queryAllDetails(WageVo wageVo) {

		List<Wage> wages;
		if(StringUtils.isNotBlank(wageVo.getWageDate())||StringUtils.isNotBlank(wageVo.getYear())){
			wages = wageRepository.queryByCriteria(wageVo);
		}else{
			wages = Lists.newArrayList();
		}
		List<List<List<WageDetailVo>>> details = Lists.newArrayList();
		if(wages!=null&&wages.size()>0){
			for(Wage wage:wages){
				List<List<WageDetailVo>> wageDetail = 
						detailWebService.queryDetail(wage.getFid(), wageVo.getMemberId());
				details.add(wageDetail);
			}
		}

		return details;
	}
	
	/**
	 * 根据年份查找所有工资单的月份
	 * @param year
	 * @return
	 */
	public List<String> queryAllMonth(String year, String deptId){
		Date temp = DateUtilTools.string2Date(year, YEAR);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(temp);
		calendar.set(Calendar.MONTH, calendar.getActualMinimum(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date yearStart = calendar.getTime();
		
		calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date yearEnd = calendar.getTime();
		
		String accountId = SecurityUtil.getFiscalAccountId();
		List<String> months = Lists.newArrayList();
		List<Date> list = queryAllMonth(yearStart, yearEnd, accountId, deptId);
//		List<Wage> dates = wageRepository.queryAllMonth(yearStart, yearEnd, accountId, deptId);
//		if(dates!=null&&dates.size()>0){
//			for (int i = 0; i < dates.size(); i++) {
//				months.add(DateUtilTools.date2String(dates.get(i).getWageDate(), MONTH));
//			}
//		}
		for(Date date:list){
			months.add(DateUtilTools.date2String(date, MONTH));
		}
		return months;
	}

	@Override
	public CrudRepository<Wage, String> getRepository() {
		return wageRepository;
	}
	/**
	 * 查找区间内所有工资的月份（排除重复月份）
	 * @param start
	 * @param end
	 * @param accountId
	 * @param deptId
	 * @return
	 */
	public   List<Date> queryAllMonth(Date start, Date end, String accountId, String deptId) 
	{
		String sql="select distinct(fdate) as fdate from tbd_wage where (fdate between ? and ? ) and (faudit_time is not null) and facc_id=? " ;
		if (StringUtils.isNotBlank(deptId)) {
			sql +=" and FDEP_ID='"+deptId+"'";
		}
		sql +=" order by fdate";
		List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql,new Object[]{start,end,accountId});
		List<Date> dates =Lists.newArrayList();
		if(maps==null) return dates;
		for (Map<String, Object> map : maps) {
			Date date = (Date) map.get("fdate");
			dates.add(date);
		}
		return dates;
	}
}
