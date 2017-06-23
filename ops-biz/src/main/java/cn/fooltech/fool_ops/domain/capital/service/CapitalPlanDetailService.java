package cn.fooltech.fool_ops.domain.capital.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanBill;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanChangeLog;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanDetail;
import cn.fooltech.fool_ops.domain.capital.repository.CapitalPlanDetailRepository;
import cn.fooltech.fool_ops.domain.capital.vo.BindingBillVo;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanBillVo;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanDetailVo;
import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.cost.service.CostBillService;
import cn.fooltech.fool_ops.domain.flow.service.TaskService;
import cn.fooltech.fool_ops.domain.flow.vo.TaskVo;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService.BillType;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.JsonUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

/**
 * 
 * @Description:资金计划明细 服务类
 * @author cwz
 * @date 2017年3月1日 下午2:09:37
 */
@Service
public class CapitalPlanDetailService extends BaseService<CapitalPlanDetail, CapitalPlanDetailVo, String> {
	/* 资金计划明细 持久层 */
	@Autowired
	private CapitalPlanDetailRepository repository;
	/* 资金计划 服务类 */
	@Autowired
	private CapitalPlanService capitalPlanService;
	/* 资金计划日志服务类 */
	@Autowired
	private CapitalPlanChangeLogService logService;
	/* 资金计划关联单据 服务类 */
	@Autowired
	private CapitalPlanBillService billService;
	/* 收付款对单网页服务类 */
	@Autowired
	private PaymentCheckService payCheckService;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	/* 费用单网页服务类 */
	@Autowired
	private CostBillService costBillService;

	/**
	 * 实体转换VO
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public CapitalPlanDetailVo getVo(CapitalPlanDetail entity) {
		CapitalPlanDetailVo vo = VoFactory.createValue(CapitalPlanDetailVo.class, entity);
		CapitalPlan capital = entity.getCapital();
		if (capital != null) {
			vo.setCapitalId(capital.getId());
			vo.setCapitalCode(capital.getCode());
		}
		User auditor = entity.getAuditor();
		if (auditor != null) {
			vo.setAuditorId(auditor.getFid());
			vo.setAuditorName(auditor.getUserName());
		}
		User badDebt = entity.getBadDebt();
		if (badDebt != null) {
			vo.setBadDebtId(badDebt.getFid());
			vo.setBadDebtName(badDebt.getUserName());
		}
		User cancelor = entity.getCancelor();
		if (cancelor != null) {
			vo.setCancelorId(cancelor.getFid());
			vo.setCancelorName(cancelor.getUserName());
		}
		User complete = entity.getComplete();
		if (complete != null) {
			vo.setCompleteId(complete.getFid());
			vo.setCompleteName(complete.getUserName());
		}
		User creator = entity.getCreator();
		if (creator != null) {
			vo.setCreatorId(creator.getFid());
			vo.setCreatorName(creator.getUserName());
		}
		return vo;
	}

	@Override
	public CrudRepository<CapitalPlanDetail, String> getRepository() {
		return repository;
	}

	/**
	 * 查找分页
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<CapitalPlanDetailVo> query(CapitalPlanDetailVo vo, PageParamater paramater) {
		Sort sort = new Sort(Sort.Direction.DESC, "paymentDate", "capital.code");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<CapitalPlanDetail> page = repository.findPageBy(vo, pageRequest);
		return getPageVos(page, pageRequest);
	}

	/**
	 * 修改或新增
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(CapitalPlanDetailVo vo) {
		CapitalPlanDetailVo vo2 = null;
		try {
			CapitalPlanDetail entity = null;
			if (Strings.isNullOrEmpty(vo.getId())) {
				entity = new CapitalPlanDetail();
				entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
				entity.setCreator(SecurityUtil.getCurrentUser());
				entity.setCreateTime(new Date());
				entity.setUpdateTime(new Date());
				entity.setOrg(SecurityUtil.getCurrentOrg());
			} else {
				entity = repository.findOne(vo.getId());

				if (entity.getUpdateTime().compareTo(vo.getUpdateTime()) != 0) {
					return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
				}
				entity.setUpdateTime(vo.getUpdateTime());
				entity.setCreator(SecurityUtil.getCurrentUser());
			}
			entity.setCapital(capitalPlanService.findOne(vo.getCapitalId()));
			entity.setExplain(vo.getExplain());
			entity.setRelationSign(vo.getRelationSign());
			entity.setRelationId(vo.getRelationId());
			entity.setPaymentDate(vo.getPaymentDate());
			BigDecimal planAmount = vo.getPlanAmount()==null?BigDecimal.ZERO:vo.getPlanAmount();
			entity.setPlanAmount(planAmount);
			BigDecimal billAmount = vo.getBillAmount()==null?BigDecimal.ZERO:vo.getBillAmount();
			entity.setBillAmount(billAmount);
			BigDecimal paymentAmount = vo.getPaymentAmount()==null?BigDecimal.ZERO:vo.getPaymentAmount();
			entity.setPaymentAmount(paymentAmount);
			entity.setRemark(vo.getRemark());
			entity.setRecordStatus(vo.getRecordStatus());
			entity.setCompleteSign(vo.getCompleteSign());
			entity.setOrgPaymentDate(vo.getOrgPaymentDate());

			repository.save(entity);
			vo2 = getVo(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildSuccessRequestResult(vo2);
	}

	/**
	 * 根据计划id删除明细
	 * 
	 * @param capitalId
	 */
	@Transactional
	public void delByCapitalId(String capitalId) {
		repository.delByCapitalId(capitalId);
	}

	/**
	 * json转list
	 * 
	 * @param details
	 *            json数据
	 * @return
	 */
	@Transactional
	public List<CapitalPlanDetailVo> getDetails(String details) {
		List<CapitalPlanDetailVo> list = JsonUtil.toObjectList(details, CapitalPlanDetailVo.class);
		return list;
	}

	/**
	 * 根据计划id查询明细
	 * 
	 * @param capitalId
	 * @return
	 */
	public List<CapitalPlanDetail> queryByCapitalId(String capitalId) {
		List<CapitalPlanDetail> list = repository.queryByCapitalId(capitalId);
		return list;
	}

	/**
	 * 根据计划id修改状态【状态 0-草稿，1-审核，2-坏账，3-完成，4-取消】
	 * @param id			主键
	 * @param recordStatus	修改后的状态
	 * @param updateTime	修改时间
	 * @param editType		可否限制：0-不限制，1-限制
	 * @return
	 */
	@Transactional
	public RequestResult changeByCapitalId(String id, int recordStatus,String updateTime,Integer editType) {
		try {
			CapitalPlanDetail entity = repository.findOne(id);
			if (entity == null) {
				return buildFailRequestResult("该记录已被删除,请刷新页面");
			}
			if (entity.getUpdateTime().compareTo(DateUtils.getDateFromString(updateTime)) != 0) {
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
			if(editType==null){
				editType = 1;//设置默认限制
			}
			Integer relationSign = entity.getRelationSign();
			Integer status = entity.getRecordStatus();
			// 审核
			// 自动写入审核人与审核时间,明细表记录也自动写入审核人与审核时间
			if (recordStatus == 1 && entity.getBadDebt() == null) {
				if (relationSign != 0) {
					return buildFailRequestResult("不是用户手动新增的记录不能审核!");
				}
				if (status != 0) {
					return buildFailRequestResult("只能在未审核状态下才能审核记录!");
				}
				entity.setRecordStatus(recordStatus);
				entity.setAuditor(SecurityUtil.getCurrentUser());
				entity.setAuditTime(new Date());
				entity.setUpdateTime(new Date());
				Date paymentDate = entity.getPaymentDate();
				Date orgPaymentDate = entity.getOrgPaymentDate();
				if(orgPaymentDate==null){
					entity.setOrgPaymentDate(paymentDate);
				}
				
				repository.save(entity);
			}
			// 反审核
			if (recordStatus == 0) {
				if (relationSign != 0) {
					return buildFailRequestResult("不是用户手动新增的记录不能反审核!");
				}
				if (status != 1) {
					return buildFailRequestResult("必须是审核状态下的记录才能反审核!");
				}
				entity.setRecordStatus(recordStatus);
				entity.setAuditor(null);
				entity.setAuditTime(null);
				entity.setUpdateTime(new Date());
				repository.save(entity);
				//反审核删除所有日志记录
				List<CapitalPlanChangeLog> logs = logService.queryByDetailId(entity.getId());
				for (CapitalPlanChangeLog log : logs) {
					logService.delete(log);
				}
			}
			// 取消
			if (recordStatus == 4) {
				// 判断状态是否为坏账（2）或完成（3），是就不能取消；
				if (status == 2 || status == 3) {
					return buildFailRequestResult("已完成/坏账不能取消!");
				}
				// 判断关联类型是否单据类型，是单据类型的不能取消；要在单据点作废才更改这个状态；
				if (relationSign != 0 && relationSign != 71 && relationSign != 72) {
					return buildFailRequestResult("不是用户手动新增的记录不能取消");
				}
				entity.setRecordStatus(recordStatus);
				entity.setCancelor(SecurityUtil.getCurrentUser());
				entity.setCancelTime(new Date());
				entity.setUpdateTime(new Date());
				repository.save(entity);
			}
			// 准备坏账
			if (recordStatus == 2) {
				if (status != 1) {
					return buildFailRequestResult("必须是审核状态下的记录才能准备坏账!");
				}
				entity.setRecordStatus(recordStatus);
				entity.setBadDebt(SecurityUtil.getCurrentUser());
				entity.setBadDebtDate(new Date());
				entity.setUpdateTime(new Date());
				repository.save(entity);
			}
			// 取消坏账
			if (recordStatus == 1 && entity.getBadDebt() != null) {
				if (status != 2) {
					return buildFailRequestResult("必须是坏账状态下的记录才能取消坏账!");
				}
				entity.setRecordStatus(recordStatus);
				entity.setBadDebt(null);
				entity.setBadDebtDate(null);
				entity.setUpdateTime(new Date());
				repository.save(entity);
			}
			// 完成：判断状态是否为审核（1），不是就不能完成；
			if (recordStatus == 3) {
				if (status != 1) {
					return buildFailRequestResult("必须是审核状态下的记录才能完成!");
				}
				BigDecimal planAmount = entity.getPlanAmount()==null?BigDecimal.ZERO:entity.getPlanAmount();
				BigDecimal paymentAmount = entity.getPaymentAmount()==null?BigDecimal.ZERO:entity.getPaymentAmount();
				BigDecimal billAmount = entity.getBillAmount()==null?BigDecimal.ZERO:entity.getBillAmount();
				// 如果主表关联类型为单据，要判断单据是否已经收/付完款，如果没有，不能完成；
				//如果主表关联类型为计划，要判断计划是否已经绑定单据，已处理好收付款，如果未处理好，提示未绑定单据或未收付款，不限制完成操作；
				if (relationSign == 72 && editType ==1) {
					if(billAmount.compareTo(BigDecimal.ZERO)==0){
						return buildFailRequestResult("该记录未绑定单据,是否继续完成!");
					}
					if(paymentAmount.compareTo(BigDecimal.ZERO)==0){
						return buildFailRequestResult("该记录没有收付款操作,是否继续完成!");
					}
					int compareTo = planAmount.compareTo(billAmount);
					if (compareTo != 0){
						return buildFailRequestResult("单据绑定金额与计划金额不一致,是否继续完成!");
					}
					int compareTo2 = planAmount.compareTo(paymentAmount);
					if (compareTo2 != 0){
						return buildFailRequestResult("收/付款操作还未完成,是否继续完成!");
					}
				} else if (relationSign > 0 && relationSign != 72) {
					int compareTo = billAmount.compareTo(paymentAmount);
					if (compareTo != 0)
						return buildFailRequestResult("请先完成收/付款操作!");
				}
				entity.setRecordStatus(recordStatus);
				entity.setComplete(SecurityUtil.getCurrentUser());
				entity.setCompleteDate(new Date());
				entity.setUpdateTime(new Date());
				repository.save(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("计划明细状态修改出错!");
		}
		return buildSuccessRequestResult();
	}

	/**
	 * 延期
	 * 
	 * @param id
	 *            明细id
	 * @param date
	 *            延期日期
	 * @param remark
	 *            备注
	 * @return
	 */
	@Transactional
	public RequestResult delayDate(String id, Date date, String remark) {

		try {
			CapitalPlanDetail detail = repository.findOne(id);
			if (detail == null) {
				return buildFailRequestResult("该记录已被删除,请刷新页面");
			}
			Integer status = detail.getRecordStatus();
			Date paymentDate = detail.getPaymentDate();
			// 判断状态是否为审核（1），不是就不能延期；
			if (status != 1) {
				return buildFailRequestResult("必须是审核状态下的记录才能延期!");
			}
			int compareTo = paymentDate.compareTo(date);
			// 判断修改后的预计收付款日期不能等于原记录的预计收付款日期；
			if (compareTo == 0) {
				return buildFailRequestResult("预计收付款日期不能等于原记录的预计收付款日期!");
			}
			// 把修改前的记录和修改后的预计收付款日期插入到变更资金计划日志表；
			CapitalPlanChangeLog log = new CapitalPlanChangeLog();
			log.setCreate(SecurityUtil.getCurrentUser());
			log.setChangeType(1);
			log.setCreateTime(new Date());
			log.setDetail(detail);
			log.setFiscalAccount(SecurityUtil.getFiscalAccount());
			log.setOrg(SecurityUtil.getCurrentOrg());
			log.setPaymentAmount(null);
			log.setPaymentDate(date);
			log.setPrePaymentDate(paymentDate);
			log.setPrePaymentAmount(null);
			log.setRemark(remark);
			logService.save(log);
			// 修改记录的预计收付款日期；
			detail.setPaymentDate(date);
			repository.save(detail);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("延期操作出错!");
		}
		return buildSuccessRequestResult();
	}

	/**
	 * 调整金额
	 * 
	 * @param id
	 *            明细id
	 * @param amount
	 *            调整后金额
	 * @param remark
	 *            备注
	 * @return
	 */
	@SuppressWarnings("null")
	@Transactional
	public RequestResult adjustedAmount(String id, BigDecimal amount, String remark) {
		try {
			if (amount.compareTo(BigDecimal.ZERO) == 0) {
				return buildFailRequestResult("调整金额不能为0!");
			}
			CapitalPlanDetail detail = repository.findOne(id);
			if (detail == null) {
				return buildFailRequestResult("该记录已被删除,请刷新页面");
			}
			CapitalPlan capital = detail.getCapital();
			Integer status = detail.getRecordStatus();
			BigDecimal planAmount = detail.getPlanAmount()==null?BigDecimal.ZERO:detail.getPlanAmount();
			BigDecimal billAmount = detail.getBillAmount()==null?BigDecimal.ZERO:detail.getBillAmount();
			BigDecimal paymentAmount = detail.getPaymentAmount()==null?BigDecimal.ZERO:detail.getPaymentAmount();
			Integer relationSign = detail.getRelationSign();
			// 判断状态是否为审核（1），不是就不能延期；
			if (status != 1) {
				return buildFailRequestResult("必须是审核状态下的记录才能调整金额!");
			}
			// 判断关联类型是否为关联单据的单据类型且计划收付金额为0，是就不能调整金额；
			if (relationSign != 0 && relationSign != 71) {
				if (planAmount.compareTo(BigDecimal.ZERO) == 0) {
					return buildFailRequestResult("关联单据类型不能调整金额");
				}

			}
			if(planAmount.compareTo(BigDecimal.ZERO)<0&&amount.compareTo(BigDecimal.ZERO)>0){
				return buildFailRequestResult("原资金计划金额为负数，调整金额请输入负数!");
			}
			if(planAmount.compareTo(BigDecimal.ZERO)>0&&amount.compareTo(BigDecimal.ZERO)<0){
				return buildFailRequestResult("原资金计划金额为正数，调整金额请输入正数!");
			}
			/*判断 | 调整金额 | >=单据金额 &| 调整金额>= | 收付款金额 | > 0；*/
//			if(amount.abs().compareTo(billAmount.abs())<0){
//				return buildFailRequestResult("调整金额小于收单据金额不能调整");
//			}
			if(amount.abs().compareTo(paymentAmount.abs())<0){
				return buildFailRequestResult("调整金额小于收付款金额不能调整");
			}
			int compareTo = planAmount.compareTo(amount);
			// 判断修改后的计划收付金额不能等于原记录的计划收付金额；
			if (compareTo == 0) {
				return buildFailRequestResult("改后的计划收付金额不能等于原记录的计划收付金额!");
			}
			// 把修改前的记录和修改后的计划收付金额插入到变更资金计划日志表；
			CapitalPlanChangeLog log = new CapitalPlanChangeLog();
			log.setCreate(SecurityUtil.getCurrentUser());
			log.setChangeType(1);
			log.setCreateTime(new Date());
			log.setDetail(detail);
			log.setFiscalAccount(SecurityUtil.getFiscalAccount());
			log.setOrg(SecurityUtil.getCurrentOrg());
			log.setPaymentAmount(amount);
			log.setPaymentDate(new Date());
			log.setPrePaymentDate(null);
			log.setPrePaymentAmount(planAmount);
			log.setRemark(remark);
			logService.save(log);
			// // 修改记录的预计收付款日期；
			// detail.setPaymentDate(date);
			detail.setPlanAmount(amount);
			detail.setRemark(remark);
			// 修改记录的计划收付金额；
			repository.save(detail);
			String capitalId = capital.getId();
			// 合计计划收付金额，修改主表的计划收付金额；
			List<CapitalPlanDetail> queryByCapitalId = queryByCapitalId(capitalId);
			BigDecimal countAmount = BigDecimal.ZERO;
			for (CapitalPlanDetail detail2 : queryByCapitalId) {
				BigDecimal planAmount2 = detail2.getPlanAmount();
				countAmount = countAmount.add(planAmount2);
			}
			capital.setPlanAmount(countAmount);
			capitalPlanService.save(capital);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("调整金额操作出错!");
		}
		return buildSuccessRequestResult();
	}

	/**
	 * 拆分明细
	 * 
	 * @param id
	 *            明细id
	 * @param amount
	 *            拆分金额
	 * @param remark
	 *            备注
	 * @return
	 */
	@SuppressWarnings("null")
	@Transactional
	public RequestResult splitDetail(String id, BigDecimal amount, String remark) {
		//标记正负数
		int type=0;
		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return buildFailRequestResult("拆分金额不能为0!");
		}
		CapitalPlanDetail detail = repository.findOne(id);
		if (detail == null) {
			return buildFailRequestResult("该记录已被删除,请刷新页面!");
		}
		Integer status = detail.getRecordStatus();
		BigDecimal planAmount = detail.getPlanAmount() == null ? BigDecimal.ZERO : detail.getPlanAmount();// 计划金额
		BigDecimal paymentAmount = detail.getPaymentAmount() == null ? BigDecimal.ZERO : detail.getPaymentAmount();// 收付款金额
		BigDecimal billAmount = detail.getBillAmount() == null ? BigDecimal.ZERO : detail.getBillAmount();// 单据金额
		Integer relationSign = detail.getRelationSign();
		// 拆分：判断状态是否为审核（1），不是就不能拆分；
		if (status != 1) {
			return buildFailRequestResult("必须是审核状态下的记录才能拆分!");
		}
		if(amount.compareTo(BigDecimal.ZERO)<0){
			amount = amount.multiply(new BigDecimal(-1));
			billAmount = billAmount.multiply(new BigDecimal(-1));
			type=1;
			
		}
		int compareTo2 = amount.abs().compareTo(paymentAmount.abs());
		if (compareTo2 < 0) {
			return buildFailRequestResult("拆分金额要大于收付款金额!");
		}
		/* 弹出窗口输入拆分金额，输入的金额必须与原金额的符号一致，即原金额是正数，则输入为正数，原金额是负数，则输入为负数； */
		if (relationSign != 0 && relationSign != 72) {
//			if (billAmount.compareTo(BigDecimal.ZERO) <= 0) {
//				return buildFailRequestResult("单据金额要大于0!");
//			}
			/*
			 * 根据关联类型来进行判断，如果关联类型为单据类型，则 | 单据金额 | > | 拆分金额 | >= | 收付款金额 | > 0，
			 */
			int compareTo = billAmount.abs().compareTo(amount.abs());
			if (compareTo <= 0) {
				return buildFailRequestResult("拆分金额要小于单据金额!");
			}
			RequestResult copyDetail = copyDetail(id, amount, 1,type,remark);
			return copyDetail;

		} else {
			/* 如果关联类型为计划事件或为空，则 |计划收付金额 | > | 拆分金额 | >= | 收付款金额 | > 0； */
			if(planAmount.compareTo(BigDecimal.ZERO)<0){
				planAmount = planAmount.multiply(new BigDecimal(-1));
				type=1;
			}
			int compareTo = planAmount.compareTo(amount);
			if (compareTo <= 0) {
				return buildFailRequestResult("拆分金额要小于计划收付金额!");
			}
			RequestResult copyDetail = copyDetail(id, amount, 2,type,remark);
			return copyDetail;
		}
	}
	/**
	 * 复制资金计划明细表
	 * @param detailId	明细对象ID
	 * @param amount	 拆分金额
	 * @param type		类型：1.单据类型，2.计划或用户手动添加类型
	 * @param zhengfu	正负标识：0-正数，1-负数
	 * @param remark	备注
	 * @return
	 */
	@Transactional
	public RequestResult copyDetail(String detailId, BigDecimal amount, int type,int zhengfu,String remark) {
		try {
			CapitalPlanDetail detail = findOne(detailId);
			if(detail==null){
				return buildFailRequestResult("改明细记录已经不存在,请刷新页面!");
			}
			/*
			 * 把当前记录复制一条插入资金计划明细表，根据关联类型来进行判断，
			 */
			CapitalPlanDetail copyDetail = new CapitalPlanDetail();
			if(zhengfu==1){
				amount = amount.multiply(new BigDecimal(-1));
			}
			/* 如果关联类型为单据类型，则计划收付金额=0，单据金额=原记录的单据金额-拆分金额，收付款金额=0， */
			if (type == 1) {
				copyDetail.setPlanAmount(BigDecimal.ZERO);
				copyDetail.setBillAmount(detail.getBillAmount().subtract(amount));
				copyDetail.setPaymentAmount(BigDecimal.ZERO);
				/* 原记录的单据金额=拆分金额， */
				detail.setBillAmount(amount);
			} else {
				/* 如果关联类型为计划事件或为空，则计划收付金额=原记录的计划收付金额-拆分金额，单据金额=0，收付款金额 =0； */
				copyDetail.setPlanAmount(detail.getPlanAmount().subtract(amount));
				copyDetail.setPaymentAmount(BigDecimal.ZERO);
				copyDetail.setBillAmount(BigDecimal.ZERO);
				/* 原记录的计划收付金额=拆分金额 新记录的创建人取当前操作人，创建时间取当前时间，其余信息与原记录一致； */
				detail.setPlanAmount(amount);
			}
			copyDetail.setAuditor(detail.getAuditor());
			copyDetail.setOrgPaymentDate(detail.getOrgPaymentDate());
			copyDetail.setRelationSign(detail.getRelationSign());
			copyDetail.setRelationId(detail.getRelationId());
			copyDetail.setAuditTime(detail.getAuditTime());
			copyDetail.setBadDebt(detail.getBadDebt());
			copyDetail.setBadDebtDate(detail.getBadDebtDate());
			copyDetail.setBadDebtSign(detail.getBadDebtSign());
			copyDetail.setCancelor(detail.getCancelor());
			copyDetail.setCancelTime(detail.getCancelTime());
			copyDetail.setCapital(detail.getCapital());
			copyDetail.setComplete(detail.getComplete());
			copyDetail.setCompleteDate(detail.getCompleteDate());
			copyDetail.setCompleteSign(detail.getCompleteSign());
			copyDetail.setCreateTime(new Date());
			copyDetail.setCreator(SecurityUtil.getCurrentUser());
			copyDetail.setExplain(detail.getExplain());
			copyDetail.setFiscalAccount(detail.getFiscalAccount());
			copyDetail.setOrg(detail.getOrg());
			copyDetail.setPaymentDate(detail.getPaymentDate());
			copyDetail.setRecordStatus(detail.getRecordStatus());
			copyDetail.setRemark(remark);
			copyDetail.setUpdateTime(new Date());
			repository.save(detail);
			repository.save(copyDetail);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("复制资金计划明细表出错");
		}

		return buildSuccessRequestResult();
	}

	/**
	 * 合并资金计划明细
	 * 
	 * @param ids
	 *            需要合并的id
	 * @param planId
	 *            主表id
	 * @param relationId
	 *            关联id
	 * @return
	 */
	@Transactional
	public RequestResult mergeDetail(String ids, String planId, String relationId) {
		try {
			Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
			List<String> fidList = splitter.splitToList(ids);
			List<CapitalPlanDetail> details = Lists.newArrayList();
			// 检查约束
			for (String id : fidList) {
				CapitalPlanDetail detail = repository.findOne(id);

				if (detail == null) {
					return buildFailRequestResult("数据已被其它用户修改，请刷新页面!");
				}
				Integer status = detail.getRecordStatus();
				String capitalId = detail.getCapital().getId();
				String relationId2 = detail.getRelationId() == null ? "" : detail.getRelationId();
				// 判断所有选中记录的状态是否为审核（1），不是就不能合并； 判断所有选中记录的主表ID与关联ID必须相同才能合并；
				if (status != 1) {
					return buildFailRequestResult("必须是审核状态下的记录才能合并!");
				}
				if (!planId.equals(capitalId)) {
					return buildFailRequestResult("合并的主表ID不一致!");
				}
				if (!relationId.equals(relationId2) && detail.getRelationSign() != 72) {
					return buildFailRequestResult("合并的关联ID不一致!");
				}
				details.add(detail);
			}
			// 合并记录
			BigDecimal planAmountSum = BigDecimal.ZERO;
			BigDecimal billAmountSum = BigDecimal.ZERO;
			BigDecimal paymentAmountSum = BigDecimal.ZERO;
			Date maxDate = details.get(0).getPaymentDate();
			String maxDetail = "";
			/*
			 * 合并后只保留选中记录中预计收付款日期最大的一条记录，并把其他选中的计划收付金额、单据金额、
			 * 收付款金额分别汇总到该记录的计划收付金额、 单据金额、 收付款金额，并把创建人取当前操作人，创建时间取当前时间；
			 */

			for (CapitalPlanDetail detail : details) {
				BigDecimal planAmount = detail.getPlanAmount() == null ? BigDecimal.ZERO : detail.getPlanAmount();
				BigDecimal billAmount = detail.getBillAmount() == null ? BigDecimal.ZERO : detail.getBillAmount();
				BigDecimal paymentAmount = detail.getPaymentAmount() == null ? BigDecimal.ZERO
						: detail.getPaymentAmount();
				Date paymentDate = detail.getPaymentDate();
				planAmountSum = planAmountSum.add(planAmount);
				billAmountSum = billAmountSum.add(billAmount);
				paymentAmountSum = paymentAmountSum.add(paymentAmount);
				int compareTo = paymentDate.compareTo(maxDate);
				if (compareTo >= 0) {
					maxDate = paymentDate;
					maxDetail = detail.getId();
				}
			}
			CapitalPlanDetail detail = repository.findOne(maxDetail);
			detail.setPlanAmount(planAmountSum);
			detail.setBillAmount(billAmountSum);
			detail.setPaymentAmount(paymentAmountSum);
			detail.setCreator(SecurityUtil.getCurrentUser());
			detail.setCreateTime(new Date());
			detail.setUpdateTime(new Date());
			repository.save(detail);
			/* 把选中记录的ID在资金计划关联单据表和变更资金计划日志表的关联数据的明细ID改变合并后的记录ID； */
			for (String detailId : fidList) {
				if (!detailId.equals(detail.getId())) {
					List<CapitalPlanBill> queryByDetail = billService.queryByDetail(detailId);
					for (CapitalPlanBill bill : queryByDetail) {
						bill.setDetail(detail);
						billService.save(bill);
					}
					List<CapitalPlanChangeLog> queryByDetailId = logService.queryByDetailId(detailId);
					for (CapitalPlanChangeLog log : queryByDetailId) {
						log.setDetail(detail);
						logService.save(log);
					}
				}
			}

			/* 删除合并后的其他明细记录 */
			for (String id : fidList) {
				if (!id.equals(detail.getId())) {
					repository.delete(id);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("合并资金计划明细出错!");
		}
		return buildSuccessRequestResult();
	}

	/**
	 * 绑定单据
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult bindingBill(CapitalPlanBillVo vo) {
		try {
			CapitalPlanDetail detail = repository.findOne(vo.getDetailId());
			if (detail == null) {
				return buildFailRequestResult("记录已被删除,请刷新页面!");
			}
			if(Strings.isNullOrEmpty(vo.getCode())){
				return buildFailRequestResult("单号不能为空");
			}
			Integer status = detail.getRecordStatus();
			/* 判断所有选中记录的状态是否为审核（1），不是就不能绑定单据； */
			if (status != 1) {
				return buildFailRequestResult("必须是审核状态下的记录才能绑定单据!");
			}
			/* 参考收付款单对单界面，单据只显示计划内事件关联的单据，勾选并填写金额后，判断输入的金额不能大于单据的总金额减单据已勾对的汇总金额， */
			BigDecimal billAmount = vo.getBillAmount();// 单据金额
			BigDecimal checkAmount = vo.getCheckAmount();// 已勾对金额
			BigDecimal inputAmount = vo.getInputAmount(); // 输入金额
			BigDecimal subtract = billAmount.subtract(checkAmount);
			if (inputAmount.abs().compareTo(subtract.abs()) > 0) {
				return buildFailRequestResult("输入的金额不能大于单据的总金额减单据已勾对的汇总金额!");
			}
			/* 更新资金计划明细表和资金计划表的单据金额和收付款金额 */
			/* 类型 1-单据金额，2-收付款金额 */
			Integer bindType = vo.getBindType();
			String capitalId = detail.getCapital().getId();
			CapitalPlan capitalPlan = capitalPlanService.findOne(capitalId);
//			Integer relationSign = vo.getRelationSign();// 关联类型
			/* 把记录插入到资金计划关联单据表 */
			/* 绑定单据时对单据金额的处理 */
			// 累加明细表单据金额
			BigDecimal billAmount2 = detail.getBillAmount() == null ? BigDecimal.ZERO : detail.getBillAmount();
			BigDecimal billAmount3 = capitalPlan.getBillAmount() == null ? BigDecimal.ZERO : capitalPlan.getBillAmount();
			// 累加明细表收付款金额
			BigDecimal paymentAmount = detail.getPaymentAmount() == null ? BigDecimal.ZERO : detail.getPaymentAmount();
			BigDecimal paymentAmount2 = capitalPlan.getPaymentAmount() == null ? BigDecimal.ZERO: capitalPlan.getPaymentAmount();

			if (bindType == 1) {// 单据金额
				/* 采购入库-11、采购发票-15、销售退货-42、销售返利-56、收货单-24、期初应付-92,作负数处理 */
//				if (relationSign == 11 || relationSign == 15 || relationSign == 42 || relationSign == 56 || relationSign == 24
//						|| relationSign == 92) {
//
//					detail.setBillAmount(billAmount2.add(inputAmount.multiply(new BigDecimal(-1))));
//					capitalPlan.setBillAmount(billAmount3.add(inputAmount.multiply(new BigDecimal(-1))));
//				}
//				/*
//				 *  费用单的费用标识为不处理的按费用金额为准，即以费用金额的正负数来确定资金计划的正负数，
//				 *  费用标识为增加应收/应付，而且关联的收支单位是供应商，作负数处理，收支单位为客户，作正数处理，
//				 *  费用标识为减少应收/应付，而且关联的收支单位是供应商，作正数处理，收支单位为客户，作负数处理，其他单据不处理； 
//				 */
//				else if (relationSign == 53) {
//					String relationId = vo.getRelationId();
//					CostBill costBill = costBillService.findOne(relationId);
//					/*费用标识 0--不处理 1--增加往来单位应收/应付款项 2--减少往来单位应收/应付款项*/
//					Integer expenseType = costBill.getExpenseType();
//					CustomerSupplierView csv = costBill.getCsv();//类型 1：客户；2：供应商
//					if(expenseType==1&&csv.getType()==2){
//						detail.setBillAmount(billAmount2.add(inputAmount.multiply(new BigDecimal(-1))));
//						capitalPlan.setBillAmount(billAmount3.add(inputAmount.multiply(new BigDecimal(-1))));
//					}
//					else if(expenseType==2&&csv.getType()==1){
//						detail.setBillAmount(billAmount2.add(inputAmount.multiply(new BigDecimal(-1))));
//						capitalPlan.setBillAmount(billAmount3.add(inputAmount.multiply(new BigDecimal(-1))));
//					}
//					else{
//						detail.setBillAmount(billAmount2.add(inputAmount));
//						capitalPlan.setBillAmount(billAmount3.add(inputAmount));
//					}
//				} else {/* 采购退货、采购返利、销售出货、销售发票、期初应收作正数处理, */
//					detail.setBillAmount(billAmount2.add(inputAmount));
//					capitalPlan.setBillAmount(billAmount3.add(inputAmount));
//				}
				detail.setBillAmount(billAmount2.add(inputAmount));
				capitalPlan.setBillAmount(billAmount3.add(inputAmount));

			} else {// 收付款金额
				/*绑定单据时对收付款金额的处理，收款单*1处理，付款单*-1处理，其他单据不处理；*/
				// 累加明细表收付款金额
//				if(relationSign==52){
//					detail.setPaymentAmount(paymentAmount.add(inputAmount.multiply(new BigDecimal(-1))));
//					capitalPlan.setPaymentAmount(paymentAmount2.add(inputAmount.multiply(new BigDecimal(-1))));
//				}else{
//					detail.setPaymentAmount(paymentAmount.add(inputAmount));
//					capitalPlan.setPaymentAmount(paymentAmount2.add(inputAmount));
//				}
				detail.setPaymentAmount(paymentAmount.add(inputAmount));
				capitalPlan.setPaymentAmount(paymentAmount2.add(inputAmount));

			}
			save(detail);
			capitalPlanService.save(capitalPlan);
			CapitalPlanBillVo planBillVo = new CapitalPlanBillVo();
			planBillVo.setBillAmount(billAmount);
			planBillVo.setBindAmount(inputAmount);
			planBillVo.setBindType(bindType);
			planBillVo.setDetailId(detail.getId());
			planBillVo.setRelationSign(vo.getRelationSign());
			planBillVo.setRelationId(vo.getRelationId());
			planBillVo.setCode(vo.getCode());
			planBillVo.setCsvId(vo.getCsvId());
			billService.save(planBillVo);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("绑定单据出错!");
		}
		return buildSuccessRequestResult();
	}

	/**
	 * 查询未绑定单据
	 * 
	 * @param vo
	 *            绑定单据VO
	 * @param paramater
	 *            分页对象
	 * @return
	 */
	public PageJson uncheckedList(BindingBillVo vo, PageParamater paramater) {
		Integer billType = vo.getBillType() == null ? 0 : vo.getBillType();
//		BillType type = payCheckService.checkBillType(billType);
		if (billType == WarehouseBuilderCodeHelper.cgrk||billType == WarehouseBuilderCodeHelper.cgfp||
			billType == WarehouseBuilderCodeHelper.xsth||
			billType == WarehouseBuilderCodeHelper.qcyf||billType == WarehouseBuilderCodeHelper.cgth||
			billType == WarehouseBuilderCodeHelper.xsch||billType == WarehouseBuilderCodeHelper.xsfp||
			billType == WarehouseBuilderCodeHelper.qcys||billType == WarehouseBuilderCodeHelper.shd) {// 仓储单据的表
			/* 采购入库-11、采购发票-15、销售退货-42、销售返利-56、收货单-24、期初应付-92,作负数处理 */
			String sql = "select b.FID as fid, b.FBILL_DATE as billDate,b.FCODE as code, ";
			sql += " CASE ISNULL(b.FCUSTOMER_ID) WHEN 1  THEN b.FSUPPLIER_ID ELSE b.FCUSTOMER_ID END as unitId,";
			sql += " CASE ISNULL(b.FCUSTOMER_ID) WHEN 1  THEN d.fname ELSE e.fname END as unitName,";
			sql += " (CASE when b.FBILL_TYPE=92 or b.FBILL_TYPE=11  ";
			sql += " or b.FBILL_TYPE=42 or b.FBILL_TYPE=15 or b.FBILL_TYPE=56 ";
			sql += " THEN b.FTOTAL_AMOUNT*-1 when b.FBILL_TYPE=24 then (b.FTOTAL_AMOUNT-b.FDEDUCTION_AMOUNT)*-1 ELSE b.FTOTAL_AMOUNT end )  as amount, ";
			sql += " CASE ISNULL((select SUM(FBIND_AMOUNT) as checkAmount from tcapital_plan_bill  c WHERE b.FID=c.FRELATION_ID)) WHEN 1  THEN 0 ";
			sql += " ELSE (select SUM(FBIND_AMOUNT) as checkAmount from tcapital_plan_bill c WHERE b.FID=c.FRELATION_ID) END as checkAmount  ";
			sql += " from tflow_task_bill a ";
			sql += " LEFT JOIN tsb_warehouse_bill b ON a.FBILL_ID=b.FID";
			sql += " LEFT JOIN tbd_supplier d on d.fid=b.FSUPPLIER_ID";
			sql += " LEFT JOIN tbd_customer e on e.fid=b.FCUSTOMER_ID";
			sql += " where a.FBILL_SIGN=" + billType;
			sql += " and a.FACC_ID='" + SecurityUtil.getFiscalAccountId() + "'";
			sql += " and a.FPLAN_ID='" + vo.getPlanId()+ "'";
			sql += " LIMIT ?,?";
			List<Map<String, Object>> map = jdbcTemplate.queryForList(sql,
					new Object[] { paramater.getStart(), paramater.getStart() + paramater.getRows() });
			List<BindingBillVo> list = BindingBillVo.toObject(map);
			List<BindingBillVo> list2 = Lists.newArrayList();
			for (BindingBillVo billVo : list) {
				BigDecimal amount = billVo.getAmount();
				BigDecimal checkAmount = billVo.getCheckAmount();
				//判断单据是否勾对完成，屏蔽已完成记录
				if(amount!=null){
					if(amount.compareTo(checkAmount)!=0&&BigDecimal.ZERO.compareTo(amount)!=0){
						list2.add(billVo);
					}
				}

			}
			return getPageJson(list2, list2.size());

		} else if (billType == WarehouseBuilderCodeHelper.skd||billType == WarehouseBuilderCodeHelper.fkd||
				   billType == WarehouseBuilderCodeHelper.cgfld||billType == WarehouseBuilderCodeHelper.xsfld) {// 收付款单的表
			/*绑定单据时对收付款金额的处理，收款单*1处理，付款单,销售返利*-1处理，其他单据不处理；*/
			String sql = "select b.FID as fid, b.FBILL_DATE as billDate,b.FCODE as code, ";
			if (billType == WarehouseBuilderCodeHelper.fkd||billType == WarehouseBuilderCodeHelper.xsfld) {
				sql += " d.fid as unitId,";
				sql += " d.fname as unitName,";
//				sql += " CASE ISNULL(b.FCUSTOMER_ID) WHEN 1  THEN NULL ELSE b.FCUSTOMER_ID END as unitId, ";
//				sql += " CASE ISNULL(b.FCUSTOMER_ID) WHEN 1  THEN NULL ELSE d.fname END as unitName, ";
				sql += " b.FAMOUNT*-1 as amount, ";
				sql += "   CASE ISNULL((select SUM(FBIND_AMOUNT) as checkAmount from tcapital_plan_bill c WHERE b.FID=c.FRELATION_ID)) WHEN 1  THEN 0 ";
				sql += "   ELSE (select SUM(FBIND_AMOUNT) as checkAmount from tcapital_plan_bill c WHERE b.FID=c.FRELATION_ID) END as checkAmount  ";
				sql += "  from tflow_task_bill a ";
				sql += " LEFT JOIN tsb_payment_bill b ON a.FBILL_ID=b.FID ";
//				sql += " LEFT JOIN tbd_customer d on d.fid=b.FCUSTOMER_ID ";
				if(billType == WarehouseBuilderCodeHelper.fkd){
					sql += " LEFT JOIN tbd_customer_supplier_view d on d.fid=b.FSUPPLIER_ID";
				}else{
					sql += " LEFT JOIN tbd_customer_supplier_view d on d.fid=b.FCUSTOMER_ID";
				}

				sql += " where a.FBILL_SIGN=" + billType;
			} else {
				sql += " d.fid as unitId,";
				sql += " d.fname as unitName,";
//				sql += " CASE ISNULL(b.FSUPPLIER_ID) WHEN 1  THEN NULL ELSE b.FSUPPLIER_ID END as unitId, ";
//				sql += " CASE ISNULL(b.FSUPPLIER_ID) WHEN 1  THEN NULL ELSE d.fname END as unitName, ";
				sql += " b.FAMOUNT as amount, ";
				sql += " CASE ISNULL((select SUM(FBIND_AMOUNT) as checkAmount from tcapital_plan_bill c WHERE b.FID=c.FRELATION_ID)) WHEN 1  THEN 0 ";
				sql += " ELSE (select SUM(FBIND_AMOUNT) as checkAmount from tcapital_plan_bill c WHERE b.FID=c.FRELATION_ID) END as checkAmount  ";
				sql += " from tflow_task_bill a ";
				sql += " LEFT JOIN tsb_payment_bill b ON a.FBILL_ID=b.FID ";
//				sql += " LEFT JOIN tbd_supplier d on d.fid=b.FSUPPLIER_ID ";
				if(billType == WarehouseBuilderCodeHelper.skd){
					sql += " LEFT JOIN tbd_customer_supplier_view d on d.fid=b.FCUSTOMER_ID";
				}else{
					sql += " LEFT JOIN tbd_customer_supplier_view d on d.fid=b.FCUSTOMER_ID";
				}
				sql += " where a.FBILL_SIGN=" + billType;
			}
			sql += " and a.FACC_ID='" + SecurityUtil.getFiscalAccountId() + "'";
			sql += " and a.FPLAN_ID='" + vo.getPlanId()+ "'";
			sql += " LIMIT ?,?";
			List<Map<String, Object>> map = jdbcTemplate.queryForList(sql,
					new Object[] { paramater.getStart(), paramater.getStart() + paramater.getRows() });
			List<BindingBillVo> list = BindingBillVo.toObject(map);
			List<BindingBillVo> list2 = Lists.newArrayList();
			for (BindingBillVo billVo : list) {
				BigDecimal amount = billVo.getAmount();
				BigDecimal checkAmount = billVo.getCheckAmount();
				//判断单据是否勾对完成，屏蔽已完成记录
				if(amount!=null){
					if(amount.compareTo(checkAmount)!=0&&BigDecimal.ZERO.compareTo(amount)!=0){
						list2.add(billVo);
					}
				}
			}
			return getPageJson(list2, list2.size());
		} else if (billType == WarehouseBuilderCodeHelper.fyd) {// 费用单的表
			/*
			 * 费用单的费用标识为不处理的按费用金额为准，即以费用金额的正负数来确定资金计划的正负数，
			 * 费用标识为增加应收/应付，而且关联的收支单位是供应商，作负数处理，收支单位为客户，
			 * 作正数处理，费用标识为减少应收/应付，而且关联的收支单位是供应商，作正数处理，收支单位为客户，作负数处理，其他单据不处理；
			 */
			String sql = "select b.FID as fid,";
			sql += " b.fdate as billDate,";
			sql += " b.FCODE as code,";
			sql += " CASE ISNULL(b.FCUSTOMER_ID) WHEN 1  THEN NULL ELSE b.FCUSTOMER_ID END as unitId,";
			sql += " CASE ISNULL(b.FCUSTOMER_ID) WHEN 1  THEN NULL ELSE d.fname END as unitName,";
			/*
			 * 费用标识=0（不处理）,费用金额*（-1）
			 * 费用标识=1（增加），收支单位=供应商（2）,费用金额*（-1）
			 * 费用标识=2（减少），收支单位=客户（1）,费用金额*（-1）
			 */
			sql += " (CASE WHEN b.FEXPENSE_TYPE=0 or(b.FEXPENSE_TYPE=1 and d.ftype=2) or(b.FEXPENSE_TYPE=2 and d.ftype=1) ";
			sql += " then b.FFREE_AMOUNT*-1 else b.FFREE_AMOUNT end) as amount, ";
			sql += " CASE ISNULL((select SUM(FBIND_AMOUNT) as checkAmount from tcapital_plan_bill c WHERE b.FID=c.FRELATION_ID)) WHEN 1  THEN 0 ";
			sql += " ELSE (select SUM(FBIND_AMOUNT) as checkAmount from tcapital_plan_bill c WHERE b.FID=c.FRELATION_ID) END as checkAmount  ";
			sql += " from tflow_task_bill a ";
			sql += " LEFT JOIN tsb_cost_bill b ON a.FBILL_ID=b.FID";
			sql += " LEFT JOIN tbd_customer_supplier_view d on d.fid=b.FCUSTOMER_ID";
			sql += " where a.FBILL_SIGN=" + billType;
			sql += " and a.FACC_ID='" + SecurityUtil.getFiscalAccountId() + "'";
			sql += " and a.FPLAN_ID='" + vo.getPlanId()+ "'";
			sql += " LIMIT ?,?";
			List<Map<String, Object>> map = jdbcTemplate.queryForList(sql,
					new Object[] { paramater.getStart(), paramater.getStart() + paramater.getRows() });
			List<BindingBillVo> list = BindingBillVo.toObject(map);
			List<BindingBillVo> list2 = Lists.newArrayList();
			for (BindingBillVo billVo : list) {
				BigDecimal amount = billVo.getAmount();
				BigDecimal checkAmount = billVo.getCheckAmount();
				//判断单据是否勾对完成，屏蔽已完成记录
				if(amount!=null){
					if(amount.compareTo(checkAmount)!=0&&BigDecimal.ZERO.compareTo(amount)!=0){
						list2.add(billVo);
					}
				}
			}
			return getPageJson(list2, list2.size());
		} else {
			throw new RuntimeException("unsupport billType!");
		}
	}
	
	
	/**
	 * 查询绑定单据（根据明细id查询【detailId】）
	 * @param vo		
	 * @param paramater
	 * @return
	 */
	public Page<CapitalPlanBillVo> checkedList(CapitalPlanBillVo vo, PageParamater paramater) {
		Page<CapitalPlanBillVo> query = billService.query(vo, paramater);
		return query;
	}
	/**
	 * 根据关联id查询资金计划
	 * @param relationId	关联id
	 * @return
	 */
	public List<CapitalPlanDetail> queryByRelation(String relationId){
		List<CapitalPlanDetail> entity = repository.queryByRelation(relationId);
		return entity;
	}
	
	/**
	 * 删除明细记录，如果明细只有一条则删除主表记录
	 */
	@Transactional
	public RequestResult delete(String id){
		try {
			CapitalPlanDetail detail = findOne(id);
			if(detail==null){
				return buildFailRequestResult("该记录已不存在，请刷新页面!");
			}
			CapitalPlan capital = detail.getCapital();
			if(capital!=null){
				List<CapitalPlanDetail> list = queryByCapitalId(capital.getId());
				if(list.size()==1){
					repository.delete(id);
					capitalPlanService.del(capital.getId(),0);
				}else{
					BigDecimal billAmount2 = detail.getBillAmount();
					repository.delete(id);
					//更新主表单据金额
					BigDecimal billAmount = capital.getBillAmount();
					capital.setBillAmount(billAmount.subtract(billAmount2));
					capitalPlanService.save(capital);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("删除资金计划明细记录出错!");
		}
		return buildSuccessRequestResult();
	}
	/**
	 * 根据主表关联id获取资金计划从表记录
	 * @param relationId 
	 * @return
	 */
	public List<CapitalPlanDetailVo> queryByPlantId(String relationId ){
		CapitalPlan capitalPlan = capitalPlanService.queryByRelation(relationId);
		if(capitalPlan!=null){
			List<CapitalPlanDetail> details = repository.queryByCapitalId(capitalPlan.getId());
			return getVos(details);
		}
		return null;
	}
	/**
	 * 明细表修改为完成状态
	 * @param id				主键
	 * @param recordStatus		完成状态标识
	 * @param updateTime		修改时间
	 * @return
	 */
	public RequestResult detailComplete(String id, int recordStatus,String updateTime) {
		try {
			CapitalPlanDetail entity = repository.findOne(id);
			if (entity == null) {
				return buildFailRequestResult("该记录已被删除,请刷新页面");
			}
			if (entity.getUpdateTime().compareTo(DateUtils.getDateFromString(updateTime)) != 0) {
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
			// 完成：判断状态是否为审核（1），不是就不能完成；
			if (recordStatus == 3) {
				entity.setRecordStatus(recordStatus);
				entity.setComplete(SecurityUtil.getCurrentUser());
				entity.setCompleteDate(new Date());
				entity.setUpdateTime(new Date());
				repository.save(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("计划明细状态修改出错!");
		}
		return buildSuccessRequestResult();
		
	}
}
