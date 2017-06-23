package cn.fooltech.fool_ops.domain.voucher.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.common.excelProcessor.impl.BillTypeProcessor;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.voucher.entity.BillSubject;
import cn.fooltech.fool_ops.domain.voucher.entity.BillSubjectDetail;
import cn.fooltech.fool_ops.domain.voucher.repository.BillSubjectRepository;
import cn.fooltech.fool_ops.domain.voucher.vo.BillSubjectDetailVo;
import cn.fooltech.fool_ops.domain.voucher.vo.BillSubjectVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.ExcelUtils.ImportType;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import net.sf.json.JSONArray;

/**
 * <p>单据、会计科目关联模板网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年12月2日
 */
@Service
public class BillSubjectService extends BaseService<BillSubject, BillSubjectVo, String>{
	private final String TEMPLATE_PATH = "classpath:xlstemplate/";

	@Autowired
	private BillSubjectRepository bsRepo;

	@Autowired
	private BillSubjectDetailService bsdService;

	/**
	 * 辅助属性服务类
	 */
	@Autowired
	private AuxiliaryAttrService auxiliaryAttrService;
	
	/**
	 * 辅助属性网页服务类
	 */
	@Autowired
	private AuxiliaryAttrService attrService; 
	/**
	 * 财务-科目服务类
	 */
	@Autowired
	private FiscalAccountingSubjectService fiscalAccountingSubjectService;
	/**
	 * 分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<BillSubjectVo> query(BillSubjectVo vo, PageParamater paramater){
		
		Sort sort = new Sort(Direction.DESC, "createTime");
		//财务账套
		String accId = SecurityUtil.getFiscalAccountId();
		PageRequest pageRequest = getPageRequest(paramater, sort);
		
		Page<BillSubject> page = bsRepo.findPageBy(accId, vo.getBillType(), pageRequest);
		
		return getPageVos(page, pageRequest);
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public RequestResult save(BillSubjectVo vo){
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return new RequestResult(RequestResult.RETURN_FAILURE,inValid);
		}
		String fid = vo.getFid();
		String remark = vo.getRemark();
		Integer billType = vo.getBillType();
		String templateCode = vo.getTemplateCode();
		String templateName = vo.getTemplateName();
		String voucherWordId = vo.getVoucherWordId();
		Date now = Calendar.getInstance().getTime();
		
		//财务账套
		FiscalAccount fiscalAccount = SecurityUtil.getFiscalAccount();
		if(fiscalAccount == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "财务账套不存在!");
		}
		
		BillSubject entity = null;
		if(StringUtils.isBlank(fid)){
			entity = new BillSubject();
			entity.setCreateTime(now);
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setFiscalAccount(fiscalAccount);
		}
		else{
			entity = bsRepo.findOne(fid);
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
		}
		entity.setRemark(remark);
		entity.setUpdateTime(now);
		entity.setBillType(billType);
		entity.setTemplateCode(templateCode);
		entity.setTemplateName(templateName);
		entity.setFiscalAccount(fiscalAccount);
		entity.setVoucherWord(auxiliaryAttrService.get(voucherWordId));
		
		bsRepo.save(entity);
		
		if(!StringUtils.isBlank(fid)){
			bsdService.deleteByTemplateId(entity.getFid());
		}
		
		//保存模板明细
		if(StringUtils.isNotBlank(vo.getDetails())){
			JSONArray jsonArray = JSONArray.fromObject(vo.getDetails());
			List list = (List) JSONArray.toCollection(jsonArray, BillSubjectDetailVo.class);
			Iterator iterator = list.iterator();
			while(iterator.hasNext()){
				BillSubjectDetailVo detail = (BillSubjectDetailVo) iterator.next();
				detail.setBillSubjectId(entity.getFid());
				String inValid2 = ValidatorUtils.inValidMsg(detail);
				if(inValid2!=null){
					return new RequestResult(RequestResult.RETURN_FAILURE,inValid2);
				}
				bsdService.save(detail);
			}
		}
		return new RequestResult();
	}
	
	/**
	 * 删除
	 * @param id 模板ID
	 * @return
	 */
	@Transactional
	public RequestResult delete(String fid){
		BillSubject billSubject = bsRepo.findOne(fid);
		if(billSubject == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "该模板不存在或已被删除!");
		}
		bsdService.deleteByTemplateId(fid);
		bsRepo.delete(billSubject);
		return new RequestResult();
	}
	
	/**
	 * 单个实体转vo
	 * @param entity
	 * @param loadDetail 是否加载明细
	 * @return
	 */
	public BillSubjectVo getVo(BillSubject entity, boolean loadDetail){
		BillSubjectVo vo = new BillSubjectVo();
		vo.setFid(entity.getFid());
		vo.setRemark(entity.getRemark());
		vo.setBillType(entity.getBillType());
		vo.setTemplateCode(entity.getTemplateCode());
		vo.setTemplateName(entity.getTemplateName());
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS));
		//凭证字
		AuxiliaryAttr voucherWord = entity.getVoucherWord();
		if(voucherWord != null){
			vo.setVoucherWordId(voucherWord.getFid());
			vo.setVoucherWordName(voucherWord.getName());
		}
		//模板明细
		if(loadDetail){
			List<BillSubjectDetail> details = bsdService.getByTemplateId(entity.getFid());
			List<BillSubjectDetailVo> detailVos = bsdService.getVos(details);
			vo.setDetails(JSONArray.fromObject(detailVos).toString());
		}
		return vo;
	}
	
	/**
	 * 添加制作凭证模版，跟模板明细
	 * @param voBeans  excel转换好的数据格式
	 * @return
	 * @throws InterruptedException
	 */
	@Transactional
	public RequestResult initBillSubject(List<ImportVoBean> voBeans) throws InterruptedException{
		if (voBeans!=null&&voBeans.size()>0) {
			
			Organization org = SecurityUtil.getCurrentOrg();
			FiscalAccount fiscalAccount = SecurityUtil.getFiscalAccount();
			User user = SecurityUtil.getCurrentUser();
			
			for (int i = 0; i < voBeans.size(); i++) {
				Thread.currentThread();
				Thread.sleep(5);//毫秒 
				Date date = new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSS");//其中yyyy-MM-dd是你要表
				//模板编号(用时间截取到毫秒数)
				String ftemplateCode = sdf.format(date);
				//获取表单传输对象- 单据、会计科目关联模板
				BillSubjectVo vo = (BillSubjectVo) voBeans.get(i).getVo();
				String fid = vo.getFid();
				Integer billType = vo.getBillType();
				Date now = Calendar.getInstance().getTime();

				//获取凭证字
				List<CommonTreeVo> attrTree = attrService.findSubAuxiliaryAttrTree("010");
				if (vo!=null&&attrTree!=null) {
					BillSubject entity = null;
					BillSubjectDetail detail = null;
					Set<CommonTreeVo> children = attrTree.get(0).getChildren();
					Iterator<CommonTreeVo> it = children.iterator();
					CommonTreeVo treeVo = it.next();
					if(StringUtils.isBlank(fid)){
						entity = new BillSubject();
						entity.setCreateTime(now);
						entity.setOrg(org);
						entity.setCreator(user);
						entity.setFiscalAccount(fiscalAccount);
						entity.setVoucherWord(auxiliaryAttrService.get(attrTree.get(0).getId()));
						entity.setUpdateTime(now);
						entity.setBillType(billType);
						entity.setTemplateCode(ftemplateCode);
						BillTypeProcessor processor = new BillTypeProcessor();
						String templateName = processor.process(billType);
						entity.setTemplateName(templateName);
						entity.setFiscalAccount(fiscalAccount);
						//设置凭证字
						entity.setVoucherWord(auxiliaryAttrService.get(treeVo.getId()));
						//保存制作凭证
						bsRepo.save(entity);
						String jsubjectName = vo.getJsubjectName();
						String dsubjectName = vo.getDsubjectName();
						//借方科目名称不为空，则添加制作凭证明细
						if (!Strings.isNullOrEmpty(jsubjectName)) {
							String[] split = jsubjectName.split("_");
							//借款科目类型
							String jcode=split[0];
							if(!Strings.isNullOrEmpty(jcode)){
								detail = new BillSubjectDetail();
								detail.setHedge(null);
								//获取科目id编号
								FiscalAccountingSubject subject = fiscalAccountingSubjectService.getByCode(jcode);
								if(subject!=null){
									detail.setSubject(subject);
									detail.setAmountSource(1);
									detail.setBillSubject(entity);
									detail.setCreateTime(now);
									detail.setDirection(1);
									detail.setFiscalAccount(fiscalAccount);
									detail.setCreator(user);
									detail.setOrg(org);
									if (vo.getHedge()!=null) {
										//红蓝对冲
										detail.setHedge(vo.getHedge());
									}else{
										//默认为蓝
										detail.setHedge(BillSubjectDetail.HEDGE_BLUE);
									}
									detail.setOrg(org);
									detail.setSubjectSource(1);
									detail.setUpdateTime(now);
									bsdService.save(detail);
								}
							}
						}
						//贷方科目名称不为空，则添加制作凭证明细
						if (!Strings.isNullOrEmpty(dsubjectName)) {
							
							String[] split = dsubjectName.split("_");
							//贷款科目类型
							String dcode=split[0];
							if(!Strings.isNullOrEmpty(dcode)){
								detail = new BillSubjectDetail();
								detail.setHedge(null);
								//获取科目id编号
								FiscalAccountingSubject subject = fiscalAccountingSubjectService.getByCode(dcode);
								if(subject!=null){
									detail.setSubject(subject);
									detail.setAmountSource(1);
									detail.setBillSubject(entity);
									detail.setCreateTime(now);
									detail.setDirection(-1);
									detail.setFiscalAccount(fiscalAccount);
									detail.setCreator(user);
									detail.setOrg(org);
									if (vo.getHedge()!=null) {
										//红蓝对冲
										detail.setHedge(vo.getHedge());
									}
									else{
										//默认为蓝
										detail.setHedge(BillSubjectDetail.HEDGE_BLUE);
									}
									detail.setOrg(org);
									detail.setSubjectSource(1);
									detail.setUpdateTime(now);
									bsdService.save(detail);
								}
							}
						}
					}
				}
			}
		}
		return new RequestResult();
	}
	/**
	 * 初始化制作凭证模版,导入excel数据
	 * @param voBeans excel 转换数据
	 * @return
	 * @throws InterruptedException 
	 */
	public RequestResult initBillSubject(){
		try {
			String downDecode = URLDecoder.decode("制作凭证.xlsx", "utf-8");
			File file = ResourceUtils.getFile(TEMPLATE_PATH+downDecode);
			if(!file.exists() || !file.canRead()){
				return buildFailRequestResult("凭证初始化模板不存在");
			}
			InputStream is = new FileInputStream(file);	
			List<ImportVoBean> voBeans = Lists.newArrayList();
			RequestResult result = ExcelUtils.importExcel(BillSubjectVo.class, is, ImportType.SEQUNENCE, voBeans, "xlsx");
			if(result.isSuccess()){
				initBillSubject(voBeans);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return buildFailRequestResult("初始化制作凭证模版解析出错");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return buildFailRequestResult("初始化制作凭证模版解析出错");
		} catch (InterruptedException e) {
			e.printStackTrace();
			return buildFailRequestResult("初始化制作凭证模版解析出错");
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("初始化制作凭证模版异常");
		}
		return buildSuccessRequestResult();
	}

	@Override
	public BillSubjectVo getVo(BillSubject entity) {
		return getVo(entity, false);
	}

	@Override
	public CrudRepository<BillSubject, String> getRepository() {
		return bsRepo;
	}
}
