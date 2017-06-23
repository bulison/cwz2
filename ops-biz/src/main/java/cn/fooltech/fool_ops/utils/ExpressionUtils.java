package cn.fooltech.fool_ops.utils;

import cn.fooltech.fool_ops.domain.report.entity.FormulaVo;
import cn.fooltech.fool_ops.domain.report.service.ProfitSheetService;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表达式工具类
 *
 * @author xjh
 */
public class ExpressionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExpressionUtils.class);

    /**
     * AA（科目）：汇总多个科目数据，取科目的所有借方，贷方的发生额；
     * AD（科目）：汇总多个科目数据，取科目的借方发生额；
     * AC（科目）：汇总多个科目数据，取科目的贷方发生额；
     * AS（科目）：汇总多个科目数据，取科目设置中余额方向的发生额；
     * CA（科目：科目）：汇总科目区间段数据，取科目的所有借方，贷方的发生额；
     * CD（科目：科目）：汇总科目区间段数据，取科目的借方发生额；
     * CC（科目：科目）：汇总科目区间段数据，取科目的贷方发生额；
     * CS（科目：科目）：汇总科目区间段数据，取科目设置中余额方向的发生额；
     * DS（行号，行号）：根据行号，累加数据；
     */
    public static final Set<String> FormulaList = Sets.newHashSet("AA", "AD", "AC", "AS", "CA", "CD", "CC", "CS", "DS", "AX", "AY");
    public static final String A_pattern = "^[\\+|-]{0,1}[AA|AD|AC|AS|AX|AY]{2}\\({1}[0-9]+(\\.{1}[0-9]{2})*(,{1}[0-9]+(\\.{1}[0-9]+)*)*\\){1}+$";
    //public static final String C_pattern = "^[\\+|-]{0,1}[CA|CD|CC|CS|CX|CY]{2}\\({1}[0-9]+(\\.{1}[0-9]{2})*:{1}[0-9]+(\\.{1}[0-9]+)*\\){1}+$";
    public static final String D_pattern = "^[\\+|-]{0,1}DS\\({1}[\\+|-]{0,1}[0-9]+(,{1}[\\+|-]{0,1}[0-9]+)*\\){1}+$";

    public static final Pattern patternA = Pattern.compile(A_pattern);
    //public static final Pattern patternC = Pattern.compile(C_pattern);
    public static final Pattern patternD = Pattern.compile(D_pattern);

    public static final String DS_FUNCTION = "DS";

    public static final Splitter splitter_colon = Splitter.on(":").trimResults().omitEmptyStrings();//冒号
    public static final Splitter splitter_comma = Splitter.on(",").trimResults().omitEmptyStrings();//逗号
    public static final Splitter splitter_slash = Splitter.on("/").trimResults().omitEmptyStrings();//斜杠

    /**
     * 检查公式项格式
     */
    public static boolean checkSingleFormula(String formula) {
        if (formula.startsWith("A") || formula.startsWith("+A") || formula.startsWith("-A")) {
            if (!ExpressionUtils.patternA.matcher(formula).matches()) {
                return false;
            }
        }
        /*else if(formula.startsWith("C")||formula.startsWith("+C")||formula.startsWith("-C")){
			if(!ExpressionUtils.patternC.matcher(formula).matches()){
				return false;
			}
		}*/
        else if (formula.startsWith("D") || formula.startsWith("+D") || formula.startsWith("-D")) {
            if (!ExpressionUtils.patternD.matcher(formula).matches()) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 计算表达式的值
     *
     * @param expression
     * @return
     */
    public static BigDecimal computeByAviatorEvaluator(String expression) {
//        System.out.println("expression:" + expression);

        if (Strings.isNullOrEmpty(expression)) return null;
        if (expression.startsWith("+")) expression = expression.substring(1);

        // 编译表达式
        Expression exp = null;
        try{
            exp = AviatorEvaluator.compile(expression);
        }catch (ArithmeticException e){
            e.printStackTrace();
            logger.error("Expression error:"+expression);
            return BigDecimal.ZERO;
        }
        
        Object result = exp.execute();
        if(result.equals(Double.NaN)||result.equals(Double.POSITIVE_INFINITY)||result.equals(Double.NEGATIVE_INFINITY)){
        	return BigDecimal.ZERO;
        }
        if(result.equals(Float.NaN)||result.equals(Float.POSITIVE_INFINITY)||result.equals(Double.NEGATIVE_INFINITY)){
        	return BigDecimal.ZERO;
        }
        BigDecimal calVal = BigDecimal.ZERO;
        if (result instanceof Long) {
            calVal = new BigDecimal((Long) result);
        } else if (result instanceof Double) {
            calVal = new BigDecimal((Double) result);
        } else if (result instanceof Integer) {
            calVal = new BigDecimal((Integer) result);
        } else if (result instanceof Float) {
            calVal = new BigDecimal((Float) result);
        }

        return calVal;
    }


    /**
     * 解析公式
     *
     * @param formula
     * @return
     */
    public static FormulaVo parseFormula(String formula) {

        String fh = "+";
        if (formula.startsWith("+") || formula.startsWith("-")||formula.startsWith("/")||formula.startsWith("*")) {
            fh = formula.substring(0, 1);
            formula = formula.substring(1);
        }
        String name = formula.substring(0, 2);
        String fullName = formula;
        String pamater = formula.substring(3, formula.length() - 1);

        return new FormulaVo(fh, name, fullName, pamater);
    }

    /**
     * 解析C公式
     * @param fromula
     * @return
     */
	/*
	public LimitParamFormulaVo parseCFormula(String formula){
		
		FormulaVo vo = parseFormula(formula);
		LimitParamFormulaVo limit = new LimitParamFormulaVo(vo.getName(),vo.getParamater());
		
		return limit;
	}*/
    /**
     * 根据正则表达式判断公式是否非法
     */
    public static boolean regularCheck(String formula){
		String regEx = "(^[(]*([AD][A-Z][(]+([+-]?\\d*,)*[+-]?\\d+[)]+)([+-/*][(]*[AD][A-Z][(]([+-]?\\d*,)*[+-]?\\d+[)]+)*$)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(formula);
		boolean rs = matcher.matches();
		return rs;
    	
    }
    /**
     * 分解表达式变成子项
     *
     * @param formula
     * @return
     */
    public static boolean splitFormula(String formula, List<String> split) {
        if (Strings.isNullOrEmpty(formula)) return true;
        formula = formula.trim();

        int rkuohao = formula.indexOf(")");
        int lkuohao = formula.indexOf("(");
        if (rkuohao == -1) return false;
        if (lkuohao == -1) return false;

        String subFunction = "";

        for(int i=0;i<formula.length()-4;i++){

            String subStr = formula.substring(i);

            for(String function:ExpressionUtils.FormulaList){
                if(subStr.startsWith(function)){
                    int frkh = subStr.indexOf(")");
                    subFunction = subStr.substring(0, frkh+1);
                    split.add(subFunction);

                    if((frkh+1)>=subStr.length())return false;
                    subStr = subStr.substring(frkh+1);

                    return splitFormula(subStr, split);
                }
            }

        }

        return false;
    }


    /**
     * 获得正负反向函数
     *
     * @return
     */
    public static String getContaryFunction(String function) {
        if (isExpressionLeft(function)) {
            FormulaVo vo = parseFormula(function);
            if (vo.getFh().equals("+")) {
                return "-" + vo.getFullName();
            } else {
                return "+" + vo.getFullName();
            }
        } else {
            return "-" + function;
        }
    }

    /**
     * 没有符号的自动添加正号
     */
    public static String addExpression(String formula) {
        if (Strings.isNullOrEmpty(formula)) {
            return "";
        }
        if (!isExpressionLeft(formula)) {
            formula = "+" + formula;
        }
        return formula;
    }

    /**
     * 判断是否前面有符号
     *
     * @param function
     * @return
     */
    public static boolean isExpressionLeft(String function) {
        if (!Strings.isNullOrEmpty(function)) {
            if (function.startsWith("+") || function.startsWith("-") || function.startsWith("*") || function.startsWith("/")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断正负(正数返回true，负数返回false)
     *
     * @return
     */
    public static boolean isPositveFh(String fh) {
        if ("-".equals(fh)) {
            return false;
        }
        return true;
    }

    /**
     * 去掉左侧符号
     *
     * @param formula
     * @return
     */
    public static String removeExpression(String formula) {
        if (Strings.isNullOrEmpty(formula)) {
            return "";
        }
        if (isExpressionLeft(formula)) {
            return formula.substring(1);
        }
        return formula;
    }

    /**
     * 去掉左侧括号
     *
     * @param formula
     * @return
     */
    public static String removeLeftKh(String formula) {
        if(Strings.isNullOrEmpty(formula))return "";
        if(formula.startsWith("(")){
            formula = formula.substring(1);
            return removeLeftKh(formula);
        }
        return formula;
    }

    /**
     * 获得左侧符号
     *
     * @param formula
     * @return
     */
    public static String getLeftExpression(String formula) {
        if (Strings.isNullOrEmpty(formula)) {
            return "";
        }
        if (isExpressionLeft(formula)) {
            return formula.substring(0, 1);
        }
        return "+";
    }

    /**
     * 符号叠加的处理
     *
     * @return
     */
    public static String computeExpression(String exp1, String exp2) {
        if (exp1.equals("-") && exp2.equals("-")) {
            return "+";
        }
        if (exp1.equals("+") && exp2.equals("+")) {
            return "+";
        }
        return "-";
    }

    /**
     * 是否+开头
     *
     * @param formula
     * @return
     */
    public static boolean isStartWithPlus(String formula) {
        if (Strings.isNullOrEmpty(formula)) {
            return false;
        }
        return formula.startsWith("+");
    }

    /**
     * 是否DS开头
     *
     * @param formula
     * @return
     */
    public static boolean isStartWithDS(String formula) {
        if (Strings.isNullOrEmpty(formula)) {
            return false;
        }
        return formula.startsWith(DS_FUNCTION);
    }

    /**
     * 替换公式，返回计算的表达式
     *
     * @return
     */
    public static String replaceFormula(String fromula, Map<String, BigDecimal> cache) {
        if (Strings.isNullOrEmpty(fromula)) return null;
        String tfromula = fromula;
        for (String replacement : cache.keySet()) {
            //小括号的正则式
            String pReplace = replacement.replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)").replaceAll("\\+", "\\\\+");
            String val = cache.get(replacement).toPlainString();
            String fh = ExpressionUtils.getLeftExpression(replacement);

            tfromula = tfromula.replaceAll(pReplace, val);
        }
        return tfromula;
    }
  
}
