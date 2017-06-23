package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.domain.basedata.service.GoodsSpecService;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsSpecVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 货品属性资源表
 */
@RestController
@RequestMapping("/api/goodsSpec")
public class GoodsSpecResource extends AbstractBaseResource {

    @Autowired
    private GoodsSpecService goodsSpecService;

    @ApiOperation("根据属性组ID获取子属性")
    @GetMapping("/query")
    public ResponseEntity query(GoodsSpecVo vo) {
        return listReponse(goodsSpecService.getChidlList(vo.getParentId(), vo.getSearchKey()));
    }
}
