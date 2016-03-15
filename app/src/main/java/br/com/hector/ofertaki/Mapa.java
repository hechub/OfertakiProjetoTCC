package br.com.hector.ofertaki;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.parse.ParseGeoPoint;

public class Mapa {

	
public ParseGeoPoint geoPonto (String strEndereco, Context context){
		
		Geocoder geocoder = new Geocoder(context);
		ParseGeoPoint geoLoja = new ParseGeoPoint();

		try {
			List<Address> addresses;
			addresses = geocoder.getFromLocationName(strEndereco, 1);
			if(addresses.size() > 0) {
			    geoLoja.setLatitude(addresses.get(0).getLatitude());
			    geoLoja.setLongitude(addresses.get(0).getLongitude());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return geoLoja;
		
	}
	
}
