package cn.fooltech.fool_ops.domain.common.entity;

/**
 * <p>导入的辅助类</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月26日
 */
public class ImportVoBean {

    private Integer row;//行数
    private Boolean vaild;//合法
    private String msg;//信息
    private Object vo;//vo对象


    /**
     * <p>未通过验证</p>
     *
     * @param row
     * @param msg
     */
    public ImportVoBean(Integer row, String msg) {
        super();
        this.row = row;
        this.vaild = false;
        this.msg = msg;
    }


    /**
     * <p>通过验证</p>
     *
     * @param row
     * @param msg
     */
    public ImportVoBean(Integer row, Object vo) {
        super();
        this.row = row;
        this.vaild = true;
        this.vo = vo;
    }


    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Boolean getVaild() {
        return vaild;
    }

    public void setVaild(Boolean vaild) {
        this.vaild = vaild;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getVo() {
        return vo;
    }

    public void setVo(Object vo) {
        this.vo = vo;
    }
}
