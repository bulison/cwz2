package cn.fooltech.fool_ops.taglib;

import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.google.common.base.Strings;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;


/*
 * xjh
 * 2016-02-24
 * 权限控制
 */
public class ReportTagOpt extends TagOpt {
    private static final long serialVersionUID = 1028248373954434117L;

    private String reportId;

    @Override
    public int doStartTag() throws JspException {
        if (Strings.isNullOrEmpty(reportId)) {
            return Tag.SKIP_BODY;            //控制标签不显示
        }
        String optStr = this.getOptCode() + ":" + reportId;
        boolean b = false;
        String[] opts = splitString(optStr, "|");
        for (int i = 0; i < opts.length; i++) {
            String opt = opts[i];
            if (SecurityUtil.isPermit(opt)) {
                b = true;
                break;
            }
            int index = opt.indexOf("&");
            if (index > 0) {
                String[] opts2 = splitString(opt, "&");
                b = true;
                for (int j = 0; j < opts2.length; j++) {
                    if (SecurityUtil.isPermit(opts2[j]) == false) {
                        b = false;
                        break;
                    }
                }
                if (b == true) {
                    break;
                }
            }
        }

        if (b) {
            return Tag.EVAL_BODY_INCLUDE;    //控制标 签对所有人显示
        } else {
            return Tag.SKIP_BODY;            //控制标签不显示
        }

    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

}
