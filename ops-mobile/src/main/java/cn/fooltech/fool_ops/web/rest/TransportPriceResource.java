package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplateDetail1;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplateDetail2;
import cn.fooltech.fool_ops.domain.basedata.service.*;
import cn.fooltech.fool_ops.domain.basedata.vo.*;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 运输费报价网页控制器类
 * </p>
 *
 * @author cwz
 * @date 2016-12-9
 */
@RestController
@RequestMapping("/api/transportPrice")
public class TransportPriceResource extends AbstractBaseResource {
    private Logger logger = Logger.getLogger(TransportPriceResource.class);
    /**
     * 运输费报价服务类
     */
    @Autowired
    private TransportPriceService priceService;
    /**
     * 运输费报价(从1表)服务类
     */
    @Autowired
    private TransportPriceDetail1Service detail1Service;
    /**
     * 运输费报价(从2表)服务类
     */
    @Autowired
    private TransportPriceDetail2Service detail2Service;

    //运输费报价模板 持久层
    @Autowired
    private TransportTemplateService templateService;
    @Autowired
    private TransportTemplateDetail1Service templateDetail1Service;
    @Autowired
    private TransportTemplateDetail2Service templateDetail2Service;

    /**
     * 运输费报价表信息
     *
     * @param vo 查询条件vo
     * @return
     */
    @ApiOperation("获取运输费报价表信息")
    @GetMapping("/list")
    public ResponseEntity list(TransportPriceVo vo) {
        PageParamater paramater = new PageParamater();
        paramater.setPage(1);
        paramater.setRows(Integer.MAX_VALUE);
        paramater.setStart(0);
        Page<TransportPriceVo> query = priceService.query(vo, paramater);
        ResponseEntity listReponse = listReponse(query.getContent());
        return listReponse;
    }

    /**
     * 新增、编辑
     *
     * @param vo
     */

    @ApiOperation("保存运输费报价")
    @PutMapping("/save")
    public ResponseEntity<TransportPriceVo> save(@RequestBody TransportPriceVo vo) {
        RequestResult result = null;
        try {
            result = priceService.save(vo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reponse(result);
    }

    /**
     * 删除运输费报价模板信息
     *
     * @param id 主键id
     * @return
     */
    @ApiOperation("删除运输费报价模板信息")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        return reponse(priceService.delete(id));
    }

    /**
     * 运输费报价模板1详细信息
     *
     * @param billId 根据主表id查询
     * @return
     */
    @ApiOperation("获取运输费报价1详细信息")
    @GetMapping("/detail")
    public ResponseEntity detail(String billId) {
        PageParamater paramater = new PageParamater();
        paramater.setPage(1);
        paramater.setRows(Integer.MAX_VALUE);
        paramater.setStart(0);
        List<TransportPriceDetail1Vo> query = detail1Service.query(billId, paramater);
        ResponseEntity listReponse = listReponse(query);
        return listReponse;
    }

    /**
     * 运输费报价模板2详细信息
     *
     * @param billId    主表id查询
     * @param detail1Id 从1表id
     * @return
     */
    @ApiOperation("获取运输费报价2详细信息")
    @GetMapping("/detail2")
    public ResponseEntity detail2(String billId, String detail1Id) {
        List<TransportPriceDetail2Vo> query = detail2Service.query(billId, detail1Id);
        ResponseEntity listReponse = listReponse(query);
        return listReponse;
    }

    /**
     * 根据用户选择运输方式和装运方式，查找运输费报价模板
     *
     * @param transportTypeId 运输方式
     * @param shipmentTypeId  装运方式
     * @param deliveryPlaceId 发货地
     * @param receiptPlaceId  收货地
     * @return
     */
    @ApiOperation("获取运输费报价模板 ")
    @GetMapping("/findByTemp")
    public ResponseEntity findByTemp(String transportTypeId, String shipmentTypeId, String deliveryPlaceId, String receiptPlaceId) {
        List<TransportTemplateVo> findByTemp = templateService.findByTemp(transportTypeId, shipmentTypeId, deliveryPlaceId, receiptPlaceId);
        ResponseEntity listReponse = listReponse(findByTemp);
        return listReponse;
    }

    /**
     * 获取运输费报价模板明细表1
     *
     * @param templateId 模版id
     * @return
     */
    @ApiOperation("获取运输费报价模板明细表1")
    @GetMapping("/findByTempDetail1")
    public ResponseEntity findByTempDetail1(String templateId) {
        List<TransportTemplateDetail1> detail2s = templateDetail1Service.queryByTemplateId(SecurityUtil.getFiscalAccountId(), templateId);
        List<TransportTemplateDetail1Vo> vos = templateDetail1Service.getVos(detail2s);
        ResponseEntity listReponse = listReponse(vos);
        return listReponse;
    }

    /**
     * 获取运输费报价模板明细表2
     *
     * @param templateId 模版id
     * @return
     */
    @ApiOperation("获取运输费报价模板明细表2")
    @GetMapping("/findByTempDetail2")
    public ResponseEntity findByTempDetail2(String templateId, String detail1Id) {
        List<TransportTemplateDetail2> detail2s = templateDetail2Service.queryByTemplateId(SecurityUtil.getFiscalAccountId(), templateId, detail1Id);
        List<TransportTemplateDetail2Vo> vos = templateDetail2Service.getVos(detail2s);
        ResponseEntity listReponse = listReponse(vos);
        return listReponse;
    }
	/**
	 * 报价过期测试
	 */
    @GetMapping("/tranSportPriceExpiredTest")
    @ApiOperation("运输费报价过期测试")
    public void tranSportPriceExpiredTest(){
		priceService.checkExpiredTransportPrice();
	}
}
