import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Collections2;

public class LCSProblem {
	public static void main0(String[] args) {
		// 保留空字符串是为了getLength()方法的完整性也可以不保留
		// 但是在getLength()方法里面必须额外的初始化c[][]第一个行第一列
		String s2 = " written";
		String s1 = " writing";

		char[] x = s1.toCharArray();
		char[] y = s2.toCharArray();

		int[][] b = getLength(x, y);

		StringBuffer result = new StringBuffer();
		Display(b, x, x.length - 1, y.length - 1, result);
	}

	public static void main(String[] args) {
		MyFilter.isLocal = true;
		// 保留空字符串是为了getLength()方法的完整性也可以不保留
		// 但是在getLength()方法里面必须额外的初始化c[][]第一个行第一列
		List<WordBean> infoBeanList = (List<WordBean>) (DBUtil.getWordDicList(
				0, Integer.MAX_VALUE, "", "", "false", "rank", "gt", "0", false)[0]);
		List<CompareBean> compareBeanList = new ArrayList<CompareBean>();
		// String s1 = "integrations";
		String s1 = "abandon";
		s1 = " " + s1;
		s1 = s1.toLowerCase();
		for (int j = 0; j < infoBeanList.size(); j++) {
			WordBean wordBean = infoBeanList.get(j);
			String s2 = " " + wordBean.getContent();
			s2 = s2.toLowerCase();
			if (s1.equals(s2) || s2.trim().length() <= 3) {
				continue;
			}
			if(wordBean.getRank() < 0 && wordBean.getRank() != -10) {
				continue;
			}
			
			
			char[] x = s1.toCharArray();
			char[] y = s2.toCharArray();

			int[][] b = getLength(x, y);
			StringBuffer common = new StringBuffer();
			Display(b, x, x.length - 1, y.length - 1, common);
			
//			String common = StringCompare.getLCString(s1.trim().toCharArray(), s2.trim().toCharArray());
			
			
			if (common.length() >= Math.min(s1.length(), s2.length()) - 1 - 2) {
				compareBeanList.add(new CompareBean(s1, s2, common.toString(), wordBean.getId()));
//				System.out.println("s1:" + s1 + " s2:" + s2 + " common:"
//						+ common);
			}
		}
		
		Collections.sort(compareBeanList);

		System.out.println("compareBeanList:" + compareBeanList);
	}

	/**
	 * @param x
	 * @param y
	 * @return 返回一个记录决定搜索的方向的数组
	 */
	public static int[][] getLength(char[] x, char[] y) {
		int[][] b = new int[x.length][y.length];
		int[][] c = new int[x.length][y.length];

		for (int i = 1; i < x.length; i++) {
			for (int j = 1; j < y.length; j++) {
				// 对应第一个性质
				if (x[i] == y[j]) {
					c[i][j] = c[i - 1][j - 1] + 1;
					b[i][j] = 1;
				}
				// 对应第二或者第三个性质
				else if (c[i - 1][j] >= c[i][j - 1]) {
					c[i][j] = c[i - 1][j];
					b[i][j] = 0;
				}
				// 对应第二或者第三个性质
				else {
					c[i][j] = c[i][j - 1];
					b[i][j] = -1;
				}
			}
		}

		return b;
	}

	// 回溯的基本实现，采取递归的方式
	public static void Display(int[][] b, char[] x, int i, int j,
			StringBuffer result) {
		if (i == 0 || j == 0)
			return;

		if (b[i][j] == 1) {
			Display(b, x, i - 1, j - 1, result);
			// System.out.print(x[i] + " ");
			result.append(x[i]);
		} else if (b[i][j] == 0) {
			Display(b, x, i - 1, j, result);
		} else if (b[i][j] == -1) {
			Display(b, x, i, j - 1, result);
		}
	}
}