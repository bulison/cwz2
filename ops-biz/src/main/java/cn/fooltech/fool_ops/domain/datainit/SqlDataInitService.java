package cn.fooltech.fool_ops.domain.datainit;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@Service("ops.SqlDataInitService")
public class SqlDataInitService implements ISqlDataInit{

	private static final String INIT_FILE = "classpath:datainit/init_data.sql";

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@Transactional
	public boolean init(LinkedHashMap<String,String> params) {
		
		try {
			
	        File sqlFile = ResourceUtils.getFile(INIT_FILE);
	        List<String> lines = Files.readLines(sqlFile, Charsets.UTF_8);
			
	        for(String line:lines){
	        	if(Strings.isNullOrEmpty(line))continue;
	        	String FID = UUID.randomUUID().toString().replaceAll("-", "");
	        	params.put("FID", FID);
	        	
	        	String exeSql = line;
	        	
	        	for(String key:params.keySet()){
	        		String processKey = ":"+key;
	        		exeSql = exeSql.replaceAll(processKey, params.get(key));
	        	}

				Query query = entityManager.createNativeQuery(exeSql);
				query.executeUpdate();
	        }
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

}
