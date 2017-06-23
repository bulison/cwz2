package cn.fooltech.fool_ops.domain.warehouse.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.UserRepository;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.repository.StockLockingRepository;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.domain.warehouse.vo.StockLockingVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.ErrorCode;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.SystemConstant;

/**
 * <p>库存锁定服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年5月30日
 */
@Service("ops.StockLocking")
public class StockLockingService {
	
	/**
	 * 库存锁定DAO类
	 */
	@Autowired
	private StockLockingRepository stockLockingRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	/**
	 * 销售出货、采购退货单处理库存锁定
	 * @param bill
	 * @param vo
	 * @return
	 */
	public RequestResult handle(WarehouseBill bill, StockLockingVo vo){
		if(vo.getCoerceOutStock() == 0){
			//非强制出库
			if(!handle(bill)){
				//当前用户是否拥有库存解锁权限
				boolean hasUnlockPermission = SecurityUtil.isPermit(SystemConstant.PERMISSION_STOCK_UNLOCK);
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("hasUnlockPermission", hasUnlockPermission == true ? 1 : 0);
				
				RequestResult result = new RequestResult();
				result.setDataExt(map);
				result.setReturnCode(RequestResult.RETURN_FAILURE);
				result.setErrorCode(ErrorCode.WAREHOUSE_NEED_UNLOCK);
				return result;
			}
		}
		else if(vo.getCoerceOutStock() == 1){
			//强制出库
			if(SecurityUtil.isPermit(SystemConstant.PERMISSION_STOCK_UNLOCK)){
				return new RequestResult();
			}
			
			String userCode = vo.getUserCode();
			String password = vo.getPassword();
			if(StringUtils.isBlank(userCode)){
				return new RequestResult(RequestResult.RETURN_FAILURE, "用户名不能为空!");
			}
			if(StringUtils.isBlank(password)){
				return new RequestResult(RequestResult.RETURN_FAILURE, "密码不能为空!");
			}
			
			if(SecurityUtil.checkUserPwd(userCode, password)){
				return new RequestResult(RequestResult.RETURN_FAILURE, "用户名或密码不正确!");
			}
			User otherUser = userRepo.findOneByUserCode(userCode);
			if(!SecurityUtil.isPermit(otherUser.getFid(), SystemConstant.PERMISSION_STOCK_UNLOCK)){
				return new RequestResult(RequestResult.RETURN_FAILURE, "输入的用户缺少库存解锁权限，请联系企业管理员进行授权!");
			}
		}
		else{
			throw new RuntimeException("参数错误!");
		}
		return new RequestResult();
	}
	
	
	/**
	 * 处理库存假锁，销售出货、采购退货单审核时执行<br>
	 * @param bill
	 * @return 是否可以直接出库
	 */
	public boolean handle(WarehouseBill bill){
		List<Integer> typeList = Lists.newArrayList(WarehouseBuilderCodeHelper.xsch, WarehouseBuilderCodeHelper.cgth);
		if(!typeList.contains(bill.getBillType())){
			return true;
		}
		
		BigDecimal lockingDays = stockLockingRepo.getMaxFakeLockingDays(bill.getFid());
		int days = lockingDays.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		
		Date billDate = bill.getBillDate();
		Date lockDate = DateUtilTools.now();
		
		lockDate = DateUtilTools.changeDateTime(lockDate, 0, -days, 0, 0, 0);
		
		if(billDate.compareTo(lockDate)<0){
			return true;
		} 
		
		Organization org = bill.getOrg();
		FiscalAccount fiscalAccount = bill.getFiscalAccount();
		return !isNeedUnclock(org.getFid(), fiscalAccount.getFid(), bill.getFid());
	}
	
	/**
	 * 根据草稿单，判断销售出货、采购退货单出货时，是否需要库存解锁权
	 * @param orgId 机构ID
	 * @param accountId 财务账套ID
	 * @param billId 单据ID
	 */
	public boolean isNeedUnclock(String orgId, String accountId, String billId){
		Integer result = stockLockingRepo.isNeedUnclock(orgId, accountId, billId);
		if(result!=null && result>0){
			return true;
		}
		return false;
	}
} 
