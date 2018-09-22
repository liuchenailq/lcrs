/**
 * 此类用于存储一条预测的具体信息
 * @author liuchen
 */

package prediction_algorithms;

public class Prediction {
	
	public String ruid;  
	public String riid;
	public double r_ui;  //真实的评分  
	public double est;   //预测的评分
	
	public Prediction(String ruid, String riid, double r_ui, double est) {
		this.ruid = ruid;
		this.riid = riid;
		this.r_ui = r_ui;
		this.est = est;
	}
	
	/**
	 * 打印信息
	 */
	public String toString (){
		return "用户ID：" + this.ruid +"; 物品ID：" + this.riid +"; 真实评分：" + this.r_ui +"; 预测评分：" + String.valueOf(this.est);
	}
	
	

}
