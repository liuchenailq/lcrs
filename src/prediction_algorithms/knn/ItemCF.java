/**
 * 基于物品的协同过滤算法
 * @author liuchen
 */

package prediction_algorithms.knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import accuracy.Accuracy;
import dataset.Dataset;
import dataset.LoadDataset;
import dataset.Testset;
import dataset.Trainset;
import model_selection.CrossValidation;
import model_selection.FoldBase;
import model_selection.KFold;
import prediction_algorithms.AlgoBase;
import prediction_algorithms.Prediction;
import prediction_algorithms.matrix_factorization.LFM;

public class ItemCF extends AlgoBase{
	
	double[][] sim;
	String sim_name;
	int k;
	public Boolean activity_punish;
	
	/**
	 * 构造函数
	 * @param sim_name 相似度计算方法名称，可选项有cosine、adjusted_cosine、pearson
	 * @param activity_punish 是否对活跃度进行惩罚
	 * @param k 用于预测评分的最近邻数目
	 */
	public ItemCF(String sim_name, int k, Boolean activity_punish) {
		this.sim_name = sim_name;
		this.k = k;
		this.activity_punish = activity_punish;
	}

	@Override
	public void fit(Trainset trainset) {
		this.trainset = trainset;
		
		//计算相似度
		System.out.println("正在计算物品的" + this.sim_name +"相似度...");
		switch(this.sim_name) {
			case "cosine":
				this.sim = Similarity.cosine(trainset, false, this.activity_punish);
				break;
			case "adjusted_cosine":
				this.sim = Similarity.adjusted_cosine(trainset, false);
				break;
			default:
				break;
		}
		System.out.println("物品的" + this.sim_name +"相似度计算完成!");
		
	}

	@Override
	public double predict(String ruid, String riid) {
		
		int uid = this.trainset.raw2inner_uid(ruid);
		int iid = this.trainset.raw2inner_iid(riid);
		
		if(uid == -1 || iid == -1) {   //当用户或物品不在训练集中,预测评分为训练集全局平均值
			return this.trainset.global_mean();
		}
		
		Map<Double, ArrayList<Integer>> neighbors = new HashMap<Double, ArrayList<Integer>>(); 
		for(int[] r : this.trainset.ur.get(uid)) {
			if(!neighbors.containsKey(this.sim[iid][r[0]])) {
				neighbors.put(this.sim[iid][r[0]], new ArrayList<Integer>());
			}
			neighbors.get(this.sim[iid][r[0]]).add(r[1]);
		}
		
		List<Map.Entry<Double, ArrayList<Integer>>> infoIds = new ArrayList<Map.Entry<Double, ArrayList<Integer>>>(neighbors.entrySet());    
		Collections.sort(infoIds, new Comparator<Map.Entry<Double, ArrayList<Integer>>>() {  
			public int compare(Map.Entry<Double, ArrayList<Integer>> o1,  Map.Entry<Double, ArrayList<Integer>> o2) {  
				return (o2.getKey()).compareTo(o1.getKey());  
	        }  
	    });  
		
		double sum_sim = 0, sum_ratings = 0;
		int actual_k = 0;
		for (Map.Entry<Double, ArrayList<Integer>> mapping : infoIds) {
			for(int value : mapping.getValue()) {
				actual_k ++;
				if(actual_k > this.k) {
					break;
				}
				sum_sim += mapping.getKey();
				sum_ratings += mapping.getKey() * value;
			}
		} 
		
		if(sum_sim == 0) {
			return this.trainset.global_mean();
		}else {
			return sum_ratings / sum_sim;
		}
	}
	
	public static void main(String[] args) {
		AlgoBase algo = new ItemCF("cosine", 40, false);
		Dataset data = LoadDataset.load_builtin("ml-100k");
		String[] measures = {"mae","rmse"};
		FoldBase fold = new KFold(5, 2);
		CrossValidation.cross_validate(algo, data, measures, fold);
	}

}
