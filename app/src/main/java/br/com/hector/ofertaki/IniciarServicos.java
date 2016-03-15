package br.com.hector.ofertaki;

import com.parse.Parse;
import com.parse.ParseInstallation;

import android.app.Application;


public class IniciarServicos extends Application {


	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Parse.initialize(this, "uUmWwZ0x0b31l93s3JHKtm4quy8Rlhg3jGU1Mi5b", "56OKLdkeRv2r0YEV8axRi6XR5ONI4PsckwdD3Re9");
		ParseInstallation.getCurrentInstallation().saveInBackground();
		super.onCreate();
	}
	
	
}
