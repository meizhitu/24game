package com.z;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Game24 {
	class TOperData {
		public int operType = -1;
		public double data = 0;
	}

	// 检查计算的参数是否合法，也可以用于过滤一些重复的表达式
	private boolean checkjisuan(double a, double b, int op) {
		if (op == '/' && b < 1e-6 && b > -1e-6)
			return false;
		if (op == '-' && b < 1e-6 && b > -1e-6)
			return false;
		if (op == '/'
				&& (Math.abs(b - 1.0f) < 1e-6 || Math.abs(b + 1.0f) < 1e-6))
			return false;
		if (op == '+' || op == '*')
			return a <= b;
		return true;
	}

	// 消除重复
	private boolean checkOp(TOperData computeData[]) {
		for (int i = 0; i < 6; i++) {
			int type1 = computeData[i].operType;
			int type2 = computeData[i + 1].operType;
			if (type1 == '-' && (type2 == '+' || type2 == '-')) {
				return false;
			} else if (type1 == '/' && (type2 == '*' || type2 == '/')) {
				return false;
			}
		}
		return true;
	}

	// 求值
	private double jisuan(double a, double b, int op) {
		switch (op) {
		case '+':
			return a + b;
		case '-':
			return a - b;
		case '*':
			return a * b;
		case '/':
			return a / b;
		default:
			return -1;
		}
	}

	// 计算表达式的值
	private double process(TOperData[] data) {
		int len = 7;
		Stack<TOperData> suffix = new Stack<TOperData>();

		for (int i = 0; i < len; i++) {
			if (data[i].operType == 0) {
				suffix.push(data[i]);
			} else if (data[i].operType > 0) {
				if (suffix.empty())
					return -1;
				if (suffix.peek().operType == 0) {
					double a = suffix.peek().data;
					suffix.pop();
					if (!suffix.empty() && suffix.peek().operType == 0) {
						double b = suffix.peek().data;
						suffix.pop();
						if (!checkjisuan(b, a, data[i].operType))
							return -1;
						double c = jisuan(b, a, data[i].operType);
						TOperData opdata = new TOperData();
						opdata.operType = 0;
						opdata.data = c;
						suffix.push(opdata);
					} else {
						return -1;
					}
				} else {
					return -1;
				}
			}
		}
		if (suffix.empty())
			return -1;
		if (suffix.peek().operType == 0) {
			double r = suffix.peek().data;
			suffix.pop();
			if (suffix.empty())
				return r;
		}
		return -1;
	}

	int op(int x) {
		if (x == '+')
			return 1;
		else if (x == '-')
			return 1;
		else if (x == '*')
			return 2;
		else if (x == '/')
			return 2;
		else
			return 0;
	}

	// 后缀转中缀
	private String posttomid(TOperData[] data) {
		// 后缀转回中缀
		Stack<TOperData> ope = new Stack<TOperData>();
		Stack<String> opn = new Stack<String>();
		String tmp1, tmp2;
		for (int i = 0; i < 7; i++) {
			if (data[i].operType == 0) {
				String tt = "";
				tt += (int) data[i].data;
				opn.push(tt);
				ope.push(data[i]);
			} else {
				if (op(ope.peek().operType) != 0
						&& (op(data[i].operType) > op(ope.peek().operType) || (op(data[i].operType) == op(ope
								.peek().operType) && (data[i].operType == '-' || data[i].operType == '/')))) {
					tmp2 = "(";
					tmp2 += opn.peek();
					tmp2 += ")";
				} else {
					tmp2 = opn.peek();
				}

				opn.pop();
				ope.pop();
				if (op(ope.peek().operType) != 0
						&& op(data[i].operType) > op(ope.peek().operType)) {
					tmp1 = "(";
					tmp1 += opn.peek();
					tmp1 += ")";
				} else {
					tmp1 = opn.peek();
				}
				ope.pop();
				opn.pop();
				tmp1 += (char) data[i].operType;
				tmp1 += tmp2;
				opn.push(tmp1);
				ope.push(data[i]);
			}
		}
		String result = opn.peek();
		return result;
	}

	public static boolean nextPermutation(int[] p) {
		int a = p.length - 2;
		while (a >= 0 && p[a] >= p[a + 1]) {
			a--;
		}
		if (a == -1) {
			return false;
		}
		int b = p.length - 1;
		while (p[b] <= p[a]) {
			b--;
		}
		int t = p[a];
		p[a] = p[b];
		p[b] = t;
		for (int i = a + 1, j = p.length - 1; i < j; i++, j--) {
			t = p[i];
			p[i] = p[j];
			p[j] = t;
		}
		return true;
	}

	public Set<String> start(int data[], boolean bAll) {
		Set<String> result = new HashSet<String>();
		int[][] shunxu = new int[][] { new int[] { 1, 1, 1, 1, 2, 2, 2 },
				new int[] { 1, 1, 1, 2, 1, 2, 2 },
				new int[] { 1, 1, 1, 2, 2, 1, 2 },
				new int[] { 1, 1, 2, 1, 1, 2, 2 },
				new int[] { 1, 1, 2, 1, 2, 1, 2 } };
		int operAll[] = { '+', '-', '*', '/' };
		TOperData[] computeData = new TOperData[7];
		int[] copydata = new int[4];
		copydata[0] = data[0];
		copydata[1] = data[1];
		copydata[2] = data[2];
		copydata[3] = data[3];
		// 5个后缀表达式遍历
		for (int m = 0; m < 5; m++) {
			data[0] = copydata[0];
			data[1] = copydata[1];
			data[2] = copydata[2];
			data[3] = copydata[3];
			do {
				int[] oper = new int[3];
				for (int n = 0; n < 4; n++) {
					oper[0] = operAll[n];
					for (int nn = 0; nn < 4; nn++) {
						oper[1] = operAll[nn];
						for (int nnn = 0; nnn < 4; nnn++) {
							oper[2] = operAll[nnn];
							int j = 0;
							int k = 0;
							for (int i = 0; i < 7; i++) {
								computeData[i] = new TOperData();
								if (shunxu[m][i] == 1) {
									computeData[i].operType = 0;
									computeData[i].data = data[j++];
								} else if (shunxu[m][i] == 2) {
									computeData[i].operType = oper[k++];
								}
							}
							if (!checkOp(computeData))
								continue;
							double r = process(computeData);
							if (r - 24 > -1e-3 && r - 24 < 1e-3) {
								result.add(posttomid(computeData));
								if (!bAll)
									return result;
							}
						}
					}
				}
			} while (nextPermutation(data));
		}
		return result;
	}

	public static void main(String[] args) {
		int[] data = new int[] { 6, 7, 11, 13 };
		Game24 g = new Game24();
		g.start(data, true);
	}
}
