package cn.fooltech.fool_ops.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.sysman.service.MupdateRecordService;
import cn.fooltech.fool_ops.domain.sysman.vo.MupdateRecordVo;
import io.swagger.annotations.ApiOperation;

/**
*
*
*/
@RestController
@RequestMapping("/api/mupdateRecord")
public class MupdateRecordResource extends AbstractBaseResource{


	@Autowired
	private MupdateRecordService mupdateRecordService;

	@ApiOperation("新增/修改")
	@PutMapping("/save")
    public ResponseEntity<MupdateRecordVo> save(@RequestBody MupdateRecordVo vo) {
        RequestResult result = null;
        try {
            result = mupdateRecordService.save(vo);
            if (result.isSuccess()) {
                String id = (String) result.getData();
                result.setData(mupdateRecordService.getById(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reponse(result);
    }

	@ApiOperation("查询分页")
	@GetMapping("/query")
	public ResponseEntity query(MupdateRecordVo vo, PageParamater paramater) {
		paramater.setPage(1);
		paramater.setRows(Integer.MAX_VALUE);
		paramater.setStart(0);
		Page<MupdateRecordVo> page = mupdateRecordService.query(vo, paramater);
		List<MupdateRecordVo> list = page.getContent();
        return listReponse(list);
	}

	@ApiOperation("删除")
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<MupdateRecordVo> delete(@PathVariable String id) {
        RequestResult result = null;
        try {
            result = mupdateRecordService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reponse(result);
    }
}
