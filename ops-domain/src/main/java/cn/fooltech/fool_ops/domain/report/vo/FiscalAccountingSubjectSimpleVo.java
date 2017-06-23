package cn.fooltech.fool_ops.domain.report.vo;

import java.util.Map;

/**
 * <p>表单传输对象- 科目简单信息</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年2月5日
 */
public class FiscalAccountingSubjectSimpleVo {

    /**
     * ID
     */
    private String fid;

    /**
     * 节点标识<br>
     * 1为子节点，0为父节点<br>
     */
    private Short flag;

    /**
     * 核算信息
     */
    private Map<String, String> accountSignMsg;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public Short getFlag() {
        return flag;
    }

    public void setFlag(Short flag) {
        this.flag = flag;
    }

    public Map<String, String> getAccountSignMsg() {
        return accountSignMsg;
    }

    public void setAccountSignMsg(Map<String, String> accountSignMsg) {
        this.accountSignMsg = accountSignMsg;
    }

}
