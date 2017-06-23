package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.PageService;
import cn.fooltech.fool_ops.web.rest.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 抽象Resource类
 * Created by xjh on 2016/12/15.
 */
public abstract class AbstractBaseResource implements PageService {

    /**
     * 修改或删除请求转ResponseEntity
     *
     * @param result
     * @return
     */
    public ResponseEntity reponse(RequestResult result) {

        //String body = JsonUtil.toJsonString(result);
        return ResponseEntity.
                ok().body(result);
    }

    /**
     * 页面列表请求转ResponseEntity
     *
     * @param page
     * @return
     */
    public ResponseEntity pageReponse(String namespace, Page page) {
        try {
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/" + namespace);
            return new ResponseEntity(page.getContent(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.
                    badRequest().body(null);
        }

    }

    /**
     * 页面列表请求转ResponseEntity
     *
     * @return
     */
    public ResponseEntity listReponse(List list) {
        return new ResponseEntity(list, HttpStatus.OK);
    }

    /**
     * 页面列表请求转ResponseEntity
     *
     * @return
     */
    public ResponseEntity pageReponse(List list, Pageable pageable, long total) {
        Page page = new PageImpl(list, pageable, total);
        return new ResponseEntity(page, HttpStatus.OK);
    }
}
