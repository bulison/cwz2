package cn.fooltech.fool_ops.domain.basedata.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Bank;
import cn.fooltech.fool_ops.domain.basedata.repository.BankRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.BankVo;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalAccountingSubjectRepository;
import cn.fooltech.fool_ops.domain.initialBank.repository.InitialBankRepository;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * <p>银行服务类</p>
 *
 * @author xjh
 * @date 2016-10-26
 */
@Service
public class BankService extends BaseService<Bank, BankVo, String> {

    private Logger logger = LoggerFactory.getLogger(BankService.class);

    @Autowired
    private BankRepository bankRepo;

    @Autowired
    private FiscalAccountingSubjectRepository subjectRepo;

    @Autowired
    private InitialBankRepository initbankRepo;

    /**
     * 查找列表
     *
     * @param vo
     * @return
     */
    public Page<BankVo> query(BankVo vo, PageParamater paramater) {

        String orgId = SecurityUtil.getCurrentOrgId();
        Sort sort = new Sort(Direction.ASC, "code", "account", "name");
        PageRequest pageRequest = getPageRequest(paramater, sort);
        return getPageVos(bankRepo.findPageBySearchKey(orgId, vo.getKeyWord(), pageRequest), pageRequest);
    }


    /**
     * 保存
     *
     * @param vo
     */
    public RequestResult save(BankVo vo) {

        String inValid = ValidatorUtils.inValidMsg(vo);
        if (inValid != null) {
            return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
        }

        RequestResult result = new RequestResult();

        String orgId = SecurityUtil.getCurrentOrgId();

        Bank bank = null;

        if (isNameExist(orgId, vo.getName(), vo.getFid())) {
            return buildFailRequestResult("名称重复!");
        }
        if (isCodeExist(orgId, vo.getCode(), vo.getFid())) {
            return buildFailRequestResult("编号重复!");
        }
        if (StringUtils.isBlank(vo.getFid())) {
            result = checkAdd(vo);
            if (result.getReturnCode() == RequestResult.RETURN_FAILURE) {
                return result;
            }
            bank = new Bank();
            bank.setOrg(SecurityUtil.getCurrentOrg());
            bank.setDept(SecurityUtil.getCurrentDept());
            bank.setCreateor(SecurityUtil.getCurrentUser());
            bank.setCreateTime(new Date());
        } else {
            bank = bankRepo.findOne(vo.getFid());
            if (bank == null) {
                return buildFailRequestResult("该记录不存在或已被删除!");
            }
            if (checkDataRealTime(vo)) {
                result = checkUpate(vo);
                if (result.getReturnCode() == RequestResult.RETURN_FAILURE) {
                    return result;
                }
            } else {
                return buildFailRequestResult("数据已被其他用户修改，请刷新再试!");
            }
        }

        bank.setUpdateTime(new Date());
        bank.setAccount(vo.getAccount());
        bank.setCode(vo.getCode());
        bank.setName(vo.getName());
        bank.setType(vo.getType());
        bank.setBank(vo.getBank());

        bankRepo.save(bank);

        return result;
    }

    /**
     * @param vo
     * @return
     */
    public List<BankVo> findAll(BankVo vo) {
        String orgId = SecurityUtil.getCurrentOrgId();
        Integer type = vo.getType();
        if (type == null) {
            return getVos(bankRepo.findByOrgId(orgId));
        } else {
            return getVos(bankRepo.findByOrgIdAndType(orgId, type));
        }
    }

    /**
     * 判断名称是否存在
     *
     * @param orgId
     * @param bankName
     * @param excludeId
     * @return
     */
    public boolean isNameExist(String orgId, String bankName, String excludeId) {
        Long count = null;
        if (Strings.isNullOrEmpty(excludeId)) {
            count = bankRepo.countByName(orgId, bankName);
        } else {
            count = bankRepo.countByName(orgId, bankName, excludeId);
        }
        if (count != null && count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断编号是否存在
     *
     * @param orgId
     * @param code
     * @param excludeId
     * @return
     */
    public boolean isCodeExist(String orgId, String code, String excludeId) {
        Long count = null;
        if (Strings.isNullOrEmpty(excludeId)) {
            count = bankRepo.countByCode(orgId, code);
        } else {
            count = bankRepo.countByCode(orgId, code, excludeId);
        }
        if (count != null && count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否能更新
     *
     * @param vo
     * @return
     */
    private RequestResult checkUpate(BankVo vo) {
        RequestResult requestResult = new RequestResult();
        Bank bank = bankRepo.findOne(vo.getFid());
        String orgId = SecurityUtil.getCurrentOrgId();

        if (vo.getType() == Bank.TYPE_BANK) {
            if (!StringUtils.isNotBlank(vo.getAccount())) {
                requestResult.setMessage("类型为银行时账号不能为空!");
                requestResult.setReturnCode(RequestResult.RETURN_FAILURE);
                return requestResult;
            }
        }
        if (StringUtils.isNotBlank(vo.getAccount())
                && !vo.getAccount().equals(bank.getAccount())) {
            Long count = bankRepo.countByAccount(orgId, vo.getAccount());
            if (count != null && count > 0) {
                return buildFailRequestResult("账号有重复!");
            }
        }
        if (!vo.getCode().equals(bank.getCode())) {
            Long count = bankRepo.countByCode(orgId, vo.getCode());
            if (count != null && count > 0) {
                return buildFailRequestResult("编号有重复!");
            }
        }

        return buildSuccessRequestResult();
    }

    /**
     * 判断是否能新增
     *
     * @param vo
     * @return
     */
    private RequestResult checkAdd(BankVo vo) {

        String orgId = SecurityUtil.getCurrentOrgId();
        if (StringUtils.isNotBlank(vo.getAccount())) {
            Long count = bankRepo.countByAccount(orgId, vo.getAccount());
            if (count != null && count > 0) {
                return buildFailRequestResult("账号有重复!");
            }
        }
        if (vo.getType() == Bank.TYPE_BANK) {
            if (!StringUtils.isNotBlank(vo.getAccount())) {
                return buildFailRequestResult("类型为银行时账号不能为空!");
            }
        }
        Long count = bankRepo.countByAccount(orgId, vo.getAccount());
        if (count != null && count > 0) {
            return buildFailRequestResult("账号有重复!");
        }
        return buildSuccessRequestResult();
    }

    /**
     * 更新操作时，校验数据的实时性
     *
     * @param vo 主键、更新时间
     * @return true 有效  false 无效
     */
    private boolean checkDataRealTime(BankVo vo) {
        if (StringUtils.isNotBlank(vo.getFid())) {
            Bank entity = bankRepo.findOne(vo.getFid());
            Date formDate = DateUtilTools.string2Time(vo.getUpdateTime());
            int num = formDate.compareTo(entity.getUpdateTime());
            return num == 0;
        }
        return true;
    }

    /**
     * 自动补全帮助方法
     *
     * @param vo
     * @return
     * @author tjr
     */
    public List<BankVo> keyHandler(BankVo vo) {
        String orgId = SecurityUtil.getCurrentOrgId();
        List<Bank> banks = bankRepo.findByAccount(orgId, vo.getAccount());
        if (null == banks || banks.size() == 0) return null;
        return getVos(banks);
    }

    /**
     * 删除银行实体
     *
     * @param vo
     */
    public RequestResult delete(BankVo vo) {
        //判断是否被现金银行期初引用
        if (initbankRepo.countByBankId(vo.getFid()) > 0) {
            return buildFailRequestResult("数据已被现金银行期初引用，不能删除");
        }

        //科目的引用
        if (subjectRepo.countByRelationId(vo.getFid()) > 0) {
            return buildFailRequestResult("数据已被科目引用，不能删除");
        }

        //收付款的引用
        /*if(paymentBillService.countByBankId(vo.getFid())>0){
			return "0";
		}*/

        bankRepo.delete(vo.getFid());
        return buildSuccessRequestResult();

    }


    @Override
    public BankVo getVo(Bank entity) {
        BankVo bankVo = VoFactory.createValue(BankVo.class, entity);
        bankVo.setType(entity.getType());
        bankVo.setCreator(entity.getCreateor().getFid());
        bankVo.setCreatorName(entity.getCreateor().getUserName());
        bankVo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
        return bankVo;
    }

    @Override
    public CrudRepository<Bank, String> getRepository() {
        return bankRepo;
    }


}
