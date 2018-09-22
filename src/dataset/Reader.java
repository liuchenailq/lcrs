/**
 * 该类用于解析数据集的每一行。
 * @author liuchen
 */

package dataset;

import java.util.List;
import java.util.Arrays;

public class Reader {
	private String line_format;
	private String separator;
	
	/**
	 * 构造函数
	 * @param line_format 文件的格式 "user item rating [timestamp]"
	 * @param separator 字段的分隔符
	 */
	public Reader(String line_format, String separator) {
		this.line_format = line_format;
		this.separator = separator;
	}
	
	/**
	 * 解析一行
	 * @param line 一行数据内容
	 * @return 返回按["user", "item", "rating", "timestamp"]排列的数组内容
	 */
	public String[] parse_line(String line) {
		List<String> list = Arrays.asList(this.line_format.split(" "));
		String[] spilted_line = line.split(this.separator);
		String[] rs = new String[4];
		
		if (list.contains("timestamp")) {
			String[] standard_format = {"user", "item", "rating", "timestamp"};
			for(int i = 0; i < 4; i++) {
				rs[i] = spilted_line[list.indexOf(standard_format[i])].trim();
			}
			
		}else {
			String[] standard_format = {"user", "item", "rating"};
			for(int i = 0; i < 3; i++) {
				rs[i] = spilted_line[list.indexOf(standard_format[i])];
			}
			rs[3] = null;
		}
		return rs;
	}
	
	public static void main(String[] args) {
		Reader reader = new Reader("item rating user", "\t");
		String[] aa = reader.parse_line("a 	b	1");
		for(int i=0;i<4;i++) {
			System.out.println(aa[i]);
		}
	}
}
