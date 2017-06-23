package cn.fooltech.fool_ops.domain.message.utils;

/**
 * 发送工具类
 * @author xjh
 *
 */
public class SendUtils {

	/**
	 * 改标识添加发送手机短信支持
	 * @param tag
	 * @return
	 */
	public static int addPhoneMsgSupport(int tag){
		int temp = tag;
		int fix = 1<<1;
		temp = temp|fix;
		return temp;
	}
	
	/**
	 * 改标识添加发送手机短信支持
	 * @param tag
	 * @return
	 */
	public static int addEmailSupport(int tag){
		int temp = tag;
		int fix = 1<<2;
		temp = temp|fix;
		return temp;
	}
	
	/**
	 * 改标识添加发送手机短信支持
	 * @param tag
	 * @return
	 */
	public static boolean isPhoneMsgSupport(int tag){
		int temp = tag;
		int fix = 1<<1;
		temp = temp&fix;
		return temp>0;
	}
	
	/**
	 * 改标识添加发送手机短信支持
	 * @param tag
	 * @return
	 */
	public static boolean isEmailSupport(int tag){
		int temp = tag;
		int fix = 1<<2;
		temp = temp&fix;
		return temp>0;
	}
	
	/**
	 * 获得标识
	 * @param tags
	 * @return
	 */
	public static int getTag(Integer phoneMsg, Integer email){
		Integer temp = 0;
		if(phoneMsg!=null){
			temp = temp|(phoneMsg<<1);
		}
		if(email!=null){
			temp = temp|(email<<2);
		}
		return temp;
	}
}
