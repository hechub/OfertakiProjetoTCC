package br.com.hector.ofertaki;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;



public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

	
	private void sendResult(int DATA_REQUEST,String result) {
	    Intent intent = new Intent();
	    intent.putExtra("dataSelecionada", result);
	    getTargetFragment().onActivityResult(
	    getTargetRequestCode(), DATA_REQUEST, intent);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		Locale l = new Locale("pt", "BR");
		final Calendar c = Calendar.getInstance(l);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		String dataSelecionada = (new SimpleDateFormat("dd/MM/yyyy").format(c.getTime()));
		
		sendResult(CadastroOfertaFragment.DATA_REQUEST, dataSelecionada);
		getDialog().dismiss();
		
	}

}
