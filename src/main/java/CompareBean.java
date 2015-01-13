import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CompareBean implements Comparable<CompareBean> {
	private String s1;
	private String s2;
	private int s2id;
	private String common;
	private int rank;

	public CompareBean(String s1, String s2, String common, int s2id) {
		super();
		this.s1 = s1;
		this.s2 = s2;
		this.common = common;
		this.s2id = s2id;
	}
	
	public int getS2id() {
		return s2id;
	}



	public void setS2id(int s2id) {
		this.s2id = s2id;
	}



	public String getS1() {
		return s1;
	}

	public void setS1(String s1) {
		this.s1 = s1;
	}

	public String getS2() {
		return s2;
	}

	public void setS2(String s2) {
		this.s2 = s2;
	}

	public String getCommon() {
		return common;
	}

	public void setCommon(String common) {
		this.common = common;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public int compareTo(CompareBean o) {
		int comLength1 = o.getCommon().length();
		int comLength2 = this.getCommon().length();
		// if(comLength1 > comLength2) {
		// return 1;
		// } else if(comLength1 == comLength2) {
		// int rank1 = getRank(o);;
		// int rank2 = getRank(this);;
		// if(rank1 > rank2) {
		// return 1;
		// } else if(rank1 < rank2) {
		// return -1;
		// }
		// return 0;
		// } else {
		// return -1;
		// }

		int rank1 = getRank(o);
		;
		int rank2 = getRank(this);

		if (rank1 > rank2) {
			return 1;
		} else if (rank1 < rank2) {
			return -1;
		} else {
			if (comLength1 > comLength2) {
				return 1;
			} else if (comLength1 == comLength2) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	private int getRank(CompareBean o) {
		String s1 = o.s1.trim();
		String s2 = o.s2.trim();
		int rank = 0;
		if (s1.equals(o.getCommon())) {
			rank += 2;
		}
		if (s2.equals(o.getCommon())) {
			rank += 2;
		}
		if (s1.contains(o.getCommon())) {
			rank++;
		}
		if (s2.contains(o.getCommon())) {
			rank++;
		}
		if (s1.startsWith(o.getCommon()) || s1.endsWith(o.getCommon())) {
			rank++;
		}
		if (s2.startsWith(o.getCommon()) || s2.endsWith(o.getCommon())) {
			rank++;
		}
		o.setRank(rank);
		return rank;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this) + "\n";
		// , ToStringStyle.MULTI_LINE_STYLE
	}

}
