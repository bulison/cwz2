package cn.fooltech.fool_ops.domain.freight.service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.redis.BaseDataCache;
import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.BaseConstant;
import cn.fooltech.fool_ops.domain.basedata.dao.PeerQuoteReportDao;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerAddressService;
import cn.fooltech.fool_ops.domain.basedata.service.GroundPriceService;
import cn.fooltech.fool_ops.domain.basedata.service.PeerQuoteService;
import cn.fooltech.fool_ops.domain.basedata.service.PurchasePriceService;
import cn.fooltech.fool_ops.domain.basedata.service.TransportPriceService;
import cn.fooltech.fool_ops.domain.basedata.service.TransportTemplateService;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.repository.FreightAddressRepository;
import cn.fooltech.fool_ops.domain.freight.vo.FastFreightAddressVo;
import cn.fooltech.fool_ops.domain.freight.vo.FreightAddressVo;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.tree.FastTreeUtils;
import cn.fooltech.fool_ops.utils.tree.TreeRootCallBack;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 *
 * 收货地址 	业务层
 * @author cwz
 *
 */
@Service
public class FreightAddressService extends BaseService<FreightAddress, FreightAddressVo, String>
		implements BaseDataCache {

	@Autowired
	FreightAddressRepository repository;
	/**
	 * 辅助属性服务类
	 */
	@Autowired
	private AuxiliaryAttrService attrService;
	
	/**
	 * 运输费报价模板
	 */
	@Autowired
	private TransportTemplateService templateService;
	/**
	 * 运输费报价 
	 */
	@Autowired
	private TransportPriceService priceService;
	/**
	 * 场地费报价
	 */
	@Autowired
	private GroundPriceService groundPriceService;
	/**
	 * 货品价格报价
	 */
	@Autowired
	private PurchasePriceService purchasePriceService;
	/**
	 * 客户/供应商默认收/发货地址
	 */
	@Autowired
	private CustomerAddressService customerAddressService;
	/**
	 * 发货单或收货单[仓储单据服务类]
	 */
	@Resource(name="ops.WarehouseBillService")
	private WarehouseBillService billService;
	
	@Autowired
	private RedisService redisService;
	@Autowired
	private PeerQuoteService quoteService;
	
	@Override
	public FreightAddressVo getVo(FreightAddress entity) {
		if(entity==null) return null;
		FreightAddressVo vo = new FreightAddressVo();
		vo.setFid(entity.getFid());
		if(entity.getGround()!=null){
			vo.setAssetgroundId(entity.getGround().getFid());
			vo.setAssetgroundName(entity.getGround().getName());
		}
		if(entity.getWarehouse()!=null){
			vo.setAssetwarehouseId(entity.getWarehouse().getFid());
			vo.setAssetwarehouseName(entity.getWarehouse().getName());
		}
		vo.setCode(entity.getCode());
		vo.setCreateTime(DateUtils.getDateString(entity.getCreateTime()));
		if(entity.getCreator()!=null){
			vo.setCreatorId(entity.getCreator().getFid());
			vo.setCreatorName(entity.getCreator().getUserName());
		}
		if(entity.getTransportLoss()!=null){
			if(!Strings.isNullOrEmpty(entity.getTransportLoss().getFid())){
				vo.setTransportLossId(entity.getTransportLoss().getFid());
				vo.setTransportLoss(entity.getTransportLoss().getName());
			}
			
		}
		vo.setDescribe(entity.getDescribe());
		vo.setEnable(entity.getEnable());
		if(entity.getFiscalAccount()!=null){
			vo.setFiscalAccountId(entity.getFiscalAccount().getFid());
			vo.setFiscalAccountName(entity.getFiscalAccount().getName());
		}
		vo.setFlag(entity.getFlag());
		vo.setFullParentId(entity.getFullParentId());
		vo.setName(entity.getName());
		if(entity.getOrg()!=null)
		vo.setOrgId(entity.getOrg().getFid());
        //父节点
       FreightAddress parent = entity.getParent();
        if (parent != null) {
        	String fid = parent.getFid();
        	String name = parent.getName();
        	String code = parent.getCode();
            vo.setParentId(fid);
            vo.setParentName(name);
            vo.setParentCode(code);
        }
		vo.setRecipientSign(entity.getRecipientSign());
        vo.setTransfer(entity.getTransfer());
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
		return vo;
	}

	/**
	 * 获得简易FreightAddressVo
	 * @param entity
	 * @return
	 */
	public FastFreightAddressVo getFastVo(FreightAddress entity){
		if(entity==null) return null;
		FastFreightAddressVo vo = new FastFreightAddressVo();
		vo.setFid(entity.getFid());
		if(entity.getWarehouse()!=null){
			vo.setAssetwarehouseId(entity.getWarehouse().getFid());
			vo.setAssetwarehouseName(entity.getWarehouse().getName());
		}
		vo.setCode(entity.getCode());
		vo.setName(entity.getName());

		//父节点
		FreightAddress parent = entity.getParent();
		if (parent != null) {
			String fid = parent.getFid();
			vo.setParentId(fid);
		}
		return vo;
	}

	/**
	 * 获得简易FreightAddressVo
	 * @param entitys
	 * @return
	 */
	public List<FastFreightAddressVo> getFastVos(List<FreightAddress> entitys) {
		List<FastFreightAddressVo> list = Lists.newArrayList();
		if (entitys == null) return list;

		list = entitys
				.stream()
				.map(p->getFastVo(p))
				.collect(Collectors.toList());

		return list;
	}

	@Override
	public CrudRepository<FreightAddress, String> getRepository() {
		return repository;
	}
	
	/**
	 * 查询收货地址树<br>
	 * @param vo
	 */
	public List<FreightAddressVo> getTree(FreightAddressVo vo) {
		boolean tree = true;
		if (vo.getFlag() != null) {
			tree = false;
		}
		final List<FreightAddress> subjects = repository.query(vo);
		final List<FreightAddressVo> vos = getVos(subjects);

		if (tree) {
			FastTreeUtils<FreightAddressVo> fastTreeUtils = new FastTreeUtils<FreightAddressVo>();
			return fastTreeUtils.buildTreeData(vos, 0, comparatorVo, new TreeRootCallBack<FreightAddressVo>() {

				@Override
				public boolean isRoot(FreightAddressVo v) {
					return !findRoot(subjects, v);
				}
			});
		} else {
			return vos;
		}
	}

	/**
	 * 查询收货地址树<br>
	 * @param vo
	 */
	public List<FastFreightAddressVo> getFastTree(FreightAddressVo vo) {

		final List<FreightAddress> subjects = repository.query(vo);
		final List<FastFreightAddressVo> vos = getFastVos(subjects);

		FastTreeUtils<FastFreightAddressVo> fastTreeUtils = new FastTreeUtils<FastFreightAddressVo>();
		return fastTreeUtils.buildTreeData(vos, 0, new Comparator<FastFreightAddressVo>() {
			@Override
			public int compare(FastFreightAddressVo o1, FastFreightAddressVo o2) {
				return o1.getCode().compareTo(o2.getCode());
			}
		}, new TreeRootCallBack<FastFreightAddressVo>() {

			@Override
			public boolean isRoot(FastFreightAddressVo v) {
				return Strings.isNullOrEmpty(v.getParentId());
			}
		});

	}

	/**
	 * 比较器
	 */
	private Comparator<FreightAddressVo> comparatorVo = new Comparator<FreightAddressVo>() {

		@Override
		public int compare(FreightAddressVo o1, FreightAddressVo o2) {
			return o1.getCode().compareTo(o2.getCode());
		}
	};

	/**
	 * 在列表中能否找到父节点
	 * 
	 * @return
	 */
	private boolean findRoot(final List<FreightAddress> subjects, final FreightAddressVo child) {
		if (Strings.isNullOrEmpty(child.getParentId())) {
			return false;
		}
		for (FreightAddress iter : subjects) {
			if (child.getParentId().equals(iter.getFid())) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 获取收货地址信息
	 * @param id 货地址信ID
	 * @return
	 */
	public FreightAddressVo getById(String id) {
		FreightAddress entity = repository.findOne(id);
		return getVo(entity);
	}

	/**
	 * 新增/编辑收货地址信息
	 * @param vo
	 */
	@Transactional
	public RequestResult save(FreightAddressVo vo) {
//		新增、修改：判断编号、名称不能重复；
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
		}
		String fid = vo.getFid();
		Date now = new Date();
		FreightAddress entity=null;
		Long codeCount = repository.queryByCodeCount(vo.getCode(),SecurityUtil.getFiscalAccountId());
		Long nameCount = repository.queryByNameCount(vo.getName(),SecurityUtil.getFiscalAccountId());
		if (StringUtils.isBlank(fid)) {
			entity = new FreightAddress();
			entity.setCreateTime(now);
			entity.setUpdateTime(now);
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setFlag(new Short("1"));
			if(codeCount>0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "编号已存在!");
			}
			if(nameCount>0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "名称已存在!");
			}
		}else{
//			修改：判断时间戳，如果不一致提示已被其他用户修改不能保存，要刷新数据；
			entity = repository.findOne(fid);
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
			if(entity.getUpdateTime().compareTo(DateUtils.getDateFromString(vo.getUpdateTime()))>0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该页面已经失效,请刷新页面数据!");
			}
			if(codeCount>0&&!entity.getCode().equals(vo.getCode())){
				return new RequestResult(RequestResult.RETURN_FAILURE, "编号已存在!");
			}
			if(nameCount>0&&!entity.getName().equals(vo.getName())){
				return new RequestResult(RequestResult.RETURN_FAILURE, "名称已存在!");
			}
			entity.setFlag(new Short("1"));
			//先判断该节点有没有给其他节点引用（是否父节点），有就不改变节点标识。
			Long countByParentId = repository.countByParentId(fid);
			if(countByParentId>0){
				entity.setFlag(new Short("0"));
				//根据父节点状态修改子节点状态为停用
				if(vo.getEnable()==0){
					updateChildsEnable(entity.getFid());
				}
			}
		}
		if(!Strings.isNullOrEmpty(vo.getAssetgroundId())){
			entity.setGround(attrService.get(vo.getAssetgroundId()));
		}
		entity.setName(vo.getName());
		String parentId = vo.getParentId();
		if(!Strings.isNullOrEmpty(parentId)){
//			List list = quoteService.queryByReceiptPlaceCount(parentId, SecurityUtil.getFiscalAccountId());
//			if(list.size()>0){
//				return buildFailRequestResult("同行报价已引用收货地已引用该数据,该地址下不能添加子地址");
//			}
//			if(purchasePriceService.queryByDeliveryPlaceCount(parentId)>0){
//				return buildFailRequestResult("货品价格报价发货地已引用该数据,该地址下不能添加子地址");
//			}
//			if(billService.queryByDeliveryPlaceCount(parentId)>0){
//				return buildFailRequestResult("发货单/收货单发货地已引用该数据,该地址下不能添加子地址");
//			}
//			if(billService.queryByReceiptPlaceCount(parentId)>0){
//				return buildFailRequestResult("发货单/收货单收货地已引用该数据,该地址下不能添加子地址");
//			}
//			if(customerAddressService.queryByDeliveryPlaceCount(parentId)>0){
//				return buildFailRequestResult("客户/供应商默认收/发货地址已引用该数据,该地址下不能添加子地址");
//			}
		}
		FreightAddress parent = repository.findOne(vo.getParentId());
		String parentIds = parent==null?"":parent.getFullParentId();
        String newParentIds = null;
        if (Strings.isNullOrEmpty(parentIds)) {
            newParentIds = parent==null?"":parent.getFid();
        } else {
            newParentIds = Joiner.on(",").skipNulls().join(parentIds, parent.getFid());
        }
        entity.setFullParentId(newParentIds);
		if(parent!=null){
			short parentEnable = parent.getEnable();
//			short enable = entity.getEnable();
			if(parentEnable==0&&vo.getEnable()==1){
				return new RequestResult(RequestResult.RETURN_FAILURE, "父节点状态为未用时，子节点不允许为启用!");
			}
			parent.setFlag(new Short("0"));
			/*1763 【东哥新需求】新增和修改货运地址表记录，父地址的收货标识应该允许为是*/
//			parent.setRecipientSign(new Short("0"));
			repository.save(parent);
			
		}
		entity.setEnable(vo.getEnable());
		entity.setCode(vo.getCode());
		entity.setDescribe(vo.getDescribe());
		entity.setParent(parent);
		//判断父节点数
		String fullParentId = newParentIds;
		if(Strings.isNullOrEmpty(fullParentId)){
			entity.setLevel(0);
		}else{
            Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
            List<String> ids = splitter.splitToList(fullParentId);
			entity.setLevel(ids.size());
		}
		entity.setRecipientSign(vo.getRecipientSign());
		if(!Strings.isNullOrEmpty(vo.getAssetwarehouseId())){
			entity.setWarehouse(attrService.get(vo.getAssetwarehouseId()));
		}
		if(!Strings.isNullOrEmpty(vo.getTransportLossId())){
			entity.setTransportLoss(attrService.findOne(vo.getTransportLossId()));
		}
		entity.setUpdateTime(now);
		entity.setTransfer(vo.getTransfer());
		
		repository.save(entity);

		redisService.remove(getCacheKey());

		return buildSuccessRequestResult(getVo(entity));
	}
	/**
	 * 判断编号是否存在
	 * @param code
	 * @return
	 */
	public RequestResult queryByCodeCount(String code){
		Long count=0l;
		try {
			count = repository.queryByCodeCount(code,SecurityUtil.getFiscalAccountId());
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("查询编号有误！");
		}
		if(count>0){
			return buildFailRequestResult("编号已存在");
		}
		return buildSuccessRequestResult();
	}
	/**
	 * 判断名称是否存在
	 * @param name
	 * @return
	 */
	public RequestResult queryByNameCount(String name){
		Long count=0l;
		try {
			count = repository.queryByNameCount(name,SecurityUtil.getFiscalAccountId());
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("查询名称有误！");
		}
		if(count>0){
			return buildFailRequestResult("名称已存在");
		}
		return buildSuccessRequestResult();
	}
	/**
	 * 根据fid删除
	 */
	@Transactional
	public RequestResult delete(String fid){
		FreightAddress address = repository.findOne(fid);
		if(address==null){
			return buildFailRequestResult("该数据已失效，请刷新页面");
		}
//		如果地址已使用（已报价[运输费报价模板,运输费报价,场地费报价,货品价格报价,同行报价]、被客户关联、被发货单或收货单关联），则不能删除；
		try {
			if(templateService.queryByDeliveryPlaceCount(fid)>0){
				return buildFailRequestResult("删除失败：运输费报价模板发货地已引用该数据!");
			}
			if(templateService.queryByReceiptPlaceCount(fid)>0){
				return buildFailRequestResult("删除失败：运输费报价模板收货地已引用该数据!");
			}
			if(priceService.queryByDeliveryPlaceCount(fid)>0){
				return buildFailRequestResult("删除失败：运输费报价发货地已引用该数据!");
			}
			if(priceService.queryByReceiptPlaceCount(fid)>0){
				return buildFailRequestResult("删除失败：运输费报价收货地已引用该数据!");
			}
			if(groundPriceService.queryByAddressCount(fid)>0){
				return buildFailRequestResult("删除失败：场地费报价已引用该数据!");
			}
			if(purchasePriceService.queryByDeliveryPlaceCount(fid)>0){
				return buildFailRequestResult("删除失败：货品价格报价发货地已引用该数据!");
			}
			if(customerAddressService.queryByDeliveryPlaceCount(fid)>0){
				return buildFailRequestResult("删除失败：客户/供应商默认收/发货地址已引用该数据!");
			}
			if(billService.queryByDeliveryPlaceCount(fid)>0){
				return buildFailRequestResult("删除失败：发货单/收货单发货地已引用该数据!");
			}
			if(billService.queryByReceiptPlaceCount(fid)>0){
				return buildFailRequestResult("删除失败：发货单/收货单收货地已引用该数据!");
			}
			List list = quoteService.queryByReceiptPlaceCount(fid, SecurityUtil.getFiscalAccountId());
			if(list.size()>0){
				return buildFailRequestResult("删除失败：同行报价已引用该数据!");
			}
			String parentId="";
			//判断是否存在父节点，如果存在则修改父节点的
			if(address.getParent()!=null){
				parentId = address.getParent().getFid();
			}
			Long byParentId = repository.countByParentId(fid);
			if(byParentId>0){
				return buildFailRequestResult("删除失败：先删除该节点下的子节点!");
			}
			repository.delete(fid);
			//先判断该父节点下有没有子节点，没有就把父节点改为子节点，有就不做操作。
			Long countByParentId = repository.countByParentId(parentId);
			if(countByParentId==0){
				FreightAddress parent = repository.findOne(parentId);
				if(parent!=null){
					parent.setFlag(new Short("1"));
					/*1763 【东哥新需求】新增和修改货运地址表记录，父地址的收货标识应该允许为是*/
//					parent.setRecipientSign(new Short("1"));
					repository.save(parent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("删除失败："+e.getMessage());
		}

		redisService.remove(getCacheKey());

		return buildSuccessRequestResult();
	}
	/**
	 * 调整父地址
	 * @param beforeFid  调整前的对象
	 * @param afterFid	   调整的对象
	 * @return
	 */
	@Transactional
	public RequestResult changeEntity(String beforeFid,String afterFid){
		FreightAddress info = repository.findOne(beforeFid);
		FreightAddress info2 = repository.findOne(afterFid);
		String fullParentId = info2.getFullParentId();
		if (!Strings.isNullOrEmpty(fullParentId)) {
			boolean contains = fullParentId.contains(info.getFid());
			if(contains==true) return buildFailRequestResult("调整地址不能为该地址子目录");
			info.setFullParentId(fullParentId+","+info2.getFid());
		}else{
			info.setFullParentId(info2.getFid());
		}
		info.setParent(repository.findOne(info2.getFid()));
		repository.save(info);
		try {
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("调整父地址失败："+e);
		}
		redisService.remove(getCacheKey());

		return buildSuccessRequestResult();
	}
	 /**
     * 获取货运地址表下拉树
     */
    public List<FreightAddressVo> findAddressTree(FreightAddressVo vo) {

        List<FreightAddress> rootData = repository.query(vo);
        String searchKey = vo.getSearchKey();
        String assetgroundId = vo.getAssetgroundId();
//        Short enable = vo.getEnable();
        List<FreightAddressVo> vos = getVos(rootData);
		if(!Strings.isNullOrEmpty(searchKey)||!Strings.isNullOrEmpty(assetgroundId)){
			List<FreightAddressVo> list2 = getVos(rootData);
			return list2;
		}
		FastTreeUtils<FreightAddressVo> fastTreeUtils = new FastTreeUtils<FreightAddressVo>();
		List<FreightAddressVo> list = fastTreeUtils.buildTreeData2(vos, 0, comparatorVo, new TreeRootCallBack<FreightAddressVo>() {

			@Override
			public boolean isRoot(FreightAddressVo v) {
				return Strings.isNullOrEmpty(v.getParentId()) ? true : false;
			}
		});

		return list;
    }
    
    /**
     * 根据父节点状态修改子节点状态为停用
     * @param fid
     */
    @Transactional
    public void updateChildsEnable(String fid){
    	repository.updateChildsEnable(fid);

		redisService.remove(getCacheKey());
    }

	/**
	 * 根据父节点找到所有子节点（包含子，子子...）
	 * @param parent
	 * @return
	 */
	public List<FreightAddress> findAllChildren(FreightAddress parent){
		String fullParentId = null;
		if(Strings.isNullOrEmpty(parent.getFullParentId())){
			fullParentId = parent.getFid()+"%";
		}else{
			fullParentId = parent.getFullParentId()+","+parent.getFid()+"%";
		}

    	return repository.findByFullParentId(fullParentId);
	}
	/**
	 * 根据父ID查找第一个子科目 
	 * @param fid
	 * @return
	 */
	public FreightAddressVo queryByParentId(String fid){
		FreightAddress info = repository.queryByParentId(fid);
		if(info!=null && info.getFlag()==0){
			FreightAddressVo vo = queryByParentId(info.getFid());
			if(vo.getFlag()==0){
				queryByParentId(fid);
			}else{
				return vo;
			}
		} 
		return getVo(info);
	}
	/**
	 * 根据id查找父节点
	 * @param fid
	 * @return
	 */
	public FreightAddressVo queryFullParentById(String fid){
		FreightAddress info = repository.findOne(fid);
		if(info.getLevel()==0){
			return getVo(info);
		}else{
			FreightAddressVo parent = queryFullParentById(info.getParent().getFid());
			if(parent.getLevel()==0){
				return parent;
			}
		}
		return null;
	}

	@Override
	public String getCacheName() {
		return BaseConstant.ADDRESS;
	}
}
