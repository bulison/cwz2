package cn.fooltech.fool_ops.component.core;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class MatchDegreeOrder extends Order {

    private static final long serialVersionUID = -6271970876245114539L;

    private final String matchStr;

    public MatchDegreeOrder(Direction direction, String property, String matchStr) {
        super(direction, property);
        this.matchStr = matchStr;
    }

    @Override
    public String toString() {
        String result = " case when locate('" + matchStr + "', " + getProperty()
                + ")=0 then 9999 else locate('" + matchStr + "', " + getProperty() + ") end "
                + getDirection();
        if (!(Sort.NullHandling.NATIVE.equals(getNullHandling()))) {
            result = result + ", " + getNullHandling();
        }

        if (isIgnoreCase()) {
            result = result + ", ignoring case";
        }

        return result;
    }


}
