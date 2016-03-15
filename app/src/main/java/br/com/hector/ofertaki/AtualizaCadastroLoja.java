package br.com.hector.ofertaki;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

public class AtualizaCadastroLoja extends Fragment{

	private Button btnConfirmaCadastro;
	private TextView txtTituloCadastroLoja;
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
		txtTituloCadastroLoja = (TextView)v.findViewById(R.id.txtTituloCadstroOferta);
		
		
		txtTituloCadastroLoja.setText("Meu Cadastro");
		edtCnpj.setEnabled(false);
		autoComplete.setEnabled(false);
		edtSenha.setVisibility(View.GONE);
		edtConfirmaSenha.setVisibility(View.GONE);
		edtEmail.setVisibility(View.GONE);
		
		preencheCamposLayout taskAtualiza = new preencheCamposLayout();
		taskAtualiza.execute();
		
		btnConfirmaCadastro.setText("Salvar");
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.categorias_loja, android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCategoria.setAdapter(adapter);
		spnCategoria.setOnItemSelectedListener(observer);
		
		btnConfirmaCadastro.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				atualizaCadastroLoja taskAtualiza = new atualizaCadastroLoja();
				taskAtualiza.execute();
				
				
			}
		});
		
		
		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
	}
	
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
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	private int getIndex(Spinner spinner, String string){
		
		int index = 0;
		
		for(int i =0; i< spinner.getCount(); i++){
			
			if(spinner.getItemAtPosition(i).equals(string)){
				index = i;
			}
			
		}
		
		return index;
	}
	
	private class atualizaCadastroLoja extends AsyncTask<Void, Void, Void>{
		
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
				Loja l = new Loja();
				l.setIdLoja(loja.getIdLoja());
				l.setNomeLoja(edtNomeLoja.getEditableText().toString());
				l.setCategoriaLoja(categoria);
				l.setTelefone(edtTelefone.getEditableText().toString());
				l.atualizaCadastroLoja(l);
				msgErrosCadastro = "Dados Atualizados";
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msgErrosCadastro = e.getMessage();
			} catch (Exception e){
				e.printStackTrace();
				msgErrosCadastro = e.getMessage();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			progress.dismiss();
			
			if(!msgErrosCadastro.equals("")){
				Toast.makeText(getActivity(), msgErrosCadastro, Toast.LENGTH_SHORT).show();
			}
			
		}
	}
	
	
	private class preencheCamposLayout extends AsyncTask<Void, Void, Void>{
		
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
			
			try{
				pUser = ParseUser.getCurrentUser();
				
				if(pUser != null){
					loja = new Loja();
					loja = loja.getDadosMinhasLoja(pUser);
				}
				
				
			}catch(ParseException e){
				
				e.printStackTrace();
				
			}catch(Exception e1){
				
				e1.printStackTrace();
				
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			edtNomeLoja.setText(loja.getNomeLoja());
			edtCnpj.setText(loja.getCnpj());
			edtTelefone.setText(loja.getTelefone());
			autoComplete.setText(loja.getEnderecoLoja());
			
			spnCategoria.setSelection(getIndex(spnCategoria, loja.getCategoriaLoja()));
			
			progress.dismiss();
		
		
	}}}
	
