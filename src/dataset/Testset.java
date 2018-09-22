/**
 * 此类用于保存测试算法的验证集
 * @author liuchen
 */

package dataset;

import java.util.ArrayList;

public class Testset {
	public ArrayList<String[]> testset;  //String[3]: ruid, riid, r_ui

	public Testset(ArrayList<String[]> testset) {
		this.testset = testset;
	}
	
	

}
