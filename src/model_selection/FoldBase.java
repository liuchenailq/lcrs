/**
 * 该类是划分数据集的抽象基类
 * @author liuchen
 */

package model_selection;

import java.util.ArrayList;

import dataset.Dataset;

public abstract class FoldBase {
	
	public int n_splits;      //数据集划分折数
	public long random_seed;  //随机种子,在shuffle为true时有效
	public Boolean shuffle;   //在划分前是否打乱数据
	
	/**
	 * 对给定的数据集进行划分
	 * @param dataset 数据集
	 * @return ArrayList<Object[]>对象，其中Object[]分别存Trainset、Testset对象
	 */
	public abstract ArrayList<Object[]> split(Dataset dataset);

}
