package com.dbhelper;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	DBHelper dbh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dbh = new DBHelper(this);
		
		Button buttonwrite = (Button) findViewById(R.id.button1);
		buttonwrite.setOnClickListener(this);
		Button buttonread = (Button) findViewById(R.id.button2);
		buttonread.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onClick(View src) {
		switch (src.getId()) {
		case R.id.button1:
			EditText txt = (EditText) findViewById(R.id.edittext);
			//writeDB
			if(dbh.insertText(txt.getText().toString())){
				Toast.makeText(getApplicationContext(),
						"Write sucessful", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(getApplicationContext(),
						"Can't write duplicate data", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.button2:
			TextView txt2 = (TextView) findViewById(R.id.textView1);
			//Replace the text in the textView with the following text.
			txt2.setText(dbh.getText());
			//readDB
			break;
		}	

	}

}