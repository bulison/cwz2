package cn.fooltech.fool_ops.domain.excelmap.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * <p>Excel关系映射</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月23日
 */
@Entity
@Table(name = "cfg_excel_map")
public class ExcelMap extends OpsEntity {

    public static final boolean ENABLE = true;
    public static final boolean DISABLE = false;
    private static final long serialVersionUID = -1735174222224786049L;
    private String clazz;//类的全名称
    private String field;//属性
    private String cnName;//中文名称
    private Integer sequence;//序号
    private Integer type;//类型标识
    private Boolean fimport;//是否应用到导入
    private Boolean fexport;//是否应用到导出
    private String processor;//处理器类全名
    private Boolean need;//导入时是否必填；当fimport=true时有效
    private Boolean validation;//是否有效：0：无效；1：有效

    @Column(name = "FCLAZZ")
    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Column(name = "FFIELD")
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Column(name = "FCN_NAME")
    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    @Column(name = "FSEQUENCE")
    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Column(name = "FVALIDATION")
    public Boolean getValidation() {
        return validation;
    }

    public void setValidation(Boolean validation) {
        this.validation = validation;
    }

    @Column(name = "FIMPORT")
    public Boolean getFimport() {
        return fimport;
    }

    public void setFimport(Boolean fimport) {
        this.fimport = fimport;
    }

    @Column(name = "FEXPORT")
    public Boolean getFexport() {
        return fexport;
    }

    public void setFexport(Boolean fexport) {
        this.fexport = fexport;
    }

    @Column(name = "FPROCESSOR")
    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    @Column(name = "FTYPE")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "FNEED")
    public Boolean getNeed() {
        return need;
    }

    public void setNeed(Boolean need) {
        this.need = need;
    }
}
