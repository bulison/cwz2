package cn.fooltech.fool_ops.domain.report.vo;

import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <p>表单传输对象- 系统报表查询条件</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年10月12日
 */
public class SysReportQueryVo implements Serializable {

    private static final long serialVersionUID = 9185688802048413862L;

    /**
     * 系统报表ID
     */
    private String sysReportId;

    /**
     * 查询条件(Json字符串)
     */
    private String condition;

    /**
     * 分页标识，默认分页<br>
     * 0 不分页  1 分页
     */
    private int flag = 1;

    public String getSysReportId() {
        return sysReportId;
    }

    public void setSysReportId(String sysReportId) {
        this.sysReportId = sysReportId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    /**
     * 获取查询条件明细
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<UserTemplateDetailVo> getConditionDetail() {
        List<UserTemplateDetailVo> result = new ArrayList<UserTemplateDetailVo>();
        if (StringUtils.isNotBlank(this.condition)) {
            JSONArray jsonArray = JSONArray.fromObject(this.condition);
            List datas = (List) JSONArray.toCollection(jsonArray, UserTemplateDetailVo.class);
            Iterator iterator = datas.iterator();
            while (iterator.hasNext()) {
                UserTemplateDetailVo vo = (UserTemplateDetailVo) iterator.next();
                result.add(vo);
            }
        }
        return result;
    }

}
