package cn.fooltech.fool_ops.web.fiscal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalPeriodVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>财务会计期间控制器类</p>
 * @author lzf
 * @version 1.0
 * @date 2015年11月18日
 */
@Controller
@RequestMapping(value = "/fiscalPeriod")
public class FiscalPeriodController extends BaseController{
	@Autowired
	private FiscalPeriodService periodService;
	
	/**
	 * 财务会计期间管理界面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/fiscal/period/manage";
	}
	
	/**
	 * 新增财务会计期间
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(HttpServletRequest request){
		FiscalPeriodVo lastPeriod = periodService.getLastPeriodPlusOne();
		request.setAttribute("lastVo", lastPeriod);
		return "/fiscal/period/edit";
	}
	
	/**
	 * 修改财务会计期间
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(FiscalPeriodVo vo,HttpServletRequest request){
		FiscalPeriodVo obj = periodService.getById(vo.getFid());
		FiscalPeriodVo lastPeriod = periodService.getPrePeriodPlusOne(vo.getFid());
		request.setAttribute("obj", obj);
		request.setAttribute("lastVo", lastPeriod);
		return "/fiscal/period/edit";
	}
	
	/**
	 * 获得第一个未结账的会计期间
	 * @return
	 */
	@RequestMapping(value = "/getFristUnCheckPeriod")
	@ResponseBody
	public FiscalPeriodVo getFristUnCheckPeriod(){
		return periodService.getVo(periodService.getFirstNotCheck());
	}
	
	/**
	 * 保存会计期间
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public RequestResult save(FiscalPeriodVo vo){
		return periodService.save(vo);
	}
	
	/**
	 * 删除会计期间
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public RequestResult delete(FiscalPeriodVo vo){
		return periodService.deleteById(vo.getFid());
	}
	
	/**
	 * 账套会计期间
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageJson list(FiscalPeriodVo vo,PageParamater pageParamater){
		Page<FiscalPeriodVo> page = periodService.query(vo, pageParamater);
		periodService.setLastPeriod(page.getContent(), periodService.getLastPeriod());
		return new PageJson(page);
	}
	
	/**
	 * 账套会计期间
	 * @return
	 */
	@RequestMapping(value = "/getAll")
	@ResponseBody
	public List<FiscalPeriodVo> getAll(FiscalPeriodVo vo,PageParamater pageParamater){
		List<FiscalPeriodVo> vos=periodService.getAllPeriod();
		
		if(vo.getDefaultSelect()!=null &&
				vo.getDefaultSelect()!=FiscalPeriodVo.DEFAULT_SELECT_NO &&
				vos.size()>0 ){
			if(vo.getDefaultSelect()==FiscalPeriodVo.DEFAULT_SELECT_FIRST){
				vos.get(0).setCheckoutStatus(1);
			}else if(vo.getDefaultSelect()==FiscalPeriodVo.DEFAULT_SELECT_NOT_CHECK){
				for(FiscalPeriodVo iter:vos){
					if(iter.getCheckoutStatus()==FiscalPeriod.USED||iter.getCheckoutStatus()==FiscalPeriod.UN_USED){
						iter.setIsChecked((short)1);
						break;
					}
				}
			}
		}
		return vos;
	}

	/**
	 * 查询所有未结账的会计期间
	 * @return
	 */
	@RequestMapping(value = "/getNotCheck")
	@ResponseBody
	public List<FiscalPeriodVo> getNotCheck(){
		List<FiscalPeriodVo> vos = periodService.getNotCheck();
		return vos;
	}
	
	/**
	 * 生成会计期间
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/create")
	@ResponseBody
	public RequestResult create(FiscalPeriodVo vo) throws Exception{
		return periodService.saveOneYear(vo);
	}
	
	/**
	 * 会计期间启用
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/launch")
	@ResponseBody
	public RequestResult launch(FiscalPeriodVo vo) throws Exception{
		return periodService.updateUsed(vo.getFid());
	}
	
	
	/**
	 * 会计期间结账
	 * @param fid 
	 * @param flag 未结转损益，是否必须结账标识，1 是 0 否
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/checked")
	public RequestResult checked(String fid, @RequestParam(defaultValue = "0", required = false) int flag) throws Exception{
		return periodService.updateChecked(fid, flag);
	}
	
	/**
	 * 会计期间反结账
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/unchecked")
	@ResponseBody
	public RequestResult unchecked(FiscalPeriodVo vo) throws Exception{
		return periodService.updateUnChecked(vo.getFid());
	}
	
	
}
