package cn.fooltech.fool_ops.component.core;

import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import java.io.Serializable;
import java.math.BigDecimal;

public interface FoolJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    public EntityManager getEntityManager();

    public default Expression round(CriteriaBuilder builder, Expression expression) {
        return new RoundFunction<BigDecimal>((CriteriaBuilderImpl) builder, expression);
    }
}
