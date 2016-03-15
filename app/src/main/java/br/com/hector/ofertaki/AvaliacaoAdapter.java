package br.com.hector.ofertaki;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class AvaliacaoAdapter extends ArrayAdapter<Avaliacao>{
	
	public AvaliacaoAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	}

	public AvaliacaoAdapter (Context context, int resource, ArrayList<Avaliacao> items) {
	    super(context, resource, items);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    View v = convertView;

	    if (v == null) {

	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        v = vi.inflate(R.layout.custom_adapter_avaliacoes, null);

	    }

	    Avaliacao a = getItem(position);

	    if (a != null) {
	    	
	    	TextView txtTituloAvaliacaoUsuario = (TextView)v.findViewById(R.id.txtTituloAvaliacaoUsuario);
	    	RatingBar rtBarAvaliacaoUsuario = (RatingBar)v.findViewById(R.id.rtBarNotaAvaliacaoUsuarios);
	    	EditText edtComentarioUsuario = (EditText)v.findViewById(R.id.edtComentarioUsuarios);
	    	
	    	edtComentarioUsuario.setEnabled(false);
	    	rtBarAvaliacaoUsuario.setMax(5);
	    	
	    	txtTituloAvaliacaoUsuario.setText(a.getTituloAvaliacao());
	    	rtBarAvaliacaoUsuario.setProgress(a.getNotaAvaliacao());
	    	edtComentarioUsuario.setText(a.getComentarioAvaliacao());

	    }

	    return v;

	}

}
