package cn.fooltech.fool_ops.eureka.rateService.service;

import cn.fooltech.fool_ops.eureka.rateService.dao.SaleOrderDao;
import cn.fooltech.fool_ops.eureka.rateService.dao.SaleOutDao;
import cn.fooltech.fool_ops.eureka.rateService.vo.*;
import cn.fooltech.fool_ops.rate.tools.ArithUtil;
import cn.fooltech.fool_ops.rate.tools.Calculate;
import cn.fooltech.fool_ops.rate.tools.EveryTradeBean;
import cn.fooltech.fool_ops.rate.tools.RateBean;
import com.google.common.collect.Maps;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>销售出库单</p>
 *
 */

@Service
public class SaleOutService {

    private final static int COUNT_NOT = 0;  // 不统计数量
    private final static int COUNT_YES = 1;  // 统计数量
    private final static int DEFAULT_OFFSET = 0;  // 页数默认从0开始
    private final static int DEFAULT_PAGE_SIZE = 10;  // 页数大小默认10

    @Autowired
    private SaleOutDao saleOutDao;


    /**
     * 销售出库单分析
     * @param startDate
     * @param endDate
     * @param customerId
     * @param saleId
     * @param saleCode
     * @param orgId
     * @param accountId
     * @param page
     * @param rows
     * @return
     */
    public Page<SaleOutVo> pageSaleOut(String startDate, String endDate,
                                       String customerId,
                                       String saleId,
                                       String saleCode,
                                       String orgId,
                                       String accountId,
                                       Double dayFundLoseRate,
                                       Integer page,
                                       Integer rows){
        // 分页
        int first = (page - 1) * rows;

        Long count = saleOutDao.countSaleOutAnalyze(startDate, endDate, customerId, saleId,
                saleCode, orgId, accountId);

        Pageable pageRequest = new PageRequest(page-1, rows);

        if(count!=null && count>0){
            List<SaleOutVo> datas = saleOutDao.saleOutAnalyze(startDate, endDate, customerId, saleId,
                    saleCode, orgId, accountId, first, rows);

            //计算预计收益率、参考收益率、实际收益率、当前预计收益率

            if(dayFundLoseRate==null)dayFundLoseRate = 0d;
            for(SaleOutVo saleOutVo:datas){
                List<TradeRecordVo> trades = saleOutDao.queryTradeRecord(saleOutVo.getSaleId());

                RateBean real = transferRealRateBean(dayFundLoseRate, trades);
                RateBean current = transferCurrentRateBean(dayFundLoseRate, trades);

                saleOutVo.setEffectiveYieldRate(new BigDecimal(ArithUtil.round(real.getCycleRate(),2)));
                saleOutVo.setCurrentYieldRate(new BigDecimal(ArithUtil.round(current.getCycleRate(),2)));
            }

            return new PageImpl<SaleOutVo>(datas, pageRequest, count);
        }else{
            return new PageImpl<SaleOutVo>(Collections.emptyList(), pageRequest, 0);
        }
    }

    /**
     * 获取当前收益率bean
     * @return
     */
    private RateBean transferCurrentRateBean(double dayFundLostRate, List<TradeRecordVo> trades){
        RateBean rateBean = new RateBean();
        rateBean.setDayFundLostRate(dayFundLostRate);

        for(TradeRecordVo trade:trades){

            EveryTradeBean etb = new EveryTradeBean();
            etb.setTime(trade.getBillDate());
            etb.setMoney(trade.getAmount().doubleValue());
            etb.setAmountType(trade.getAmountType());

            //收入
            if(trade.getPaymentType()==TradeRecordVo.PAYMENT_TYPE_INCOME){
                rateBean.getIncomeList().add(etb);
            }else{
                //支出
                rateBean.getExpendList().add(etb);
            }
        }

        Calculate.initRateBean(rateBean);
        return rateBean;
    }

    /**
     * 获取实际收益率bean
     * @return
     */
    private RateBean transferRealRateBean(double dayFundLostRate, List<TradeRecordVo> trades){
        RateBean rateBean = new RateBean();
        rateBean.setDayFundLostRate(dayFundLostRate);

        for(TradeRecordVo trade:trades){

            EveryTradeBean etb = new EveryTradeBean();
            etb.setTime(trade.getBillDate());
            etb.setMoney(trade.getAmount().doubleValue());
            etb.setAmountType(trade.getAmountType());

            //收入
            if(trade.getPaymentType()==TradeRecordVo.PAYMENT_TYPE_INCOME){
                rateBean.getIncomeList().add(etb);
            }else{
                //支出
                rateBean.getExpendList().add(etb);
            }
        }

        Calculate.initRealRateBean(rateBean);
        return rateBean;
    }

    /**
     * 销售出库单关联分页
     * @param saleOutId
     * @param page
     * @param rows
     * @return
     */
    public Page<SaleOutRelationVo> pageSaleOutRelation(String saleOutId,
                                                       Integer page,
                                                       Integer rows){
        // 分页
        int first = (page - 1) * rows;

        Long count = saleOutDao.countSaleOutRelationAnalyze(saleOutId);

        Pageable pageRequest = new PageRequest(page-1, rows);

        if(count!=null && count>0){
            List<SaleOutRelationVo> datas = saleOutDao.saleOutRelationAnalyze(saleOutId, first, rows);
            return new PageImpl<SaleOutRelationVo>(datas, pageRequest, count);
        }else{
            return new PageImpl<SaleOutRelationVo>(Collections.emptyList(), pageRequest, 0);
        }

    }


    /**
     * 销售出库单明细分析
     * @param startDate
     * @param endDate
     * @param customerId
     * @param saleId
     * @param saleCode
     * @param orgId
     * @param accountId
     * @param page
     * @param rows
     * @return
     */
    public Page<SaleOutDetailVo> pageSaleOutDetail(String startDate, String endDate,
                                                   String customerId,
                                                   String saleId,
                                                   String saleCode,
                                                   String goodId,
                                                   String goodSpecId,
                                                   String orgId,
                                                   String accountId,
                                                   Integer page,
                                                   Integer rows){
        // 分页
        int first = (page - 1) * rows;

        Long count = saleOutDao.countSaleOutAnalyze(startDate, endDate, customerId, saleId,
                saleCode, orgId, accountId);

        Pageable pageRequest = new PageRequest(page-1, rows);

        if(count!=null && count>0){
            List<SaleOutDetailVo> datas = saleOutDao.saleOutDetailAnalyze(startDate, endDate, customerId, saleId,
                    saleCode, goodId, goodSpecId, orgId, accountId, first, rows);
            return new PageImpl<SaleOutDetailVo>(datas, pageRequest, count);
        }else{
            return new PageImpl<SaleOutDetailVo>(Collections.emptyList(), pageRequest, 0);
        }

    }
}
