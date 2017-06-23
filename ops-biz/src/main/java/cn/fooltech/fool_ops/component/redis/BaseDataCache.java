package cn.fooltech.fool_ops.component.redis;

import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * Created by Derek on 2017/1/4.
 */
public interface BaseDataCache {

    public String getCacheName();

    public default String getCacheKey(){
        String orgId = SecurityUtil.getCurrentOrgId();
        String accId = SecurityUtil.getFiscalAccountId();
        String key = getCacheName()+":"+orgId+":"+accId;
        return key;
    }

}
