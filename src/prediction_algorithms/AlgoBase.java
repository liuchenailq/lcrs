/**
 * 此类是所有预测算法的抽象基类
 * @author liuchen
 */

package prediction_algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dataset.Testset;
import dataset.Trainset;

public abstract class AlgoBase {
	
	public Trainset trainset;
	
	/**
	 * 在给定训练集上训练算法
	 * @param trainset 训练集
	 */
	public abstract void fit(Trainset trainset);
	
	/**
	 * 预测user对item的评分
	 * @param ruid 原始userID
	 * @param riid 原始itemID
	 * @return 评分
	 */
	public abstract double predict(String ruid, String riid);
	
	/**
	 * 预测预测user对item的评分，并将预测信息打包
	 * @param ruid 原始userID
	 * @param riid 原始itemID
	 * @param r_ui 真实评分， 0代表无此信息
	 * @return Prediction对象
	 */
	public Prediction predict(String ruid, String riid, double r_ui) {
		double est = this.predict(ruid, riid);
		return new Prediction(ruid, riid, r_ui, est);
	}
	
	/**
	 * 对测试集的进行预测
	 * @param testset 测试集
	 * @return ArrayList<Prediction>对象
	 */
	public ArrayList<Prediction> test(Testset testset){
		ArrayList<Prediction> pres = new ArrayList<Prediction>();
		for(String[] t : testset.testset) {
			Prediction pre = this.predict(t[0], t[1], Double.parseDouble(t[2]));
			pres.add(pre);
		}
		return pres;
	}
	
	/**
	 * 进行Top-N推荐
	 * @param pres 预测结果集
	 * @param n 每个用户推荐物品数
	 * @return Map<String, ArrayList<String>>对象
	 */
	public Map<String, ArrayList<String>> get_top_n(ArrayList<Prediction> pres, int n){
		Map<String, ArrayList<String>> top_n = new HashMap<String, ArrayList<String>>();
		return top_n;
	}
	
}
