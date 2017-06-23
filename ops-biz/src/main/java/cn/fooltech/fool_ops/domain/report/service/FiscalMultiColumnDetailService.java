package cn.fooltech.fool_ops.domain.report.service;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Bank;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.service.BankService;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.service.SupplierService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.report.entity.FiscalMultiColumn;
import cn.fooltech.fool_ops.domain.report.entity.FiscalMultiColumnDetail;
import cn.fooltech.fool_ops.domain.report.repository.MultiColumnDetailRepository;
import cn.fooltech.fool_ops.domain.report.vo.FiscalMultiColumnDetailVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>多栏明细账设置明细网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年1月26日
 */
@Service
public class FiscalMultiColumnDetailService extends BaseService<FiscalMultiColumnDetail, 
	FiscalMultiColumnDetailVo, String> {
	
	@Autowired
	private FiscalMultiColumnService multiColumnService;
	
	@Autowired
	private MultiColumnDetailRepository multiColumnDetailRepo;
	
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	@Autowired
	private BankService bankService;
	
	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private AuxiliaryAttrService auxiliaryAttrService;
	
	@Autowired
	private GoodsService goodsService;
	
	/**
	 * 新增(不能修改)
	 * @param vo
	 */
	public void save(FiscalMultiColumnDetailVo vo){
		String fiscalMultiColumnId = vo.getFiscalMultiColumnId();
		String subjectId = vo.getSubjectId();
		Integer auxiliaryType = vo.getAuxiliaryType();
		String auxiliaryAttrId = vo.getAuxiliaryAttrId();
		Integer direction = vo.getDirection(); //余额方向
		
		Assert.isTrue(StringUtils.isNotBlank(fiscalMultiColumnId), "多栏明细设置ID不能为空!");
		Assert.isTrue(StringUtils.isNotBlank(subjectId), "科目ID不能为空");
		
		FiscalMultiColumn multiColumn = multiColumnService.get(fiscalMultiColumnId);
		FiscalMultiColumnDetail detail = new FiscalMultiColumnDetail();
		detail.setFiscalMultiColumn(multiColumn);
		detail.setDirection(direction);
		detail.setCreator(SecurityUtil.getCurrentUser());
		detail.setCreateTime(Calendar.getInstance().getTime());
		if(auxiliaryType == null){
			//明细表只有科目信息
			FiscalAccountingSubject subject = subjectService.get(auxiliaryAttrId);
			detail.setSubject(subject);
		}
		else{
			//明细表有核算项目信息
			detail.setAuxiliaryType(auxiliaryType);
			detail.setAuxiliaryAttrId(auxiliaryAttrId);
			detail.setSubject(multiColumn.getSubject());
			
		}
		multiColumnDetailRepo.save(detail);
	}
	
	/**
	 * 删除
	 * @param entities
	 */
	public void delete(List<FiscalMultiColumnDetail> entities){
		multiColumnDetailRepo.delete(entities);
	}
	
	/**
	 * 单个实体转vo
	 * @param entity
	 * @return
	 */
	public FiscalMultiColumnDetailVo getVo(FiscalMultiColumnDetail entity){
		Integer auxiliaryType = entity.getAuxiliaryType(); //核算项目类别
		
		FiscalMultiColumnDetailVo vo = new FiscalMultiColumnDetailVo();
		vo.setFid(entity.getFid());
		vo.setDirection(entity.getDirection());
		vo.setAuxiliaryType(auxiliaryType);
		vo.setAuxiliaryAttrId(entity.getAuxiliaryAttrId());
		//设置
		FiscalMultiColumn column = entity.getFiscalMultiColumn();
		if(column != null){
			vo.setFiscalMultiColumnId(column.getFid());
			vo.setFiscalMultiColumnName(column.getName());
		}
		//科目
		FiscalAccountingSubject subject = entity.getSubject();
		if(subject != null){
			vo.setSubjectId(subject.getFid());
			vo.setSubjectName(subject.getName());
		}
		if(auxiliaryType == null){
			FiscalAccountingSubject accountingSubject = entity.getSubject();
			vo.setAuxiliaryAttrId(accountingSubject.getFid());
			vo.setAuxiliaryAttrCode(accountingSubject.getCode());
			vo.setAuxiliaryAttrName(accountingSubject.getName());
			return vo;
		}
		else{
			//核算项目
			switch(entity.getAuxiliaryType()){
				case 1:{
					//银行
					Bank bank = bankService.get(entity.getAuxiliaryAttrId());
					if(bank != null){
						vo.setAuxiliaryAttrCode(bank.getCode());
						vo.setAuxiliaryAttrName(bank.getName());
					}
					break;
				}
				case 2:{
					//供应商
					Supplier supplier = supplierService.get(entity.getAuxiliaryAttrId());
					if(supplier != null){
						vo.setAuxiliaryAttrCode(supplier.getCode());
						vo.setAuxiliaryAttrName(supplier.getName());
					}
					break;
				}
				case 3:{
					//客户
					Customer customer = customerService.get(entity.getAuxiliaryAttrId());
					if(customer != null){
						vo.setAuxiliaryAttrCode(customer.getCode());
						vo.setAuxiliaryAttrName(customer.getName());
					}
					break;
				}
				case 4:{
					//部门
					Organization org = orgService.get(entity.getAuxiliaryAttrId());
					if(org != null){
						vo.setAuxiliaryAttrCode(org.getOrgCode());
						vo.setAuxiliaryAttrName(org.getOrgName());
					}
					break;
				}
				case 5:{
					//人员
					Member member = memberService.get(entity.getAuxiliaryAttrId());
					if(member != null){
						vo.setAuxiliaryAttrCode(member.getUserCode());
						vo.setAuxiliaryAttrName(member.getUsername());
					}
					break;
				}
				case 6:
				case 7:{
					//仓库、项目
					AuxiliaryAttr attr = auxiliaryAttrService.get(entity.getAuxiliaryAttrId());
					if(attr != null){
						vo.setAuxiliaryAttrCode(attr.getCode());
						vo.setAuxiliaryAttrName(attr.getName());
					}
					break;
				}
				case 8:{
					//货品
					Goods goods = goodsService.get(entity.getAuxiliaryAttrId());
					if(goods != null){
						vo.setAuxiliaryAttrCode(goods.getCode());
						vo.setAuxiliaryAttrName(goods.getName());
					}
					break;
				}
				default:
					break;
			}
		}
		return vo;
	}
	
	/**
	 * 获取动态列标题
	 * @param columnId 多栏明细设置ID
	 * @param direction 科目方向
	 * @return
	 */
	public List<FiscalMultiColumnDetailVo> getDynamicTitles(String columnId, int direction){
		List<FiscalMultiColumnDetail> list = multiColumnDetailRepo.findByColumnIdAndDirection(columnId, direction);
		return getVos(list);
	}

	@Override
	public CrudRepository<FiscalMultiColumnDetail, String> getRepository() {
		return multiColumnDetailRepo;
	}
	
}
