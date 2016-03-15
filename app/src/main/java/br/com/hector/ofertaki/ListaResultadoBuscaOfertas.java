package br.com.hector.ofertaki;

import java.util.ArrayList;

import com.parse.ParseException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ListaResultadoBuscaOfertas extends ListFragment {
	
	
	private String tituloQuery;
	private ListaAdapter adapter;
	private ArrayList<Oferta> ofertasEncontradas;
	private buscaOfertas taskBuscaOfertas;
	private Oferta o;
	private boolean sucesso = true;
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
	
	private interface onListaCompleta{
		
		public void onListaCarregada();
		
	}
	
	public void onListItemClick(android.widget.ListView l, View v, int position, long id) {
		
		l.getItemAtPosition(position);
		
		Oferta o = (Oferta) l.getItemAtPosition(position);		
		
		if (o != null) {
			
			Intent i = new Intent(getActivity(),PagerDetalharOfertaActivity.class);
			i.putExtra("ofertaSelecionada", o.getId());
			startActivity(i);
		}
		
		
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		
		tituloQuery = getArguments().getString("tituloQuery");
		
		taskBuscaOfertas = new buscaOfertas(new onListaCompleta() {
			
			@Override
			public void onListaCarregada() {
				adapter = new ListaAdapter(getActivity(), R.layout.lista_ofertas, ofertasEncontradas,true);
				setListAdapter(adapter);
				
			}
		});
		taskBuscaOfertas.execute(tituloQuery);
		
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	
	private class buscaOfertas extends AsyncTask<String, Void, ArrayList<Oferta>>{
		
		private onListaCompleta listener;
		
		public buscaOfertas(onListaCompleta listener){
			this.listener = listener;
		}
		
		@Override
		protected ArrayList<Oferta> doInBackground(String... params) {
			
			try {
				ArrayList<Oferta> ofertas = new ArrayList<Oferta>();
				ofertas = Oferta.buscarOfertasTitulo(params[0]);
				sucesso = true;
				return ofertas;
			}catch(ParseException e1){
				sucesso = false;
				e1.printStackTrace();
				
			}catch (Exception e) {
				// TODO Auto-generated catch block
				sucesso = false;
				e.printStackTrace();
			}
			return null;
			
		}
		
		@Override
		protected void onPostExecute(ArrayList<Oferta> result) {
			if (sucesso) {
				ofertasEncontradas = result;
				listener.onListaCarregada();
			}else if(getActivity() !=null && !sucesso){
				Toast.makeText(getActivity(), "Erro na conexão", Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
}
