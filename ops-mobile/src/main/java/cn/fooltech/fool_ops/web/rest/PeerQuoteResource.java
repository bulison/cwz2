package cn.fooltech.fool_ops.web.rest;


import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.PeerQuoteService;
import cn.fooltech.fool_ops.domain.basedata.vo.PeerQuoteVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 同行报价
 * Created by xjh
 */
@RestController
@RequestMapping("/api/peerQuote")
public class PeerQuoteResource extends AbstractBaseResource {

    private static final String NameSpace = "peerQuote";

    @Autowired
    private PeerQuoteService peerQuoteService;

    @ApiOperation("同行报价新增/修改")
    @PutMapping("/save")
    @PostMapping("/save")
    public ResponseEntity save(@RequestBody PeerQuoteVo vo) {
        RequestResult result = peerQuoteService.save(vo);
        return reponse(result);
    }

    @ApiOperation("同行报价获取上次报价")
    @GetMapping("queryLast")
    public ResponseEntity queryLast(@RequestParam String goodsId,
        String goodSpecId, String unitId, String receiptPlace){
        RequestResult result = peerQuoteService.queryLast(goodsId, goodSpecId, unitId, receiptPlace);
        return reponse(result);
    }

    @ApiOperation("同行报价查询分页")
    @GetMapping("/query")
    public ResponseEntity query(PeerQuoteVo vo, PageParamater paramater) {
        return pageReponse(NameSpace, peerQuoteService.query(vo, paramater));
    }

    @ApiOperation("删除同行报价")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        return reponse(peerQuoteService.delete(id));
    }

}
