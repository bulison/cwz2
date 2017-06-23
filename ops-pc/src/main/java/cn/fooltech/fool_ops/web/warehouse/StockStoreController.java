package cn.fooltech.fool_ops.web.warehouse;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsSpecService;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsSpecVo;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsVo;
import cn.fooltech.fool_ops.domain.warehouse.service.StockStoreService;
import cn.fooltech.fool_ops.domain.warehouse.vo.StockStoreVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <p>即时分仓库存网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2016年4月12日
 */
@Controller
@RequestMapping("/stockStore")
public class StockStoreController {

	@Autowired
	private StockStoreService stockStoreService;
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private GoodsSpecService specService;
	
	/**
	 * 查询即时分仓库存
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public List<StockStoreVo> query(StockStoreVo stockVo){
		return stockStoreService.query(stockVo);
	}
	
	/**
	 * 查询即时分仓库存
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryTreeFormat")
	public JSONObject queryTreeFormat(StockStoreVo stockVo){
		List<StockStoreVo> stocks = stockStoreService.query(stockVo);
		
		//货品、属性、仓库
		Map<String, Map<String, Map<String, StockStoreVo>>> cache = Maps.newLinkedHashMap();
		
		String goodsName = null, specName = null;
		GoodsVo goodsVo = goodsService.getById(stockVo.getGoodsId());
		goodsName = goodsVo.getName();
		
		GoodsSpecVo specVo = null;
		
		if(!Strings.isNullOrEmpty(stockVo.getGoodsSpecId())){
			specVo = specService.getById(stockVo.getGoodsSpecId());
			specName = specVo.getName();
		}
		
		for(StockStoreVo vo:stocks){
			String goodsId = vo.getGoodsId();
			String specId = goodsId+"#"+(vo.getGoodsSpecId()==null?"":vo.getGoodsSpecId());
			String warehouseId = specId+"#"+vo.getWarehouseId();
			if(cache.containsKey(goodsId)){
				Map<String, Map<String, StockStoreVo>> cache2 = cache.get(goodsId);
				if(cache2.containsKey(specId)){
					Map<String, StockStoreVo> cache3 = cache2.get(specId);
					cache3.put(warehouseId, vo);
				}else{
					Map<String, StockStoreVo> cache3 = Maps.newLinkedHashMap();
					cache3.put(warehouseId, vo);
					cache2.put(specId, cache3);
				}
			}else{
				Map<String, StockStoreVo> cache3 = Maps.newLinkedHashMap();
				cache3.put(warehouseId, vo);
				
				Map<String, Map<String, StockStoreVo>> cache2 = Maps.newLinkedHashMap();
				cache2.put(specId, cache3);
				
				cache.put(goodsId, cache2);
			}
		}
		
		JSONArray array1 = new JSONArray();
		
		for(String iter1:cache.keySet()){
			JSONObject json1 = new JSONObject();
			
			Map<String, Map<String, StockStoreVo>> iter2Map = cache.get(iter1);
			
			JSONArray array2 = new JSONArray();
			
			for(String iter2:iter2Map.keySet()){
				
				JSONObject json2 = new JSONObject();
				Map<String, StockStoreVo> iter3Map = iter2Map.get(iter2);
				
				JSONArray array3 = new JSONArray();
				for(StockStoreVo iter3:iter3Map.values()){
				
					JSONObject json3 = new JSONObject();
					
					json1.put("goodsId", iter3.getGoodsId());
					json1.put("goodsCode", iter3.getGoodsCode());
					json1.put("goodsName", iter3.getGoodsName());
					
					json2.put("goodsSpecId", iter3.getGoodsSpecId()==null?"":iter3.getGoodsSpecId());
					json2.put("goodsSpecName", iter3.getGoodsSpecName()==null?"":iter3.getGoodsSpecName());
					
					json3.put("warehouseId", iter3.getWarehouseId());
					json3.put("warehouseName", iter3.getWarehouseName());
					json3.put("accountQuentity", iter3.getAccountQuentity());
					json3.put("checkoutQuentity", iter3.getCheckoutQuentity());
					
					array3.add(json3);
				}
				json2.put("children", array3);
				array2.add(json2);
			}
			json1.put("children", array2);
			array1.add(json1);
		}
		JSONObject result = new JSONObject();
		result.put("data", array1);
		result.put("goodsName", goodsName);
		result.put("specName", specName);
		return result;
	}
	
}
