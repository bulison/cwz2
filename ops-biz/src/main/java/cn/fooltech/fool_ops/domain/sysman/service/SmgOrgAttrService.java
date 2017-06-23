package cn.fooltech.fool_ops.domain.sysman.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalAccountRepository;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalAccountVo;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.message.service.PushService;
import cn.fooltech.fool_ops.domain.report.service.CashFlowAnalysisService;
import cn.fooltech.fool_ops.domain.report.vo.CashFlowAnalysisVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.SmgOrgAttr;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.OrganizationRepository;
import cn.fooltech.fool_ops.domain.sysman.repository.SmgOrgAttrRepository;
import cn.fooltech.fool_ops.domain.sysman.vo.SmgOrgAttrVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

/**
 * 预警阈值设置 服务类
 */
@Service
public class SmgOrgAttrService extends BaseService<SmgOrgAttr, SmgOrgAttrVo, String> {

	@Autowired
	private SmgOrgAttrRepository repository;
	
	/**
	 * 现金流量汇总分析
	 */
	@Autowired
	private CashFlowAnalysisService analysisService;
	
	@Autowired
	private PushService pushService;
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FiscalAccountRepository accountRepository;
	
	

	/**
	 * 实体转换VO
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public SmgOrgAttrVo getVo(SmgOrgAttr entity) {
		SmgOrgAttrVo vo = VoFactory.createValue(SmgOrgAttrVo.class, entity);
		Organization org = entity.getOrg();
		if (org != null) {
			vo.setOrgId(org.getFid());
			vo.setOrgName(org.getOrgName());
		}
		return vo;
	}

	@Override
	public CrudRepository<SmgOrgAttr, String> getRepository() {
		return repository;
	}

	/**
	 * 查找分页
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<SmgOrgAttrVo> query(SmgOrgAttrVo vo, PageParamater paramater) {

		// String accId = SecurityUtil.getFiscalAccountId();
		// String creatorId = SecurityUtil.getCurrentUserId();
		// Sort sort = new Sort(Sort.Direction.DESC, "code");
		PageRequest pageRequest = getPageRequest(paramater);
		Page<SmgOrgAttr> page = repository.findPageBy(vo, pageRequest);
		return getPageVos(page, pageRequest);
	}

	/**
	 * 修改或新增
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(SmgOrgAttrVo vo) {

		SmgOrgAttr entity = null;
		if (Strings.isNullOrEmpty(vo.getFid())) {
			entity = new SmgOrgAttr();
			entity.setCreateTime(new Date());
			entity.setOrg(SecurityUtil.getCurrentOrg());
		} else {
			entity = repository.findOne(vo.getFid());

			// if (entity.getUpdateTime().compareTo(vo.getUpdateTime()) != 0) {
			// return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			// }
		}

		entity.setKey(vo.getKey());
		entity.setValue(vo.getValue());
		entity.setDescribe(vo.getDescribe());
		entity.setName(vo.getName());
		entity.setRecordState(vo.getRecordState());
		entity.setType(vo.getType());
		entity.setIds(vo.getIds());

		repository.save(entity);

		return buildSuccessRequestResult(getVo(entity));
	}

	/**
	 * 查询收货单亏损预警阈值
	 * 
	 * @param orgId
	 *            机构id
	 * @return
	 */
	public SmgOrgAttrVo queryByOrg(String orgId, String key) {
		SmgOrgAttr entity = repository.queryByOrg(orgId, key);
		if (entity != null) {
			return getVo(entity);
		} else {
			return null;
		}
	}

	/**
	 * 检查资金池预警额度,如果不高于，则需要预警,自动发送信息给“发送预警人”
	 */
	@Transactional
	public void checkWarningQuota() {
		try {
			//轮询机构
			List<Organization> organizations = organizationRepository.queryRootOrganization();
			for (Organization organization : organizations) {
				String orgId = organization.getFid();
				String warningQuota = "";
				String alarmDuration = "";
				String sendWarning = "";

				SmgOrgAttrVo queryByOrg = queryByOrg(orgId, "WARNING_QUOTA");// 资金池预警额度
				if (queryByOrg != null) {
					warningQuota = queryByOrg.getValue();
				}
				SmgOrgAttrVo queryByOrg2 = queryByOrg(orgId, "ALARM_DURATION");// 预警时长
				if (queryByOrg2 != null) {
					alarmDuration = queryByOrg2.getValue();
				}
				SmgOrgAttrVo queryByOrg3 = queryByOrg(orgId, "SEND_WARNING");// 发送预警人
				if (queryByOrg3 != null) {
					sendWarning = queryByOrg3.getIds();
				}
				if (!Strings.isNullOrEmpty(warningQuota) && !Strings.isNullOrEmpty(alarmDuration)) {
					Sort sort = new Sort(Direction.DESC, "name");
					List<FiscalAccount> fiscalAccounts = accountRepository.findByOrgId(orgId, sort);
					for (FiscalAccount fiscalAccount : fiscalAccounts) {
						CashFlowAnalysisVo vo = new CashFlowAnalysisVo();
						vo.setStartDay(DateUtils.getCurrentDate());
						Date currentDate = DateUtils.getDateFromString(DateUtils.getCurrentDate());
						Date endDate = DateUtilTools.changeDateTime(currentDate, 0, Integer.valueOf(alarmDuration), 0, 0, 0);
						vo.setEndDay(DateUtils.getDateString(endDate));
						
						PageParamater paramater = new PageParamater();
						paramater.setRows(Integer.MAX_VALUE);
						paramater.setStart(0);
						List<CashFlowAnalysisVo> list = analysisService.getList(vo,orgId,fiscalAccount.getFid(),paramater);
						String msg="资金池预警额度:"+warningQuota+",低于预警;";
						for (CashFlowAnalysisVo analysisVo : list) {
							String paymentDate = analysisVo.getPaymentDate();
							Date paymentDate2 = DateUtils.getDateFromString(paymentDate);
							Date currentDate2 = DateUtils.getDateFromString(DateUtils.getCurrentDate());
							if(paymentDate2.compareTo(currentDate2)<0) continue;
							String amount = analysisVo.getAmount();
							BigDecimal warningQuota1 = new BigDecimal(warningQuota);
							BigDecimal amount1 = new BigDecimal(amount);
							if(amount1.compareTo(warningQuota1)>0) continue;
							msg += "日期："+paymentDate+",";
							msg += "结余金额："+amount+";";
							
						}
						Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
						if(sendWarning==null) continue;
						if(msg.indexOf("结余金额")<0) continue;
						List<String> list2 = splitter.splitToList(sendWarning);
						for (String userId : list2) {
							User receiver = userService.findOne(userId);
							User sender=null;
							String title="资金池额度预警";
							messageService.sendCaptialWarningMsg(sender, receiver, title, msg, fiscalAccount);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
