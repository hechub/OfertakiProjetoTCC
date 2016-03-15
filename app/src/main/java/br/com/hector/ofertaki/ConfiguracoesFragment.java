package br.com.hector.ofertaki;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;

public class ConfiguracoesFragment extends Fragment{

	private Switch swAlertaOferta;
	private TextView txtSobre, txtMeuCadastro;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.configuracoes_view, container, false);
		
		swAlertaOferta = (Switch)v.findViewById(R.id.swAlertaOfertas);
		txtSobre = (TextView)v.findViewById(R.id.txtSobre);
		txtMeuCadastro = (TextView)v.findViewById(R.id.txtMeuCadastro);
		
		txtMeuCadastro.setVisibility(View.GONE);
		
		if(ParseUser.getCurrentUser() != null){
			txtMeuCadastro.setVisibility(View.VISIBLE);
		}
		
		
		swAlertaOferta.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				Loja l = new Loja();
				String idInstalacao = ParseInstallation.getCurrentInstallation().getInstallationId();
				ArrayList<Loja> favoritos = new ArrayList<Loja>();
				favoritos = l.getLojasFavoritas(idInstalacao);
				
				if(isChecked){	
					try {
						for(Loja loja : favoritos){
							
							ParsePush.subscribeInBackground(loja.getIdLoja());
							
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(getActivity(), "Erro, tente novamente", Toast.LENGTH_SHORT).show();
					}
				}else{					
					
					try {
						for(Loja loja : favoritos){
							
							ParsePush.unsubscribeInBackground(loja.getIdLoja());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(getActivity(), "Erro, tente novamente", Toast.LENGTH_SHORT).show();
					}
				}
				
				
				
			}
		});
		
		txtMeuCadastro.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				FragmentManager fm = getFragmentManager();
				Fragment f;
				f = new AtualizaCadastroLoja();
				fm.beginTransaction().replace(R.id.frameMapa, f).addToBackStack(null).commit();
				
			}
		});
		
		txtSobre.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

				builder.setMessage("FATEC IPIRANGA - 2015\nCurso ADS \nEquipe de Desenvolvimento: \n\nAline Karen Ike \nHector A. Custódio \nKarintia Evelyn D. Alencar")
				       .setTitle("Sobre o Aplicativo");
				// Add the buttons
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				               // User clicked OK button
				        	   
				           }
				       });
				
				AlertDialog dialog = builder.create();
				dialog.show();
				
				
				
			}
		});
		
		return v;
	}
	
	
	
}
