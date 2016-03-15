package br.com.hector.ofertaki;

import java.io.IOException;
import java.util.List;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

public class CadastroLojaFragment extends Fragment {

	private Button btnConfirmaCadastro;
	private EditText edtNomeLoja, edtCnpj,
			edtTelefone, edtEmail, edtSenha, edtConfirmaSenha;
	private Spinner spnCategoria;
	private String categoria;
	private AutoCompleteTextView autoComplete;
	
	private String nomeLoja, cnpj, endereco, telefone, email, senha, confirmaSenha;
	
	private Lojista lojista;
	
	private Handler handler;
	
	private ProgressDialog progress;
	
	private ParseUser pUser;
	private Loja loja;
	
	private String msgErrosCadastro;
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);

		//Parse.enableLocalDatastore(getActivity());

	}
	
	OnItemSelectedListener observer = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			categoria = (String) parent.getItemAtPosition(position);
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	};
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.cadastro_loja, container, false);
		btnConfirmaCadastro = (Button) v.findViewById(R.id.btnConfirmaCadastro);
		edtNomeLoja = (EditText) v.findViewById(R.id.edtNomeLoja);
		edtCnpj = (EditText) v.findViewById(R.id.edtCnpj);
		spnCategoria = (Spinner) v.findViewById(R.id.spnCategoria);
		edtTelefone = (EditText) v.findViewById(R.id.edtTelefone);
		edtEmail = (EditText) v.findViewById(R.id.edtEmail);
		edtSenha = (EditText) v.findViewById(R.id.edtSenha);
		edtConfirmaSenha = (EditText) v.findViewById(R.id.edtConfirmaSenha);
		autoComplete = (AutoCompleteTextView)v.findViewById(R.id.autocomplete);
		autoComplete.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.auto_complete));
		
		autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		        // Get data associated with the specified position
		        // in the list (AdapterView)
		        String description = (String) parent.getItemAtPosition(position);
		        autoComplete.setText(description);
		        Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
		    }
		});
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.categorias_loja, android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCategoria.setAdapter(adapter);
		spnCategoria.setOnItemSelectedListener(observer);
		
		btnConfirmaCadastro.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Parse.initialize(getActivity(),
						"uUmWwZ0x0b31l93s3JHKtm4quy8Rlhg3jGU1Mi5b",
						"56OKLdkeRv2r0YEV8axRi6XR5ONI4PsckwdD3Re9");
				
				 int erros = 0;
				 StringBuilder mensagemErro = new StringBuilder();
					
				 try {
					 nomeLoja = edtNomeLoja.getEditableText().toString();
					 cnpj = edtCnpj.getEditableText().toString(); 
					 endereco = autoComplete.getText().toString();
					 telefone = edtTelefone.getEditableText().toString(); 
					 email = edtEmail.getEditableText().toString(); 
					 senha = edtSenha.getEditableText().toString(); 
					 confirmaSenha = edtConfirmaSenha.getEditableText().toString();
					 
					 
					 loja = new Loja();
					 loja.setNomeLoja(nomeLoja);
					 loja.setCnpj(cnpj);
					 loja.setCategoriaLoja(categoria);
					 loja.setEnderecoLoja(endereco);
					 loja.setTelefone(telefone);
					 
					 lojista = new Lojista();
					 lojista.setEmail(email);
					 lojista.setSenha(senha, confirmaSenha);
				} catch (IllegalArgumentException e2) {
					
					e2.printStackTrace();
					mensagemErro.append(e2.getMessage());
					erros ++;
					
				}
		
				if (erros == 0) {
					
					progress = new ProgressDialog(getActivity());
					progress.setMessage("Cadastrando Loja");
					progress.setCancelable(false);
					progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progress.show();
					new Thread() {
						
						public void run(){
							
							try {
								loja.setGeoPointLoja(new Mapa().geoPonto(endereco,getActivity()));
								pUser = lojista.CadastrarUsuario();
								
								if(pUser != null){
									
									loja.CadastraLoja(pUser);
									
									msgErrosCadastro = "Logado com sucesso";
									handler.sendEmptyMessage(0);
									FragmentManager fm = getFragmentManager();
									fm.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
								}
								
							} catch (ParseException e1) {
								
								e1.printStackTrace();
								progress.dismiss();
								
								if(pUser != null && (e1.getCode() != ParseException.USERNAME_TAKEN)){
									try {
										ParseUser.logOut();
										pUser.delete();
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
									
								switch(e1.getCode()){
								case ParseException.DUPLICATE_VALUE:
									msgErrosCadastro = "CNPJ já Existe";
									break;
									
								case ParseException.CONNECTION_FAILED:
									msgErrosCadastro = "Problema no Cadastro, verifique sua conexão";
									break;
									
								case ParseException.USERNAME_TAKEN:
									msgErrosCadastro = "Email já cadastrado";
									break;
									
								case ParseException.REQUEST_LIMIT_EXCEEDED:
									msgErrosCadastro = "Problema no Cadastro, verifique sua conexão";
									break;
								default:
									msgErrosCadastro = e1.getMessage();
									break;
								
								}
								handler.sendEmptyMessage(0);
								
							}catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								progress.dismiss();
								msgErrosCadastro = "Erro ao cadastrar loja";
								handler.sendEmptyMessage(0);
						}
							
						}
						
						
					}.start();
				}else{
					Toast.makeText(getActivity(), "Erro no Cadastro: \n"+mensagemErro, Toast.LENGTH_LONG).show();
					//handler.sendEmptyMessage(0);
				}

				
				handler = new Handler(){
					public void handleMessage(android.os.Message msg){
						progress.dismiss();
						Toast.makeText(getActivity(), msgErrosCadastro, Toast.LENGTH_LONG).show();
					}
				};
				
					

			}
		});

		return v;
	}

}
