package br.com.hector.ofertaki;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Avaliacao {

	private int notaAvaliacao;
	private String tituloAvaliacao, comentarioAvaliacao;
	private int mediaAvaliacao = 0;
	
	
	
	public int getMediaAvaliacao() {
		return mediaAvaliacao;
	}
	public void setMediaAvaliacao(int mediaAvaliacao) {
		this.mediaAvaliacao = mediaAvaliacao;
	}
	public int getNotaAvaliacao() {
		return notaAvaliacao;
	}
	public void setNotaAvaliacao(int notaAvaliacao) {
		this.notaAvaliacao = notaAvaliacao;
	}
	
	public String getTituloAvaliacao() {
		return tituloAvaliacao;
	}
	public void setTituloAvaliacao(String tituloAvaliacao) {
		
		
		if(tituloAvaliacao.equals("")){
			throw new IllegalArgumentException("Campos em Branco\n");
		}else{
			this.tituloAvaliacao = tituloAvaliacao;
		}
		
	}
	
	public String getComentarioAvaliacao() {
		return comentarioAvaliacao;
	}
	public void setComentarioAvaliacao(String comentarioAvaliacao) {
		this.comentarioAvaliacao = comentarioAvaliacao;
		
		if(comentarioAvaliacao.equals("")){
			throw new IllegalArgumentException("Campos em Branco\n");
		}else{
			this.comentarioAvaliacao = comentarioAvaliacao;
		}
	}
	
	public void AvaliarOferta(ParseObject oferta){
		ParseObject avaliacao = new ParseObject("Avaliacao");
		
		avaliacao.put("notaAvaliacao", this.notaAvaliacao);
		avaliacao.put("tituloAvaliacao", this.tituloAvaliacao);
		avaliacao.put("comentarioAvaliacao", this.comentarioAvaliacao);
		avaliacao.put("ofertaAvaliada", oferta);
		
		
		try {
			avaliacao.save();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void calculaMedia(ArrayList<Avaliacao> listaAvaliacoes){
		
		int soma = 0, total;
		total = listaAvaliacoes.size();
		
		for(Avaliacao a : listaAvaliacoes){
			
			soma = soma + a.getNotaAvaliacao(); 
			
		}
		
		if (total != 0) {
			this.mediaAvaliacao = soma / total;
		}
		
	}
	
	public ArrayList<Avaliacao> getAvaliacoes(ParseObject oferta){
		
		ArrayList<Avaliacao> listaAvaliacoes = new ArrayList<Avaliacao>();
		
		ArrayList<ParseObject> AvaliacoesParse = new ArrayList<ParseObject>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Avaliacao");
		query.whereEqualTo("ofertaAvaliada", oferta);
				
		try {
			AvaliacoesParse = (ArrayList<ParseObject>) query.find();
			for(ParseObject o : AvaliacoesParse){
				Avaliacao x = new Avaliacao();
				
				x.setTituloAvaliacao(o.getString("tituloAvaliacao"));
				x.setComentarioAvaliacao(o.getString("comentarioAvaliacao"));
				x.setNotaAvaliacao(o.getInt("notaAvaliacao"));
				
				listaAvaliacoes.add(x);
				
			}
			
			calculaMedia(listaAvaliacoes);
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return listaAvaliacoes;
		
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.tituloAvaliacao + this.mediaAvaliacao;
	}
	
	
	
}
