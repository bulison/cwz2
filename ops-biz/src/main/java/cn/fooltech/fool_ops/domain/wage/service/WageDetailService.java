package cn.fooltech.fool_ops.domain.wage.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.wage.entity.Wage;
import cn.fooltech.fool_ops.domain.wage.entity.WageDetail;
import cn.fooltech.fool_ops.domain.wage.entity.WageFormula;
import cn.fooltech.fool_ops.domain.wage.repository.WageDetailRepository;
import cn.fooltech.fool_ops.domain.wage.vo.WageDetailVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>工资明细网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-12-21 15:56:21
 */
@Service
public class WageDetailService extends BaseService<WageDetail,WageDetailVo,String> {
	
	/**
	 * 工资明细持久层
	 */
	@Autowired
	private WageDetailRepository detailRepository;

	
	/**
	 * 工资公式服务类
	 */
	@Autowired
	private WageFormulaService formulaService;
	
	
	/**
	 * 人员服务类
	 */
	@Autowired
	protected MemberService memberService;
	
	
	/**
	 * 单个工资明细实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public WageDetailVo getVo(WageDetail entity){
		if(entity == null)
			return null;
		WageDetailVo vo = new WageDetailVo();
		vo.setValue(entity.getValue());
		vo.setFid(entity.getFid());
		
		WageFormula formula = entity.getFormula();
		if(formula!=null){
			vo.setFormula(formula.getFormula());
			vo.setFormulaId(formula.getFid());
		}
		
		Member member = entity.getMember();
		if(member!=null){
			vo.setMemberId(member.getFid());
			vo.setMemberCode(member.getUserCode());
			vo.setMemberName(member.getUsername());
			vo.setMemberDept(member.getDept().getOrgName());
		}
		
		Wage wage = entity.getWage();
		if(wage!=null){
			vo.setWageId(wage.getFid());
			vo.setWageDate(DateUtilTools.date2String(wage.getWageDate(), MONTH));
			vo.setDeptId(wage.getDept().getFid());
			vo.setDeptName(wage.getDept().getOrgName());
		}
		
		return vo;
	}
	
	/**
	 * 删除工资明细<br>
	 */
	public RequestResult delete(String fid){
		detailRepository.delete(fid);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 获取工资明细信息
	 * @param fid 工资明细ID
	 * @return
	 */
	public WageDetailVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(detailRepository.findOne(fid));
	}

	/**
	 * 新增工资明细
	 * @param vo
	 */
	public List<WageDetail> save(List<WageDetailVo> vos, Wage wage) {
		
		List<WageDetail> details = Lists.newArrayList();
		List<WageFormula> formulas = formulaService.getByAccountId(SecurityUtil.getFiscalAccountId(), null);
		
		Map<String, Object> map = Maps.newHashMap();
		
		for(WageDetailVo vo:vos){
			WageFormula formula = formulaService.get(vo.getFormulaId());
			
			if(formula.getColumnType()==WageFormula.TYPE_INPUT){
				if(formula.getIsView()==WageFormula.VIEW){
					map.put(formula.getColumnName(), vo.getValue().doubleValue());
				}else{
					map.put(formula.getColumnName(), formula.getDefaultValue());
				}
			}
		}
		
		for(WageDetailVo vo:vos){
			WageFormula formula = formulaService.get(vo.getFormulaId());
			
			WageDetail entity = new WageDetail();
			if(formula.getColumnType()==WageFormula.TYPE_FORMULA){
				
				BigDecimal result = new BigDecimal( recurseValue(formula, formulas, map));
				result = result.setScale(2, RoundingMode.HALF_UP);
				entity.setValue(result);
			}else{
				entity.setValue(vo.getValue());
			}
			
			entity.setWage(wage);
			entity.setFormula(formulaService.get(vo.getFormulaId()));
			entity.setMember(memberService.get(vo.getMemberId()));
			
			detailRepository.save(entity);
			details.add(entity);
		}
		
		return details;
	}

	/**递归计算公式的值
	 * @param formula
	 * @param formulaList
	 * @param map
	 * @return
	 */
	private double recurseValue(WageFormula formula, List<WageFormula> formulaList,
			Map<String, Object> map) {
		
		String strformula = formula.getFormula();
		if(StringUtils.isBlank(strformula) && formula.getColumnType()==WageFormula.TYPE_FORMULA){
			return 0;
		}
		
		Splitter splitter = Splitter.on(WageFormulaService.expressionPattern).trimResults().omitEmptyStrings();
		List<String> operator = splitter.splitToList(strformula);
		
		if(operator.size()==1 &&  NumberUtils.isNumber(operator.get(0))){
			return Double.parseDouble(operator.get(0));
		}
		
		for(String current:operator){
			
			if(!StringUtils.isNumeric(current)){
				for(WageFormula iter:formulaList){
					if(current.equals(iter.getColumnName())){
						if(iter.getColumnType()==WageFormula.TYPE_FORMULA){
							double recurseVal = recurseValue(iter, formulaList, map);
							map.put(current, recurseVal);
						}
						break;
					}
				}
			}
		}
		return calValue(formula.getFormula(), map);
	}
	
	/**
	 * 计算表达式的值
	 * @param expression
	 * @param map
	 * @return
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	private double calValue(String expression, Map<String, Object> map){
		
		// 编译表达式 
        Expression compiledExp = AviatorEvaluator.compile(expression); 
        Object obj = compiledExp.execute(map);
        
        if(obj instanceof Long){
        	return ((Long)obj).doubleValue();
        }
        if(obj instanceof Integer){
        	return ((Integer)obj).doubleValue();
        }
        if(obj instanceof BigDecimal){
        	return ((BigDecimal)obj).doubleValue();
        }
        if(obj instanceof Short){
        	return ((Short)obj).doubleValue();
        }
        if(obj instanceof Float){
        	return ((Float)obj).doubleValue();
        }
        return (Double)obj;
	}

	/**
	 * 根据wageId删除关联的工资明细
	 * @param wageId
	 */
	public void deleteByWageId(String wageId) {
		List<WageDetail> list = detailRepository.findByWageId(wageId);
		for(WageDetail entity : list){
			detailRepository.delete(entity);
		}
	}
	
	/**
	 * 根据wageId获取关联的工资明细
	 * @param wageId
	 * @param memberFid 
	 */
	public List<WageDetailVo> getByWageId(String wageId, String memberId) {
		return getVos(detailRepository.getByWageMember(wageId, memberId));
	}
	
	
	/**
	 * 根据wageId获取详情
	 * @param wageId
	 * @return
	 */
	public List<List<WageDetailVo>> queryDetail(String wageId, String memberFid){
		List<WageDetailVo> details = getByWageId(wageId, memberFid);

		Map<String, List<WageDetailVo>> cache = Maps.newHashMap();

		for (WageDetailVo detailVo : details) {
			String memberId = detailVo.getMemberId();
			List<WageDetailVo> cacheDetail = cache.get(memberId);
			if (cacheDetail == null) {
				cacheDetail = Lists.newArrayList();
			}
			cacheDetail.add(detailVo);
			cache.put(memberId, cacheDetail);
		}

		List<List<WageDetailVo>> process = Lists.newArrayList();
		process.addAll(cache.values());

		Collections.sort(process, new Comparator<List<WageDetailVo>>() {

			@Override
			public int compare(List<WageDetailVo> o1, List<WageDetailVo> o2) {
				if (o1 != null && o1.size() > 0 && o2 != null && o2.size() > 0) {
					String memberCode1 = o1.get(0).getMemberCode();
					String memberCode2 = o2.get(0).getMemberCode();
					return memberCode1.compareTo(memberCode2);
				}
				return 0;
			}
		});
		
		return process;
	}
	
	/**
	 * 获取存在的工资列
	 * @param fid
	 * @return
	 */
	public List<WageFormula> getExistFormula(String wageId) {
		List<String> strings = detailRepository.getExistFormula(wageId);
		List<WageFormula> byWageFormula = detailRepository.getByWageFormula(strings);
		return byWageFormula;
	}
	
	/**
	 * 根据账套id删除工资明细
	 * @param fiscalAccountId 账套id
	 */
	public void delByFiscalAccountId(String fiscalAccountId){
		detailRepository.delByFiscalAccountId(fiscalAccountId);
	}
	
	/**
	 * 根据工资公式id查找工资明细
	 * @param fid 工资公式id
	 * @return
	 */
	public Long countByColumn(String fid){
		return detailRepository.countByColumn(fid);
	}

	@Override
	public CrudRepository<WageDetail, String> getRepository() {
		return this.detailRepository;
	}
	
	/**
	 * 根据部门、工资项目，获取某个月的工资单ID<br>
	 * 过滤已生成凭证和未审核的单据<br>
	 * @param fiscalAccountId 财务账套ID
	 * @param month 月份(年+月)
	 * @param formulaId 工资项目ID
	 * @param deptId 部门ID
	 * @return
	 * @author rqh
	 */
	public List<String> getWageIds(String fiscalAccountId, Date month, String formulaId, String deptId){
		return this.detailRepository.getWageIds(fiscalAccountId, month, formulaId, deptId);
	}
	
	/**
	 * 根据工资项目，获取某工资单下的工资明细<br>
	 * 过滤已生成凭证和未审核的单据<br>
	 * @param fiscalAccountId 财务账套ID
	 * @param wageId 工资单ID
	 * @param formulaId 工资项目ID
	 * @return
	 * @author rqh
	 */
	public List<String> getDetailIds(String fiscalAccountId, String wageId, String formulaId){
		return this.detailRepository.getDetailIds(fiscalAccountId, wageId, formulaId);
	}
	
	/**
	 * 获取某工资单的金额总数
	 * @param wageId 工资ID
	 * @param formulaId 工资项目ID
	 * @return
	 * @author rqh
	 */
	public BigDecimal sumAmountByWageIdAndFormulaId(String wageId, String formulaId){
		
		BigDecimal result = null;
		if(StringUtils.isNotBlank(formulaId)){
			result = detailRepository.sumAmountByWageIdAndFormulaId(wageId, formulaId);
		}else{
			result = detailRepository.sumAmountByWageId(wageId);
		}
		if(result != null){
			return result;
		}
		return BigDecimal.ZERO;
	}
}
