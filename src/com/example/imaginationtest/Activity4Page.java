package com.example.imaginationtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity4Page extends Activity {

	private Button Act4_ClearButton;
	private Button Act4_EraserButton;
	private Button Act4_NextPageButton;
	private Button Act4_PreviousPageButton;
	private Button Act4_RedoButton;
	private Button Act4_SaveFileButton;
	private Button Act4_UndoButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity4_page);
		
		Init();
	
	}
	
	void Init()
	{
		// ¶s¿…Button
		this.Act4_SaveFileButton = (Button) findViewById(R.id.Act4_SaveFileButton);
		this.Act4_SaveFileButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}

}
