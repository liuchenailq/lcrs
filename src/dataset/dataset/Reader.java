/**
 * 该类用于解析数据集的每一行。
 * @author liuchen
 */

package dataset;

import java.util.List;
import java.util.Arrays;

public class Reader {
	private String line_format;   //文件的行格式，比如 "user item rating [timestamp]"
	private String separator;     //字段分隔符
	
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
	
}
