package br.com.hector.ofertaki;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;

public class DialogOndeEncontrar extends DialogFragment{

	private TextView txtContatoOndeEncontrar, txtNomeLojaOndeEncontrar, 
	txtCategoriaLojaOndeEncontrar, txtEnderecoLojaOndeEncontrar, txtTelefoneLojaOndeEncontrar, txtOkOndeEncontrar;
	private String nomeLoja,categoriaLoja, enderecoLoja,telefoneLoja;
	private ParseObject l;
	private ProgressBar pBarOndeEncontrar;
	private Handler handler;
	private Oferta o;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	private class carregaInformacaoLoja extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			l = new ParseObject("Loja");
			l.setObjectId(o.getLojaOferta(o));
			try {
				nomeLoja = (l.fetch().getString("nomeLoja"));
				categoriaLoja = (l.fetch().getString("categoriaLoja"));
				telefoneLoja = (l.fetch().getString("telefone"));
				enderecoLoja = (l.fetch().getString("enderecoLoja"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			txtNomeLojaOndeEncontrar.setText(nomeLoja);
			txtCategoriaLojaOndeEncontrar.setText(categoriaLoja);
			txtTelefoneLojaOndeEncontrar.setText(telefoneLoja);
			txtEnderecoLojaOndeEncontrar.setText(enderecoLoja);
			
			txtNomeLojaOndeEncontrar.setVisibility(View.VISIBLE);
			txtCategoriaLojaOndeEncontrar.setVisibility(View.VISIBLE);
			txtTelefoneLojaOndeEncontrar.setVisibility(View.VISIBLE);
			txtContatoOndeEncontrar.setVisibility(View.VISIBLE);
			txtEnderecoLojaOndeEncontrar.setVisibility(View.VISIBLE);
			txtOkOndeEncontrar.setVisibility(View.VISIBLE);
			
			pBarOndeEncontrar.setVisibility(View.GONE);
		}
		
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.dialog_onde_encontrar, container, false);
		
		o = (Oferta) getArguments().getSerializable("oferta");
		
		getDialog().setTitle("Onde Encontrar?");
		
		pBarOndeEncontrar = (ProgressBar)v.findViewById(R.id.pBarOndeEncontrar);
		txtNomeLojaOndeEncontrar = (TextView)v.findViewById(R.id.txtNomeLojaOndeEncontrar);
		txtCategoriaLojaOndeEncontrar = (TextView)v.findViewById(R.id.txtCategoriaLojaOndeEncontrar);
		txtTelefoneLojaOndeEncontrar = (TextView)v.findViewById(R.id.txtTelefoneLojaOndeEncontrar);
		txtEnderecoLojaOndeEncontrar = (TextView)v.findViewById(R.id.txtEnderecoLojaOndeEncontrar);
		txtOkOndeEncontrar = (TextView)v.findViewById(R.id.txtOkOndeEncontrar);
		txtContatoOndeEncontrar = (TextView)v.findViewById(R.id.txtContatoOndeEncontrar);

		txtNomeLojaOndeEncontrar.setVisibility(View.INVISIBLE);
		txtCategoriaLojaOndeEncontrar.setVisibility(View.INVISIBLE);
		txtTelefoneLojaOndeEncontrar.setVisibility(View.INVISIBLE);
		txtContatoOndeEncontrar.setVisibility(View.INVISIBLE);
		txtEnderecoLojaOndeEncontrar.setVisibility(View.INVISIBLE);
		txtOkOndeEncontrar.setVisibility(View.INVISIBLE);
		
		pBarOndeEncontrar.setVisibility(View.VISIBLE);
		
		carregaInformacaoLoja task = new carregaInformacaoLoja();
		task.execute();
		
		
		txtOkOndeEncontrar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				getDialog().dismiss();
			}
		});
		
		
		return v;
		
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	
}
