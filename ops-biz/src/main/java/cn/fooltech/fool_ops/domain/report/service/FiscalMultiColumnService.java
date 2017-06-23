package cn.fooltech.fool_ops.domain.report.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.entity.StorageBaseEntity;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.entity.Bank;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.BankRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.CustomerRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.SupplierRepository;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalAccountingSubjectRepository;
import cn.fooltech.fool_ops.domain.member.repository.MemberRepository;
import cn.fooltech.fool_ops.domain.report.entity.FiscalMultiColumn;
import cn.fooltech.fool_ops.domain.report.repository.MultiColumnRepository;
import cn.fooltech.fool_ops.domain.report.vo.AuxiliaryAttrSimpleVo;
import cn.fooltech.fool_ops.domain.report.vo.FiscalAccountingSubjectSimpleVo;
import cn.fooltech.fool_ops.domain.report.vo.FiscalMultiColumnDetailVo;
import cn.fooltech.fool_ops.domain.report.vo.FiscalMultiColumnVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.repository.OrganizationRepository;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



/**
 * <p>多栏明细账设置网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年1月26日
 */
@Service
public class FiscalMultiColumnService extends BaseService<FiscalMultiColumn, FiscalMultiColumnVo, String> {
	
	@Autowired
	private MultiColumnRepository multiColumnRepo;
	
	@Autowired
	private FiscalMultiColumnDetailService multiColumnDetailService;
	
	@Autowired
	private FiscalAccountingSubjectRepository subjectRepo;
	
	@Autowired
	private MemberRepository memberRepo;
	
	@Autowired
	private GoodsRepository goodsRepo;
	
	@Autowired
	private BankRepository bankRepo;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private SupplierRepository supplierRepo;
	
	@Autowired
	private OrganizationRepository orgRepo;
	
	@Autowired
	private AuxiliaryAttrRepository attrRepo;
	
	
	/**
	 * 获取设置列表
	 * @return
	 */
	public List<FiscalMultiColumnVo> list(){
		String accId = SecurityUtil.getFiscalAccountId();
		List<FiscalMultiColumn> datas = multiColumnRepo.findByAccId(accId);
		return getVos(datas);
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	public RequestResult save(FiscalMultiColumnVo vo){
		String fid = vo.getFid();
		String subjectId = vo.getSubjectId();
		String name = vo.getName();
		Date now = Calendar.getInstance().getTime();
			
		if(!checkDataRealTime(vo)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "页面失效，请重新刷新!");
		}
		
		FiscalMultiColumn multiColumn = null;
		if(StringUtils.isBlank(fid)){
			multiColumn = new FiscalMultiColumn();
			multiColumn.setCreateTime(now);
			multiColumn.setOrg(SecurityUtil.getCurrentOrg());
			multiColumn.setCreator(SecurityUtil.getCurrentUser());
			multiColumn.setDept(SecurityUtil.getCurrentDept());
		}
		else {
			multiColumn = multiColumnRepo.findOne(fid);
			if(multiColumn == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该设置不存在或已被删除!");
			}
		}
		multiColumn.setName(name);
		multiColumn.setUpdateTime(now);
		//财务账套
		multiColumn.setFiscalAccount(SecurityUtil.getFiscalAccount());
		//科目
		if(StringUtils.isNotBlank(subjectId)){
			multiColumn.setSubject(subjectRepo.findOne(subjectId));
		}
		
		multiColumnRepo.save(multiColumn);
		
		//删除对应明细
		multiColumnDetailService.delete(multiColumn.getDetails());
		
		//新增明细
		List<FiscalMultiColumnDetailVo> detailVos = getDetailsFromVo(vo);
		for(FiscalMultiColumnDetailVo detailVo : detailVos){
			detailVo.setAuxiliaryType(vo.getAuxiliaryType());
			detailVo.setFiscalMultiColumnId(multiColumn.getFid());
			multiColumnDetailService.save(detailVo);
		}
		return new RequestResult();
	}
	
	
	/**
	 * 删除
	 * @param fid 
	 * @return
	 */
	public RequestResult delete(String fid){
		FiscalMultiColumn multiColumn = multiColumnRepo.findOne(fid);
		if(multiColumn == null){
			return buildFailRequestResult("该设置不存在或已被删除!");
		}
		multiColumnDetailService.delete(multiColumn.getDetails());
		multiColumnRepo.delete(multiColumn);
		return new RequestResult();
	}
	
	/**
	 * 获取明细的vo信息
	 * @param vo
	 * @return
	 */
	public List<FiscalMultiColumnDetailVo> getDetailsFromVo(FiscalMultiColumnVo vo){
		List<FiscalMultiColumnDetailVo> details = new ArrayList<FiscalMultiColumnDetailVo>();
		if(StringUtils.isNotBlank(vo.getDetails())){
			JSONArray jsonArray = JSONArray.fromObject(vo.getDetails());
			for(int i=0; i<jsonArray.size(); i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				FiscalMultiColumnDetailVo detail = (FiscalMultiColumnDetailVo) 
						JSONObject.toBean(jsonObject, FiscalMultiColumnDetailVo.class);
				details.add(detail);
			}
		}
		return details;
	}
	
	/**
	 * 更新操作时，校验数据的实时性
	 * @param vo 主键、更新时间
	 * @return true 有效  false 无效 
	 */
	public boolean checkDataRealTime(FiscalMultiColumnVo vo){
		if(StringUtils.isNotBlank(vo.getFid())){
			FiscalMultiColumn entity = multiColumnRepo.findOne(vo.getFid());
			Date formDate = DateUtils.getDateFromString(vo.getUpdateTime());
			int num = formDate.compareTo(entity.getUpdateTime());
			return num == 0;
		}
		return true;
	}
	
	/**
	 * 多个单据转实体
	 * @param entity
	 * @return
	 */
	@Override
	public FiscalMultiColumnVo getVo(FiscalMultiColumn entity){
		FiscalMultiColumnVo vo = new FiscalMultiColumnVo();
		vo.setFid(entity.getFid());
		vo.setName(entity.getName());
		vo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
		//科目
		FiscalAccountingSubject subject = entity.getSubject();
		if(subject != null){
			vo.setSubjectId(subject.getFid());
			vo.setSubjectName(subject.getName());	
		}
		//部门
		Organization dept = entity.getDept();
		if(dept != null){
			vo.setDeptId(dept.getFid());
			vo.setDeptName(dept.getOrgName());
		}
		//明细
		List<FiscalMultiColumnDetailVo> details = multiColumnDetailService.getVos(entity.getDetails());
		vo.setDetails(JSONArray.fromObject(details).toString());
		//核算项目类别
		if(CollectionUtils.isNotEmpty(details)){
			FiscalMultiColumnDetailVo detail = details.get(0);
			vo.setAuxiliaryType(detail.getAuxiliaryType());
		}
		return vo;
	}
	
	/**
	 * 获取自动编排的数据
	 * @param subjectId 科目ID
	 * @param auxiliaryType 辅助属性类别: 1银行、2供应商、3销售商、4部门、5职员、6仓库、7项目，8货品
	 * @param level 层级数
	 * @param flag 是否自动编排  1 是 0 否
	 */
	public List<AuxiliaryAttrSimpleVo> autoArrange(String subjectId, Integer auxiliaryType, Integer level, int flag){
		List<AuxiliaryAttrSimpleVo> emptyList = new ArrayList<AuxiliaryAttrSimpleVo>();
		FiscalAccountingSubject subject = subjectRepo.findOne(subjectId);
		if(auxiliaryType == null){
			if(subject == null){
				return Collections.emptyList();
			}
			
			if(flag == 1){
				return getMsgOfSujbect(subject, level);
			}
			else{
				return getMsgOfSujbect(subject, level);
			}
		}
		else{
			switch(auxiliaryType){
				case 1:{
					//银行
					return level == 1 ? getMsgOfBank(subject) : emptyList;
				}
				case 2:{
					//供应商
					return level == 1 ? getMsgOfSupplier(subject) : emptyList;
				}
				case 3:{
					//客户
					return level == 1 ? getMsgOfCustomer(subject) : emptyList;
				}
				case 4:{
					//部门
					return getMsgOfDept(subject, level.shortValue());
				}
				case 5:{
					//人员
					return level == 1 ? getMsgOfMember(subject) : emptyList;
				}
				case 6:{
					//仓库
					return getMsgOfAuxiliaryAttr(subject, AuxiliaryAttrType.CODE_WAREHOUSE, level);
				}
				case 7:{
					//项目
					return getMsgOfAuxiliaryAttr(subject, AuxiliaryAttrType.CODE_PROJECT, level);
				}
				case 8:{
					//货品
					return getMsgOfGoods(subject, level);
				}
				default:{
					return emptyList;
				}
			}
		}
	}
	
	
	/**
	 * 获取科目信息(非自动编排)
	 * @param subject 科目
	 * @param level 科目的层级数
	 * @return
	 */
	private List<AuxiliaryAttrSimpleVo> getMsgOfSujbect(FiscalAccountingSubject subject, Integer level){
		
		String accId = SecurityUtil.getFiscalAccountId();
		List<AuxiliaryAttrSimpleVo> resultList = new ArrayList<AuxiliaryAttrSimpleVo>();
		List<FiscalAccountingSubject> entities = subjectRepo.findByCodeAndLevel(accId, subject.getCode(), level);
		for(FiscalAccountingSubject entity : entities){
			resultList.add(new AuxiliaryAttrSimpleVo(entity.getFid(), entity.getFid(), entity.getCode(), entity.getName(), entity.getDirection()));
		}
		return resultList;
	}
	
	/**
	 * 获取银行信息
	 * @param subject 科目
	 * @return
	 */
	private List<AuxiliaryAttrSimpleVo> getMsgOfBank(FiscalAccountingSubject subject){
		List<AuxiliaryAttrSimpleVo> resultList = new ArrayList<AuxiliaryAttrSimpleVo>();
		String orgId = SecurityUtil.getCurrentOrgId();
		List<Bank> entites = bankRepo.findByOrgId(orgId);
		for(Bank entity : entites){
			try{
				String code = entity.getCode();
				String name = entity.getName();
				resultList.add(new AuxiliaryAttrSimpleVo(subject.getFid(), entity.getFid(), code, name, subject.getDirection()));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return resultList;
	}
	
	/**
	 * 获取客户信息
	 * @param subject 科目
	 * @return
	 */
	private List<AuxiliaryAttrSimpleVo> getMsgOfCustomer(FiscalAccountingSubject subject){
		List<AuxiliaryAttrSimpleVo> resultList = new ArrayList<AuxiliaryAttrSimpleVo>();
		String orgId = SecurityUtil.getCurrentOrgId();
		List<Customer> entites = customerRepo.findByOrgId(orgId);
		for(Customer entity : entites){
			try{
				String code = entity.getCode();
				String name = entity.getName();
				resultList.add(new AuxiliaryAttrSimpleVo(subject.getFid(), entity.getFid(), code, name, subject.getDirection()));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return resultList;
	}
	
	/**
	 * 获取供应商信息
	 * @param subject 科目
	 * @return
	 */
	private List<AuxiliaryAttrSimpleVo> getMsgOfSupplier(FiscalAccountingSubject subject){
		List<AuxiliaryAttrSimpleVo> resultList = new ArrayList<AuxiliaryAttrSimpleVo>();
		String orgId = SecurityUtil.getCurrentOrgId();
		List<Supplier> entites = supplierRepo.findByOrgId(orgId);
		for(Supplier entity : entites){
			try{
				String code = entity.getCode();
				String name = entity.getName();
				resultList.add(new AuxiliaryAttrSimpleVo(subject.getFid(), entity.getFid(), code, name, subject.getDirection()));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return resultList;
	}
	
	/**
	 * 获取人员信息
	 * @param subject 科目
	 * @return
	 */
	private List<AuxiliaryAttrSimpleVo> getMsgOfMember(FiscalAccountingSubject subject){
		List<AuxiliaryAttrSimpleVo> resultList = new ArrayList<AuxiliaryAttrSimpleVo>();
		String orgId = SecurityUtil.getCurrentOrgId();
		short status = StorageBaseEntity.STATUS_ENABLE;
		List<Member> members = memberRepo.findByEnableAndOrgId(orgId, status);
		for(Member member : members){
			resultList.add(new AuxiliaryAttrSimpleVo(subject.getFid(), member.getFid(), member.getUserCode(), member.getUsername(), subject.getDirection()));
		}
		return resultList;
	}
	
	/**
	 * 获取辅助属性信息
	 * @param subject 科目
	 * @param categoryCode 辅助属性类型的编号
	 * @param level 辅助属性的层级数
	 * @return
	 */
	private List<AuxiliaryAttrSimpleVo> getMsgOfAuxiliaryAttr(FiscalAccountingSubject subject, String categoryCode, Integer level){
		List<AuxiliaryAttrSimpleVo> resultList = new ArrayList<AuxiliaryAttrSimpleVo>();
		String orgId = SecurityUtil.getCurrentOrgId();
		List<AuxiliaryAttr> attrList = attrRepo.findBy(orgId, categoryCode, level);
		for(AuxiliaryAttr attr : attrList){
			resultList.add(new AuxiliaryAttrSimpleVo(subject.getFid(), attr.getFid(), attr.getCode(), attr.getName(), subject.getDirection()));
		}
		return resultList;
	}
	
	
	/**
	 * 获取货品信息
	 * @param subject 科目
	 * @param level 货品的层级数
	 * @return
	 */
	private List<AuxiliaryAttrSimpleVo> getMsgOfGoods(FiscalAccountingSubject subject, Integer level){
		List<AuxiliaryAttrSimpleVo> resultList = new ArrayList<AuxiliaryAttrSimpleVo>();
		String orgId = SecurityUtil.getCurrentOrgId();
		List<Goods> goodsList = goodsRepo.findByLevel(orgId, level);
		for(Goods goods : goodsList){
			resultList.add(new AuxiliaryAttrSimpleVo(subject.getFid(), goods.getFid(), goods.getCode(), goods.getName(), subject.getDirection()));
		}
		return resultList;
	}
	
	/**
	 * 获取部门信息
	 * @param subject 科目
	 * @param level 部门的层级数
	 * @return
	 */
	private List<AuxiliaryAttrSimpleVo> getMsgOfDept(FiscalAccountingSubject subject, Short level){
		List<AuxiliaryAttrSimpleVo> resultList = new ArrayList<AuxiliaryAttrSimpleVo>();
		
		String orgId = SecurityUtil.getCurrentOrgId();
		List<Organization> orgList = orgRepo.findByLevel(orgId, level);
		for(Organization org : orgList){
			resultList.add(new AuxiliaryAttrSimpleVo(subject.getFid(), org.getFid(), org.getOrgCode(), org.getOrgName(), subject.getDirection()));
		}
		return resultList;
	}
	
	/**
	 * 获取科目信息(核算科目类别)
	 * @param subjectId 科目ID
	 */
	public FiscalAccountingSubjectSimpleVo getSubjectMsg(String subjectId){
		FiscalAccountingSubject subject = subjectRepo.findOne(subjectId);
		
		FiscalAccountingSubjectSimpleVo vo = new FiscalAccountingSubjectSimpleVo();
		vo.setFid(subject.getFid());
		vo.setFlag(subject.getFlag());
		//核算内容
		Map<String, String> map = new HashMap<String, String>();
		if(subject.getSupplierSign() == FiscalAccountingSubject.ACCOUNT){
			map.put("2", "供应商");
		}
		if(subject.getCustomerSign() == FiscalAccountingSubject.ACCOUNT){
			map.put("3", "客户");
		}
		if(subject.getDepartmentSign() == FiscalAccountingSubject.ACCOUNT){
			map.put("4", "部门");
		}
		if(subject.getMemberSign() == FiscalAccountingSubject.ACCOUNT){
			map.put("5", "人员");
		}
		if(subject.getWarehouseSign() == FiscalAccountingSubject.ACCOUNT){
			map.put("6", "仓库");
		}
		if(subject.getProjectSign() == FiscalAccountingSubject.ACCOUNT){
			map.put("7", "项目");
		}
		if(subject.getGoodsSign() == FiscalAccountingSubject.ACCOUNT){
			map.put("8", "货品");
		}
		
		if(map.size() > 0){
			vo.setAccountSignMsg(map);
		}
		return vo;
	}

	@Override
	public CrudRepository<FiscalMultiColumn, String> getRepository() {
		return multiColumnRepo;
	}
	
}
