package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.GroundPriceService;
import cn.fooltech.fool_ops.domain.basedata.vo.GroundPriceVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 场地报价
 * Created by xjh
 */
@RestController
@RequestMapping("/api/groundPrice")
public class GroundPriceResource extends AbstractBaseResource {

    private static final String NameSpace = "groundPrice";

    @Autowired
    private GroundPriceService groundPriceService;

    @ApiOperation("场地报价新增/修改")
    @PutMapping("/save")
    public ResponseEntity<GroundPriceVo> save(@RequestBody GroundPriceVo vo) {
        RequestResult result = null;
        try {
            result = groundPriceService.save(vo);
            if (result.isSuccess()) {
                String id = (String) result.getData();
                result.setData(groundPriceService.getById(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reponse(result);
    }

    @ApiOperation("场地报价查询分页")
    @GetMapping("/query")
    public ResponseEntity<List<GroundPriceVo>> query(GroundPriceVo vo, PageParamater paramater) {
        paramater.setStart(0);
        paramater.setPage(1);
        paramater.setRows(Integer.MAX_VALUE);
        return pageReponse(NameSpace, groundPriceService.query(vo, paramater));
    }

    @ApiOperation("场地报价删除")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GroundPriceVo> delete(@PathVariable String id) {
        RequestResult result = null;
        try {
            result = groundPriceService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reponse(result);
    }
}
