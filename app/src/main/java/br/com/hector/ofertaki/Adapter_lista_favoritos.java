package br.com.hector.ofertaki;

import java.util.List;
import java.util.zip.Inflater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter_lista_favoritos extends ArrayAdapter<Loja> {

	public Adapter_lista_favoritos(Context context, int resource,
			List<Loja> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View v = convertView;
		
		if (v == null) {

	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        v = vi.inflate(R.layout.adapter_lista_favoritos_view, null);

	    }
		
		Loja l = getItem(position);
		
		if(l != null){
		
			ImageView imgCategoriaLoja = (ImageView)v.findViewById(R.id.imgCategoriaLojaFavorita);
			TextView txtNomeLojaFavorita = (TextView)v.findViewById(R.id.txtNomeLojaFavorita);
			
			txtNomeLojaFavorita.setText(l.getNomeLoja());
			
			switch (l.getCategoriaLoja()){
			
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
		
		
		
		return v;
	}
	
	
}
