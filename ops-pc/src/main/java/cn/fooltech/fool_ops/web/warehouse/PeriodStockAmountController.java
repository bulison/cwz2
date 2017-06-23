package cn.fooltech.fool_ops.web.warehouse;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.member.vo.MemberVo;
import cn.fooltech.fool_ops.domain.warehouse.service.PeriodStockAmountService;
import cn.fooltech.fool_ops.domain.warehouse.vo.PeriodStockAmountVo;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/periodStockAmount")
public class PeriodStockAmountController {

    @Autowired
    private PeriodStockAmountService stockAmountService;

    /**
     * 查询库存分页
     * @param vo
     * @param pageParamater
     * @return
     */
    @GetMapping("/query")
    public PageJson query(PeriodStockAmountVo vo, PageParamater pageParamater){
        Page<PeriodStockAmountVo> page = stockAmountService.query(vo, pageParamater);
        return new PageJson(page);
    }

    /**
     * 导出
     * @throws Exception
     */
    @RequestMapping(value="/export")
    public void export(PeriodStockAmountVo vo, HttpServletResponse response) throws Exception{

        PageParamater pageParamater = new PageParamater();
        pageParamater.setPage(1);//导出所有
        pageParamater.setRows(Integer.MAX_VALUE);
        Page<PeriodStockAmountVo> page = stockAmountService.query(vo, pageParamater);
        List<PeriodStockAmountVo> vos = page.getContent();

        try {
            ExcelUtils.exportExcel(PeriodStockAmountVo.class, vos, "期间分仓库存.xls", response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
