package com.z;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity {
	private ImageView[] cards;
	private Button btn;
	private Button next;
	private Button btnAns;
	private Button btnNoAns;
	private Button btnAllAns;
	private EditText et;
	private EditText etAns;
	private int[] data;
	private ScrollView sv;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		cards = new ImageView[4];
		cards[0] = (ImageView) findViewById(R.id.card1);
		cards[1] = (ImageView) findViewById(R.id.card2);
		cards[2] = (ImageView) findViewById(R.id.card3);
		cards[3] = (ImageView) findViewById(R.id.card4);
		sv = (ScrollView) findViewById(R.id.scroll);
		btn = (Button) findViewById(R.id.go);
		btnAns = (Button) findViewById(R.id.answer);
		btnAllAns = (Button) findViewById(R.id.buttonAllAns);
		et = (EditText) findViewById(R.id.input);
		etAns = (EditText) findViewById(R.id.editTextAnswer);
		next = (Button) findViewById(R.id.next);
		btnNoAns = (Button) findViewById(R.id.buttonNoAnswer);
		resetPuke();
		btn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				String inputStr = et.getText().toString();
				if (checkInput(inputStr)) {
					Toast.makeText(MainActivity.this, "答对了", Toast.LENGTH_SHORT)
							.show();

				} else {
					Toast.makeText(MainActivity.this, "答错了,再想想吧",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		next.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				resetPuke();
				et.setText("");
				etAns.setText("");
			}
		});

		btnNoAns.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Game24 g = new Game24();
				Set<String> str = g.start(data, false);
				if (str.size() > 0) {
					Toast.makeText(MainActivity.this, "再试试吧,有答案的",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, "答对了,真的是无解的！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnAllAns.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Game24 g = new Game24();
				Arrays.sort(data);
				Set<String> str = g.start(data, true);
				if (str.size() > 0) {
					etAns.setText("");
					for (String s : str) {
						etAns.append(s + "\r\n");
					}
				} else {
					etAns.setText("无解");
				}
			}
		});
		et.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (!arg1) {
					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(arg0.getWindowToken(), 0);
					sv.smoothScrollTo(0, 0);
				}
			}
		});
		btnAns.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Game24 g = new Game24();
				Arrays.sort(data);
				Set<String> str = g.start(data, false);
				if (str.size() > 0) {
					etAns.setText("");
					for (String s : str) {
						etAns.append(s + "\r\n");
					}
				} else {
					etAns.setText("无解");
				}
			}
		});

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	private boolean isOp(char c) {
		if (c == '+' || c == '-' || c == '*' || c == '/')
			return true;
		return false;
	}

	private boolean isDigit(char c) {
		if (c <= '9' && c >= '0')
			return true;
		return false;
	}

	private boolean isValidChar(char c) {
		if (c == '+' || c == '-' || c == '*' || c == '/'
				|| (c >= '0' && c <= '9') || c == '(' || c == ')')
			return true;
		return false;
	}

	private boolean checkInput(String input) {
		input = input.replace(" ", "");
		int opCount = 0;
		List<Integer> d = new ArrayList<Integer>();
		String sb = "";
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (isOp(c))
				opCount++;
			if (!isValidChar(c))
				return false;
			if (isDigit(c)) {
				sb += c;
			} else {
				if (!sb.isEmpty()) {
					d.add(Integer.valueOf(sb));
					sb = "";
				}
			}
		}
		if (!sb.isEmpty()) {
			d.add(Integer.valueOf(sb));
			sb = "";
		}
		if (opCount != 3)
			return false;
		if (d.size() != 4)
			return false;
		Collections.sort(d);
		List<Integer> lst = new ArrayList<Integer>();
		lst.add(data[0]);
		lst.add(data[1]);
		lst.add(data[2]);
		lst.add(data[3]);
		Collections.sort(lst);
		for (int i = 0; i < 4; i++) {
			if (d.get(i) != lst.get(i))
				return false;
		}
		double ca = Calculator.calc(input);
		if (Math.abs(ca - 24) < 1e-6)
			return true;
		return false;

	}

	private void resetPuke() {
		int d1 = Math.abs((int) (Math.random() * 140)) % 13 + 1;
		int d2 = Math.abs((int) (Math.random() * 140)) % 13 + 1;
		int d3 = Math.abs((int) (Math.random() * 140)) % 13 + 1;
		int d4 = Math.abs((int) (Math.random() * 140)) % 13 + 1;
		List<Integer> lst = new ArrayList<Integer>();
		lst.add(d1);
		lst.add(d2);
		lst.add(d3);
		lst.add(d4);
		Collections.sort(lst);
		data = new int[] { lst.get(0), lst.get(1), lst.get(2), lst.get(3) };
		for (int i = 0; i < data.length; i++) {
			int resID = getResources().getIdentifier("p" + data[i], "drawable",
					"com.z");
			cards[i].setImageResource(resID);
		}
	}
}
