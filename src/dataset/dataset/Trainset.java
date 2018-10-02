/**
 * 该类包含了算法训练所需的全部训练数据
 * 其中原始的userID和itemID被系统转化为以整型为介质的ID
 * @author liuchen
 */

package dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trainset {
	public Map<Integer, ArrayList<int[]>> ur;   //用户-物品倒排表
	public Map<Integer, ArrayList<int[]>> ir;   //物品-用户倒排表
	public int n_users;   //用户数
	public int n_items;   //物品数
	public int n_ratings;  //评分数
	private Map<String, Integer> raw2inner_id_users;  //原始userID到系统userID的映射
	private Map<String, Integer> raw2inner_id_items;  //原始itemID到系统itemID的映射
	private Map<Integer, String> inner2raw_id_users;  //系统userID到原始userID的映射
	private Map<Integer, String> inner2raw_id_items;  //系统itemID到原始itemID的映射
	private double global_mean;   //评分均值
	
	public Trainset(Map<Integer, ArrayList<int[]>> ur, Map<Integer, ArrayList<int[]>> ir, int n_users, int n_items,
			int n_ratings, Map<String, Integer> raw2inner_id_users,
			Map<String, Integer> raw2inner_id_items) {
		this.ur = ur;
		this.ir = ir;
		this.n_users = n_users;
		this.n_items = n_items;
		this.n_ratings = n_ratings;
		this.raw2inner_id_users = raw2inner_id_users;
		this.raw2inner_id_items = raw2inner_id_items;
		this.inner2raw_id_users = null;
		this.inner2raw_id_items = null;
		this.global_mean = -1.0;
	}
	
	/**
	 * 返回原始userID对应的系统userID
	 * @param ruid 原始userID
	 * @return 系统userID
	 */
	public int raw2inner_uid(String ruid) {
		try {
			return this.raw2inner_id_users.get(ruid);
		}catch (Exception e) {
			return -1;
		}
		
	}
	
	/**
	 * 返回原始itemID对应的系统itemID
	 * @param riid 原始的itemID
	 * @return 系统的itemID
	 */
	public int raw2inner_iid(String riid) {
		try {
			return this.raw2inner_id_items.get(riid);
		}catch(Exception e) {
			return -1;
		}
	}
	
	/**
	 * 返回系统itemID对应的原始itemID
	 * @param uid 系统itemID
	 * @return 原始itemID
	 */
	public String inner2raw_uid(int uid) {
		if(this.inner2raw_id_users == null) {
			this.inner2raw_id_users = new HashMap<Integer, String>();
			for(String key : this.raw2inner_id_users.keySet()) {
				this.inner2raw_id_users.put(this.raw2inner_id_users.get(key), key);
			}
		}
		return this.inner2raw_id_users.get(uid);
	}
	
	/**
	 * 返回系统itemID对应的原始itemID
	 * @param iid 系统itemID
	 * @return 原始itemID
	 */
	public String inner2raw_iid(int iid) {
		if(this.inner2raw_id_items == null) {
			this.inner2raw_id_items = new HashMap<Integer, String>();
			for(String key : this.raw2inner_id_items.keySet()) {
				this.inner2raw_id_items.put(this.raw2inner_id_items.get(key), key);
			}
		}
		return this.inner2raw_id_items.get(iid);
	}
	
	/**
	 * 将训练集中可能的评分预测构成测试集
	 * @return Testset实例
	 */
	public Testset build_anti_testset() {
		ArrayList<String[]> testset = new ArrayList<String[]>();
		for(int u = 0 ; u< this.n_users; u++) {
			List<Integer> u_i = new ArrayList<Integer>();
			for(int[] r : this.ur.get(u)) {
				u_i.add(r[0]);
			}
			for(int i = 0; i<this.n_items; i++) {
				if(!u_i.contains(i)) {
					String[] test = {this.inner2raw_uid(u), this.inner2raw_iid(i), String.valueOf(this.global_mean())};
					testset.add(test);
				}
			}
		}
		return new Testset(testset);
	}
	
	/**
	 *全局评分均值
	 * @return double
	 */
	public double global_mean() {
		if (this.global_mean == -1.0) {
			double sum = 0.0;
			//计算全局评分均值
			for(int u : this.ur.keySet()) {
				for(int[] r : this.ur.get(u)) {
					sum += r[1];
				}
			}
			this.global_mean = sum / this.n_ratings;
		}
		return this.global_mean;
	}

}
