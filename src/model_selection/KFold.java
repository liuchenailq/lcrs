/**
 * 该类是一个基本的K-折交叉验证迭代器
 * @author liuchen
 */

package model_selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import accuracy.Accuracy;
import dataset.Dataset;
import dataset.LoadDataset;
import dataset.Testset;
import dataset.Trainset;
import prediction_algorithms.Prediction;
import prediction_algorithms.knn.ItemCF;
import prediction_algorithms.knn.UserCF;

public class KFold extends FoldBase{
	
	/**
	 * 设置random_seed并默认shuffle为True,则在划分前打乱数据，并且保证每次返回的训练集、验证集一致
	 */
	public KFold(int n_splits, long random_seed) {
		this.n_splits = n_splits;
		this.random_seed = random_seed;
		this.shuffle = true;
	}
	
	/**
	 * 默认shuffle为false,则在划分前不打乱数据，每次返回的训练集、验证集一定始终一致
	 * 
	 */
	public KFold(int n_splits) {
		this.n_splits = n_splits;
		this.shuffle = false;
	}

	@Override
	public ArrayList<Object[]> split(Dataset dataset) {
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		ArrayList<String[]> ratings = dataset.raw_ratings;
		if(this.shuffle) {   //打乱评分数据
			Collections.shuffle(ratings, new Random(this.random_seed));
		}
		int step = ratings.size() / this.n_splits;
		int start = 0, stop = 0;
		for(int i = 0; i < this.n_splits; i++) {
			start = stop;
			stop += step;
			ArrayList<String[]> testset = new ArrayList<String[]>(ratings.subList(start, stop));
			ArrayList<String[]> trainset1 = new ArrayList<String[]>(ratings.subList(0, start));
			ArrayList<String[]> trainset2 = new ArrayList<String[]>(ratings.subList(stop, ratings.size()));
			trainset1.addAll(trainset2);
			Object[] obj = {new Dataset(trainset1).built_full_trainset(), new Dataset(testset).built_full_testset()};
			data.add(obj);
		}
		
		return data;
	}
	
	public static void main(String[] args) {
		KFold kf = new KFold(5);
		Dataset data = LoadDataset.load_builtin("ml-100k");
		ArrayList<Object[]> aa = kf.split(data);
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
