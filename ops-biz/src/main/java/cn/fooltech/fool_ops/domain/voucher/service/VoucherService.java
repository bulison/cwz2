package cn.fooltech.fool_ops.domain.voucher.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalConfig;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalConfigService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalAccountingSubjectVo;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.UserRepository;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherDetail;
import cn.fooltech.fool_ops.domain.voucher.repository.VoucherRepository;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherDetailVo;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import net.sf.json.JSONArray;


/**
 * <p>凭证网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年11月23日
 */
@Service
public class VoucherService extends BaseService<Voucher, VoucherVo, String>{
	
	/**
	 * 凭证服务类
	 */
	@Autowired
	private VoucherRepository voucherRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	/**
	 * 辅助属性服务类
	 */
	@Autowired
	private AuxiliaryAttrService attrService;
	
	/**
	 * 财务会计期间服务类
	 */
	@Autowired
	private FiscalPeriodService fiscalPeriodService;
	
	/**
	 * 财务-科目网页服务类 
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 凭证明细服务类
	 */
	@Autowired
	private VoucherDetailService detailService;
	
	/**
	 * 财务参数设置服务类
	 */
	@Autowired
	private FiscalConfigService configService;
	
	/**
	 * 凭证、单据关联服务类
	 */
	@Autowired
	private VoucherBillService voucherBillService;
	
	/**
	 * 分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<VoucherVo> query(VoucherVo vo, PageParamater paramater){
		
		Sort sort = new Sort(Direction.DESC, "voucherDate", "voucherWord.name", "voucherNumber");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		String accId = SecurityUtil.getFiscalAccountId();
		if(!Strings.isNullOrEmpty(vo.getSubjectId())){
			FiscalAccountingSubject accountingSubject = subjectService.findOne(vo.getSubjectId());
			vo.setSubjectLevel(accountingSubject.getLevel());
			vo.setSubjectAttrId(accountingSubject.getSubject().getFid());
			vo.setSubjectFlag(accountingSubject.getFlag());
			vo.setSubjectCode(accountingSubject.getCode());
		}
		Page<Voucher> page = voucherRepo.findPageByVo(accId, vo, pageRequest);
				
		return getPageVos(page, pageRequest);
	}
	
	/**
	 * 添加凭证时，获取默认信息
	 * @param vo
	 */
	public Map<String, Object> getDefaultMessage(VoucherVo vo){
		Map<String, Object> datas = new HashMap<String, Object>();
		
		//财务账套ID
		String fiscalAccountId = SecurityUtil.getFiscalAccountId();
		//凭证日期
		Date voucherDate = DateUtilTools.string2Date(vo.getVoucherDate(), "yyyy-MM-dd"); 
		if(voucherDate == null){
			FiscalPeriod period = fiscalPeriodService.getFirstNotCheck();
			Date today = Calendar.getInstance().getTime();
			if(period != null){
				voucherDate = period.getEndDate();
				if(voucherDate.compareTo(today) > 0){
					voucherDate = today;
				}
			}
			else{
				voucherDate = today;
			}
		}
		
		//财务会计期间
		FiscalPeriod fiscalPeriod = fiscalPeriodService.getPeriod(voucherDate, fiscalAccountId, FiscalPeriod.USED);
		if(fiscalPeriod == null){
			return datas;
		}
		
		String voucherWordId = vo.getVoucherWordId(); //凭证字ID
		if(StringUtils.isBlank(voucherWordId)){
			Voucher newestVoucher = voucherRepo.getNewestVoucher(fiscalPeriod.getFid());
			if(newestVoucher != null){
				AuxiliaryAttr voucherWord = newestVoucher.getVoucherWord();
				//默认凭证字
				datas.put("voucherWordId", voucherWord.getFid());
				datas.put("voucherWordName", voucherWord.getName());
				//默认凭证号
				Integer voucherNumber = getMaxVoucherNumber(fiscalAccountId, fiscalPeriod.getFid(), voucherWord.getFid());
				if(voucherNumber != null){
					datas.put("voucherNumber", voucherNumber + 1);
				}
			}
		}
		else{
			//默认凭证字
			AuxiliaryAttr voucherWord = attrService.get(voucherWordId);
			datas.put("voucherWordId", voucherWord.getFid());
			datas.put("voucherWordName", voucherWord.getName());
			//默认凭证号
			Integer voucherNumber = getMaxVoucherNumber(fiscalAccountId, fiscalPeriod.getFid(), voucherWord.getFid());
			if(voucherNumber != null){
				datas.put("voucherNumber", voucherNumber + 1);
			}
		}
		
		//凭证日期
		datas.put("voucherDate", voucherDate);
		//顺序号
		/*if(vo.getNumber() == null || 0 == vo.getNumber()){
			int num = voucherService.getMaxNumber(fiscalPeriod.getFid()) + 1;
			datas.put("number", num + 1);
		}*/
		
		//摘要是否默认取上一条
		FiscalConfig config1 = configService.getConfig(fiscalAccountId, FiscalConfig.F01);
		datas.put("getLastResume", config1.getValue());
		
		//不允许手工修改凭证号
		FiscalConfig config2 = configService.getConfig(fiscalAccountId, FiscalConfig.F05);
		datas.put("editVoucherNumber", config2 == null ? FiscalConfig.TRUE : config2.getValue());
		
		return datas;
	}
	
	/**
	 * 获取最大的凭证号
	 * @param fiscalAccountId 财务账套ID
	 * @param fiscalPeriodId 财务会计期间ID
	 * @param voucherWordId 凭证字ID
	 * @return
	 */
	public Integer getMaxVoucherNumber(String fiscalAccountId, String fiscalPeriodId, String voucherWordId){
	//	FiscalConfig config = configService.getConfig(fiscalAccountId, FiscalConfig.F05); //是否可以手动修改凭证号
		FiscalConfig config = configService.getConfig(fiscalAccountId, FiscalConfig.F03);//凭证号是否以凭证字划分
		Integer max = 0;
//		Integer max = null;
			if(config.getValue().equals(FiscalConfig.TRUE)){
				max = voucherRepo.getMaxVoucherNumberAuto(fiscalPeriodId, voucherWordId);
			}else{
				max = voucherRepo.getMaxVoucherNumberAuto(fiscalPeriodId,null);
		}
		return max;
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(VoucherVo vo){
		//验证凭证
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return new RequestResult(RequestResult.RETURN_FAILURE,inValid);
		}
		String fid = vo.getFid();
		Long number = vo.getNumber(); //顺序号
		Date voucherDate = DateUtilTools.string2Date(vo.getVoucherDate(), "yyyy-MM-dd"); //凭证日期
		Long voucherNumber = vo.getVoucherNumber(); //凭证号
		Long accessoryNumber = vo.getAccessoryNumber(); //附件数
		String postPeopleId = vo.getPostPeopleId(); //记账人
		String supervisorId = vo.getSupervisorId(); //凭证主管
		String voucherWordId = vo.getVoucherWordId(); //凭证字
		Date now = Calendar.getInstance().getTime();
		BigDecimal totalAmount = vo.getTotalAmount();
		
		//校验数据
		RequestResult result = checkBySave(vo);
		if(result.getReturnCode() == RequestResult.RETURN_FAILURE){
			return result;
		}
		
		Voucher entity = null;
		if(StringUtils.isBlank(fid)){
			entity = new Voucher();
			entity.setCreateTime(now);
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setDept(SecurityUtil.getCurrentDept());
			entity.setCreator(SecurityUtil.getCurrentUser());
			//财务账套
			FiscalAccount fiscalAccount = SecurityUtil.getFiscalAccount();
			entity.setFiscalAccount(fiscalAccount);
		}
		else{
			entity = voucherRepo.findOne(fid);
		}
		if(number!=null)entity.setNumber(number.intValue());
		entity.setUpdateTime(now);
		entity.setVoucherDate(voucherDate);
		entity.setTotalAmount(totalAmount);
		if(voucherNumber!=null)entity.setVoucherNumber(voucherNumber.intValue());
		if(accessoryNumber!=null)entity.setAccessoryNumber(accessoryNumber.intValue());
		
		//财务会计期间
		FiscalPeriod fiscalPeriod = fiscalPeriodService.getPeriod(voucherDate, entity.getFiscalAccount().getFid(), FiscalPeriod.USED);
		entity.setFiscalPeriod(fiscalPeriod);
			
		//记账人
		if(StringUtils.isNotBlank(postPeopleId)){
			entity.setPostPeople(userRepo.findOne(postPeopleId));
		}
		//凭证主管
		if(StringUtils.isNotBlank(supervisorId)){
			entity.setSupervisor(userRepo.findOne(supervisorId));
		}
		//凭证字、凭证字号
		if(StringUtils.isNotBlank(voucherWordId)){
			AuxiliaryAttr voucherWord = attrService.get(voucherWordId);
			entity.setVoucherWord(voucherWord);
			entity.setVoucherWordNumber(voucherWord.getName() + "-" + voucherNumber);
		}
		//顺序号
		if(entity.getNumber() == null || 0 == entity.getNumber()){
			Integer num = getMaxNumber(fiscalPeriod.getFid())+1;
			entity.setNumber(num);
		}
		
		voucherRepo.save(entity);
		
		return saveDetails(entity, vo.getDetailList());
	}
	
	/**
	 * 新增凭证明细
	 * @param voucher 凭证
	 * @param details 凭证vo
	 */
	@Transactional
	private RequestResult saveDetails(Voucher voucher, List<VoucherDetailVo> details){
		detailService.deleteByVoucher(voucher.getFid()); //删除相关凭证明细
		if(CollectionUtils.isNotEmpty(details)){
			FiscalAccount fiscalAccount = voucher.getFiscalAccount();
			for(VoucherDetailVo detail : details){
				detail.setFid(null);
				detail.setVoucherId(voucher.getFid());
				detail.setFiscalAccountId(fiscalAccount.getFid());
				//验证凭证明细
				String inValid = ValidatorUtils.inValidMsg(detail);
				if(inValid!=null){
					return new RequestResult(RequestResult.RETURN_FAILURE,inValid);
				}
				detailService.save(detail);
			}
		}
		return new RequestResult();
	}
	
	/**
	 * 审核
	 * @param id
	 * @return
	 */
	public RequestResult audit(String id){
		Voucher voucher = voucherRepo.findOne(id);
		User creator = voucher.getCreator();
		User currentUser = SecurityUtil.getCurrentUser();
		FiscalAccount fiscalAccount = voucher.getFiscalAccount(); //财务账套
		FiscalPeriod fiscalPeriod = voucher.getFiscalPeriod(); //当前财务会计期间
		FiscalPeriod lastFiscalPeriod = fiscalPeriodService.getPrePeriod(fiscalPeriod.getFid()); //上一个财务会计期间 
		
		if(currentUser.getFid().equals(creator.getFid())){
			String configValue = configService.getConfigValue(fiscalAccount.getFid(), FiscalConfig.F06, FiscalConfig.FALSE);
			if(configValue.equals(FiscalConfig.FALSE)){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该凭证的创建人不允许审核当前凭证!");
			}
		}
		
		if(voucher.getRecordStatus() != Voucher.STATUS_UNAUDITED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "凭证不是处于未审核状态!");
		}
		if(!detailService.checkAmount(voucher.getFid())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "借方总金额与贷方总金额不相等!");
		}
		
		FiscalConfig config2 = configService.getConfig(fiscalAccount.getFid(), FiscalConfig.F02);
		if(lastFiscalPeriod != null && config2.getValue().equals(FiscalConfig.FALSE) && lastFiscalPeriod.getCheckoutStatus() != FiscalPeriod.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "上一个财务会计期间未结账!");
		}
		
		voucher.setAuditor(currentUser);
		voucher.setRecordStatus(Voucher.STATUS_AUDITED);
		voucher.setAuditDate(Calendar.getInstance().getTime());
		voucherRepo.save(voucher);
		return new RequestResult();
	}
	
	/**
	 * 批量审核
	 * @param vo
	 * @return
	 */
	public RequestResult batchAudit(VoucherVo vo){
		String voucherWordId = vo.getVoucherWordId(); //凭证字
		String fiscalPeriodId = vo.getFiscalPeriodId(); //财务会计期间
		Integer endNumber = vo.getEndVoucherNumber(); //结束凭证号
		Integer startNumber = vo.getStartVoucherNumber(); //开始凭证号
	
		if(StringUtils.isBlank(fiscalPeriodId)){
			return buildFailRequestResult("财务会计期间不能为空!");
		}
		List<Voucher> vouchers = voucherRepo.findBy(fiscalPeriodId, voucherWordId, startNumber, endNumber);
		
		int totalSuccess = 0, totalFail = 0; 
		for(Voucher voucher : vouchers){
			RequestResult result = audit(voucher.getFid());
			if(result.getReturnCode() == RequestResult.RETURN_FAILURE){
				totalFail++;
			}
			else{
				totalSuccess++;
			}
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("fail", totalFail);
		map.put("success", totalSuccess);
		
		RequestResult result = new RequestResult();
		result.setData(map);
		return result;
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@Transactional
	public RequestResult delete(String id){
		Voucher voucher = voucherRepo.findOne(id);
		if(isUsed(voucher)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，该凭证已被使用!");
		}
		if(voucher.getRecordStatus() != Voucher.STATUS_UNAUDITED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，该凭证不允许删除!");
		}
		detailService.deleteByVoucher(voucher.getFid());
		voucherBillService.deleteByVoucherId(voucher.getFid());
		voucherRepo.delete(voucher);
		return new RequestResult();
	}
	
	/**
	 * 作废
	 * @param id
	 * @return
	 */
	public RequestResult cancel(String id){
		Voucher voucher = voucherRepo.findOne(id);
		FiscalPeriod fiscalPeriod = voucher.getFiscalPeriod();
		if(voucher.getRecordStatus() != Voucher.STATUS_UNAUDITED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，凭证不是处于未审核状态!");
		}
		if(fiscalPeriod.getCheckoutStatus() == FiscalPeriod.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，财务会计期间已结账!");
		}
		voucher.setCancelor(SecurityUtil.getCurrentUser());
		voucher.setRecordStatus(Voucher.STATUS_CANCELED);
		voucher.setCancelDate(Calendar.getInstance().getTime());
		voucherRepo.save(voucher);
		return new RequestResult();
	}
	
	/**
	 * 反审核
	 * @param id
	 * @return
	 */
	public RequestResult reverseAudit(String id){
		Voucher voucher = voucherRepo.findOne(id);
		FiscalPeriod fiscalPeriod = voucher.getFiscalPeriod();
		if(voucher.getRecordStatus() != Voucher.STATUS_AUDITED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，凭证不是处于已审核状态!");
		}
		if(fiscalPeriod.getCheckoutStatus() == FiscalPeriod.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，财务会计期间已结账!");
		}
		voucher.setSupervisor(null);
		voucher.setAuditor(null);
		voucher.setAuditDate(null);
		voucher.setRecordStatus(Voucher.STATUS_UNAUDITED);
		voucherRepo.save(voucher);
		return new RequestResult();
	}
	
	/**
	 * 反作废
	 * @param id
	 * @return
	 */
	public RequestResult reverseCancel(String id){
		Voucher voucher = voucherRepo.findOne(id);
		FiscalPeriod fiscalPeriod = voucher.getFiscalPeriod();
		if(voucher.getRecordStatus() != Voucher.STATUS_CANCELED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，凭证不是处于已作废状态!");
		}
		if(fiscalPeriod.getCheckoutStatus() == FiscalPeriod.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，财务会计期间已结账!");
		}
		voucher.setCancelor(null);
		voucher.setCancelDate(null);
		voucher.setRecordStatus(Voucher.STATUS_UNAUDITED);
		voucherRepo.save(voucher);
		return new RequestResult();
	}
	
	/**
	 * 签字
	 * @param id
	 * @return
	 */
	public RequestResult signature(String id){
		Voucher voucher = voucherRepo.findOne(id);
		if(voucher.getRecordStatus() != Voucher.STATUS_AUDITED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，凭证不是处于已审核状态!");
		}
		voucher.setSupervisor(SecurityUtil.getCurrentUser());
		voucherRepo.save(voucher);
		return new RequestResult();
	}
	
	/**
	 * 过账
	 * @return
	 */
	public RequestResult postAccount(VoucherVo vo){
		//财务账套
		FiscalAccount fiscalAccount = SecurityUtil.getFiscalAccount();
		//第一个未结账的财务会计期间
		FiscalPeriod period = fiscalPeriodService.getFirstNotCheck();
		if(period == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "找不到需要过账的财务会计期间!");
		}
		
		int totalSuccess = 0; //成功数量
		boolean isContinuous = true; //是否连续
		Integer lastVoucherNumber = null; //上一个凭证号
		String lastVoucherWordId = null; //上一个凭证字
		Integer flag = vo.getPostAccountFlag(); //0 终止、1警告、2允许
		FiscalConfig config = configService.getConfig(fiscalAccount.getFid(), FiscalConfig.F03);
		if(config.getValue().equals(FiscalConfig.TRUE)){
			//凭证号区分凭证字
			List<Voucher> list = voucherRepo.getAuditedVouchersByOrders(fiscalAccount.getFid(), period.getStartDate(), period.getEndDate());
			for(int i=0; i<list.size(); i++){
				Voucher curVoucher = list.get(i);
				int curVoucherNumber = curVoucher.getVoucherNumber();
				String curVoucherWordId = curVoucher.getVoucherWord().getFid();
				
				if(lastVoucherNumber != null && lastVoucherWordId != null){
					if(curVoucherWordId.equals(lastVoucherWordId) && curVoucherNumber != (lastVoucherNumber + 1)){
						//不连续
						if(0 == flag){
							isContinuous = false;
							break;
						}
						else if(1 == flag){
							isContinuous = false;
						}
					}
				}
				
				totalSuccess++;
				executePost(curVoucher);
				
				if(i + 1 < list.size()){
					Voucher nextVoucher = list.get(i+1);
					if(!curVoucherWordId.equals(nextVoucher.getVoucherWord().getFid())){
						lastVoucherNumber = null;
						lastVoucherWordId = null;
					}
					else{
						lastVoucherNumber = curVoucherNumber;
						lastVoucherWordId = curVoucherWordId;
					}
				}
			}
		}
		else{
			//凭证号不区分凭证字
			List<Voucher> list = voucherRepo.getAuditedVouchersByOrders(fiscalAccount.getFid(), period.getStartDate(), period.getEndDate());
										     
			for(Voucher voucher : list){
				int curVoucherNumber = voucher.getVoucherNumber();
				if(lastVoucherNumber != null && curVoucherNumber != (lastVoucherNumber + 1)){
					//不连续
					if(0 == flag){
						isContinuous = false;
						break;
					}
					else if(1 == flag){
						isContinuous = false;
					}
				}
				totalSuccess++;
				executePost(voucher);
				lastVoucherNumber = curVoucherNumber;
			}
		}
		
		RequestResult result = new RequestResult();
		Map<String, Object> map = new HashMap<String, Object>();
		if(0 == flag && !isContinuous){
			result.setReturnCode(RequestResult.RETURN_FAILURE);
			result.setMessage("过账被迫终止，存在不连续的凭证号!");
		}
		if(1 == flag && !isContinuous){
			result.setReturnCode(RequestResult.RETURN_SUCCESS);
			result.setMessage("警告，存在不连续的凭证号!");
		}
		map.put("totalSuccess", totalSuccess);
		result.setData(map);
		return result;
	}
	
	/**
	 * 执行过账操作
	 */
	private void executePost(Voucher voucher){
		voucher.setPostPeople(SecurityUtil.getCurrentUser());
		voucher.setRecordStatus(Voucher.STATUS_POSTED);
		voucherRepo.save(voucher);
	}
	/**
	 * 反过账
	 * @return
	 */
	public RequestResult reversePostAccount(VoucherVo vo){
		int success=0,fail=0;
		if(StringUtils.isNotBlank(vo.getIds())){
			String[] ids = vo.getIds().split(",");
			for(String id : ids){
				Voucher voucher = voucherRepo.findOne(id);
				FiscalPeriod fiscalPeriod = voucher.getFiscalPeriod();
				//会计期间已过账，则不能反过账
				if(fiscalPeriod.getCheckoutStatus() != FiscalPeriod.CHECKED){
					if(voucher.getRecordStatus() == Voucher.STATUS_POSTED){
						voucher.setPostPeople(null);
						voucher.setRecordStatus(Voucher.STATUS_AUDITED);
						voucherRepo.save(voucher);
						success++;
					}
				}else{
					fail++;
				}
			}
		}
		return buildSuccessRequestResult("反过账完成！成功数："+success+",失败数："+fail+"。");
	}
	
	/**
	 * 更新操作时，校验数据的实时性
	 * @param vo 主键、更新时间
	 * @return true 有效  false 无效 
	 */
	private boolean checkDataRealTime(VoucherVo vo){
		if(StringUtils.isNotBlank(vo.getFid())){
			Voucher entity = voucherRepo.findOne(vo.getFid());
			Date formDate = DateUtils.getDateFromString(vo.getUpdateTime());
			int num = formDate.compareTo(entity.getUpdateTime());
			return num == 0;
		}
		return true;
	}
	
	/**
	 * 新增、保存时校验数据
	 * @param vo
	 * @return
	 */
	private RequestResult checkBySave(VoucherVo vo){
		if(StringUtils.isNotBlank(vo.getFid())){
			Voucher entity = voucherRepo.findOne(vo.getFid());
			if(entity.getRecordStatus() != Voucher.STATUS_UNAUDITED){
				return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，凭证不是处于未审核状态!");	
			}
		}
		//凭证日期
		Date voucherDate = DateUtilTools.string2Date(vo.getVoucherDate(), "yyyy-MM-dd");
		//财务会计期间
		FiscalPeriod fiscalPeriod = null;
		if(StringUtils.isBlank(vo.getFid())){
			fiscalPeriod = fiscalPeriodService.getPeriod(voucherDate, SecurityUtil.getFiscalAccountId());	
		}
		else{
			fiscalPeriod = fiscalPeriodService.getPeriod(voucherDate, vo.getFiscalAccountId());
		}
		if(!checkDataRealTime(vo)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "页面数据失效，请重新刷新页面!");
		}
		if(fiscalPeriod == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，财务会计期间不存在!");
		}
		if(fiscalPeriod.getCheckoutStatus() == FiscalPeriod.UN_USED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，财务会计期间未启用!");
		}
		if(fiscalPeriod.getCheckoutStatus() == FiscalPeriod.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，财务会计期间已结账!");
		}
		//凭证号
		int voucherNumber = (int) (vo.getVoucherNumber()==null?0:vo.getVoucherNumber());
		FiscalConfig config = configService.getConfig(SecurityUtil.getFiscalAccountId(), FiscalConfig.F03);//凭证号是否以凭证字划分
		if(config.getValue().equals(FiscalConfig.TRUE)){
			if(voucherRepo.isWordAndNumberExist(fiscalPeriod.getFid(), vo.getVoucherWordId(), voucherNumber, vo.getFid())){
				return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，凭证字+凭证号重复!");
			}
		}else{
			if(voucherRepo.isVoucherNumberExist(fiscalPeriod.getFid(), voucherNumber, vo.getFid())){
				return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，凭证号重复!");
			}
		}
		if(vo.getNumber()!=null){
			int number = vo.getNumber().intValue();
			if(voucherRepo.isNumberExist(fiscalPeriod.getFid(), number, vo.getFid())){
				return new RequestResult(RequestResult.RETURN_FAILURE, "无效操作，顺序号重复!");
			}
		}
		
		return new RequestResult();
	}
	
	/**
	 * 多个实体转vo
	 * @param entities
	 * @return
	 */
	public List<VoucherVo> getVos(List<Voucher> entities){
		List<VoucherVo> vos = new ArrayList<VoucherVo>();
		if(CollectionUtils.isNotEmpty(entities)){
			for(Voucher entity : entities){
				vos.add(getVo(entity, false));
			}
		}
		return vos;
	}	
	
	/**
	 * 单个实体转vo
	 * @param entity
	 * @param loadDetail 是否加载凭证明细信息
	 * @return
	 */
	public VoucherVo getVo(Voucher entity, boolean loadDetail){
		if(entity == null) 
			return null;
		VoucherVo vo = new VoucherVo();
		vo.setFid(entity.getFid());
		if (entity.getNumber()!=null) {
			vo.setNumber(entity.getNumber().longValue());
		}
		vo.setRecordStatus(entity.getRecordStatus());
		vo.setVoucherDate(DateUtils.getStringByFormat(entity.getVoucherDate(), DateUtilTools.DATE_PATTERN_YYYY_MM_DD));
		vo.setTotalAmount(entity.getTotalAmount());
		if (entity.getVoucherNumber()!=null){
			vo.setVoucherNumber(entity.getVoucherNumber().longValue());
		}
		vo.setVoucherWordNumber(entity.getVoucherWordNumber());
		if (entity.getAccessoryNumber()!=null){
			vo.setAccessoryNumber(entity.getAccessoryNumber().longValue());
		}
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS));
		//组织机构
		Organization org = entity.getOrg();
		if(org != null){
			vo.setOrgId(org.getFid());
		}
		//财务账套
		FiscalAccount fiscalAccount = entity.getFiscalAccount();
		if(fiscalAccount != null){
			vo.setFiscalAccountId(fiscalAccount.getFid());
			vo.setFiscalAccountName(fiscalAccount.getName());
		}
		//记账人
		User postPeople = entity.getPostPeople();
		if(postPeople != null){
			vo.setPostPeopleId(postPeople.getFid());
			vo.setPostPeopleName(postPeople.getUserName());
		}
		//凭证主管
		User supervisor = entity.getSupervisor();
		if(supervisor != null){
			vo.setSupervisorId(supervisor.getFid());
			vo.setSupervisorName(supervisor.getUserName());
		}
		//凭证字
		AuxiliaryAttr voucherWord = entity.getVoucherWord();
		if(voucherWord != null){
			vo.setVoucherWordId(voucherWord.getFid());
			vo.setVoucherWordName(voucherWord.getName());
		}
		//创建人
		User creator = entity.getCreator();
		if(creator != null){
			vo.setCreatorId(creator.getFid());
			vo.setCreatorName(creator.getUserName());
			vo.setCreateTime(DateUtilTools.time2String(entity.getCreateTime()));
		}
		//审核人
		User auditor = entity.getAuditor();
		if(auditor != null){
			vo.setAuditorId(auditor.getFid());
			vo.setAuditorName(auditor.getUserName());
			vo.setAuditDate(DateUtilTools.time2String(entity.getAuditDate()));
		}
		//作废人
		User cancelor = entity.getCancelor();
		if(cancelor != null){
			vo.setCancelorId(cancelor.getFid());
			vo.setCancelorName(cancelor.getUserName());
			vo.setCancelDate(DateUtilTools.time2String(entity.getCancelDate()));
		}
		//凭证摘要
		VoucherDetail voucherDetail = detailService.getFirstDetailOfVoucher(entity.getFid());
		if(voucherDetail != null){
			vo.setVoucherResume(voucherDetail.getResume());
		}
		//凭证明细
		if(loadDetail){
			List<VoucherDetailVo> details = detailService.getByVoucher(entity.getFid());
			vo.setDetails(JSONArray.fromObject(details).toString());
		}
		return vo;
	}
	
	/**
	 * 获取下一个凭证<br>
	 * 同一个财务会计期间内，凭证字不变，凭证号加一<br>
	 * @param id 凭证ID
	 * @return
	 */
	public VoucherVo getNextVoucher(String id){
		Voucher curVoucher = voucherRepo.findOne(id);
		Integer voucherNumber = curVoucher.getVoucherNumber(); //凭证号
		AuxiliaryAttr voucherWord = curVoucher.getVoucherWord(); //凭证字
		FiscalPeriod fiscalPeriod = curVoucher.getFiscalPeriod();//财务会计期间
		Voucher nextVoucher = voucherRepo.getNextVoucher(fiscalPeriod.getFid(), voucherWord.getFid(), voucherNumber + 1);
		return getVo(nextVoucher, true);
	}
	
	/**
	 * 根据科目（包括七个辅助核算、单位、外币币别），合并凭证明细
	 * @param voucherId 凭证ID
	 */
	public void mergeDetailBySubject(String voucherId){
		voucherRepo.mergeDetailBySubject(voucherId);
	}

	@Override
	public VoucherVo getVo(Voucher entity) {
		return getVo(entity, false);
	}

	@Override
	public CrudRepository<Voucher, String> getRepository() {
		return voucherRepo;
	}
	
	/**
	 * 判断凭证是否已使用
	 * @param voucher 凭证
	 * @return
	 */
	public boolean isUsed(Voucher voucher){
		//TODO 判断凭证是否已使用
		return false;
	}
	
	/**
	 * 判断账务会计期间的所有凭证是否已过账或者作废
	 * @param fiscalPeriodId 会计期间ID
	 * @return
	 */
	public boolean isAllPostedOrCanceled(String fiscalPeriodId){
		List<Integer> statusLists = Lists.newArrayList(Voucher.STATUS_AUDITED, Voucher.STATUS_UNAUDITED);
		Long count = voucherRepo.countByPeriodIdAndStatus(fiscalPeriodId, statusLists);
		
		if(count!=null && count>0){
			return false;
		}
		return true;
	}
	
	/**
	 * 获取某个财务会计期间下最大的凭证号
	 * @param fiscalPeriodId 财务会计期间ID
	 * @return
	 */
	public Integer getMaxVoucherNumber(String fiscalPeriodId){
		Integer max = voucherRepo.getMaxVoucherNumber(fiscalPeriodId);
		if(max==null)max = 0;
		return max;
	}
	
	/**
	 * 获取某个财务会计期间下，最大的凭证顺序号
	 * @param fiscalPeriodId 财务会计期间ID
	 * @return
	 */
	public Integer getMaxNumber(String fiscalPeriodId){
		Integer max = voucherRepo.getMaxNumber(fiscalPeriodId);
		if(max==null)max = 0;
		return max;
	}
	
	/**
	 * 更新合计金额
	 */
	public void updateSumAmount(String voucherId){
		BigDecimal sumAmount = detailService.sumAmountByVoucherId(voucherId);
		Voucher voucher = findOne(voucherId);
		voucher.setTotalAmount(sumAmount);
		voucherRepo.save(voucher);
	}
	
	/**
	 * 更新合计金额
	 */
	public void updateSumAmount(Voucher voucher){
		List<VoucherDetail> details = voucher.getDetails();
		BigDecimal sum = BigDecimal.ZERO;
		for(VoucherDetail detail:details){
			sum = NumberUtil.add(sum, detail.getDebitAmount());
		}
		voucher.setTotalAmount(sum);
		voucherRepo.save(voucher);
	}
}
