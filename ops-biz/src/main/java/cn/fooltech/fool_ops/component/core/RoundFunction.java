package cn.fooltech.fool_ops.component.core;

import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.hibernate.jpa.criteria.Renderable;
import org.hibernate.jpa.criteria.compile.RenderingContext;
import org.hibernate.jpa.criteria.expression.function.ParameterizedFunctionExpression;

import javax.persistence.criteria.Expression;
import java.io.Serializable;

/**
 * 保留digit位小数
 *
 * @param <N>
 * @author xjh
 */
public class RoundFunction<N extends Number> extends ParameterizedFunctionExpression<N> implements Serializable {
    private static final long serialVersionUID = -3886248787891736007L;
    private int digit = 2;

    public RoundFunction(CriteriaBuilderImpl criteriaBuilder, Expression expression) {
        super(criteriaBuilder, expression.getJavaType(), "round", new Expression[]{expression});
    }

    public RoundFunction(CriteriaBuilderImpl criteriaBuilder, Expression expression, int digit) {
        super(criteriaBuilder, expression.getJavaType(), "round", new Expression[]{expression});
        this.digit = digit;
    }

    protected boolean isStandardJpaFunction() {
        return false;
    }

    public String render(RenderingContext renderingContext) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(getFunctionName()).append("(");
        renderArguments(buffer, renderingContext);
        buffer.append(')');
        return buffer.toString();
    }

    protected void renderArguments(StringBuilder buffer, RenderingContext renderingContext) {
        String sep = "";
        for (Expression argument : getArgumentExpressions()) {
            buffer.append(sep).append(((Renderable) argument).render(renderingContext));
            sep = ", ";
        }
        buffer.append(sep).append(digit);
    }
}