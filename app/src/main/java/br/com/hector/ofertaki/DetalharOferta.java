package br.com.hector.ofertaki;


import java.io.Serializable;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class DetalharOferta extends Fragment{

	private TextView txtTituloOfertaDetalhe, txtDataFinalOfertaDetalhe, txtPrecoOfertaDetalhe, txtOndeEncontrar;
	private EditText edtDescricaoOfertaDetalhe;
	private ImageView imgOfertaDetalhe;
	private RatingBar rtBarNota;
	private Button btnAvaliarOferta;
	private Oferta o;
	private ListView listAvaliacao;
	private Avaliacao a;
	private ParseObject x;
	private static ArrayList<Avaliacao> avaliacoes;
	private int mediaAvaliacoes;
	private ProgressDialog progress;
	
	private String tituloOferta;
	private String dataFinalOferta;
	private String precoOferta;
	private String descOferta;
	private Bitmap imgOferta;
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		menu.clear();
		inflater.inflate(R.menu.detalhar_oferta_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	
	public static DetalharOferta newInstance(Serializable s){
		DetalharOferta d = new DetalharOferta();
		Bundle b = new Bundle();
		b.putSerializable("ofertaPager", s);
		d.setArguments(b);
		return d;
		
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id){
			case R.id.acao_favoritos:
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

				builder.setMessage("Adicionar esta loja aos Favoritos?")
				       .setTitle("Favoritos");
				// Add the buttons
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				               // User clicked OK button
				        	   try {
								String idInstalacao = ParseInstallation.getCurrentInstallation().getInstallationId();
								   String favorito = o.getLojaOferta(o);
								   Loja l = new Loja();
								   l.favoritarLoja(idInstalacao, favorito);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Toast.makeText(getActivity(), "Erro, verifique sua conexão", Toast.LENGTH_SHORT).show();
							}
				           }
				       });
				builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				               // User cancelled the dialog
				           }
				       });
				
				AlertDialog dialog = builder.create();
				dialog.show();
				
				
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private Loja detalherLoja (ParseObject l){
		
		Loja loja = new Loja();
		try {
			loja.setNomeLoja(l.fetch().getString("nomeLoja"));
			loja.setEnderecoLoja(l.fetch().getString("enderecoLoja"));
			loja.setTelefone(l.fetch().getString("telefone"));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return loja;
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		
		outState.putSerializable("ofertaSalva", o);
		
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		
		if(savedInstanceState != null){
			o = (Oferta) savedInstanceState.getSerializable("ofertaSalva");
		}else{
			o = (Oferta)getArguments().getSerializable("ofertaPager");
		}
		
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View b = inflater.inflate(R.layout.lista_detalhar_oferta,container,false);
		View v = inflater.inflate(R.layout.detalhar_oferta, null);
		//View c = inflater.inflate(R.layout.header_lista_detalhe_loja, null);
		
		listAvaliacao = (ListView)b.findViewById(R.id.listaDetalharOfertas);
		listAvaliacao.setHeaderDividersEnabled(true);
		
		
		rtBarNota = (RatingBar)v.findViewById(R.id.rtBarAvaliacoes);
		txtTituloOfertaDetalhe = (TextView)v.findViewById(R.id.txtTituloOfertaDetalhar);
		txtDataFinalOfertaDetalhe = (TextView)v.findViewById(R.id.txtDataFinalOfertaDetalhar);
		txtPrecoOfertaDetalhe = (TextView)v.findViewById(R.id.txtPrecoOfertaDetalhe);
		txtOndeEncontrar = (TextView)v.findViewById(R.id.txtOndeEncontrar);
		edtDescricaoOfertaDetalhe = (EditText)v.findViewById(R.id.edtDescricaoOfertaDetalhar);
		btnAvaliarOferta = (Button)v.findViewById(R.id.btnAvaliarOferta);
		imgOfertaDetalhe = (ImageView)v.findViewById(R.id.imgOfertaDetalhar);
		edtDescricaoOfertaDetalhe.setEnabled(false);
		
		a = new Avaliacao();
		x = new ParseObject("Oferta");
		x.setObjectId(o.getId());
		rtBarNota.setMax(5);
		
		
		rtBarNota.setProgress(a.getMediaAvaliacao());
		
		CarregaDetalhesOferta taskDetalhes = new CarregaDetalhesOferta();
		taskDetalhes.execute();

		
		//preencher list view com avaliacoes e carrega avaliacoes do cloud database
		CarregaMediaAvaliacoes taskCalculaMedia = new CarregaMediaAvaliacoes();
		listAvaliacao.addHeaderView(v,null,false);
		taskCalculaMedia.execute();
		
		txtOndeEncontrar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment dialog = new DialogOndeEncontrar();
				
				Bundle b = new Bundle();
				b.putSerializable("oferta", o);
				dialog.setArguments(b);
				dialog.show(getFragmentManager(), "dialog onde encontrar");
				
				
			}
		});
		
		btnAvaliarOferta.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DialogFragment dialog = new AvaliarDialog();
				
				Bundle b = new Bundle();
				b.putString("codigo", o.getId());
				dialog.setArguments(b);
				dialog.show(getFragmentManager(), "dialog avaliar");
				
			}
		});
		
		return b;
	}
	
	private class CarregaDetalhesOferta extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(getActivity());
			progress.setMessage("Carregando Oferta");
			progress.setCancelable(false);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			tituloOferta = o.getTitulo_oferta();
			dataFinalOferta = ("Oferta Válida Até: "+o.getDataFinal_oferta());
			precoOferta = ("R$: "+o.getPreco_oferta());
			descOferta = (o.getDescricao_oferta());
			imgOferta = (o.getImagemOferta());
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			progress.dismiss();
			txtTituloOfertaDetalhe.setText(tituloOferta);
			txtDataFinalOfertaDetalhe.setText(dataFinalOferta);
			txtPrecoOfertaDetalhe.setText(precoOferta);
			edtDescricaoOfertaDetalhe.setText(descOferta);
			imgOfertaDetalhe.setImageBitmap(imgOferta);
		}
	}
	
	private class CarregaMediaAvaliacoes extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {

			try {
				avaliacoes = a.getAvaliacoes(x);
				mediaAvaliacoes = a.getMediaAvaliacao();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if (getActivity() != null) {
				AvaliacaoAdapter adapter = new AvaliacaoAdapter(getActivity(),
						R.layout.custom_adapter_avaliacoes, avaliacoes);
				listAvaliacao.setAdapter(adapter);
				rtBarNota.setProgress(mediaAvaliacoes);
			}
		}
	}
	
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	
}
