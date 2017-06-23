package cn.fooltech.fool_ops.web.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.entity.Dictionary;
import cn.fooltech.fool_ops.domain.base.service.DictionaryService;
import cn.fooltech.fool_ops.domain.base.vo.DictionaryVo;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/Dictionary")
public class DictionaryController {
	@Autowired
	private DictionaryService dictionaryService;
	/*
	 * 查找全部字典
	 */
	@ApiOperation("显示所有字典")
	@GetMapping("/queryall")
	public Page<DictionaryVo> queryAll(PageParamater pageParamater){
		return dictionaryService.queryAll(pageParamater);
	}
	/*
	 * 根据ID查找单个字典
	 */
	@ApiOperation("根据主键查找某个字典")
	@GetMapping("/queryById/{id}")
	public DictionaryVo queryById(@PathVariable String id){
		return dictionaryService.queryById(id);
	}
	/**
	 * 根据key查找单个字典
	 * @param vo
	 * @return
	 */
	@ApiOperation("根据key查找某一个字典")
	@GetMapping("/queryOneByKey/{key}")
	public Dictionary queryOneByKey(@PathVariable String key){
		return dictionaryService.queryOneByKey(key);
	}
	/**
	 * 根据KEY查找字典
	 * @param vo
	 * @return
	 */
	@ApiOperation("根据key查找字典")
	@GetMapping("/queryByKey/{key}")
	public List<Dictionary> queryByKey(@PathVariable String key){
		return dictionaryService.queryByKey(key);
	}
	/*
	 * 添加或修改字典
	 */
	@ApiOperation("添加或修改字典")
	@PutMapping("/save")
	public RequestResult save(DictionaryVo vo){
		return dictionaryService.save(vo);
	}
	/**
	 * 根据id删除字典
	 * @param id
	 * @return
	 */
	@ApiOperation("删除字典(根据主键)")
	@DeleteMapping("/delete/{id}")
	public RequestResult delete(@PathVariable String id){
		return dictionaryService.deleteDictionaryById(id);
	}
	
}
