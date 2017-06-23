package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.basedata.service.SupplierService;
import cn.fooltech.fool_ops.domain.basedata.vo.SupplierVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 供应商
 * Created by cwz
 */
@RestController
@RequestMapping("/api/supplier")
public class SupplierResource extends AbstractBaseResource {

    /**
     * 供应商服务类
     */
    @Autowired
    private SupplierService supplierService;

    /**
     * 供应商列表信息(JSON)
     *
     * @param vo
     * @param page  当前页数
     * @param rows  页面大小
     * @param model
     * @return
     */
    @ApiOperation("获取供应商列表信息")
    @GetMapping(value = "/list")
    public ResponseEntity list(SupplierVo vo, PageParamater pageParamater) {
        pageParamater.setPage(1);
        pageParamater.setRows(Integer.MAX_VALUE);
        pageParamater.setStart(0);
        Page<SupplierVo> page = supplierService.query(vo, pageParamater);
        List<SupplierVo> list = page.getContent();
        return listReponse(list);
    }


    /**
     * 编辑页面
     *
     * @param id 供应商ID
     * @return
     */
    @GetMapping("/get")
    @ApiOperation("根据fid获取供应商信息")
    @ResponseBody
    public SupplierVo edit(String id) {
        SupplierVo supplier = supplierService.getById(id);
        return supplier;
    }


    /**
     * 判断编号是否有效
     * @param vo
     * @return
     */
//	@RequestMapping("/isCodeValid")
//	@ResponseBody
//	public RequestResult isCodeValid(SupplierVo vo){
//		return supplierService.isCodeValid(vo);
//	}


    /**
     * 模糊查询(根据供应商编号、供应商名称)
     *
     * @param vo
     * @return
     */
    @ResponseBody
    @GetMapping("/vagueSearch")
    @ApiOperation("模糊查询(根据客户编号、客户名称)")
    public List<SupplierVo> vagueSearch(SupplierVo vo) {
        return supplierService.vagueSearch(vo);
    }

}
