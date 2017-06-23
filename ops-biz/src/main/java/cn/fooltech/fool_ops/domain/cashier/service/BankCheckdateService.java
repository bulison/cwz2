package cn.fooltech.fool_ops.domain.cashier.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.cashier.entity.BankCheckdate;
import cn.fooltech.fool_ops.domain.cashier.repository.BankCheckdateRepository;
import cn.fooltech.fool_ops.domain.cashier.vo.BankCheckdateVo;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>轧账日期网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-12-14 15:50:04
 */
@Service
public class BankCheckdateService extends BaseService<BankCheckdate,BankCheckdateVo,String>{
	
	/**
	 * 轧账日期 持久层
	 */
	@Autowired
	private BankCheckdateRepository repository;
	
	
	/**
	 * 查询轧账日期列表信息，按照轧账日期主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<BankCheckdate> query(BankCheckdateVo bankCheckdateVo,PageParamater pageParamater){
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest request = getPageRequest(pageParamater,sort);
		return repository.query(bankCheckdateVo, request);
	}
	
	
	/**
	 * 单个轧账日期实体转换为vo
	 * @param entity
	 * @return
	 */
	public BankCheckdateVo getVo(BankCheckdate entity){
		if(entity == null)
			return null;
		BankCheckdateVo vo = new BankCheckdateVo();
		vo.setCheckDate(DateUtilTools.date2String(entity.getCheckDate(), DATE_TIME));
		vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		
		User creator = entity.getCreator();
		if(creator!=null){
			vo.setCreatorId(creator.getFid());
			vo.setCreatorName(creator.getUserName());
		}
		
		FiscalAccount account = entity.getFiscalAccount();
		if(account!=null){
			vo.setFiscalAccountId(account.getFid());
			vo.setFiscalAccountName(account.getName());
		}
		
		return vo;
	}
	
	/**
	 * 反扎帐<br>
	 */
	public RequestResult cancle(){
		String accountId = SecurityUtil.getFiscalAccountId();
		BankCheckdate checkdate = getMaxCheckDate(accountId);
		if(checkdate==null)return new RequestResult(RequestResult.RETURN_FAILURE, "无结账记录");
		delete(checkdate.getFid());
		return buildSuccessRequestResult();
	}
	
	/**
	 * 获取轧账日期信息
	 * @param fid 轧账日期ID
	 * @return
	 */
	public BankCheckdateVo getByFid(String fid) {
		Assert.notNull(fid);
		return getById(fid);
	}
	

	/**
	 * 新增/编辑轧账日期
	 * @param vo
	 */
	public RequestResult save(BankCheckdateVo vo) {
		
		String accountId = SecurityUtil.getFiscalAccountId();
		BankCheckdate checkdate = getMaxCheckDate(accountId);
		if(checkdate!=null){
			Date voCheckDate = DateUtilTools.string2Date(vo.getCheckDate(), DATE);
			if(voCheckDate.compareTo(checkdate.getCheckDate())<=0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "结账日期要大于结账日期表中所有记录的结账日期");
			}
		}
		
		BankCheckdate entity = new BankCheckdate();
		Date checkDate = DateUtilTools.string2Date(vo.getCheckDate(), DateUtilTools.DATE_PATTERN_YYYY_MM_DD);
		entity.setCheckDate(checkDate);
		entity.setCreateTime(new Date());
		entity.setFid(vo.getFid());
		entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setOrg(SecurityUtil.getCurrentOrg());
		entity.setDept(SecurityUtil.getCurrentDept());
		
		save(entity);
		
		return buildSuccessRequestResult();
	}
	/**
	 * 获取最大日期扎帐
	 * @return
	 */
	public BankCheckdate getMaxCheckDate(){
		String accountId = SecurityUtil.getFiscalAccountId();
		return getMaxCheckDate(accountId);
	}

	
	/**
	 * 统计总数
	 * @return
	 */
	public long countAll(String accountId){
		Long count = repository.countAll(accountId);
		return count!=null?count:0;
	}
	
	/**
	 * 获取最大日期扎帐
	 * @param accountId
	 * @return
	 */
	public BankCheckdate getMaxCheckDate(String accountId){
		return repository.getMaxCheckDate(accountId);
	}
	
	/**
	 * 根据账套获取所有数据
	 * @param fiscalAccountId
	 * @return
	 */
	public List<BankCheckdate> getAll(String fiscalAccountId) {
		return repository.getAll(fiscalAccountId);
	}
	
	/**
	 * 复制数据
	 * @param oldAccount
	 * @param newAccount
	 * @param subjectCache
	 * @throws CloneNotSupportedException 
	 */
	public void saveCopyAccount(FiscalAccount oldAccount,
			FiscalAccount newAccount) throws CloneNotSupportedException {
		List<BankCheckdate> dataList = this.getAll(oldAccount.getFid());
		
		for(BankCheckdate data:dataList){
			
			BankCheckdate newData = (BankCheckdate)data.clone();
			newData.setFiscalAccount(newAccount);
			save(newData);
		}
	}

	@Override
	public CrudRepository<BankCheckdate, String> getRepository() {
		return repository;
	}

}
