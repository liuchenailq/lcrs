/**
 * 此类用于保存测试算法的验证集
 * @author liuchen
 */

package dataset;

import java.util.ArrayList;

public class Testset {
	private ArrayList<String[]> testset;  //原始测试集

	public Testset(ArrayList<String[]> testset) {
		this.testset = testset;
	}

	public ArrayList<String[]> get_testset() {
		return this.testset;
	}
	
	
	
}
