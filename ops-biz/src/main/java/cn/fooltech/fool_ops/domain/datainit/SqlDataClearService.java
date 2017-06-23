package cn.fooltech.fool_ops.domain.datainit;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service("ops.SqlDataClearService")
public class SqlDataClearService{

	private static final String ORG_ID_KEY = ":FORG_ID";
	private static final String ACC_ID_KEY = ":FACC_ID";

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public boolean clearData(String orgId, String accId, String file) {
		try {
			
	        File sqlFile = ResourceUtils.getFile(file);
	        List<String> lines = Files.readLines(sqlFile, Charsets.UTF_8);

			for(String line:lines){
				if(StringUtils.isBlank(line)){
					continue;
				}

				if(Strings.isNullOrEmpty(line))continue;
				String exeSql = line.replaceAll(ORG_ID_KEY, orgId);
				exeSql = exeSql.replaceAll(ACC_ID_KEY, accId);
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
