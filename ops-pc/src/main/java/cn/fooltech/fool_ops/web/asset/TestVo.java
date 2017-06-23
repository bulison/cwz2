package cn.fooltech.fool_ops.web.asset;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Derek on 2016/12/13.
 */
public class TestVo {

    @JSONField(format = "yyyy-MM-dd")
    private Date adate;

    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date bdate;

    private Date cdate;

    private String name;

    public Date getAdate() {
        return adate;
    }

    public void setAdate(Date adate) {
        this.adate = adate;
    }

    public Date getBdate() {
        return bdate;
    }

    public void setBdate(Date bdate) {
        this.bdate = bdate;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
