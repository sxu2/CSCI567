package com.example.simpleui;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button buttontest1 = (Button) findViewById(R.id.button1);
		buttontest1.setOnClickListener(this);
		Button buttontest2 = (Button) findViewById(R.id.button2);
		buttontest2.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onClick(View src) {
		switch (src.getId()) {
		case R.id.button1:
			TextView txt1 = (TextView) findViewById(R.id.textView1);
			txt1.setText("Hello");
			break;
		case R.id.button2:
			TextView txt2 = (TextView) findViewById(R.id.textView1);
			txt2.setText("World");
			break;
		}


	}

}
