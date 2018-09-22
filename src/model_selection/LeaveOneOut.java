/**
 * 该类利用留一法进行划分数据集
 * 每个用户都有一个评分项放在验证集中
 * @author liuchen
 */

package model_selection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import accuracy.Accuracy;
import dataset.Dataset;
import dataset.LoadDataset;
import dataset.Testset;
import dataset.Trainset;
import prediction_algorithms.Prediction;
import prediction_algorithms.knn.ItemCF;
import prediction_algorithms.knn.UserCF;

public class LeaveOneOut extends FoldBase{
	
	public int min_n_ratings;   //当一个用户的评分项数目大于这个数时，才有一个评分项放在验证集中，此数的最小值应该为2
	
	/**
	 * 设置random_seed并默认shuffle为True,保证每次返回的训练集、验证集一致
	 */
	public LeaveOneOut(int n_splits, long random_seed, int min_n_ratings) {
		this.n_splits = n_splits;
		this.random_seed = random_seed;
		this.min_n_ratings = min_n_ratings;
		this.shuffle = true;
	}
	
	/**
	 * 默认shuffle为False,则可以不设置random_seed，则每次返回的训练集、验证集不一定一致
	 */
	public LeaveOneOut(int n_splits, int min_n_ratings) {
		this.n_splits = n_splits;
		this.min_n_ratings = min_n_ratings;
		this.shuffle = false;
	} 

	@Override
	public ArrayList<Object[]> split(Dataset dataset) {
		ArrayList<Object[]> data = new ArrayList<Object[]>();  
		Random random = null;
		if(this.shuffle) {
			random = new Random(this.random_seed);
		}else {
			random = new Random();
		}
		
		//整理用户-物品表
		HashMap<String, ArrayList<String[]>> user_items = new HashMap<String, ArrayList<String[]>>();
		for(String[] r : dataset.raw_ratings) {
			if(!user_items.containsKey(r[0])) {
				user_items.put(r[0], new ArrayList<String[]>());
			}
			user_items.get(r[0]).add(r);
		}
		
		for(int i = 0; i < this.n_splits; i++) {
			ArrayList<String[]> train = new ArrayList<String[]>();
			ArrayList<String[]> test = new ArrayList<String[]>();
			for(String user : user_items.keySet()) {
				if(user_items.get(user).size() < this.min_n_ratings) {
					train.addAll(user_items.get(user));
				}else {
					int j = random.nextInt(user_items.get(user).size());
					int k = 0;
					for(String[] r : user_items.get(user)) {
						if(k == j) {
							test.add(r);
						}else {
							train.add(r);
						}
						k++;
					}
				}
			}
			Object[] obj = {new Dataset(train).built_full_trainset(), new Dataset(test).built_full_testset()};
			data.add(obj);
		}
		return data;
	}
	
	public static void main(String[] args) {
		LeaveOneOut lo = new LeaveOneOut(5,2,2);
		Dataset data = LoadDataset.load_builtin("ml-100k");
		ArrayList<Object[]> aa = lo.split(data);
		UserCF icf = new UserCF("cosine", 40, true);
		double sum = 0;
		for(Object[] a : aa) {
			Trainset train = (Trainset)a[0];
			Testset test = (Testset)a[1];
			icf.fit(train);
			ArrayList<Prediction> pres = icf.test(test);
			double tt = Accuracy.rmse(pres);
			System.out.println(tt);
			sum += tt;
		}
		System.out.println(sum);
	}

}
