package cn.fooltech.fool_ops.domain.message.dao;

import cn.fooltech.fool_ops.domain.message.vo.TodoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 消息mapper
 */
@Mapper
public interface MessageDao{

    /**
     * 找待办任务分页
     * @param accId
     * @param receiverId
     * @return
     */
    public List<TodoVo> findTodoPage(@Param("accId") String accId,
                                     @Param("receiverId") String receiverId);
}
