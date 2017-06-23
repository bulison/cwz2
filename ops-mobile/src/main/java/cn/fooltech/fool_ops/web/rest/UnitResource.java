package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.service.UnitService;
import cn.fooltech.fool_ops.domain.basedata.vo.UnitVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 单位Resource
 * Created by xjh
 */
@RestController
@RequestMapping("/api/unit")
public class UnitResource extends AbstractBaseResource {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UnitService unitService;

    /**
     * 根据货品获取单位组下的所有单位
     *
     * @param goodsId
     * @return
     */
    @ApiOperation("根据货品获取单位组下的所有单位")
    @GetMapping("/list")
    public ResponseEntity list(@RequestParam String goodsId, String searchKey) {
        Goods goods = goodsService.get(goodsId);
        String unitParentId = goods.getUnitGroup().getFid();
        List<UnitVo> units = unitService.getChildsOfMatch(unitParentId, searchKey);
        return ResponseEntity.ok().body(units);
    }


}
