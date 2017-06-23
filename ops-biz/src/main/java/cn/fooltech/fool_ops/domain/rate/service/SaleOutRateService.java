package cn.fooltech.fool_ops.domain.rate.service;

import cn.fooltech.fool_ops.domain.base.service.AbstractBaseService;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanDetail;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanDetailService;
import cn.fooltech.fool_ops.domain.rate.entity.SaleOutRate;
import cn.fooltech.fool_ops.domain.rate.repository.SaleOutRateRepository;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillDetailRepository;
import cn.fooltech.fool_ops.rate.tools.ArithUtil;
import cn.fooltech.fool_ops.rate.tools.Calculate;
import cn.fooltech.fool_ops.rate.tools.EveryTradeBean;
import cn.fooltech.fool_ops.rate.tools.RateBean;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 销售出库单收益率Service
 */
@Service
public class SaleOutRateService extends AbstractBaseService<SaleOutRate, String>{

    @Autowired
    private SaleOutRateRepository repository;

    @Autowired
    private WarehouseBillDetailRepository billDetailRepository;

    @Autowired
    private LoanRateService loanRateService;

    @Autowired
    private CapitalPlanDetailService capitalPlanDetailService;

    @Override
    public CrudRepository<SaleOutRate, String> getRepository() {
        return repository;
    }

    /**
     * 计算预计收益率，参考收益率
     * @param bill
     * @return
     */
    @Transactional
    public void computeRate(WarehouseBill bill, List<WarehouseBillDetail> details){

        String billId = bill.getFid();
        BigDecimal costAmount = sumCostAmount(details);

        EveryTradeBean etb1 = new EveryTradeBean();
        etb1.setAmountType(EveryTradeBean.AMOUNT_TYPE_EXPECT);
        etb1.setMoney(costAmount.doubleValue());
        etb1.setTime(bill.getBillDate());

        double dayFunLostRate = loanRateService.getBankRate(SecurityUtil.getCurrentOrgId());

        RateBean expect = new RateBean();
        expect.setDayFundLostRate(dayFunLostRate);
        expect.setExpendList(Lists.newArrayList(etb1));

        List<CapitalPlanDetail> cpds = capitalPlanDetailService.queryByRelation(billId);

        if(cpds.size()==0)return;
        for(CapitalPlanDetail cpd:cpds){
            EveryTradeBean etb2 = new EveryTradeBean();
            etb2.setAmountType(EveryTradeBean.AMOUNT_TYPE_EXPECT);
            etb2.setMoney(cpd.getBillAmount().doubleValue());
            etb2.setTime(cpd.getOrgPaymentDate());
            expect.getIncomeList().add(etb2);
        }

        SaleOutRate saleOutRate = repository.findByBillId(billId);

        if(saleOutRate==null){
            saleOutRate = new SaleOutRate();
        }

        Calculate.initRateBean(expect);

        saleOutRate.setSaleOutId(billId);
        saleOutRate.setUpdateTime(new Date());
        saleOutRate.setEstimateRate(new BigDecimal(ArithUtil.round(
                expect.getCycleRate(), 4)));
        saleOutRate.setFiscalAccount(bill.getFiscalAccount());
        saleOutRate.setOrg(bill.getOrg());
        saleOutRate.setMarketRate(new BigDecimal(ArithUtil.round(
                expect.getMarketRate(), 4)));

        repository.save(saleOutRate);
    }

    /**
     * 合计成本金额
     * @param details
     * @return
     */
    private BigDecimal sumCostAmount(List<WarehouseBillDetail> details){
        BigDecimal sum = BigDecimal.ZERO;
        for(WarehouseBillDetail detail:details){
            sum = NumberUtil.add(sum, detail.getCostAmount());
        }
        return sum;
    }
}
