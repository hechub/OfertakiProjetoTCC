package br.com.hector.ofertaki;

import java.util.ArrayList;

import com.parse.ParseException;
import com.parse.ParseInstallation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MinhasOfertasFragment extends ListFragment {
	
	private ProgressDialog dialog;
	private static Handler handler;
	private ArrayList<Oferta> listaOfertas;
	private static ArrayAdapter<Oferta> adapter;
	private LayoutInflater inflate;
	private ProgressDialog progress;
	private Oferta o;
	private String msgErros;
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.cadastro_loja_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		switch(id){
			case R.id.acao_novaOferta:
				Fragment f = new CadastroOfertaFragment();
				
				
				if(getListView().getCount() < 10){
					
					o = new Oferta();
					Bundle bd = new Bundle();
					bd.putSerializable("oferta_nova", o);
					f.setArguments(bd);
					getFragmentManager().beginTransaction().replace(R.id.frameMapa, f).addToBackStack(null).commit();
					
				}else{
					Toast.makeText(getActivity(), "Você só pode publicar 10 ofertas", Toast.LENGTH_SHORT).show();
				}
				
				break;
			case R.id.acao_excluirOferta:
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

				builder.setMessage("Deletar Oferta Selecionada?")
				       .setTitle("Confirmação");
				// Add the buttons
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) { 	  
				        	  try {
				        		  deletaOferta taskdeletar = new deletaOferta();
					        	  taskdeletar.execute();
				  			} catch (NullPointerException e) {
				  				e.printStackTrace();
				  			}
				           }
				       });
				builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				               // User cancelled the dialog
				           }
				       });
				
				AlertDialog dialog = builder.create();
				
			try {
				o = (Oferta) getListView().getItemAtPosition(getListView().getCheckedItemPosition());
				if (o!=null) {
					dialog.show();
				}else{
					Toast.makeText(getActivity(), "Selecione uma oferta para deletar", Toast.LENGTH_SHORT).show();	
				}
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				break;
				
			case R.id.acao_editarOferta:

				o = (Oferta) getListView().getItemAtPosition(getListView().getCheckedItemPosition());
			
			if (o != null) {
				
				Bundle b = new Bundle();
				b.putSerializable("oferta_editar", o);
				FragmentManager fmanager = getFragmentManager();
				Fragment edicao = new CadastroOfertaFragment();
				edicao.setArguments(b);
				fmanager.beginTransaction().replace(R.id.frameMapa, edicao)
						.addToBackStack(null).commit();
				
			}else{
				
				Toast.makeText(getActivity(),
						"Selecione uma oferta para editar", Toast.LENGTH_SHORT)
						.show();
				
			}
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public class deletaOferta extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(getActivity());
			progress.setMessage("Aguarde!");
			progress.setCancelable(false);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				o = (Oferta) getListView().getItemAtPosition(getListView().getCheckedItemPosition());
				o.excluiOferta();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			MinhasOfertasFragment.reloadLista((Oferta) getListView().getItemAtPosition(getListView().getCheckedItemPosition()));
			progress.dismiss();
		}
	}
	
	private static void reloadLista(Oferta o){
		adapter.remove(o);
		adapter.notifyDataSetChanged();
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		setHasOptionsMenu(true);	
		super.onCreate(savedInstanceState);
	}
	

	
	private class carregaLista extends AsyncTask<Void,Void,Void>{
		@Override
		protected void onPreExecute() {
			dialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Oferta o = new Oferta();
				Lojista l = new Lojista();
				listaOfertas = o.getMinhasOfertas(l.minhaLoja(getActivity()));
				msgErros = "";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msgErros = "Erro, Verifique sua conexão";
				
			}catch(Exception e){
				msgErros = "Erro, Tente Novamente";
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(!msgErros.equals("")){
				Toast.makeText(getActivity(), msgErros, Toast.LENGTH_SHORT).show();
				getFragmentManager().popBackStack();
			}else{
				LayoutInflater lInflater = getInflater();
				adapter = new ListaAdapter(lInflater.getContext(),R.layout.lista_ofertas,listaOfertas);
				setListAdapter(adapter);
				//adapter.notifyDataSetChanged();
				getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			}
			
			dialog.dismiss();
			
		}
		
	}
	
	private void setInflater(LayoutInflater i){
		this.inflate = i;;
	}
	
	private LayoutInflater getInflater(){
		return this.inflate;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		dialog = new ProgressDialog(getActivity());
		dialog.setMessage("Carregando suas Ofertas");
		dialog.setCancelable(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		setInflater(inflater);
		carregaLista taskCarregaLista = new carregaLista();
		taskCarregaLista.execute();
		
		
		
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	
	
}
