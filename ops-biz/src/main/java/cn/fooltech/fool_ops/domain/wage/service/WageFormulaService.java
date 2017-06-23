package cn.fooltech.fool_ops.domain.wage.service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import cn.fooltech.fool_ops.domain.voucher.repository.WageVoucherRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.googlecode.aviator.AviatorEvaluator;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.wage.entity.WageFormula;
import cn.fooltech.fool_ops.domain.wage.repository.WageFormulaRepository;
import cn.fooltech.fool_ops.domain.wage.vo.WageFormulaVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>工资公式网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-12-21 10:05:05
 */
@Service
public class WageFormulaService extends BaseService<WageFormula,WageFormulaVo,String> {
	
	public static final Pattern expressionPattern = Pattern.compile("[+|\\-|*|/|(|)]");
	
	/**
	 * 工资公式持久层
	 */
	@Autowired
	private WageFormulaRepository formulaRepository;
	 
	/**
	 * 工资服务类
	 */
	@Autowired
	private WageDetailService detailService;


	@Autowired
	private WageVoucherRepository wageVoucherRepository;
	
	
	/**
	 * 查询工资公式列表信息，按照工资公式主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<WageFormulaVo> query(WageFormulaVo vo,PageParamater pageParamater){
		
		String accountId=SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Direction.ASC, "orderNo");
		PageRequest request = getPageRequest(pageParamater,sort);
		Page<WageFormula> page = formulaRepository.query(vo, accountId, request);
		return getPageVos(page, request);
	}
	
	
	/**
	 * 单个工资公式实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public WageFormulaVo getVo(WageFormula entity){
		if(entity == null)
			return null;
		WageFormulaVo vo = new WageFormulaVo();
		vo.setColumnName(entity.getColumnName());
		vo.setColumnType(entity.getColumnType());
		vo.setFormula(entity.getFormula());
		vo.setDefaultValue(entity.getDefaultValue());
		vo.setOrderNo(entity.getOrderNo());
		vo.setRemark(entity.getRemark());
		vo.setIsView(entity.getIsView());
		vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime(), DATE_TIME));
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		
		return vo;
	}
	
	/**
	 * 删除工资公式<br>
	 */
	public RequestResult delete(String fid){
		
		Long count = detailService.countByColumn(fid);
		if(count>0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "已有工资单关联公式");
		}

		Long count2 = wageVoucherRepository.countByWageColumnId(fid);
		if(count2>0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "已有凭证关联公式项");
		}
		
		WageFormula entity = formulaRepository.findOne(fid);
		
		List<WageFormula> formulas = formulaRepository.getByAccountId(SecurityUtil.getFiscalAccountId(), null);
		Splitter splitter = Splitter.on(WageFormulaService.expressionPattern).trimResults().omitEmptyStrings();
		for(WageFormula formula:formulas){
			if(!formula.getFid().equals(fid)){
				List<String> operators = splitter.splitToList(formula.getFormula());
				for(String operator:operators){
					if(operator.equals(entity.getColumnName())){
						return new RequestResult(RequestResult.RETURN_FAILURE, "有公式关联不能删除");
					}
				}
			}
		}
		
		formulaRepository.delete(fid);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 获取工资公式信息
	 * @param fid 工资公式ID
	 * @return
	 */
	public WageFormulaVo getByFid(String fid) {
		Assert.notNull(fid);
		WageFormulaVo byId = super.getById(fid);
		return byId;
	}
	

	/**
	 * 新增/编辑工资公式
	 * @param vo
	 */
	public RequestResult save(WageFormulaVo vo) {
		
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
		}
		
		if(NumberUtils.isDigits(vo.getColumnName())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "工资项名称不能纯数字");
		}
		
		String accountId = SecurityUtil.getFiscalAccountId();
		if(formulaRepository.countByColumnName(vo,accountId)>0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "工资项目名称重复");
		}
		if(formulaRepository.countByColumnNo(vo,accountId)>0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "顺序号重复");
		}
		
		if(vo.getColumnType()==WageFormula.TYPE_FORMULA){
			
			// 编译表达式 
			String expression = vo.getFormula();
			if(Strings.isNullOrEmpty(expression))return new RequestResult(RequestResult.RETURN_FAILURE, "类型为公式计算时公式必填");
			List<WageFormula> formulas = formulaRepository.queryByCriteria(vo,accountId);
			
			try {
				AviatorEvaluator.compile(expression);
			} catch (Exception e) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "计算公式异常");
			}
			
			WageFormula temp = new WageFormula();
			temp.setColumnName(vo.getColumnName());
			temp.setColumnType(vo.getColumnType());
			temp.setFormula(vo.getFormula());
			temp.setDefaultValue(vo.getDefaultValue());
			formulas.add(temp);
			
			if(!checkRef(formulas)){
				return new RequestResult(RequestResult.RETURN_FAILURE, "公式循环引用");
			}
			if(!checkRefSafe(vo.getFormula(), formulas)){
				return new RequestResult(RequestResult.RETURN_FAILURE, "公式引用非法");
			}
		}
		
		WageFormula entity = null;
		if(StringUtils.isBlank(vo.getFid())){
			 entity = new WageFormula();
			 entity.setCreateTime(new Date());
			 entity.setCreator(SecurityUtil.getCurrentUser());
			 entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			 entity.setOrg(SecurityUtil.getCurrentOrg());
			 entity.setDept(SecurityUtil.getCurrentDept());
		}else {
			entity = formulaRepository.findOne(vo.getFid());
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
		}
		
		entity.setColumnName(vo.getColumnName());
		entity.setColumnType(vo.getColumnType());
		entity.setFormula(vo.getFormula());
		entity.setDefaultValue(vo.getDefaultValue());
		entity.setOrderNo(vo.getOrderNo());
		entity.setRemark(vo.getRemark());
		entity.setIsView(vo.getIsView());
		
		formulaRepository.save(entity);
		
		RequestResult result = new RequestResult();
		vo.setFid(entity.getFid());
		result.setData(vo);
		return result;
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	private boolean checkRefSafe(String formula, List<WageFormula> formulas){
		
		Splitter splitter = Splitter.on(WageFormulaService.expressionPattern).trimResults().omitEmptyStrings();
		List<String> operators = splitter.splitToList(formula);
		Set<String> cache = Sets.newHashSet();
		
		for (WageFormula iter : formulas) {
			cache.add(iter.getColumnName());
		}
		
		for(String operator:operators){
			if(!NumberUtils.isNumber(operator)){
				if(!cache.contains(operator)){
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	private boolean checkRef(List<WageFormula> formulas){
		
		for(WageFormula formula:formulas){
			if(formula.getColumnType()==WageFormula.TYPE_FORMULA){
				boolean flag = recurseCheckRef(formula.getFormula(), formulas, formula.getColumnName());
				if(flag==false)return false;
			}
		}
		return true;
	}
	
	/**
	 * 检查是否有递归引用
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	private boolean recurseCheckRef(String expression, List<WageFormula> formulas, String self){
		
		if(StringUtils.isBlank(expression) || StringUtils.isNumeric(expression)){
			return true;
		}
		
		Splitter splitter = Splitter.on(expressionPattern).trimResults().omitEmptyStrings();
		List<String> splits = splitter.splitToList(expression);
		
		for(String split:splits){
			if(split.equals(self))return false;
		}
		
		for(String split:splits){
			for(WageFormula iter:formulas){
				if(split.equals(iter.getColumnName())){
					if(iter.getColumnType()==WageFormula.TYPE_FORMULA){
						return recurseCheckRef(iter.getFormula(), formulas, self);
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 获取账套下所有公式
	 * @return
	 */
	public List<WageFormulaVo> getByAccountId(Short isView){
		
		return getVos(formulaRepository.getByAccountId(SecurityUtil.getFiscalAccountId(), isView));
	}


	@Override
	public CrudRepository<WageFormula, String> getRepository() {
		return formulaRepository;
	}

	/**
	 * 根据账套查找
	 * @param fiscalAccountId
	 * @return
	 */
	public List<WageFormula> getByAccountId(String fiscalAccountId, Short isView) {
		
		return formulaRepository.getByAccountId(fiscalAccountId,isView);
		
	}
}
