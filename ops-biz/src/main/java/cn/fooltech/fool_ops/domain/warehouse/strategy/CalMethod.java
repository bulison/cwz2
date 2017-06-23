package cn.fooltech.fool_ops.domain.warehouse.strategy;

import com.google.common.collect.Sets;
import lombok.ToString;

import java.util.Set;

/**
 * 运算策略类
 * Created by xjh on 2016/12/21.
 */
@ToString
public class CalMethod {

    //运算方式1
    public Compute com1;

    //运算方式2
    public Compute com2;


    //目标字段
    public String targetField;

    //运算字段1
    public String field1;

    //运算字段2
    public String field2;

    //运算字段3(如果不为空，优先运算)
    public String field3;

    //字段1，2的引用对象的类型
    public Ref fieldRef;

    //计算单据仓库的类型
    public Warehouse warehouse = Warehouse.In;

    public CalMethod(String targetField, String field1, Compute com1, String field2) {
        this.com1 = com1;
        this.targetField = targetField;
        this.field1 = field1;
        this.field2 = field2;
        this.fieldRef = Ref.R10;
    }

    public CalMethod(String targetField, String field1, Compute com1, String field2, Compute com2, String field3) {
        this.com1 = com1;
        this.com2 = com2;
        this.targetField = targetField;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.fieldRef = Ref.R10T;
    }

    public CalMethod(String targetField, String field1, Compute com1, String field2, Compute com2, String field3, Warehouse warehouse) {
        this.com1 = com1;
        this.com2 = com2;
        this.targetField = targetField;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.fieldRef = Ref.R100;
        this.warehouse = warehouse;
    }

    public CalMethod(String targetField, String field1, Compute com1, String field2, Ref ref) {
        this.com1 = com1;
        this.targetField = targetField;
        this.field1 = field1;
        this.field2 = field2;
        this.fieldRef = ref;
    }

    public CalMethod(String targetField, String field1, Compute com1, String field2, Compute com2, String field3, Ref ref) {
        this.com1 = com1;
        this.com2 = com2;
        this.targetField = targetField;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.fieldRef = ref;
    }

    /**
     * 目标 操作符 变量
     * 返回1表示字段取目标字段值, 返回0表示取临时变量字段值
     * @return
     */
    public int firstFieldRef() {
        Set<Ref> refs = Sets.newHashSet(Ref.R00, Ref.R000, Ref.R01, Ref.R001, Ref.R010, Ref.R011);
        if (refs.contains(fieldRef)) {
            return 0;
        }
        return 1;
    }

    /**
     * 目标 操作符 变量
     * 返回1表示字段取目标字段值, 返回0表示取临时变量字段值
     * @return
     */
    public int secondFieldRef() {
        Set<Ref> refs = Sets.newHashSet(Ref.R00, Ref.R000, Ref.R10, Ref.R001, Ref.R100, Ref.R101, Ref.R10T);
        if (refs.contains(fieldRef)) {
            return 0;
        }
        return 1;
    }

    /**
     * 目标 操作符 变量
     * 返回1表示字段取目标字段值, 返回0表示取临时变量字段值，返回2表示取总仓成本单价
     * @return
     */
    public int thirdFieldRef() {
        Set<Ref> refs = Sets.newHashSet(Ref.R000, Ref.R010, Ref.R100, Ref.R110);
        if(fieldRef==Ref.R10T){
            return 2;
        }else if (refs.contains(fieldRef)) {
            return 0;
        }
        return 1;
    }

    /**
     * 是否有三个运算字段
     * @return
     */
    public boolean hasThirdRef(){
        Set<Ref> refs = Sets.newHashSet(Ref.R00, Ref.R01, Ref.R10, Ref.R11);
        if (refs.contains(fieldRef)) {
            return false;
        }
        return true;
    }

    public enum Warehouse {
        In,
        Out
    }

    public enum Compute {
        Add,
        Sub,
        Mul,
        Div
    }

    public enum Ref {
        R00,
        R01,
        R10,
        R11,
        R000,
        R001,
        R010,
        R011,
        R100,
        R101,
        R110,
        R111,
        R10T//从总仓查询再获取
    }
}
