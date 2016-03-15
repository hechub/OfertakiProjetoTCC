package br.com.hector.ofertaki;

import java.util.ArrayList;

import com.parse.ParseInstallation;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MeusFavoritos extends ListFragment {

	private ProgressDialog dialog;
	private ArrayList<Loja> listaFavoritos;
	private ArrayAdapter<Loja> adapter;
	private Handler handler;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		dialog = new ProgressDialog(getActivity());
		dialog.setMessage("Aguarde");
		dialog.setCancelable(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
		
		Thread t = new Thread(){
			Loja l = new Loja();
			
			public void run(){
				listaFavoritos = l.getLojasFavoritas(ParseInstallation.getCurrentInstallation().getInstallationId());
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						adapter = new Adapter_lista_favoritos(getActivity(), R.layout.adapter_lista_favoritos_view, listaFavoritos);
						setListAdapter(adapter);
						
					}
				});
				handler.sendEmptyMessage(0);
			}
		};
		
		t.start();
		
		
		handler = new Handler(){
			public void handleMessage(android.os.Message msg){
				dialog.dismiss();
				getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

			}
		};
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	
	
}
