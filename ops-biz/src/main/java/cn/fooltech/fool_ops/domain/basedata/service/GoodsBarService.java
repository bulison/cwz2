package cn.fooltech.fool_ops.domain.basedata.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsBar;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsBarRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsSpecRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.UnitRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsBarVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;


/**
 * <p>单位服务类</p>
 *
 * @author xjh
 * @date 2016-10-26
 */
@Service
public class GoodsBarService extends BaseService<GoodsBar, GoodsBarVo, String> {

    @Autowired
    private GoodsBarRepository barRepo;

    @Autowired
    private GoodsRepository goodsRepo;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UnitRepository unitRepo;

    @Autowired
    private GoodsSpecRepository specRepo;

    @Override
    public CrudRepository<GoodsBar, String> getRepository() {
        return barRepo;
    }

    @Override
    public GoodsBarVo getVo(GoodsBar entity) {
        if (entity == null)
            return null;
        GoodsBarVo vo = new GoodsBarVo();
        vo.setBarCode(entity.getBarCode());
        vo.setCreateTime(DateUtilTools.time2String(entity.getCreateTime()));
        vo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
        vo.setFid(entity.getFid());

        Goods goods = entity.getGoods();
        if (goods != null) {
            vo.setGoodsId(goods.getFid());
            vo.setGoodsName(goods.getName());
            vo.setGoodsCode(goods.getCode());
        }
        GoodsSpec spec = entity.getSpec();
        if (spec != null) {
            vo.setGoodsSpecId(spec.getFid());
            vo.setGoodsSpecName(spec.getName());
            vo.setGoodsSpecGroupId(spec.getParent().getFid());
            vo.setGoodsSpecGroupName(spec.getParent().getName());
        }
        Unit unit = entity.getUnit();
        if (unit != null) {
            vo.setUnitId(unit.getFid());
            vo.setUnitName(unit.getName());
            vo.setUnitGroupId(unit.getParent().getFid());
            vo.setUnitGroupName(unit.getParent().getName());
        }
        return vo;
    }

    /**
     * 查询货品条码列表信息，按照货品条码主键降序排列<br>
     * 默认为第一页，每页大小默认为10<br>
     *
     * @param vo
     */
    public Page<GoodsBarVo> query(GoodsBarVo goodsBarVo, PageParamater pageParamater) {

        String orgId = SecurityUtil.getCurrentOrgId();
        Sort sort = new Sort(Direction.DESC, "createTime");
        PageRequest pageRequest = getPageRequest(pageParamater, sort);

        return getPageVos(barRepo.findPageByGoodsId(orgId, goodsBarVo.getGoodsId(), pageRequest), pageRequest);
    }

    /**
     * 判断条码是否已存在
     *
     * @param barCode
     * @param orgId
     * @return
     */
    public boolean isBarCodeExist(String barCode, String orgId, String excludeId) {
        Long count = null;
        if (Strings.isNullOrEmpty(excludeId)) {
            count = barRepo.countByBarCode(orgId, barCode);
        } else {
            count = barRepo.countByBarCode(orgId, barCode, excludeId);
        }
        if (count != null && count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据条码查询货品信息
     *
     * @param barCode
     * @return
     */
    public GoodsBarVo queryByBar(String barCode) {
        String orgId = SecurityUtil.getCurrentOrgId();
        GoodsBar bar = barRepo.findTopByBarCode(orgId, barCode);

        if (bar == null) {

            Goods goods = goodsRepo.findTopByBarCode(orgId, barCode);
            if (goods == null) return null;

            GoodsBarVo vo = new GoodsBarVo();
            vo.setBarCode(barCode);
            vo.setGoodsId(goods.getFid());
            vo.setGoodsName(goods.getName());
            vo.setGoodsCode(goods.getCode());

            if (goods.getGoodsSpec() != null) {
                GoodsSpec specGroup = goods.getGoodsSpec();
                vo.setGoodsSpecGroupId(specGroup.getFid());
                vo.setGoodsSpecGroupName(specGroup.getName());
            }

            Unit unit = goods.getUnit();
            vo.setUnitId(unit.getFid());
            vo.setUnitName(unit.getName());
            vo.setUnitGroupId(unit.getParent().getFid());
            vo.setUnitGroupName(unit.getParent().getName());

            return vo;
        } else {
            return getVo(bar);
        }
    }

    /**
     * 批量保存s
     *
     * @param vos
     * @return
     */
    @Transactional
    public RequestResult saveMulti(String vos, String goodsId) {

        Goods goods = goodsRepo.findOne(goodsId);
        if (goods == null) {
            return buildFailRequestResult("货品不存在或已被其他用户删除，请刷新再试");
        }

        String orgId = SecurityUtil.getCurrentOrgId();
        User user = SecurityUtil.getCurrentUser();
        Organization curOrg = SecurityUtil.getCurrentOrg();

        List<GoodsBar> list = Lists.newArrayList();
        if (StringUtils.isNotBlank(vos)) {
            JSONArray array = JSONArray.fromObject(vos);
            List bars = (List) JSONArray.toCollection(array, GoodsBarVo.class);
            Iterator iterator = bars.iterator();
            while (iterator.hasNext()) {
                GoodsBarVo vo = (GoodsBarVo) iterator.next();
                String inValid = ValidatorUtils.inValidMsg(vo);
                if (inValid != null) {
                    return buildFailRequestResult(inValid);
                }

                Unit unit = unitRepo.findOne(vo.getUnitId());
                if (unit == null) {
                    return buildFailRequestResult("单位不存在或已被其他用户删除，请刷新再试[" + vo.getBarCode() + "]");
                }

                GoodsSpec spec = null;
                if (!Strings.isNullOrEmpty(vo.getGoodsSpecId())) {
                    spec = specRepo.findOne(vo.getGoodsSpecId());
                }

                String barCode = vo.getBarCode();
                String fid = vo.getFid();
                if (isBarCodeExist(barCode, orgId, fid)) {
                    return buildFailRequestResult("货品条码冲突[" + vo.getBarCode() + "]");
                }
                if (goodsService.isBarCodeExist(orgId, barCode)) {
                    return buildFailRequestResult("货品条码冲突[" + vo.getBarCode() + "]");
                }

                GoodsBar entity = new GoodsBar();
                entity.setFid(null);
                entity.setCreateTime(DateUtilTools.now());
                entity.setCreator(user);
                entity.setUpdateTime(DateUtilTools.now());
                entity.setBarCode(vo.getBarCode());
                entity.setGoods(goods);
                entity.setSpec(spec);
                entity.setUnit(unit);
                entity.setOrg(curOrg);

                list.add(entity);
            }

            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    GoodsBar cur = list.get(i);
                    GoodsBar move = list.get(j);
                    if (cur.getBarCode().equals(move.getBarCode())) {
                        String format = "第{0}行与第{1}货品条码冲突[{2}]";
                        String msg = String.format(format, i + 1, j + 1, move.getBarCode());
                        return buildFailRequestResult(msg);
                    }
                }
            }

            if (!Strings.isNullOrEmpty(goodsId)) {
                List<GoodsBar> barCodes = barRepo.findByGoodsId(goodsId);
                barRepo.deleteInBatch(barCodes);
            }
            for (GoodsBar entity : list) {
                barRepo.save(entity);
            }
            return buildSuccessRequestResult();
        } else {
            return buildFailRequestResult("没有数据");
        }
    }

    /**
     * 根据货品ID，属性ID，单位ID查询
     *
     * @param goodsId
     * @param specId
     * @param unitId
     * @return
     */
    public GoodsBar findByGoods(String goodsId, String specId, String unitId) {
        return barRepo.findTopByGoods(goodsId, specId, unitId);
    }
	/**
	 * 根据货品ID删除
	 */
    @Transactional
	public void deleteByGoodsId(String goodsId){
		barRepo.deleteByGoodsId(goodsId);
	}
}
