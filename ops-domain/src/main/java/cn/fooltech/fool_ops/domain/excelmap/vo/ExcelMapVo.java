package cn.fooltech.fool_ops.domain.excelmap.vo;

import java.io.Serializable;

public class ExcelMapVo implements Serializable {

    private static final long serialVersionUID = 8782675186210014927L;

    private String fid;
    private String clazz;//类的全名称
    private String field;//属性
    private String cnName;//中文名称
    private Integer sequence;//序号
    private Integer type;//类型标识
    private Integer fimport;//是否应用到导入
    private Integer fexport;//是否应用到导出
    private String processor;//处理器类全名
    private Integer need;//导入时是否必填；当fimport=true时有效
    private Integer validation;//是否有效：0：无效；1：有效

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFimport() {
        return fimport;
    }

    public void setFimport(Integer fimport) {
        this.fimport = fimport;
    }

    public Integer getFexport() {
        return fexport;
    }

    public void setFexport(Integer fexport) {
        this.fexport = fexport;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public Integer getNeed() {
        return need;
    }

    public void setNeed(Integer need) {
        this.need = need;
    }

    public Integer getValidation() {
        return validation;
    }

    public void setValidation(Integer validation) {
        this.validation = validation;
    }
}
