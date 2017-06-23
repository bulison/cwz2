package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsVo;
import cn.fooltech.fool_ops.domain.basedata.vo.GroundPriceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 货品资源表
 */
@RestController
@RequestMapping("/api/goods")
public class GoodsResource extends AbstractBaseResource {

    private static final String NameSpace = "goods";

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/query")
    public ResponseEntity<List<GroundPriceVo>> query(GoodsVo vo, PageParamater paramater) {
        return pageReponse(NameSpace, goodsService.getChilds(vo, paramater));
    }

}
