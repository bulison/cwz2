package cn.fooltech.fool_ops.domain.flow.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.base.Splitter;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.flow.entity.MsgWarnSetting;
import cn.fooltech.fool_ops.domain.flow.entity.Supervise;
import cn.fooltech.fool_ops.domain.flow.repository.SuperviseRepository;
import cn.fooltech.fool_ops.domain.flow.vo.SuperviseVo;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.UserRepository;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;



/**
 * <p>监督人网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-05-17 15:09:31
 */
@Service
public class SuperviseService extends BaseService<Supervise,SuperviseVo,String> {
	
	/**
	 * 监督人服务类
	 */
	@Autowired
	private SuperviseRepository repository;
	
	/**
	 * 消息预警配置服务类
	 */
	@Autowired
	private MsgWarnSettingService settingService;
	
	@Autowired
	private UserRepository userRepo;
	/**
	 * 查询监督人列表信息，按照监督人主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<Supervise> query(SuperviseVo vo,PageParamater pageParamater){
		Sort sort = new Sort(Direction.DESC, "fid");
		PageRequest request = getPageRequest(pageParamater,sort);
		Page<Supervise> query = repository.query(vo, request);
		return query;
	}
	
	/**
	 * 根据配置ID查询监督人列表信息
	 */
	public List<SuperviseVo> queryBySettingId(String settingId, Integer type){
		return getVos(repository.queryBySettingId(settingId, type));
	}
	
	
	/**
	 * 单个监督人实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public SuperviseVo getVo(Supervise entity){
		if(entity == null)
			return null;
		SuperviseVo vo = new SuperviseVo();
		vo.setType(entity.getType());
		vo.setFid(entity.getFid());
		vo.setWarnSettingId(entity.getWarnSetting().getFid());
		vo.setSuperviseId(entity.getSupervise().getFid());
		vo.setSuperviseName(entity.getSupervise().getUserName());
		
		return vo;
	}
	
	/**
	 * 删除监督人<br>
	 */
	public RequestResult delete(String fid){
		deleteBySettingId(fid);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 获取监督人信息
	 * @param fid 监督人ID
	 * @return
	 */
	public SuperviseVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(repository.findOne(fid));
	}
	

	/**
	 * 新增/编辑监督人
	 * @param vo
	 */
	public RequestResult save(SuperviseVo vo) {
		
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		
		Supervise entity = null;
		if(StringUtils.isBlank(vo.getFid())){
			entity = new Supervise();
			entity.setOrg(SecurityUtil.getCurrentOrg());
		}else {
			entity = repository.findOne(vo.getFid());
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
		}
		
		User supervise = userRepo.findOne(vo.getSuperviseId());
		MsgWarnSetting setting = settingService.get(vo.getWarnSettingId());
		
		entity.setSupervise(supervise);
		entity.setWarnSetting(setting);
		entity.setType(vo.getType());
		
		repository.save(entity);
		
		return buildSuccessRequestResult();
	}

	@Override
	public CrudRepository<Supervise, String> getRepository() {
		return repository;
	}
	/**
	 * 根据settingId删除监督人
	 */
	public void deleteBySettingId(String settingId){

		List<Supervise> list = repository.queryBywarnSetting(settingId);
		for(Supervise supervise:list){
			super.delete(supervise.getFid());
		}
	}
	/**
	 * 保存监督人
	 */
	public void saveSupervises(MsgWarnSetting setting, String superviseIds){
		Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
		List<String> superviseIdList = splitter.splitToList(superviseIds);
		for(String superviseId:superviseIdList){
			
			User user = userRepo.findOne(superviseId);
			if(user==null)continue;
			Supervise supervise = new Supervise();
			supervise.setOrg(setting.getOrg());
			supervise.setSupervise(user);
			supervise.setType(setting.getSendType());
			supervise.setWarnSetting(setting);
			save(supervise);
		}
	}
}
