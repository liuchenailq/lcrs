/**
 * 隐语义模型
 * @author liuchen
 */

package prediction_algorithms.matrix_factorization;

import java.util.ArrayList;
import java.util.Random;

import accuracy.Accuracy;
import dataset.Dataset;
import dataset.LoadDataset;
import dataset.Testset;
import dataset.Trainset;
import model_selection.CrossValidation;
import model_selection.FoldBase;
import model_selection.KFold;
import model_selection.LeaveOneOut;
import prediction_algorithms.AlgoBase;
import prediction_algorithms.Prediction;

public class LFM extends AlgoBase{
	public int n_factors;   //隐类个数
	public String optimize_measure;   //优化方法，可选项有sgd、als
	public double reg;   //正则化参数
	public double learning_rate;   //学习速率
	public int n_epochs;  //迭代次数
	public double[][] q;  //隐类-物品矩阵
	public double[][] p;  //隐类-用户矩阵
	
	
	public LFM(int n_factors, String optimize_measure, double reg, double learning_rate, int n_epochs) {
		this.n_factors = n_factors;
		this.optimize_measure = optimize_measure;
		this.reg = reg;
		this.learning_rate = learning_rate;
		this.n_epochs = n_epochs;
	}
	
	/**
	 * 利用优化方法求解q、p矩阵
	 * @param trainset 训练集
	 */
	@Override
	public void fit(Trainset trainset) {
		this.trainset = trainset;
		this.q = new double[this.n_factors][this.trainset.n_items];
		this.p = new double[this.n_factors][this.trainset.n_users];
		//随机初始化
		Random random = new Random();
		for(int j = 0; j<this.n_factors;j++) {
			for(int i = 0; i<this.trainset.n_items;i++) {
				this.q[j][i] = random.nextDouble();
			}
			for(int i = 0; i<this.trainset.n_users;i++) {
				this.p[j][i] = random.nextDouble();
			}
		}
		switch(this.optimize_measure){
			case "sgd":
				this.sgd();
				break;
			case "als":
				break;
			default:
				break;
		}
		
	}
	
	
	@Override
	public double predict(String ruid, String riid) {
		int uid = this.trainset.raw2inner_uid(ruid);
		int iid = this.trainset.raw2inner_iid(riid);
		
		if(uid == -1 || iid == -1) {   //当用户或物品不在训练集中,预测评分为训练集全局平均值
			return this.trainset.global_mean();
		}
		
		double est = 0;
		for(int j =0;j<this.n_factors;j++) {
			est += this.q[j][iid] * this.p[j][uid];
		}
		return est;
	}
	
	/**
	 * 利用随机梯度下降方法进行优化
	 */
	public void sgd() {
		int i = 0;
		for(int epoch = 0 ; epoch < this.n_epochs; epoch++) {
			for(int u : this.trainset.ur.keySet()) {
				for(int[] r : this.trainset.ur.get(u)) {
					i = r[0]; 
					double eui = r[1];
					for(int j = 0; j<this.n_factors;j++) {
						eui = eui - this.q[j][i] * this.p[j][u];
					}
					//更新
					for(int j = 0; j<this.n_factors;j++) {
						this.q[j][i] = this.q[j][i] - this.learning_rate * (this.reg * this.q[j][i] - eui * this.p[j][u]);
						this.p[j][u] = this.p[j][u] - this.learning_rate * (this.reg * this.p[j][u] - eui * this.q[j][i]);
					}
					
				}
			}
		}	
	}
	
	public static void main(String[] args) {
		AlgoBase algo = new LFM(100, "sgd", 0.1, 0.02, 20);   //当reg=0.1,learning_rate = 0.02时最优
		Dataset data = LoadDataset.load_builtin("ml-100k");
		String[] measures = {"mae","rmse"};
		FoldBase fold = new KFold(5, 2);
		CrossValidation.cross_validate(algo, data, measures, fold);
	}
	
	
	

}
