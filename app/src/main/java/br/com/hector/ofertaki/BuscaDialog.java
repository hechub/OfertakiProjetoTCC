package br.com.hector.ofertaki;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BuscaDialog extends DialogFragment {

	private Spinner spnTipoBusca;
	private TextView txtTituloOferta;
	private AutoCompleteTextView acBuscaEndereco;
	private String tipoBusca;
	private ImageView imgOkBusca;
	private String acao = "endereco";
	private String query;
	public static int REQUEST_CODE_ENDERECO = 12;
	public static int REQUEST_CODE_TITULO = 13;
	
	OnItemSelectedListener observer = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			switch (position){
			case 0:
				acBuscaEndereco.setVisibility(View.VISIBLE);
				txtTituloOferta.setVisibility(View.GONE);
				acao = "endereco";
				break;
			
			case 1:
				acBuscaEndereco.setVisibility(View.GONE);
				txtTituloOferta.setVisibility(View.VISIBLE);
				acao = "categoria";
				break;
				
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private void sendResult(int REQUEST_CODE,String result) {
	    Intent intent = new Intent();
	    intent.putExtra("query", result);
	    getTargetFragment().onActivityResult(
	    getTargetRequestCode(), REQUEST_CODE, intent);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = inflater.inflate(R.layout.busca_dialog, container, false);
		imgOkBusca = (ImageView)v.findViewById(R.id.imgOkBusca);
		spnTipoBusca = (Spinner)v.findViewById(R.id.spnTipoBusca);
		acBuscaEndereco = (AutoCompleteTextView)v.findViewById(R.id.acBuscaEndereco);
		txtTituloOferta = (TextView)v.findViewById(R.id.txtBuscaTituloOferta);
		getDialog().setTitle("Pesquisa de Ofertas");
		
		
		acBuscaEndereco.setVisibility(View.GONE);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.tipo_busca, android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnTipoBusca.setAdapter(adapter);
		spnTipoBusca.setOnItemSelectedListener(observer);
		
		
		acBuscaEndereco.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.auto_complete));
		
		
		imgOkBusca.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(acao.equals("endereco")){
					
					query = acBuscaEndereco.getEditableText().toString();
					sendResult(REQUEST_CODE_ENDERECO,query);
					getDialog().dismiss();
					
				}else{
					
					String tituloQuery = txtTituloOferta.getEditableText().toString();
					getDialog().dismiss();
					sendResult(REQUEST_CODE_TITULO, tituloQuery);
							
					
				}
				
				
			}
		});
		
		
		return v;
	}
	
}
