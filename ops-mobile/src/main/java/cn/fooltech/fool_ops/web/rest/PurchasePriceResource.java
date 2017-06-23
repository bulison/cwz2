package cn.fooltech.fool_ops.web.rest;


import cn.fooltech.fastjson.annotation.SerializeField;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.PurchasePriceService;
import cn.fooltech.fool_ops.domain.basedata.vo.PurchasePriceVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 *货品报价
 *
 */
@RestController
@RequestMapping("/api/purchasePrice")
public class PurchasePriceResource extends AbstractBaseResource {

    private static final String NameSpace = "purchasePrice";

    @Autowired
    private PurchasePriceService purchasePriceService;

    @ApiOperation("货品报价新增/修改")
    @PutMapping("/save")
    public ResponseEntity save(@RequestBody PurchasePriceVo vo) {

        RequestResult result = purchasePriceService.save(vo);
        return reponse(result);
    }

    @SerializeField(clazz =Page.class )
    @ApiOperation("货品报价查询分页 可按时间段、供应商、货品等进行查询")
    @GetMapping("/query")
    public ResponseEntity query(PurchasePriceVo vo, PageParamater paramater) {
        Page page = purchasePriceService.query(vo, paramater);
        return pageReponse(NameSpace, page);
    }

    @GetMapping("/lastquote")
    public ResponseEntity lastquote(String supplierId,
                                    String goodsId, String unitId, String goodsSpecId, String deliveryPlace) {
        PurchasePriceVo vo = new PurchasePriceVo();
        vo.setSupplierId(supplierId);
        vo.setGoodsId(goodsId);
        vo.setUnitId(unitId);
        vo.setGoodSpecId(goodsSpecId);
        vo.setDeliveryPlace(deliveryPlace);
        PageParamater pageParamater = new PageParamater(1, 1, 1);
        return pageReponse(NameSpace, purchasePriceService.query(vo, pageParamater));
    }

    @ApiOperation("货品报价删除")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        return reponse(purchasePriceService.delete(id));
    }
    @ApiOperation("报价过期测试")
    @GetMapping("/purchasePriceExpiredTest")
    public void purchasePriceExpiredTest(){
    	purchasePriceService.checkExpiredPurchasePrice();
    }

}
