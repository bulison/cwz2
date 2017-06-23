package cn.fooltech.fool_ops.web.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

public abstract class BaseController {

	/**
	 * 绑定日期转换
	 */
	/*@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);// 严格限制日期输入
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}*/
	
}
