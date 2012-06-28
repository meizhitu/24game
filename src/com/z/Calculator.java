package com.z;

public class Calculator {
	public static double calc(String s) {
		int lastpos = -1;
		char lastOps = ' ';
		int rightBracket = 0;
		for (int i = s.length() - 1; i >= 0; i--) {
			switch (s.charAt(i)) {
			case ')':
				rightBracket++;
				break;
			case '(':
				rightBracket--;
				break;
			}
			if (rightBracket > 0)
				continue;

			switch (s.charAt(i)) {
			case '+':
				return calc(s.substring(0, i))
						+ calc(s.substring(i + 1, s.length()));
			case '-':
				return calc(s.substring(0, i))
						- calc(s.substring(i + 1, s.length()));
			case '*':
				if (lastpos < 0) {
					lastpos = i;
					lastOps = '*';
				}
				break;
			case '/':
				if (lastpos < 0) {
					lastpos = i;
					lastOps = '/';
				}
				break;
			}
		}
		if (lastOps == '*') {
			return calc(s.substring(0, lastpos))
					* calc(s.substring(lastpos + 1, s.length()));
		}
		if (lastOps == '/') {
			return calc(s.substring(0, lastpos))
					/ calc(s.substring(lastpos + 1, s.length()));
		}
		if (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')')
			return calc(s.substring(1, s.length() - 1));

		return Double.parseDouble(s);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(calc("(1+2)*1*8"));
	}

}
