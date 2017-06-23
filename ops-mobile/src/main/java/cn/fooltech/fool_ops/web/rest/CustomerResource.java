package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerService;
import cn.fooltech.fool_ops.domain.basedata.vo.CustomerVo;
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
 * 客户
 *
 * @author cwz
 */
@RestController
@RequestMapping(value = "/api/customer")
public class CustomerResource extends AbstractBaseResource {

    /**
     * 客户网页服务类
     */
    @Autowired
    private CustomerService customerService;


    /**
     * 客户列表信息(JSON)
     *
     * @param vo
     * @param page  当前页数
     * @param rows  页面大小
     * @param model
     * @return
     * @throws Exception
     */
    @ApiOperation("获取客户列表信息")
    @GetMapping(value = "/list")
    public ResponseEntity list(CustomerVo vo, PageParamater pageParamater) throws Exception {
        pageParamater.setPage(1);
        pageParamater.setRows(Integer.MAX_VALUE);
        pageParamater.setStart(0);
        Page<CustomerVo> page = customerService.query(vo, pageParamater);
        List<CustomerVo> list = page.getContent();
        return listReponse(list);
    }

    /**
     * 编辑页面
     *
     * @param id 客户ID
     * @return
     */
    @GetMapping("/get")
    @ApiOperation("根据fid获取客户信息")
    public CustomerVo edit(String id) {
        CustomerVo customer = customerService.getById(id);
//		model.addAttribute("customer", customer);
//		return "/basedata/customer/customerEdit";
        return customer;
    }

    /**
     * 判断编号是否有效
     * @param vo
     * @return
     */
//	@RequestMapping("/isCodeValid")
//	@ResponseBody
//	public RequestResult isCodeValid(CustomerVo vo){
//		return customerService.isCodeValid(vo);
//	}


    /**
     * 模糊查询(根据客户编号、客户名称)
     *
     * @param vo
     * @return
     */
    @ResponseBody
    @GetMapping("/vagueSearch")
    @ApiOperation("模糊查询(根据客户编号、客户名称)")
    public List<CustomerVo> vagueSearch(CustomerVo vo) {
        return customerService.vagueSearch(vo);
    }

}
