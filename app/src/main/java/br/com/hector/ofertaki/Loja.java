package br.com.hector.ofertaki;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import br.com.hector.ofertaki.Oferta.tipoStatusOferta;

import com.google.android.maps.GeoPoint;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class Loja{

	private String nomeLoja;
	private String enderecoLoja;
	private String categoriaLoja;
	private String telefone;
	private String cnpj;
	private ParseGeoPoint geoPointLoja;
	private String idLoja;

	public ParseGeoPoint getGeoPointLoja() {
		return geoPointLoja;
	}
	public void setGeoPointLoja(ParseGeoPoint geoPointLoja) {
		this.geoPointLoja = geoPointLoja;
	}
	public String getNomeLoja() {
		return nomeLoja;
	}
	
	public void setNomeLoja(String nomeLoja) {
		
		if(nomeLoja.equals("")){
			throw new IllegalArgumentException("Nome Loja Inválido\n");
		}else{
			this.nomeLoja = nomeLoja;
		}
				
	}
	
	public String getEnderecoLoja() {
		return enderecoLoja;
	}
	public void setEnderecoLoja(String enderecoLoja) {
		this.enderecoLoja = enderecoLoja;
	}
	public String getCategoriaLoja() {
		return categoriaLoja;
	}
	
	
	public void setCategoriaLoja(String categoriaLoja) {
		if(categoriaLoja.equals("Categoria")){
			throw new IllegalArgumentException("Selecione uma Categoria");
		}else{
			this.categoriaLoja = categoriaLoja;
		}
			
	}
	
	
	public String getIdLoja() {
		return idLoja;
	}
	public void setIdLoja(String idLoja) {
		this.idLoja = idLoja;
	}
	
	
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone){
		if(!Validacoes.isNumeros(telefone)){
			throw new IllegalArgumentException("Telefone Inválido");
		}else{
			this.telefone = telefone;
		}
	}
	
	
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		if(!Validacoes.isCnpjValido(cnpj)){
			throw new IllegalArgumentException("CNPJ Inválido\n");
		}else{
			this.cnpj = cnpj;
		}
	}
	
	public void atualizaCadastroLoja (Loja l)throws ParseException{
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Loja");
		ParseObject loja = new ParseObject("Loja");
		
		
		loja = query.get(l.getIdLoja());
		
		Log.d("atualiza", l.getIdLoja());
		
		if(loja !=null){
			
			loja.setObjectId(l.getIdLoja());
			loja.put("nomeLoja", l.getNomeLoja());
			loja.put("categoriaLoja", l.getCategoriaLoja());
			loja.put("telefone", l.getTelefone());
			loja.save();
			
		}
	
	}
	
	
	public Loja getDadosMinhasLoja (ParseUser user)throws ParseException{
		
		Loja l = new Loja();
		
		ParseObject p = new ParseObject("Loja");
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Loja");
		query.whereEqualTo("parent", user);

			p = query.find().get(0);
			
			
			if(p!= null){
				
				l.setIdLoja(p.getObjectId());
				l.setNomeLoja(p.getString("nomeLoja"));
				l.setEnderecoLoja(p.getString("enderecoLoja"));
				l.setCategoriaLoja("categoriaLoja");
				l.setTelefone(p.getString("telefone"));
				l.setCnpj(p.getString("cnpj"));			
			}

		return l;
	}
	
	
	public ArrayList<Loja> getLojasFavoritas (String idUsuario){
		
		ArrayList<Loja> listaFavoritas = new ArrayList<Loja>();
		ArrayList<ParseObject> listaFavoritasParse = new ArrayList<ParseObject>();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorito");
		query.whereEqualTo("usuarioId", idUsuario);
		
		try {
			listaFavoritasParse = (ArrayList<ParseObject>) query.find();
			
			for(ParseObject o : listaFavoritasParse){
				Loja l = new Loja();
				ParseObject lojaParse = new ParseObject("Loja");
				lojaParse = o.getParseObject("lojaFavorita");
				l.setIdLoja(lojaParse.getObjectId());
				l.setNomeLoja(lojaParse.fetchIfNeeded().getString("nomeLoja"));
				l.setCategoriaLoja(lojaParse.fetchIfNeeded().getString("categoriaLoja"));
				listaFavoritas.add(l);
			}
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaFavoritas;
		
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.nomeLoja;
	}
	
	
	static int getQtdFavorito(ParseObject loja){
		int qtd = 0;
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorito");
		query.whereEqualTo("lojaFavorita", loja);
		
		try {
			qtd = query.count();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qtd;
	}
	
	public void favoritarLoja(String idUsuario, String idLoja){
		
		ParseObject po = new ParseObject("Favorito");
		ParseObject lojaParseObject = new ParseObject("Loja");
		lojaParseObject.setObjectId(idLoja);
		
		po.put("usuarioId", idUsuario);
		po.put("lojaFavorita", lojaParseObject);
		po.saveInBackground();
		ParsePush.subscribeInBackground(idLoja);
	}
	
	public String CadastraLoja(ParseUser dono) throws ParseException{
		ParseObject cadastroLoja = new ParseObject("Loja");

		cadastroLoja.put("nomeLoja", this.nomeLoja);
		cadastroLoja.put("enderecoLoja", this.enderecoLoja);
		cadastroLoja.put("categoriaLoja", this.categoriaLoja);
		cadastroLoja.put("telefone", this.telefone);
		cadastroLoja.put("cnpj", this.cnpj);
		cadastroLoja.put("parent", dono);
		cadastroLoja.put("geoPointLoja", this.geoPointLoja);
		cadastroLoja.save();
		
		return cadastroLoja.getObjectId();
		
	}
	
}
