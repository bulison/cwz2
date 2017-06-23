package cn.fooltech.fool_ops.web.basedata;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.domain.basedata.BaseConstant;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerService;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerSupplierService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.service.SupplierService;
import cn.fooltech.fool_ops.domain.basedata.vo.CsvVo;
import cn.fooltech.fool_ops.domain.basedata.vo.CustomerVo;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsVo;
import cn.fooltech.fool_ops.domain.basedata.vo.SupplierVo;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.member.vo.MemberVo;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.sysman.service.UserService;
import cn.fooltech.fool_ops.domain.sysman.vo.OrganizationVo;
import cn.fooltech.fool_ops.domain.sysman.vo.UserVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;


/**
 * 基础数据集成接口
 *
 * @author xjh
 */
@Controller
@RequestMapping(value = "/basedata")
public class BaseDataController {

    final Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
    final int timeout =1*60*60*24*31*12;

    @Autowired
    private RedisService redisService;
    /**
     * 客户网页服务类
     */
    @Autowired
    private CustomerService customerService;

    /**
     * 供应商服务类
     */
    @Autowired
    private SupplierService supplierService;

    /**
     * 货品服务类
     */
    @Autowired
    private GoodsService goodsService;
    /**
     * 机构服务类
     */
    @Autowired
    private OrgService orgService;
    /**
     * 人员网页服务类
     */
    @Autowired
    private MemberService memberService;
    /**
     * 人员服务类
     */
    @Autowired
    private UserService userService;
    /**
     * 辅助属性网页服务类
     */
    @Autowired
    private AuxiliaryAttrService attrService;
    
    /**
	 * 收支单位网页服务类
	 */
	@Autowired
	private CustomerSupplierService csvService;

    /**
     * 查找基础数据
     * Customer客户
     * Supplier供应商
     * Goods货品
     * Organization部门
     * Member人员
     * User用户
     * CSV 收支单位
     * AuxiliaryAttr_Area地区
     * AuxiliaryAttr_CustomerType客户类别
     * AuxiliaryAttr_Warehouse仓库
     * AuxiliaryAttr_Education学历
     * AuxiliaryAttr_Credit征信级别
     * AuxiliaryAttr_GoodsType货品类别
     * AuxiliaryAttr_JobStatus在职状况
     * AuxiliaryAttr_CostType费用项目
     * AuxiliaryAttr_SubjectType科目类别
     * AuxiliaryAttr_VoucherWord凭证字
     * AuxiliaryAttr_Currency币别
     * AuxiliaryAttr_Project财务项目
     * AuxiliaryAttr_Abstract摘要
     * AuxiliaryAttr_SettlementType结算方式
     * AuxiliaryAttr_Asset固定资产类型
     * AuxiliaryAttr_Print打印类型
     * <p>
     * 例如：param=Customer,Supplier
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/query", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> query(String param) {

        long time1 = System.currentTimeMillis();

        Map<String, Object> cache = Maps.newHashMap();
        List<String> params = splitter.splitToList(param);

        String orgId = SecurityUtil.getCurrentOrgId();
        String accId = SecurityUtil.getFiscalAccountId();

        String scene = orgId + ":" + accId;

        for (String item : params) {
            if (item.contains(BaseConstant.CUSTOMER)) {
                String key = BaseConstant.CUSTOMER + ":" + scene;
                CustomerVo vo = new CustomerVo();
                List<CustomerVo> customerVos = null;

                customerVos = redisService.get(String.valueOf(key),
                        new TypeReference<List<CustomerVo>>() {});
                if (customerVos == null) {
                    customerVos = customerService.vagueSearch(vo);
                    	redisService.set(key, customerVos, timeout);
                }
                cache.put(BaseConstant.CUSTOMER, customerVos);
            } else if (item.contains(BaseConstant.SUPPLIER)) {
                SupplierVo vo = new SupplierVo();
                String key = BaseConstant.SUPPLIER + ":" + scene;
                List<SupplierVo> supplierVos = redisService.get(key,
                        new TypeReference<List<SupplierVo>>(){});
                if (supplierVos == null) {
                    supplierVos = supplierService.vagueSearch(vo);
                    redisService.set(key,supplierVos,timeout);
                }
                cache.put(BaseConstant.SUPPLIER, supplierVos);

            } else if (item.contains(BaseConstant.GOODS)) {
                GoodsVo vo = new GoodsVo();
                String key = BaseConstant.GOODS + ":" + scene;
                List<GoodsVo> goodsVos=redisService.get(key,new TypeReference<List<GoodsVo>>(){});

                if (goodsVos == null) {
                    goodsVos = goodsService.vagueSearch(vo);
                    redisService.set(key, goodsVos,timeout);
                }
                cache.put(BaseConstant.GOODS, goodsVos);

            } else if (item.contains(BaseConstant.DEMARTMENT)) {
                String key = BaseConstant.DEMARTMENT + ":" + scene;

                List<OrganizationVo> orgVos =redisService.get(key,
                        new TypeReference<List<OrganizationVo>>(){});
                if (orgVos == null) {
                    orgVos = orgService.getTreeData();
                    redisService.set(key, orgVos,timeout);
                }
                cache.put(BaseConstant.DEMARTMENT, orgVos);

            } else if (item.contains(BaseConstant.MEMBER)) {
                MemberVo vo = new MemberVo();
                String key = BaseConstant.MEMBER + ":" + scene;

                List<MemberVo> memberVos=redisService.get(key,
                        new TypeReference<List<MemberVo>>(){});
                if (memberVos == null) {
                    memberVos = memberService.vagueSearch(vo);
                    redisService.set(key, memberVos,timeout);
                }

                cache.put(BaseConstant.MEMBER, memberVos);

            } else if (item.contains(BaseConstant.USER)) {
                UserVo vo = new UserVo();

                String key = BaseConstant.USER + ":" + scene;

                List<UserVo> userVos =redisService.get(key,
                        new TypeReference<List<UserVo>>(){});
                if (userVos == null) {
                    userVos = userService.vagueSearch(vo);
                    redisService.set(key, userVos,timeout);
                }
                cache.put(BaseConstant.USER, userVos);

            } else if (item.startsWith(BaseConstant.AUXILIARY_ATTR)) {
                queryAuxiliaryAttr(cache, item, scene);
            } else if (item.startsWith(BaseConstant.CSV)) {
                Page<CsvVo> csvs = csvService.query(new CsvVo(),
                            new PageParamater());

                cache.put(BaseConstant.CSV, csvs.getContent());
            }

        }

        long time2 = System.currentTimeMillis();
        long time = time2 - time1;
        System.out.println("用时：" + time + "ms");

        return cache;
    }

    /**
     * 从缓存查找辅助属性
     */
    private void queryAuxiliaryAttr(Map<String, Object> cache, String baseConstantType, String scene) {
        String key = baseConstantType + ":" + scene;

        List<CommonTreeVo> attrVos=redisService.get(key,  new TypeReference<List<CommonTreeVo>>(){});

        if (attrVos == null) {
            String categoryCode = BaseConstant.getCategoryCode(baseConstantType);
            attrVos = attrService.findSubAuxiliaryAttrTree(categoryCode);
            redisService.set(key,attrVos,timeout);
        }
        cache.put(baseConstantType, attrVos);
    }




	/*@ResponseBody
	@RequestMapping("/testSet")
	public String testSet(@RequestParam(defaultValue="aaa", required=false) String key, @RequestParam(defaultValue="bbb", required=false)String value) {
		redisService.save(key, value);
		return "ok";
	}

	@ResponseBody
	@RequestMapping("/testGet")
	public String testGet(@RequestParam(defaultValue="aaa", required=false) String key) {
		return redisService.select(key, String.class);
	}

	@ResponseBody
	@RequestMapping("/testDelete")
	public String testDelete(@RequestParam(defaultValue="aaa", required=false) String key) {
		redisService.delete(key);
		return "ok";
	}*/
}
