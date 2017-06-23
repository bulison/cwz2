package cn.fooltech.fool_ops.domain.basedata.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.BillRule;
import cn.fooltech.fool_ops.domain.basedata.entity.GroundPrice;
import cn.fooltech.fool_ops.domain.basedata.repository.BillRuleRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GroundPriceRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.PeerQuoteRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.TransportPriceRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.BillRuleVo;
import cn.fooltech.fool_ops.domain.capital.repository.CapitalPlanBillRepository;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanBillService;
import cn.fooltech.fool_ops.domain.cost.repository.CostBillRepository;
import cn.fooltech.fool_ops.domain.payment.repository.PaymentBillRepository;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillRepository;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * <p>单据规则服务类</p>
 *
 * @author xjh
 * @date 2016-10-26
 */
@Service
public class BillRuleService extends BaseService<BillRule, BillRuleVo, String> {

    private static final String LAZY_CODE_INIT = "1";
    private static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";

    //仓储表
    public final static List<Integer> warehouseTypes = Lists.newArrayList(

            WarehouseBuilderCodeHelper.qckc,
            WarehouseBuilderCodeHelper.qcyf,
            WarehouseBuilderCodeHelper.qcys,
            WarehouseBuilderCodeHelper.cgdd,
            WarehouseBuilderCodeHelper.cgrk,
            WarehouseBuilderCodeHelper.cgth,
            WarehouseBuilderCodeHelper.cgxjd,
            WarehouseBuilderCodeHelper.cgsqd,
            WarehouseBuilderCodeHelper.cgfp,
            WarehouseBuilderCodeHelper.pdd,
            WarehouseBuilderCodeHelper.dcd,
            WarehouseBuilderCodeHelper.bsd,
            WarehouseBuilderCodeHelper.scll,
            WarehouseBuilderCodeHelper.sctl,
            WarehouseBuilderCodeHelper.cprk,
            WarehouseBuilderCodeHelper.cptk,
            WarehouseBuilderCodeHelper.xsdd,
            WarehouseBuilderCodeHelper.xsch,
            WarehouseBuilderCodeHelper.xsth,
            WarehouseBuilderCodeHelper.xsbjd,
            WarehouseBuilderCodeHelper.xsfp,
            WarehouseBuilderCodeHelper.xsfp,
            WarehouseBuilderCodeHelper.xsfp,
            WarehouseBuilderCodeHelper.scjhd,
            WarehouseBuilderCodeHelper.fhd,
            WarehouseBuilderCodeHelper.shd
    );

    //费用单
    public final static List<Integer> costTypes = Lists.newArrayList(
            WarehouseBuilderCodeHelper.fyd
    );

    //收付款
    public final static List<Integer> payTypes = Lists.newArrayList(
            WarehouseBuilderCodeHelper.skd,
            WarehouseBuilderCodeHelper.fkd,
            WarehouseBuilderCodeHelper.cgfld,
            WarehouseBuilderCodeHelper.xsfld,
            WarehouseBuilderCodeHelper.fysqd
    );

    //同行报价单
    public final static List<Integer> peerQuoteTypes = Lists.newArrayList(
            WarehouseBuilderCodeHelper.thbjd
    );

    //场地费报价单
    public final static List<Integer> groundPriceTypes = Lists.newArrayList(
            WarehouseBuilderCodeHelper.thbjd
    );

    //运输费报价单
    public final static List<Integer> transportPriceTypes = Lists.newArrayList(
            WarehouseBuilderCodeHelper.thbjd
    );

    //资金计划
    public final static List<Integer> captianPlanTypes = Lists.newArrayList(
            WarehouseBuilderCodeHelper.zjjh
    );



    private Logger logger = LoggerFactory.getLogger(BillRuleService.class);
    @Autowired
    private BillRuleRepository billRuleRepo;

    @Autowired
    private WarehouseBillRepository billRepo;

    @Autowired
    private CostBillRepository costbillRepo;

    @Autowired
    private PaymentBillRepository paybillRepo;

    @Autowired
    private PeerQuoteRepository peerQuoteRepo;

    @Autowired
    private TransportPriceRepository transportPriceRepo;

    @Autowired
    private CapitalPlanBillRepository cpbillRepo;

    @Autowired
    private GroundPriceRepository gpRepo;

    /**
     * 查询规则列表
     *
     * @param pageParamater
     * @return
     */
    public Page<BillRuleVo> query(PageParamater pageParamater) {
        Sort sort = new Sort(Direction.ASC, "billType");
        String orgId = SecurityUtil.getCurrentOrgId();
        PageRequest pageRequest = getPageRequest(pageParamater, sort);
        return getPageVos(billRuleRepo.findPageByOrgId(orgId, pageRequest), pageRequest);
    }

    @Override
    public BillRuleVo getVo(BillRule entity) {
        BillRuleVo vo = VoFactory.createValue(BillRuleVo.class, entity);
        vo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
        return vo;
    }

    /**
     * 编辑保存(没有新增)
     *
     * @param vo
     * @return
     */
    @Transactional
    public RequestResult save(BillRuleVo vo) {
        String fid = vo.getFid();
        Integer ruleType = vo.getRuleType(); //规则类型
        String prefix = vo.getPrefix(); //前缀
        String date = vo.getDate(); //日期格式
        Integer serial = vo.getSerial(); //序号长度
        ruleType = ruleType == null ? BillRule.RULE_TYPE_SYS : ruleType;

        RequestResult result = checkByRule(vo);
        if (result.getReturnCode() == RequestResult.RETURN_FAILURE) {
            return result;
        }

        BillRule entity = billRuleRepo.findOne(fid);
        if (entity == null) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
        }
        entity.setRuleType(ruleType);
        if (ruleType == BillRule.RULE_TYPE_SYS) {
            entity.setDate(date);
            entity.setSerial(serial);
            entity.setPrefix(prefix == null ? "" : prefix);
        }
        updateNewLazyCode(entity);
        billRuleRepo.save(entity);
        return result;
    }

    /**
     * 更新到新的现时单号
     *
     * @param entity
     */
    public void updateNewLazyCode(BillRule entity) {
        String newLazyCode = getNewLazyCode(entity.getLazyCode(), entity.getSerial()); //创建新的现时单号
        entity.setLazyCode(newLazyCode);
        entity.setUpdateTime(new Date());
        billRuleRepo.save(entity);
    }

    /**
     * 获取新的现时单号(序号)
     *
     * @param lazyCode 现时单号
     * @param length   序号长度
     * @return
     */
    private String getNewLazyCode(String lazyCode, int length) {
        Integer newCode = Integer.parseInt(lazyCode) + 1;
        return formatLazyCode(newCode.toString(), length);
    }

    /**
     * 格式化现时单号
     *
     * @param lazyCode 现时单号
     * @param length   序号长度，不足前面补0
     * @return
     */
    private String formatLazyCode(String lazyCode, int length) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length - lazyCode.length(); i++) {
            buffer.append("0");
        }
        buffer.append(lazyCode);
        return buffer.toString();
    }

    /**
     * 重置所有延时单号
     *
     * @param dateFormats
     */
    @Transactional
    public boolean updateAllLazyCode(List<String> dateFormats) {

        List<BillRule> datas = billRuleRepo.findByDateIn(dateFormats);

        for (BillRule rule : datas) {
            rule.setLazyCode(formatLazyCode(LAZY_CODE_INIT, rule.getSerial()));
            rule.setUpdateTime(new Date());
            billRuleRepo.save(rule);
        }
        return true;
    }

    /**
     * 根据规则校验
     *
     * @param vo
     * @return
     */
    public RequestResult checkByRule(BillRuleVo vo) {
        if (!canUpdate(vo.getBillType())) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "不允许修改，该规则已被使用!");
        }
        if (!checkDataRealTime(vo)) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "页面数据失效，请重新刷新页面!");
        }
        if (vo.getRuleType() == BillRule.RULE_TYPE_SYS) {
            if (StringUtils.isBlank(vo.getDate())) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "年月日格式不能为空!");
            }
            if (StringUtils.isBlank(vo.getPrefix())) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "前缀不能为空!");
            }
            if (vo.getSerial() == null) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "序列号长度不能为空!");
            }
            if (vo.getSerial() <= 0) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "序列号长度必须大于零!");
            }
        }
        return new RequestResult();
    }

    /**
     * 检测是否可以编辑<br>
     * 修改判断是否有单据，如果有则不能编辑<br>
     *
     * @param billType 单据类型
     * @return
     */
    public boolean canUpdate(int billType) {
        Long count = 0L;

        String orgId = SecurityUtil.getCurrentOrgId();

        if(warehouseTypes.contains(billType)){
            count = billRepo.countByBillType(orgId, billType);
        }else if(costTypes.contains(billType)){
            count = costbillRepo.countByBillType(orgId);
        }else if(payTypes.contains(billType)){
            count = paybillRepo.countByBillType(orgId, billType);
        }else if(peerQuoteTypes.contains(billType)){
            count = peerQuoteRepo.countByBillType(orgId);
        }else if(groundPriceTypes.contains(billType)){
            count = gpRepo.countByBillType(orgId);
        }else if(transportPriceTypes.contains(billType)){
            count = transportPriceRepo.countByBillType(orgId);
        }else if(captianPlanTypes.contains(billType)){
            count = cpbillRepo.countByBillType(orgId);
        }

        if (count != null && count > 0) {
            return false;
        }
        return true;
    }

    /**
     * 获取新单号
     *
     * @param billType 单据类型
     * @return
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public String getNewCode(int billType) {
        return createFullCode(SecurityUtil.getCurrentOrgId(), billType);
    }

    /**
     * 获取某个机构下新的完整单号
     *
     * @param orgId    机构ID
     * @param billType 单据类型
     */
    public String createFullCode(String orgId, int billType) {
        BillRule rule = billRuleRepo.findTopByBillType(orgId, billType);
        if (rule == null) {
            logger.error("单据单号生成规则不存在!");
            throw new RuntimeException("单据单号生成规则不存在!");
        }

        if (rule.getRuleType() == BillRule.RULE_TYPE_USER) {
            return "";
        }
        String fullCode = createFullCode(rule); //完整的单号
        String newLazyCode = getNewLazyCode(rule.getLazyCode(), rule.getSerial()); //创建新的现时单号
        rule.setLazyCode(newLazyCode);
        rule.setUpdateTime(new Date());
        billRuleRepo.save(rule);
        return fullCode;
    }

    /**
     * 根据某个规则类型，生成新的完整单号
     *
     * @param rule
     * @return
     */
    private String createFullCode(BillRule rule) {
        String dateFormat = rule.getDate();
        if (StringUtils.isBlank(dateFormat)) {
            dateFormat = DEFAULT_DATE_FORMAT;
        }
        String date = DateUtilTools.date2String(new Date(), dateFormat);
        return rule.getPrefix() + date + rule.getLazyCode();
    }

    /**
     * 更新操作时，校验数据的实时性
     *
     * @param vo 主键、更新时间
     * @return true 有效  false 无效
     */
    private boolean checkDataRealTime(BillRuleVo vo) {
        BillRule entity = billRuleRepo.findOne(vo.getFid());
        Date formDate = DateUtilTools.string2Time(vo.getUpdateTime());
        int num = formDate.compareTo(entity.getUpdateTime());
        return num == 0;
    }

    /**
     * 根据类型查找单据规则
     *
     * @param types
     * @return
     */
    public List<BillRuleVo> findByBillTypes(List<Integer> types) {
        String orgId = SecurityUtil.getCurrentOrgId();
        if (CollectionUtils.isNotEmpty(types)) {
            return getVos(billRuleRepo.findByBillTypes(orgId, types));
        }
        return getVos(billRuleRepo.findByBillTypes(orgId));
    }

    @Override
    public CrudRepository<BillRule, String> getRepository() {
        return billRuleRepo;
    }
}
