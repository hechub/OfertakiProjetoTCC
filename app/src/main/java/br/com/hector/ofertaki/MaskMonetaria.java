package br.com.hector.ofertaki;

import java.text.NumberFormat;
import java.util.Locale;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class MaskMonetaria {

	
	public static TextWatcher insert (final EditText mEditText){
		return new TextWatcher() {
			
			boolean isUpdating;
			NumberFormat mNF = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(isUpdating){
					isUpdating = false;
					return;
				}
				
				isUpdating = true;
				String str = s.toString();
				boolean hasMask = (str.indexOf("R$") >= -1 && str.indexOf(".") >= -1 && str.indexOf(",") >= -1);
				
				if (hasMask){
					str = str.replaceAll("[R$]", "").replaceAll("[.]","").replaceAll("[,]","");
				}
				
				try{
					double value = (Double.parseDouble(str) / 100);
					str = mNF.format(value);
					mEditText.setText(str);
					mEditText.setSelection(str.length());
				}catch(Exception e){
					e.printStackTrace();	
					s = "";
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		};
		
	}
	
	
	
	
	
}
