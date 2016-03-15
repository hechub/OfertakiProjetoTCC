package br.com.hector.ofertaki;

import java.util.InputMismatchException;
import java.util.regex.Pattern;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class Validacoes {

	private static final String apenasNumeros = "[0-9]+";
	private static final String apenasLetras = "[A-Za-z]+";
	private static final String padraoData = "[0-9]{2}/[0-9]{2}/[0-9]{4}";
	private static final String padraoCnpj = "[0-9]{14}";
	private static final String padraoEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
												+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	
	public static boolean isEmail(String s){
		return Pattern.matches(padraoEmail, s);
	}
	
	public static boolean isNumeros (String s){
		return Pattern.matches(apenasNumeros, s);
	}
	
	public static boolean isLetras (String s){
		return true;
	}
	
	public static boolean isDataValida (String data){
		return Pattern.matches(padraoData, data);
	}
	
	public static boolean isInternet (Context c){
		ConnectivityManager conManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(conManager != null){
			NetworkInfo netInfo = conManager.getActiveNetworkInfo();
			if(netInfo != null){
				return netInfo.isConnected();
			}
		}
		return false;
	}
	
	public static boolean isGpS (Context c){
		LocationManager manager = (LocationManager) c.getSystemService( Context.LOCATION_SERVICE );

	    if ( manager != null ) {
	        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    }
	    return false;
	}
	
	
	public static boolean isCnpjValido (String cnpj){
	
		if(cnpj.length() != 14 || cnpj.equals("00000000000000") || cnpj.equals("11111111111111") || cnpj.equals("22222222222222") || cnpj.equals("33333333333333") 
				|| cnpj.equals("44444444444444") || cnpj.equals("55555555555555") || cnpj.equals("66666666666666") || cnpj.equals("77777777777777") || cnpj.equals("88888888888888")
				|| cnpj.equals("99999999999999")){
			return false;
		}
			
		char dig13, dig14;
		int sm, i, r, num, peso;
		
		try{
			
			//calculo primeiro digito
			sm = 0;
			peso = 2;
			
			for(i=11; i>=0; i--){
				num = (int)(cnpj.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10){
					peso = 2;
				}
			}
			
			r = sm % 11;
			if ((r==0) || (r==1)){
				dig13 = '0';
			}else{
				dig13 = (char)((11-r) + 48);
			}
			
			sm = 0;
			peso = 2;
			for(i=12; i>=0; i--){
				num = (int)(cnpj.charAt(i)-48);
				sm=sm+(num*peso);
				peso = peso + 1;
				if(peso == 10){
					peso = 2;
				}
			}
			
			r = sm % 11;
			if((r==0) || (r==1)){
				dig14='0';
			}else{
				dig14 = (char)((11-r)+48);
			}
			
			//Verifica os digitos
			if((dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13)))
				return true;
			else
				return false;
			
			
		}catch(InputMismatchException erro){
			return false;
		}
		
		
	}
	
}
