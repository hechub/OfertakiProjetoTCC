package br.com.hector.ofertaki;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends ArrayAdapter<String> {

	public MenuAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	}

	public MenuAdapter(Context context, int resource, ArrayList<String> items) {
	    super(context, resource, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    View v = convertView;

	    if (v == null) {

	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        v = vi.inflate(R.layout.custom_menu, null);

	    }

	    String p = getItem(position);

	    if (p != null) {

	        TextView opcao = (TextView) v.findViewById(R.id.txtOpcaoMenu);
	        opcao.setText(p);
	        ImageView imgOpcao = (ImageView)v.findViewById(R.id.imgIconeMenu);
	        //imgOpcao.setBackgroundResource(R.drawable.icone_inicio);
	        
	        switch(p){
	        case "Inicio":
	        	imgOpcao.setBackgroundResource(R.drawable.icone_inicio);
	        	break;
	        case "Minhas Ofertas":
	        	imgOpcao.setBackgroundResource(R.drawable.icone_minhaoferta);
	        	break;
	        case "Favoritos":
	        	imgOpcao.setBackgroundResource(R.drawable.icone_favorito);
	        	break;
	        case "Configurações":
	        	imgOpcao.setBackgroundResource(R.drawable.icone_configuracoes);
	        	break;
	        case "Sair":
	        	imgOpcao.setBackgroundResource(R.drawable.icone_sair);
	        	break;
	        case "Entrar":
	        	imgOpcao.setBackgroundResource(R.drawable.icone_entrar);
	        	break;
	        }
	        
	        
	    }

	    return v;

	}
	

}
