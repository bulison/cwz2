package cn.fooltech.fool_ops.domain.warehouse.service;

import java.util.List;

import cn.fooltech.fool_ops.domain.warehouse.repository.SaleOrderRepository;
import cn.fooltech.fool_ops.domain.warehouse.vo.SaleOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>销售订单分析与销售订单明细分析<p>
 */

@Service
public class SaleOrderService {

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    /**
     * 查询销售订单列表
     * @param vo SaleOrderVo
     * @param paramater PageParamater
     */
    public List<SaleOrderVo> getSaleOrderList(SaleOrderVo vo, PageParamater paramater) {
        // 机构ID和账套ID
        String orgId = SecurityUtil.getCurrentOrgId();
        String accId = SecurityUtil.getFiscalAccountId();

        // 分页
        int pageSize = paramater.getRows();
        int pageNo = paramater.getPage();
        int first = (pageNo - 1) * pageSize;

        List<Object[]> list = saleOrderRepository.querySaleOrderList(orgId, accId, vo.getStartDate(),
                vo.getEndDate(), vo.getSaleCode(), vo.getSupplierCode(), vo.getSaleId(), first, pageSize);

        return transObject2Vo(list);
    }

    /**
     * 计算销售订单的总数
     * @param vo 销售订单
     * @return long销售订单的总数
     */
    public long countSaleOrderList(SaleOrderVo vo){
        String orgId = SecurityUtil.getCurrentOrgId();
        String accId = SecurityUtil.getFiscalAccountId();
        return saleOrderRepository.countSaleOrderList(orgId, accId, vo.getStartDate(),
                vo.getEndDate(), vo.getSaleCode(), vo.getSupplierCode(), vo.getSaleId());
    }

    /**
     * 将查询到的数据转换成VO
     */
    private List<SaleOrderVo> transObject2Vo(List<Object[]> list) {
        List<SaleOrderVo> vos = Lists.newArrayList();
        if (list != null) {
            list.forEach(row -> {
                String[] thisRow = new String[row.length];
                for (int i=0; i<row.length; i++) {
                    thisRow[i] = row[i] != null ? row[i].toString():"";
                }
                SaleOrderVo vo = new SaleOrderVo();
                vo.setSaleCode(thisRow[0]);
                vo.setSale(thisRow[1]);
                vo.setSupplierCode(thisRow[2]);
                vo.setSupplierName(thisRow[3]);
                vo.setAmount(thisRow[4]);
                vo.setSaleDate(thisRow[5]);
                vo.setFinishDate(thisRow[6]);
                vo.setLastDate(thisRow[7]);
                vo.setBackAmount(thisRow[8]);
                vo.setLastBackDate(thisRow[9]);
                vo.setLastIncomeDate(thisRow[10]);
                vo.setHasIncome(thisRow[11]);
                vo.setNotIncome(thisRow[12]);
                vo.setSaleExp(thisRow[13]);
                vo.setGoodsFee(thisRow[14]);
                vos.add(vo);
            });
        }
        return vos;
    }

}
