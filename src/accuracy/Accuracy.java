/**
 * 该类用于计算预测的精度
 * 支持的精度指标有rmse、mae、fcp
 * 该类不应该被实例化，所有方法为静态方法
 * @author liuchen
 */

package accuracy;

import java.util.ArrayList;

import prediction_algorithms.Prediction;

public class Accuracy {
	
	/**
	 * 均方根误差
	 * @param pres 预测结果集
	 * @return double
	 */
	public static double rmse(ArrayList<Prediction> pres) {
		double r = 0.0;
		for(Prediction p : pres) {
			r += (p.est - p.r_ui) * (p.est - p.r_ui);
		}
		
		if(pres.size() == 0) {
			return 0;
		}else {
			return Math.sqrt(r / pres.size());
		}
	}
	
	/**
	 * 平均绝对误差
	 * @param pres 预测结果集
	 * @return double
	 */
	public static double mae(ArrayList<Prediction> pres) {
		double r = 0.0;
		for(Prediction p : pres) {
			r += Math.abs(p.est - p.r_ui);
		}
		
		if(pres.size() == 0) {
			return 0;
		}else {
			return r / pres.size();
		}
	}

}
