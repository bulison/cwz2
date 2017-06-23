/**
 * 
 */
package cn.fooltech.fool_ops.domain.common.service;

import cn.fooltech.fool_ops.component.core.ImageScale;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.core.filesystem.FileSystem;
import cn.fooltech.fool_ops.component.core.filesystem.FileUtil;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.common.entity.Attach;
import cn.fooltech.fool_ops.domain.common.repository.AttachRepository;
import cn.fooltech.fool_ops.domain.common.vo.AttachVo;
import cn.fooltech.fool_ops.domain.common.vo.Base64Img;
import cn.fooltech.fool_ops.domain.flow.service.FlowAttachService;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>附件服务类</p>
 * @author ljb
 * @version 1.0
 * @date 2014年7月9日
 */
@Service("ops.AttachService")
public class AttachService extends BaseService<Attach, AttachVo, String> {

	/**
	 * 附件repo
	 */
	@Autowired
	private AttachRepository attachRepo;
	
	/**
	 * 文件服务器
	 */
	@Autowired
	private FileSystem fileSystem;

	@Autowired
	private FlowAttachService flowAttachService;
	
	/**
	 * 删除实体同时删除文件
	 */
	public void deleteEntityAndFile(Attach entity) {
		deleteFile(entity);
		attachRepo.delete(entity);
	}

	public void deleteAllById(String... ids) {
		if(ids != null && ids.length > 0) {
			for (String id : ids) {
				deleteById(id);
			}
		}
	}

	@Transactional
	public void deleteById(String id) {
		flowAttachService.deleteByColumn(id);
		deleteEntityAndFile(attachRepo.findOne(id));
	}
	
	/**
	 * 保存附件文件，并生成图片缩略图
	 * @param originPath 原图路径
	 * @param targetPath 目标路径
	 * @param scaleFormat  例：ImageScale.SIZE_8 | ImageScale.SIZE_16 表示同时生成宽度为8、16的图片
	 * @param roundedCroner 圆角半径
	 */
	public boolean processImage(String originPath, String targetPath, String fileNameExtend, int scaleFormat, int roundedCroner) {
		
		//取出文件后缀名称
		String fileSuffix = originPath.substring(originPath.lastIndexOf('.') + 1).toLowerCase();
		String filePath = fileSystem.getRoot() + targetPath;
		
		if(ImageScale.SupportImageType.contains(fileSuffix)){
			
			//TODO 图片缩放后放哪里？是否要关联附件实体？如何关联？
			try {
				ImageScale scale = new ImageScale();
//				scale.scaleByFormat(filePath, filePath, scaleFormat, true, roundedCroner);
				
				String left = targetPath.substring(0, targetPath.indexOf('.'));
				String right = targetPath.substring(targetPath.indexOf('.'));
				String newsaveToFileStr = left + "_" + fileNameExtend + right;
				scale.scaleByFormat(filePath, newsaveToFileStr, scaleFormat, false);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
		return false;
		
	}

	public Attach saveFile(String busId, byte[] buffer, String stuff){
		Attach attach = new Attach();
		attach.setBusId(busId);
		attach.setCreateTime(new Date());
		attach.setCreator(SecurityUtil.getCurrentUser());
		attach.setStatus(Attach.STATUS_INSERT);
		attach.setType(Attach.TYPE_IMAGE);
		attach.setTitle(stuff);

		Date createTime = attach.getCreateTime();

		String year = DateUtilTools.date2String(createTime, "yyyy");
		String yearMonth = DateUtilTools.date2String(createTime, "yyyyMM");
		String yearMonthDay = DateUtilTools.date2String(createTime, "yyyyMMdd");
		String fileName = UUID.randomUUID().toString() + (stuff == null ? "" : "." + stuff);
		String folder = "uploadfiles" + File.separator + year + File.separator + yearMonth + File.separator + yearMonthDay;
		try {
			fileSystem.write(buffer, fileName, folder);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		attach.setFilePath(folder + File.separator + fileName);
		attachRepo.save(attach);
		return attach;
	}
	
	/**
	 * 保存附件文件
	 * @param entity
	 */
	public Attach saveFile(Attach entity) {
		if(entity != null && entity.getFile() != null) {
			File file = entity.getFile();
			if(file != null) {
				String stuff = FileUtil.getSuffix(entity.getFileName());
				Date createTime = entity.getCreateTime();
				if(createTime == null) {
					createTime = new Date();
				}
				
				String year = DateUtilTools.date2String(createTime, "yyyy");
				String yearMonth = DateUtilTools.date2String(createTime, "yyyyMM");
				String yearMonthDay = DateUtilTools.date2String(createTime, "yyyyMMdd");
				String fileName = UUID.randomUUID().toString() + (stuff == null ? "" : "." + stuff);
				String folder = "uploadfiles" + File.separator + year + File.separator + yearMonth + File.separator + yearMonthDay;				
				try {
					fileSystem.write(file, fileName, folder);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				
				entity.setFilePath(folder + File.separator + fileName);
				
			}
		}
		return entity;
	}

	/**
	 * 保存附件文件
	 * @param entity
	 * @param file
	 * @return
	 */
	public Attach saveMultiPartFile(Attach entity, MultipartFile file) {
		if(entity != null) {
			if(file != null) {
				String stuff = FileUtil.getSuffix(entity.getFileName());
				Date createTime = entity.getCreateTime();
				if(createTime == null) {
					createTime = new Date();
				}
				
				String year = DateUtilTools.date2String(createTime, "yyyy");
				String yearMonth = DateUtilTools.date2String(createTime, "yyyyMM");
				String yearMonthDay = DateUtilTools.date2String(createTime, "yyyyMMdd");
				String fileName = UUID.randomUUID().toString() + (stuff == null ? "" : "." + stuff);
				String folder = "uploadfiles" + File.separator + year + File.separator + yearMonth + File.separator + yearMonthDay;				
				try {
					
					fileSystem.write(file.getInputStream(), fileName, folder);
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				
				entity.setFilePath(folder + File.separator + fileName);
			}
		}
		return entity;
	}
	
	/**
	 * 删除附件文件
	 * @param attach
	 */
	protected void deleteFile(Attach attach) {
		if(attach != null && StringUtils.isNotBlank(attach.getFilePath())) {
			String filePath = attach.getFilePath();
			String root = fileSystem.getRoot();
			File file = new File(root + File.separator + filePath);
			if(file.exists()){
				file.delete();
			}
		}
	}

	/**
	 * 增加保存文件处理
	 * 
	 * 存储目录按 年份/月份/日期
	 */
	public void saveWithFile(Attach entity) {
		Date createTime = entity.getCreateTime();
		
		if(createTime == null) {
			createTime = new Date();
			entity.setCreateTime(createTime);
		}
		
		//保存文件
		saveFile(entity);
		attachRepo.save(entity);
	}

	/**
	 * 获取附件，同时加载其文件对象
	 * @param id
	 * @return
	 */
	public Attach getWithFile(String id) {
		Attach entity = attachRepo.findOne(id);
		
		//加载文件
		if(entity != null && StringUtils.isNotBlank(entity.getFilePath())) {
			String filePath = entity.getFilePath();
			String root = fileSystem.getRoot();
			File file = new File(root + File.separator + filePath);	
			entity.setFile(file);
		}		
		
		return entity;
	}
	
	/**
	 * 修改附件状态
	 * @param busId
	 */
	public void updateByBusId(String busId, String status){
		List<Attach> attachs =  attachRepo.findByBusId(busId);  
		if(attachs != null && !attachs.isEmpty()){
			for(Attach attach : attachs){
				attach.setStatus(status);
				attachRepo.save(attach);
			}
		}
	} 
	
	/**
	 * 根据业务ID删除附件
	 * @param busId
	 */
	public void deleteByBusId(String busId) {
		List<Attach> attaches = attachRepo.findByBusId(busId);
		
		if(attaches != null && !attaches.isEmpty()) {
			for (Attach attach : attaches) {
				attachRepo.delete(attach);
			}
		}
	}
	
	/**
	 * 按业务ID同步附件，替换其已存在的附件
	 * @param busId
	 * @param attaches
	 */
	public void syncByBusId(String busId, List<Attach> attaches) {
		Assert.notNull(busId, "必须指定业务ID!");
		
		if(attaches == null || attaches.isEmpty()) {
			return ;
		}
		
		for (Attach attach : attaches) {
			attach.setBusId(busId);
			String status = attach.getStatus();
			if(status == Attach.STATUS_DELETE) {
				attachRepo.delete(attach);
			} else if(status == Attach.STATUS_INSERT) {
				attachRepo.save(attach);
			} else if(status == Attach.STATUS_UPDATE) {
				attachRepo.save(attach);
			} 
		}
		
	}
	
	/**
	 * 根据业务ID获取附件信息
	 * @param busId
	 * @return
	 */
	public List<AttachVo> getAttachVosByBusId(String busId){
		return getVos(attachRepo.findByBusId(busId));
	}

	@Override
	public AttachVo getVo(Attach entity) {
		AttachVo vo = new AttachVo();
		vo.setTitle(entity.getTitle());
		vo.setFid(entity.getFid());
		vo.setType(entity.getType());
		vo.setStatus(entity.getStatus());
		vo.setFileName(entity.getFileName());
		vo.setCreateTime(entity.getCreateTime());
		String path = entity.getFilePath();
		if(path!=null){
			vo.setFilePath(path.replace("\\", "/"));
		}else{
			path = "";
		}
		
		// 创建人
		if(entity.getCreator() != null){
			vo.setCreatorFid(entity.getCreator().getFid());
			vo.setCreatorName(entity.getCreator().getUserName());
		}
		
		return vo;
	}

	@Override
	public CrudRepository<Attach, String> getRepository() {
		return this.attachRepo;
	}

	/**
	 * 保存base64格式的图片
	 * @param base64Strs
	 * @param busId
	 * @return
	 */
	@Transactional
	public RequestResult saveBase64Attach(String base64Strs, String busId){

		List<Attach> exists = attachRepo.findByBusId(busId);

		if(!Strings.isNullOrEmpty(base64Strs)){
			List<Base64Img> datas = JSONArray.parseArray(base64Strs, Base64Img.class);

			for(Base64Img data:datas){
				byte[] buffer = BaseEncoding.base64().decode(data.getBase64());

				saveFile(busId, buffer, data.getImgType());
			}
		}

		for(Attach exist:exists){
			deleteEntityAndFile(exist);
		}

		return buildSuccessRequestResult();
	}

	/**
	 * 获取base64图片
	 * @param busId
	 * @return
	 */
	public List<Base64Img> getImgs(String busId){
		List<Attach> attaches = attachRepo.findByBusId(busId);
		List<Base64Img> imgs = Lists.newArrayList();

		for(Attach attach:attaches){

			try{
				Base64Img img = new Base64Img();

				String filePath = fileSystem.getRoot() + File.separator + attach.getFilePath();

				InputStream is = new FileInputStream(filePath);

				byte[] buffer = FileUtil.toBytes(is);

				img.setBase64(BaseEncoding.base64().encode(buffer));
				img.setImgType(attach.getTitle());
				imgs.add(img);
			}catch (FileNotFoundException e){
				e.printStackTrace();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		return imgs;
	}

}
