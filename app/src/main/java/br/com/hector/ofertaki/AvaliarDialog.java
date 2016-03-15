package br.com.hector.ofertaki;

import com.parse.ParseException;
import com.parse.ParseObject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class AvaliarDialog extends DialogFragment{

	private EditText edtTituloAvaliar, edtComentarioAvaliar;
	private RatingBar rtBarNotaAvaliar;
	private TextView txtOkAvaliarOferta;
	private String codigoOfertaSelecionada;
	private ProgressBar proBarSalvarAvaliacao;
	private ParseObject o;
	private Handler handler;
	private Avaliacao a;
	private String msgErros;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.avaliar_dialog, container, false);
		
		codigoOfertaSelecionada = getArguments().getString("codigo");
		o = new ParseObject("Oferta");
		o.setObjectId(codigoOfertaSelecionada);
		
		getDialog().setTitle("Avaliar Oferta");
		proBarSalvarAvaliacao = (ProgressBar)v.findViewById(R.id.proBarSalvarAvaliacao);
		proBarSalvarAvaliacao.setVisibility(View.INVISIBLE);
		
		edtTituloAvaliar = (EditText)v.findViewById(R.id.edtTituloOfertaAvaliar);
		edtComentarioAvaliar = (EditText)v.findViewById(R.id.edtComentarioAvaliar);
		rtBarNotaAvaliar = (RatingBar)v.findViewById(R.id.rtBarNotaOfertaAvaliar);
		txtOkAvaliarOferta = (TextView)v.findViewById(R.id.txtOkAvaliarOferta);
		rtBarNotaAvaliar.setMax(5);
		
		txtOkAvaliarOferta.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				a = new Avaliacao();
				try {
					a.setNotaAvaliacao(rtBarNotaAvaliar.getProgress());
					a.setComentarioAvaliacao(edtComentarioAvaliar.getText().toString());
					a.setTituloAvaliacao(edtTituloAvaliar.getText().toString());
					msgErros = "";
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					msgErros = e1.getMessage();
				}
				
				txtOkAvaliarOferta.setVisibility(View.INVISIBLE);
				proBarSalvarAvaliacao.setVisibility(View.VISIBLE);
				
				new Thread(){
					
					public void run(){
						
						if (msgErros.equals("")) {
							try {
								a.AvaliarOferta(o);
								handler.sendEmptyMessage(0);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							handler.sendEmptyMessage(0);
						}
					}
				}.start();
				
				handler = new Handler(){
					public void handleMessage(android.os.Message msg){
						
						if (msgErros.equals("")) {
							getDialog().dismiss();
						}else{
							txtOkAvaliarOferta.setVisibility(View.VISIBLE);
							proBarSalvarAvaliacao.setVisibility(View.INVISIBLE);
							Toast.makeText(getActivity(), msgErros, Toast.LENGTH_SHORT).show();
						}
					}
				};	
				
			}
		});
		
		
		return v;
		
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	
}
