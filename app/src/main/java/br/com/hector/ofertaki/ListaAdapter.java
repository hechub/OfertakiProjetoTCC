package br.com.hector.ofertaki;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.parse.ParseCloud;
import com.parse.ParseObject;

import br.com.hector.ofertaki.Oferta.tipoStatusOferta;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class ListaAdapter extends ArrayAdapter<Oferta>{
	
	private boolean cliente = false;
	private ViewHolder holder;
	private int media;
	private Oferta o;
	
	public ListaAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	}

	public ListaAdapter(Context context, int resource, ArrayList<Oferta> items) {
	    super(context, resource, items);
	}
	
	public ListaAdapter(Context context, int resource, ArrayList<Oferta> items, boolean cliente){
		super(context,resource,items);
		this.cliente = true;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Math.min(10, super.getCount());
	}
	
	static class ViewHolder{
		ImageView imgOferta;
		TextView txtTituloOferta;
		TextView txtPeriodoOferta;
		TextView txtPrecoOferta;
		RatingBar rtbar;
		ProgressBar pBarCarregandolista;
		ImageView imgPublicada;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    View v = convertView;

	    if (v == null) {

	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        v = vi.inflate(R.layout.lista_ofertas, null);
	        holder = new ViewHolder();
	    	holder.imgOferta = (ImageView)v.findViewById(R.id.imgImagemListaOferta);
	    	holder.txtTituloOferta = (TextView) v.findViewById(R.id.txtTituloOfertaLista);
	    	holder.txtPeriodoOferta = (TextView) v.findViewById(R.id.txtPeriodoOfertaLista);
	    	holder.txtPrecoOferta = (TextView) v.findViewById(R.id.txtPrecoOfertaLista);
	    	holder.rtbar = (RatingBar) v.findViewById(R.id.imgRBarNota);
	    	holder.imgPublicada = (ImageView)v.findViewById(R.id.imgPublicadaMapa);
	    	holder.pBarCarregandolista = (ProgressBar)v.findViewById(R.id.pBarCarregandoLista);
	    	holder.imgPublicada.setVisibility(View.GONE);
	    	holder.rtbar.setVisibility(View.GONE);
	    	holder.pBarCarregandolista.setVisibility(View.GONE);
	    	v.setTag(holder);
	    }else{
	    	holder = (ViewHolder) v.getTag();
	    }
	    o = getItem(position);

	    if (o != null) {
	        
	        if (!cliente) {
	        	holder.rtbar.setVisibility(View.VISIBLE);
	        	holder.imgPublicada.setVisibility(View.VISIBLE);
	        	
	        	Avaliacao a = new Avaliacao();
	 	        ParseObject p= new ParseObject("Oferta");
	 	        p.setObjectId(o.getId());
	 	        a.getAvaliacoes(p);   
	 			holder.rtbar.setMax(5);
	 			holder.rtbar.setProgress(a.getMediaAvaliacao());
	        	
				if (o.getStatusOferta().equals(tipoStatusOferta.EM_MAPA)) {
					holder.imgPublicada.setBackgroundResource(R.drawable.ic_em_mapa);
				} else {
					holder.imgPublicada.setBackgroundResource(R.drawable.ic_em_lista);
				}
			}
	                
	        holder.imgOferta.setImageBitmap(o.getImagemOferta());
	        holder.txtTituloOferta.setText(o.getTitulo_oferta());
	        holder.txtPeriodoOferta.setText("De: "+o.getDataInicio_oferta()+" Até: "+o.getDataFinal_oferta());
	        holder.txtPrecoOferta.setText(o.getPreco_oferta());
    
	    }

	    return v;

	}
}
