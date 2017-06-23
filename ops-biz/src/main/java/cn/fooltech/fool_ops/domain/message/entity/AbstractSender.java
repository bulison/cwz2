package cn.fooltech.fool_ops.domain.message.entity;

import cn.fooltech.fool_ops.domain.message.sender.factory.Sender;

public abstract class AbstractSender implements Sender {

    protected String code;

    public String getCode() {
        return code;
    }

    public abstract void setCode(String code);

    @Override
    public boolean isSupport(String code) {
        return this.code.equals(code);
    }
}
