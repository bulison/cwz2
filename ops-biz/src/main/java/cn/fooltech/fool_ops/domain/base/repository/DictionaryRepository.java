package cn.fooltech.fool_ops.domain.base.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.fooltech.fool_ops.domain.base.entity.Dictionary;
import cn.fooltech.fool_ops.domain.base.vo.DictionaryVo;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, String>{
/**
 * 查找所有字典并且分页	
 * @param pageable 页码参数
 * @return
 */
	public default Page<Dictionary> query(Pageable pageable){
		return findAll(pageable);
	};
	
	/**
	 * 通过KEY查找某一个字典
	 * @param key
	 * @return
	 */
	//@Query ("select d from Dictionary d WHERE fkey=?1 limit 0,1")
	@Query(nativeQuery=true,value = "select * from smg_tdict where fkey=?1 limit 0,1")
	public Dictionary queryOneByKey(String key);
	/**
	 * 根据key查找字典
	 */
	@Query("select D from Dictionary D where fkey=?1")
	public List<Dictionary> queryByKey(String key);
}
