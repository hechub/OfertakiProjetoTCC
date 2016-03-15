package br.com.hector.ofertaki;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;


public class CadastroOfertaFragment extends Fragment {
	
	public static ImageView imvImagemOferta;
	public static int DATA_REQUEST = 15;
	private CheckBox ckBoxMarcarMapa;
	private EditText edtTituloOferta, edtDataFinalOferta, edtPrecoOferta, edtDescricaoOferta;
	private Button btnConfimaCadastroOferta;
	private TextWatcher dataFinalMask, precoOferta;
	private ProgressDialog progress;
	private Oferta minhaOferta, o;
	private Lojista lojista;
	private Boolean sucesso;
	private String acao = "Cadastrando Oferta";
	private String msgErros;
	private DialogFragment selecionaDataDialog;
	private boolean erro = false;
	private AlertDialog.Builder builder;
	private boolean atualizarMarcacao;
	
	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
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
		// TODO Auto-generated method stub
		
		o = (Oferta) getArguments().getSerializable("oferta_editar");
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == DATA_REQUEST){
			
			edtDataFinalOferta.setText(data.getExtras().getString("dataSelecionada"));
			
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.cadastro_oferta, container, false);
		
		imvImagemOferta = (ImageView)v.findViewById(R.id.imvImagemOferta);
		edtTituloOferta = (EditText)v.findViewById(R.id.edtTituloOferta);
		edtDataFinalOferta = (EditText)v.findViewById(R.id.edtDataFinalOferta);
		edtDescricaoOferta = (EditText)v.findViewById(R.id.edtDescricaoOferta);
		edtPrecoOferta = (EditText)v.findViewById(R.id.edtPrecoOferta);
		ckBoxMarcarMapa = (CheckBox)v.findViewById(R.id.ckBoxMarcarMapa);
		btnConfimaCadastroOferta = (Button)v.findViewById(R.id.btnConfirmaCadastroOferta);
		
		dataFinalMask = Mask.insert("##/##/####", edtDataFinalOferta);
		precoOferta = MaskMonetaria.insert(edtPrecoOferta);
		
		
		edtDataFinalOferta.setFocusable(false);
		
		edtDataFinalOferta.addTextChangedListener(dataFinalMask);
		edtPrecoOferta.addTextChangedListener(precoOferta);
		selecionaDataDialog = new DateDialog();
		selecionaDataDialog.setTargetFragment(this, DATA_REQUEST);
		
		
		
		
		if(o != null){
			
			edtTituloOferta.setText(o.getTitulo_oferta());
			edtDataFinalOferta.setText(o.getDataFinal_oferta());
			edtDescricaoOferta.setText(o.getDescricao_oferta());
			edtPrecoOferta.setText(o.getPreco_oferta());
			imvImagemOferta.setImageBitmap(o.getImagemOferta());
			acao = "Atualizando Oferta";
			
		};
		
		progress = new ProgressDialog(getActivity());
		progress.setMessage(acao);
		progress.setCancelable(false);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
		//Listener para Edit Text Validade da oferta, que abre um DateDialog para sele��o da Data.
		
		edtDataFinalOferta.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				selecionaDataDialog.show(getFragmentManager(), "Escolha Data");
				
			}
		});
		
		
		//Listener para bot�o de Cadastrar Oferta
		btnConfimaCadastroOferta.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String tituloOferta = edtTituloOferta.getText().toString();
				String dataInicioOferta = format.format(Calendar.getInstance().getTime());
				String dataFinalOferta = edtDataFinalOferta.getText().toString();
				String precoOferta = edtPrecoOferta.getText().toString();
				String descricaoOferta = edtDescricaoOferta.getText().toString();
				Bitmap imagemOferta = ((BitmapDrawable)imvImagemOferta.getDrawable()).getBitmap();
			
				minhaOferta = new Oferta();
				
				if (o != null) {
					minhaOferta.setId(o.getId());
				};
				
				try {
					minhaOferta.setTitulo_oferta(tituloOferta);
					minhaOferta.setDatas_oferta(dataFinalOferta, dataInicioOferta);
					minhaOferta.setPreco_oferta(precoOferta);
					minhaOferta.setDescricao_oferta(descricaoOferta);
					minhaOferta.setImagemOferta(MainActivity.redimensionaBitmap(imagemOferta));
					lojista = new Lojista();
					erro = false;
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
					erro = true;
				}catch(Exception e1){
					e1.printStackTrace();
					erro = true;
				}
				
				
				if (!erro) {
					
					final cadastraOferta TaskcadastraOferta = new cadastraOferta();
					
					confirmaMudancaMapa confirmacao = new confirmaMudancaMapa(new OnTaskCompleted() {
						
						@Override
						public void onTaskCompleted() {
							TaskcadastraOferta.execute();
						}
					});
					
					confirmacao.recebeConfirmacao();
				}
				
			}	
		});
		
		
		
		imvImagemOferta.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment menu = new menuAddImagem();
				menu.show(getChildFragmentManager(), "opcaoImagem");
				
			}
		});
		
		
		return v;
	}
	
	public class confirmaMudancaMapa{
		
		private OnTaskCompleted callback;
		
		public confirmaMudancaMapa(OnTaskCompleted callback){
			this.callback = callback;
		}
		
		public void recebeConfirmacao(){
			
			if(ckBoxMarcarMapa.isChecked()){
				builder = new AlertDialog.Builder(getActivity());
				
				builder.setMessage("Se voc� tiver outra oferta no mapa ela ser� substitu�da, Continuar?")
						.setTitle("Marcar no Mapa");
				
				builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// User clicked OK button
								
								//atualizaOfertaNoMapa taskAtualizaMapa = new atualizaOfertaNoMapa();
								//taskAtualizaMapa.execute();
								atualizarMarcacao = true;
								dialog.dismiss();
								callback.onTaskCompleted();
								
							}
						});
				
				builder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.dismiss();
							}
						});

				AlertDialog dialog = builder.create();
				dialog.show();
			}else{
				atualizarMarcacao = false;
				callback.onTaskCompleted();
			}
			
		}
		
		
	}

	private class cadastraOferta extends AsyncTask<Void,Void,Void>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progress.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
		
			try {
				
				if (acao.equals("Atualizando Oferta")) {
					minhaOferta.atualizaOferta(minhaOferta);

				} else if (acao.equals("Cadastrando Oferta")){
					minhaOferta.CadastraOferta(lojista.minhaLoja(getActivity()));
					
				}
				
				minhaOferta.limpaOfertaPrincipal(lojista.minhaLoja(getActivity()),atualizarMarcacao);
				minhaOferta.atualizaOfertaPrincipal(minhaOferta,atualizarMarcacao);
				
				
				
				sucesso=true;
				
			} catch (ParseException e) {
				e.printStackTrace();
				sucesso=false;
				
				switch(e.getCode()){
				case ParseException.DUPLICATE_VALUE:
					msgErros = "Oferta J� Cadastrada";
					break;
					
				case ParseException.CONNECTION_FAILED:
					msgErros = "Problema no Cadastro, verifique sua conex�o";
					break;

					
				case ParseException.REQUEST_LIMIT_EXCEEDED:
					msgErros = "Problema no Cadastro, verifique sua conex�o";
					break;
				default:
					msgErros = e.getMessage();
					break;
				
			}
			}catch(Exception e){
				e.printStackTrace();
				msgErros = "Problema no Cadastro, verifique sua conex�o";
				sucesso=false;
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			progress.dismiss();
			if(sucesso){
				Toast.makeText(getActivity(), "Oferta Cadastrada Com Sucesso", Toast.LENGTH_LONG).show();						
				FragmentManager fm = getFragmentManager();
				fm.popBackStack();
			}else if(!sucesso){
				Toast.makeText(getActivity(), msgErros, Toast.LENGTH_SHORT).show();
			}
			
		}
		
		
		@Override
		protected void onCancelled(Void result) {
			// TODO Auto-generated method stub
			super.onCancelled(result);
		}
	}
	
	
	public class menuAddImagem extends DialogFragment{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			
			AlertDialog.Builder builder = new Builder(getActivity());
			builder.setTitle("Selecione");
			
			builder.setItems(R.array.menuImagem, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
					case 0:
						Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
						i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
						
						getActivity().startActivityForResult(i, 1);
						
						break;
					case 1:
						
						Intent x = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						
						getActivity().startActivityForResult(x, 2);

						
						break;
					}
					
				}
			});
			return builder.create();
		}
		
	}
}



