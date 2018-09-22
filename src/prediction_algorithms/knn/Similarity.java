/**
 * 该类用于相似度的计算
 * 该类不应该被实例化，其方法都为静态方法
 * @author liuchen
 */

package prediction_algorithms.knn;

import java.util.ArrayList;
import java.util.Map;
import dataset.Trainset;

public class Similarity {
	
	/**
	 * 余弦相似度计算
	 * @param trainset 训练集
	 * @param user_based 指定是计算用户还是物品的余弦相似度
	 * @param activity_punish 是否对活跃度进行惩罚
	 * @return 相似度矩阵
	 */
	public static double[][] cosine(Trainset trainset, Boolean user_based, Boolean activity_punish){
		int n_x = 0;
		Map<Integer, ArrayList<int[]>> yr = null;
		
		if(user_based) {
			n_x = trainset.n_users;
			yr = trainset.ir;
		}else {
			n_x = trainset.n_items;
			yr = trainset.ur;
		}
		
		double[][] sim = new double[n_x][n_x];
		int[][] com = new int[n_x][n_x];
		double[][] denominator = new double[n_x][n_x];
		double[][] sqi = new double[n_x][n_x];
		double[][] sqj = new double[n_x][n_x];
		
		int xi = 0, xj = 0;
		for (int y : yr.keySet()) {
			for (int[] x1 : yr.get(y)) {
				xi = x1[0];
				for (int[] x2 : yr.get(y)) {
					xj = x2[0];
					com[xi][xj]++;
					if(activity_punish) {  //对活跃度进行惩罚
						denominator[xi][xj] += x1[1] * x2[1] * (1.0 / Math.log(1 + yr.get(y).size()));
					}else {
						denominator[xi][xj] += x1[1] * x2[1];
					}
					sqi[xi][xj] += x1[1] * x1[1];
					sqj[xi][xj] += x2[1] * x2[1];
				}
			}
		}
		for (int i = 0; i<n_x; i++) {
			sim[i][i] = 1;
			for(int j = i+1; j<n_x; j++) {
				if (com[i][j] == 0) {
					sim[i][j] = 0;
				}else {
					sim[i][j] = denominator[i][j] / Math.sqrt(sqi[i][j] * sqj[i][j]);
				}
				sim[j][i] = sim[i][j];
			}
		}
		return sim;
	}
	
	/**
	 * 修正的余弦相似度计算
	 * @param trainset 训练集
	 * @param user_based 指定是计算用户还是物品的修正的余弦相似度
	 * @return
	 */
	public static double[][] adjusted_cosine(Trainset trainset, Boolean user_based){
		int n_x = 0;
		Map<Integer, ArrayList<int[]>> yr = null;
		if(user_based) {
			n_x = trainset.n_users;
			yr = trainset.ir;
		}else {
			n_x = trainset.n_items;
			yr = trainset.ur;
		}
		
		//计算用户评分均值
		double[] user_mean = new double[trainset.n_users];
		for(int u : trainset.ur.keySet()) {
			for(int[] r : trainset.ur.get(u)) {
				user_mean[u] += r[1];
			}
			user_mean[u] = user_mean[u] / trainset.ur.get(u).size();
		}
		
		double[][] sim = new double[n_x][n_x];
		int[][] com = new int[n_x][n_x];
		double[][] denominator = new double[n_x][n_x];
		double[][] sqi = new double[n_x][n_x];
		double[][] sqj = new double[n_x][n_x];
		
		int xi = 0, xj = 0;
		
		if(user_based) {
			for (int y : yr.keySet()) {
				for (int[] x1 : yr.get(y)) {
					xi = x1[0];
					for (int[] x2 : yr.get(y)) {
						xj = x2[0];
						com[xi][xj]++;
						denominator[xi][xj] += (x1[1] - user_mean[xi]) * (x2[1] - user_mean[xj]);
						sqi[xi][xj] += (x1[1] - user_mean[xi]) * (x1[1] - user_mean[xi]);
						sqj[xi][xj] += (x2[1] - user_mean[xj]) * (x2[1] - user_mean[xj]);
					}
				}
			}
		}else {
			for (int y : yr.keySet()) {
				for (int[] x1 : yr.get(y)) {
					xi = x1[0];
					for (int[] x2 : yr.get(y)) {
						xj = x2[0];
						com[xi][xj]++;
						denominator[xi][xj] += (x1[1] - user_mean[y]) * (x2[1] - user_mean[y]);
						sqi[xi][xj] += (x1[1] - user_mean[y]) * (x1[1] - user_mean[y]);
						sqj[xi][xj] += (x2[1] - user_mean[y]) * (x2[1] - user_mean[y]);
					}
				}
			}
		}
		
		for (int i = 0; i<n_x; i++) {
			sim[i][i] = 1;
			for(int j = i+1; j<n_x; j++) {
				if (com[i][j] == 0) {
					sim[i][j] = 0;
				}else {
					if(sqi[i][j] * sqj[i][j] == 0.0) {
						sim[i][j] = 0;
					}else {
						sim[i][j] = denominator[i][j] / Math.sqrt(sqi[i][j] * sqj[i][j]);
					}
				}
				sim[j][i] = sim[i][j];
			}
		}
		
		return sim;
	}

}
