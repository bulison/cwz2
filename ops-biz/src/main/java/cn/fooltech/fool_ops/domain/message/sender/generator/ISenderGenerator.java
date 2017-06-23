package cn.fooltech.fool_ops.domain.message.sender.generator;

import java.util.Set;

import cn.fooltech.fool_ops.domain.message.sender.factory.Sender;


public interface ISenderGenerator {

	public Set<Sender> construct(String ...code);
}
