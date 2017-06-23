package cn.fooltech.fool_ops.domain.analysis.vo;

/**
 * Created by Derek on 2017/4/26.
 */
public class BillTitle{

    private String placeId;
    private String placeName;
    private String parentPlaceId;
    private String parentPlaceName;
    private Integer sort = 0;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getParentPlaceId() {
        return parentPlaceId;
    }

    public void setParentPlaceId(String parentPlaceId) {
        this.parentPlaceId = parentPlaceId;
    }

    public String getParentPlaceName() {
        return parentPlaceName;
    }

    public void setParentPlaceName(String parentPlaceName) {
        this.parentPlaceName = parentPlaceName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BillTitle){
            BillTitle data = (BillTitle)obj;
            if(data.getPlaceId().equals(placeId))return true;
        }
        return super.equals(obj);
    }
}
