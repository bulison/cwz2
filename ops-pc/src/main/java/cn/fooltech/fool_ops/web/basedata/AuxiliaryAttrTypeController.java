package cn.fooltech.fool_ops.web.basedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrTypeService;
import cn.fooltech.fool_ops.domain.basedata.vo.AuxiliaryAttrTypeVo;
import cn.fooltech.fool_ops.utils.StringUtils;

/**
 * 辅助属性类型控制类
 * @author lgk
 * @date 2015年12月25日下午2:55:23
 * @version V1.0
 */
@Controller
@RequestMapping("/basedata/auxiliarytype")
public class AuxiliaryAttrTypeController{
    @Autowired
	private AuxiliaryAttrTypeService auxiliaryAttrTypeWebService;
	/**
	 * 辅助属性类型界面
	 * @return
	 */
    @RequestMapping("/listAuxiliaryAttrType")
    public String listAuxiliaryAttrType(){
    	return "/basedata/auxiliaryAttr/listAuxillaryAtrrType";
    }
    /**
     * 编辑辅助属性类型界面
     * @param model
     * @param fid
     * @return
     */
	@RequestMapping("/editAuxiliaryAttrType")
    public String editAuxiliaryAttrType(ModelMap model,String fid){
		if(StringUtils.isNotBlank(fid)){
		   model.addAttribute("vo", auxiliaryAttrTypeWebService.getByFid(fid));
		}
		return "/basedata/auxiliaryAttr/addAuxiliaryAttrType";
    }
	/**
	 * 列表
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageJson list(AuxiliaryAttrTypeVo vo,PageParamater paramater){
	   Page<AuxiliaryAttrTypeVo> page =  auxiliaryAttrTypeWebService.query(vo, paramater);
	   PageJson pageJson = new PageJson(page);
	   return pageJson;
	}
	/**
	 * 保存
	 * @param vo
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public RequestResult save(AuxiliaryAttrTypeVo vo){
		return auxiliaryAttrTypeWebService.save(vo);
	}

}
