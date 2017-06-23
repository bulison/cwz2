package cn.fooltech.fool_ops.web.warehouse;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.warehouse.service.PeriodAmountService;
import cn.fooltech.fool_ops.domain.warehouse.service.PeriodStockAmountService;
import cn.fooltech.fool_ops.domain.warehouse.vo.PeriodAmountVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.PeriodStockAmountVo;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/periodAmount")
public class PeriodAmountController {

    @Autowired
    private PeriodAmountService amountService;

    /**
     * 查询库存分页
     * @param vo
     * @param pageParamater
     * @return
     */
    @GetMapping("/query")
    public PageJson query(PeriodAmountVo vo, PageParamater pageParamater){
        Page<PeriodAmountVo> page = amountService.query(vo, pageParamater);
        return new PageJson(page);
    }

    /**
     * 导出
     * @throws Exception
     */
    @RequestMapping(value="/export")
    public void export(PeriodAmountVo vo, HttpServletResponse response) throws Exception{

        PageParamater pageParamater = new PageParamater();
        pageParamater.setPage(1);//导出所有
        pageParamater.setRows(Integer.MAX_VALUE);
        Page<PeriodAmountVo> page = amountService.query(vo, pageParamater);
        List<PeriodAmountVo> vos = page.getContent();

        try {
            ExcelUtils.exportExcel(PeriodAmountVo.class, vos, "期间总仓库存.xls", response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
