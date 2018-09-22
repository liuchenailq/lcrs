/**
 * 该类利用交叉验证测试一个算法模型
 * 该类不应该被实例化，所有方法为静态方法
 * @author liuchen
 */

package model_selection;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import accuracy.Accuracy;
import dataset.Dataset;
import dataset.LoadDataset;
import dataset.Reader;
import dataset.Testset;
import dataset.Trainset;
import prediction_algorithms.AlgoBase;
import prediction_algorithms.Prediction;
import prediction_algorithms.knn.ItemCF;
import prediction_algorithms.knn.UserCF;

public class CrossValidation {
	
	/**
	 * 基本的交叉验证方法
	 * @param algo 要测试的算法模型
	 * @param data 数据集
	 * @param measures 评级指标，如["rmse","mae"]
	 * @param fold 划分数据集的基类
	 */
	public static void cross_validate(AlgoBase algo,Dataset data,String[] measures,FoldBase fold) {
		ArrayList<Object[]> train_test = fold.split(data);
		int k = 0;
		HashMap<Integer, Double[]> accuracys = new HashMap<Integer, Double[]>(); 
		for(Object[] tt : train_test) {
			k++;
			Trainset train = (Trainset)tt[0];
			Testset test = (Testset)tt[1];
			algo.fit(train);
			ArrayList<Prediction> pres = algo.test(test);
			Double[] acc = new Double[measures.length]; 
			int index = 0;
			for(String measure : measures) {
				switch(measure) {
				case "rmse":
					acc[index++] = Accuracy.rmse(pres);
					break;
				case "mae":
					acc[index++] = Accuracy.mae(pres);
					break;
				default:
					break;
				}
			}
			accuracys.put(k, acc);
		}
		print(accuracys, measures);  
	}
	
	/**
	 * 打印测评结果信息
	 * @param accuracys 测评结果
	 * @param measures  测评指标
	 */
	public static void print(HashMap<Integer, Double[]> accuracys, String[] measures) {
		DecimalFormat df = new DecimalFormat( "0.0000 ");
		System.out.print("测评指标：     ");
		for(String measure : measures) {
			System.out.print( measure+"   ");
		}
		System.out.println();
		double[] mean = new double[measures.length];  //每一个测评指标的平均值
		for(int key : accuracys.keySet()) {
			System.out.print("第"+key+"折结果：    ");
			int j = 0;
			for(Double a : accuracys.get(key)) {
				mean[j] += a;
				j++;
				System.out.print(df.format(a)+"   ");
			}
			System.out.println();
		}
		System.out.print("平均值：         ");
		for(int k = 0;k< measures.length;k++) {
			mean[k] = mean[k] / accuracys.size();
			System.out.print(df.format(mean[k])+"   ");
		}
	}
	public static void main(String[] args) {
		AlgoBase algo = new UserCF("cosine", 30, true);
		Dataset data = LoadDataset.load_builtin("ml-100k");
		String[] measures = {"mae","rmse"};
		FoldBase fold = new KFold(5, 2);
		CrossValidation.cross_validate(algo, data, measures, fold);
	}

}
