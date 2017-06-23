package cn.fooltech.fool_ops.taglib;

import cn.fooltech.fool_ops.utils.SecurityUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.util.Stack;
import java.util.StringTokenizer;


/*
 * lixiaolin
 * 2015-05-19
 * 权限控制
 */
public class TagOpt extends javax.servlet.jsp.tagext.TagSupport {
    protected String optCode;

    protected static String[] splitString(String srcStr, String sparator) {
        String[] str = null;
        try {
            if (srcStr == null || "".equals(srcStr)) {
                return str;
            }
            Stack<String> stk = new Stack<String>();
            StringTokenizer st = new StringTokenizer(srcStr, sparator);
            while (st.hasMoreTokens()) {
                stk.push(st.nextToken());
            }
            int size = stk.size();
            if (size > 0) {
                str = new String[size];
                for (int i = size - 1; i >= 0; i--) {
                    str[i] = stk.pop();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public String getOptCode() {
        return optCode;
    }

    public void setOptCode(String optCode) {
        this.optCode = optCode;
    }

    @Override
    public int doEndTag() throws JspException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int doStartTag() throws JspException {
        String optStr = this.getOptCode();
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

    @Override
    public Tag getParent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setParent(Tag arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void release() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPageContext(PageContext arg0) {
        // TODO Auto-generated method stub

    }

//	public static void main(String[] args) {
//		String opt = "opt1&opt2&opt3&opt4";
//		String[] opts = splitString(opt, "|");
//		System.out.println(opts.length);
//
//	}
}
