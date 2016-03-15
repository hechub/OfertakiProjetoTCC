package br.com.hector.ofertaki;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

public class NotificacoesOferta extends ParsePushBroadcastReceiver {
	
	protected static String idOferta="";
	
	@Override
	protected void onPushOpen(Context context, Intent intent) {

		
		
		try{
			Bundle extras = intent.getExtras();
			if(extras != null){
				
				String jsonData = extras.getString("com.parse.Data");
				JSONObject json;
				json = new JSONObject(jsonData);
				idOferta = (String) json.get("oferta");
				
				Intent i = new Intent(context,PagerDetalharOfertaActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("ofertaSelecionada",idOferta);
				context.startActivity(i);
			}
			
		}catch(JSONException e){
			e.printStackTrace();
			super.onPushOpen(context, intent);
		}
		
	}
}
