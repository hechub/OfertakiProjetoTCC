package br.com.hector.ofertaki;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.internal.AsyncCallback;

public abstract class ControlaFragmento extends FragmentActivity {
	
protected abstract Fragment createFragment();
	
	private DrawerLayout telaPrincipal;
	@SuppressWarnings("deprecation")
	private ActionBarDrawerToggle telaToggle;
	private static ListView conteudoMenu;
	private static ArrayList<String> opcoesMenu = new ArrayList<String>();
	private TextView txtnomeLojaLogada, txtemailLojaLogada;
	private View v;
	private ProgressBar pBarHeaderMenu;
	private String nomeLojaLogada, emailLojaLogada, categoriaLojaLogada;
	private atualizaInformacaoMenu task;
	private ImageView imgIconeLoja;

	private boolean verificaLogado (){
		ParseUser logado = ParseUser.getCurrentUser();
		if(logado != null){
			return true;
		};
		return false;
		
	}
	
	private class atualizaInformacaoMenu extends AsyncTask<Void,Void,Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			
			Lojista l = new Lojista();
			ParseObject o = l.minhaLoja(getApplicationContext());
			
			if (verificaLogado()) {
				nomeLojaLogada = o.getString("nomeLoja");
				try {
					emailLojaLogada = ParseUser.getCurrentUser().getUsername();
				} catch (Exception e) {
					e.printStackTrace();
				}
				categoriaLojaLogada = o.getString("categoriaLoja");
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {	
			conteudoMenu.removeHeaderView(v);
			conteudoMenu.addHeaderView(v,null,false);
			try {
				if (!nomeLojaLogada.equals(null) || !emailLojaLogada.equals(null) || !categoriaLojaLogada.equals(null)) {
					txtnomeLojaLogada.setText(nomeLojaLogada);
					txtemailLojaLogada.setText(emailLojaLogada);
					atualizaImagemLoja(imgIconeLoja, categoriaLojaLogada);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//pBarHeaderMenu.setVisibility(View.INVISIBLE);
			
		}
		
	}
	
	private void atualizaImagemLoja(ImageView imgCategoriaLoja, String CategoriaLoja){
		
		switch (CategoriaLoja){
		
		case "Moda":
			
			imgCategoriaLoja.setBackgroundResource(R.drawable.ic_cat_moda);
			break;
		case "Saúde e beleza":
			
			imgCategoriaLoja.setBackgroundResource(R.drawable.ic_cat_saude);
			break;
		case "Tecnologia":
			
			imgCategoriaLoja.setBackgroundResource(R.drawable.ic_cat_tecnologia);
			break;
		case "Infantil":
			
			imgCategoriaLoja.setBackgroundResource(R.drawable.ic_cat_infantil);
			break;
		case "Comidas e Bebidas":
			
			imgCategoriaLoja.setBackgroundResource(R.drawable.ic_cat_alimento);
			break;
		case "Utilidades e Decoração":
			
			imgCategoriaLoja.setBackgroundResource(R.drawable.ic_cat_utilidades);
			break;
		case "Livros e Papelaria":
			
			imgCategoriaLoja.setBackgroundResource(R.drawable.ic_cat_livro);
			break;
		case "Outros":
			
			imgCategoriaLoja.setBackgroundResource(R.drawable.ic_cat_diversos);
			break;
		
		}
	}
	
	public void atualizaMenu(){
		if(verificaLogado()){
			opcoesMenu.set(4,"Sair");
			if (Validacoes.isInternet(getApplication())) {
				task = new atualizaInformacaoMenu();
				task.execute();
			}
		}else{
			conteudoMenu.removeHeaderView(v);
			opcoesMenu.set(4,"Entrar");
		}
		
		conteudoMenu.setAdapter(new MenuAdapter(this,R.layout.custom_menu,opcoesMenu));
		
	}
	
	@Override
		protected void onSaveInstanceState(Bundle outState) {
			// TODO Auto-generated method stub
			outState.putSerializable("menuSalvo",opcoesMenu);
			super.onSaveInstanceState(outState);
		}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
			setContentView(R.layout.container_principal);
			
			
			getActionBar().setHomeButtonEnabled(true);
			telaPrincipal = (DrawerLayout) findViewById(R.id.menuDeslizante);
			
			v = View.inflate(getApplication(), R.layout.head_menu, null);
			imgIconeLoja = (ImageView)v.findViewById(R.id.imgLojaLogadaMenu);
			txtnomeLojaLogada = (TextView)v.findViewById(R.id.txtNomeLojaMenu);
			txtemailLojaLogada = (TextView)v.findViewById(R.id.txtEmailLojaMenu);
			//pBarHeaderMenu = (ProgressBar)v.findViewById(R.id.pBarHeaderMenu);
			
			//pBarHeaderMenu.setVisibility(View.VISIBLE);
			
			
			conteudoMenu = (ListView) findViewById(R.id.conteudoMenu);
			opcoesMenu.clear();
			opcoesMenu.add("Inicio");
			opcoesMenu.add("Minhas Ofertas");
			opcoesMenu.add("Favoritos");
			opcoesMenu.add("Configurações");
			if (verificaLogado()) {
				opcoesMenu.add("Sair");
			} else {
				opcoesMenu.add("Entrar");
			}
			MenuAdapter mAdapter = new MenuAdapter(this, R.layout.custom_menu);
			
			mAdapter.addAll(opcoesMenu);
			
			if (verificaLogado() && Validacoes.isInternet(getApplicationContext())) {
				task = new atualizaInformacaoMenu();
				task.execute();
			}
			conteudoMenu.setAdapter(mAdapter);
			conteudoMenu.setPadding(0, 50, 0, 50);
			

		telaToggle = new ActionBarDrawerToggle(this, telaPrincipal,R.drawable.ic_drawer,R.string.app_name, R.string.hello_world){
			@Override
			public void onDrawerClosed(View drawerView) {
				
				super.onDrawerClosed(drawerView);
				atualizaMenu();
				
			}
			
			@Override
			public void onDrawerOpened(View drawerView) {
				
				super.onDrawerOpened(drawerView);
				atualizaMenu();
				
			}

		};
		
		telaPrincipal.setDrawerListener(telaToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.frameMapa);
		
		if(fragment == null){
			fragment = createFragment();
			fm.beginTransaction().add(R.id.frameMapa, fragment).commit();
		}
		
		conteudoMenu.setOnItemClickListener(new OnItemClickListener() {
			FragmentManager fm = getSupportFragmentManager();
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				
				Fragment f;
				String opcao = parent.getItemAtPosition(position).toString();
				telaPrincipal.closeDrawers();
				
				switch(opcao){
					case "Inicio":
						
						getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
						break;
						
					case "Minhas Ofertas":
						if (ParseUser.getCurrentUser() != null) {
							f = new MinhasOfertasFragment();
							fm.beginTransaction().replace(R.id.frameMapa, f)
									.addToBackStack(null).commit();
						}
					break;
					
					case "Favoritos":
						
						f = new MeusFavoritos();
						fm.beginTransaction().replace(R.id.frameMapa, f).addToBackStack(null).commit();
						break;
					case "Configurações":
						
						f = new ConfiguracoesFragment();
						fm.beginTransaction().replace(R.id.frameMapa, f).addToBackStack(null).commit();
						break;
						
					case "Sair":
						ParseUser.logOut();
						getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
						break;
						
					case "Entrar":
						f = new LoginFragment();
						getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
						fm.beginTransaction().replace(R.id.frameMapa, f).addToBackStack(null).commit();
						
						break;
				
				}
				
				telaPrincipal.closeDrawers();
				
			}
		});
		
	}	
	
	@Override
		protected void onPostCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onPostCreate(savedInstanceState);
			telaToggle.syncState();
		}
	
	
	@Override
		public void onConfigurationChanged(Configuration newConfig) {
			// TODO Auto-generated method stub
			super.onConfigurationChanged(newConfig);
			telaToggle.onConfigurationChanged(newConfig);
		}
	
	@Override
		public boolean onOptionsItemSelected(MenuItem item) {

			if(telaToggle.onOptionsItemSelected(item)){
				return true;
			}
		
			return super.onOptionsItemSelected(item);
		}
	
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		
		return super.onCreateOptionsMenu(menu);
	}*/
	
}
