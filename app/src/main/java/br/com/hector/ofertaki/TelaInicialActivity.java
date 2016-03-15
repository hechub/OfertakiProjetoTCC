package br.com.hector.ofertaki;

import java.io.Serializable;

import com.parse.Parse;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import br.com.hector.ofertaki.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class TelaInicialActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tela_inicial);
		
		Parse.initialize(this,
				"uUmWwZ0x0b31l93s3JHKtm4quy8Rlhg3jGU1Mi5b",
				"56OKLdkeRv2r0YEV8axRi6XR5ONI4PsckwdD3Re9");
		
		new Handler().postDelayed(new Runnable() {
	        @Override
	        public void run() 
	        {
	        	
	        	
	        startActivity(new Intent(TelaInicialActivity.this,MainActivity.class));
	        finish();
	        }
	    }, 1000);
		
	}
	
	
}
