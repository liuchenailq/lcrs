/**
 * 该类用于将数据集转换成训练集、验证集
 * @author liuchen
 */

package dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Dataset {
	private ArrayList<String[]> raw_ratings;   //原始的数据集
	
	public Dataset(ArrayList<String[]> raw_ratings) {
		this.raw_ratings = raw_ratings;
	}
	
	public ArrayList<String[]> get_raw_ratings() {
		return this.raw_ratings;
	}

	/**
	 * 将原始评分数据raw_ratings全部转换为训练集
	 * @return Trainset实例
	 */
	public Trainset built_full_trainset() {
		Map<Integer, ArrayList<int[]>> ur = new HashMap<Integer, ArrayList<int[]>>();
		Map<Integer, ArrayList<int[]>> ir = new HashMap<Integer, ArrayList<int[]>>();
		Map<String, Integer> raw2inner_id_users = new HashMap<String, Integer>();
		Map<String, Integer> raw2inner_id_items = new HashMap<String, Integer>();
		int current_uid = 0, current_iid = 0;
		
		for(String[] rating : this.raw_ratings){
            String raw_uid = rating[0], raw_iid = rating[1], raw_rating = rating[2], raw_timestamp = rating[3];
            if(!raw2inner_id_users.containsKey(raw_uid)) {
            	raw2inner_id_users.put(raw_uid, current_uid);
            	ur.put(current_uid, new ArrayList<int[]>());
            	current_uid++;
            }
            
            if(!raw2inner_id_items.containsKey(raw_iid)) {
            	raw2inner_id_items.put(raw_iid, current_iid);
            	ir.put(current_iid, new ArrayList<int[]>());
            	current_iid++;
            }
            
            int[] temp1 = {raw2inner_id_items.get(raw_iid), Integer.parseInt(raw_rating.trim()), Integer.parseInt(raw_timestamp.trim())};
            int[] temp2 = {raw2inner_id_users.get(raw_uid), Integer.parseInt(raw_rating.trim()), Integer.parseInt(raw_timestamp.trim())};
            ur.get(raw2inner_id_users.get(raw_uid)).add(temp1);
            ir.get(raw2inner_id_items.get(raw_iid)).add(temp2);
         }
		
		return new Trainset(ur, ir, current_uid, current_iid, this.raw_ratings.size(), raw2inner_id_users, raw2inner_id_items);
    }
	
	/**
	 * 将原始评分数据raw_ratings全部转换为验证集
	 * @return Testset实例
	 */
	public Testset built_full_testset() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		for(String[] r : this.raw_ratings) {
			String[] temp = {r[0], r[1], r[2]};
			list.add(temp);
		}
		return new Testset(list);
	}
	
}

