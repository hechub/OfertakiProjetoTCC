package br.com.hector.ofertaki;

import java.util.ArrayList;
import java.util.Collection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;

public class PagerDetalharOfertaActivity extends FragmentActivity {

	
	private ViewPager vPagerDetalharOferta;
	private ArrayList<Oferta> ofertasLojaSelecionada;
	private Handler handler = new Handler();
	private ProgressDialog progress;
	private Oferta o;
	private ProgressBar pBar;
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putParcelableArrayList("listaOfertas", ofertasLojaSelecionada);
	}
	
	private class carregaPagerOfertas extends AsyncTask<Bundle, Void, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected Void doInBackground(Bundle... params) {
			
			if (params[0] == null) {
				ofertasLojaSelecionada = o.ofertasLoja(o);
			}else{
				ofertasLojaSelecionada = params[0].getParcelableArrayList("listaOfertas");
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			//progress.dismiss();
			pBar.setVisibility(View.GONE);
			FragmentManager fm = getSupportFragmentManager();
			vPagerDetalharOferta.setAdapter(new FragmentStatePagerAdapter(fm) {
				
				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					return ofertasLojaSelecionada.size();
				}
				
				@Override
				public Fragment getItem(int pos) {
					// TODO Auto-generated method stub
					
					return DetalharOferta.newInstance(ofertasLojaSelecionada.get(pos));		
					
					
				}
			});
			
			int i = 0;
			
			for (Oferta x : ofertasLojaSelecionada){
				
				
				if(x.getId().equals(o.getId())){
					vPagerDetalharOferta.setCurrentItem(i);
				}
				i++;
				
			}
		}
	}
	
	@Override
	protected void onCreate(final Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		

		getActionBar().setDisplayHomeAsUpEnabled(true);
		LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.pager_detalhar_oferta, null);
		
		vPagerDetalharOferta = (ViewPager)v.findViewById(R.id.mVPagerDetalharOferta);
		setContentView(vPagerDetalharOferta);
		
		pBar = (ProgressBar)v.findViewById(R.id.pBarPager);
		pBar.setVisibility(View.VISIBLE);
		
		String s = getIntent().getExtras().getString("ofertaSelecionada");
		o = new Oferta();
		o.setId(s);
		
		//progress = new ProgressDialog(getApplicationContext());
		//progress.setMessage("Carregando Oferta");
		//progress.setCancelable(false);
	//	progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		//progress.show();
		
		carregaPagerOfertas taskCarregaPager = new carregaPagerOfertas();
		taskCarregaPager.execute(arg0);
		
		

	
	}
	
}
