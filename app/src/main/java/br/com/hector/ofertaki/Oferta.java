package br.com.hector.ofertaki;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

public class Oferta implements Serializable, Parcelable{
	
	public enum tipoStatusOferta {EM_MAPA,EM_LISTA};
	private tipoStatusOferta statusOferta;
	private String titulo_oferta;
	private String descricao_oferta;
	private String dataInicio_oferta, dataFinal_oferta, preco_oferta;
	private Bitmap imagemOferta;
	private String id;
	private LatLng geoPoint;
	private int notaOferta;
	
	
	public Oferta (){
		
	}
	
	public tipoStatusOferta getStatusOferta() {
		return statusOferta;
	}
	public void setStatusOferta(tipoStatusOferta statusOferta) {
		this.statusOferta = statusOferta;
	}
	
	
	public int getNotaOferta() {
		return notaOferta;
	}
	public void setNotaOferta(int notaOferta) {
		this.notaOferta = notaOferta;
	}
	
	
	public Bitmap getImagemOferta() {
		return imagemOferta;
	}
	public void setImagemOferta(Bitmap imagemOferta) {
		this.imagemOferta = imagemOferta;
	}
	
	
	public String getTitulo_oferta() {
		return titulo_oferta;
	}
	public void setTitulo_oferta(String titulo_oferta) {
		
		if(titulo_oferta.equals("")){
			throw new IllegalArgumentException("Digite um Título");
		}else{
			this.titulo_oferta = titulo_oferta;	
		}
		
	}
	
	
	public String getDescricao_oferta() {
		return descricao_oferta;
	}
	public void setDescricao_oferta(String descricao_oferta) {
		if(descricao_oferta.equals("")){
			throw new IllegalArgumentException("Digite uma descrição");
			
		}else{
			this.descricao_oferta = descricao_oferta;
		}
	}
	
	
	public String getDataInicio_oferta() {
		return dataInicio_oferta;
	}	
	public void setDataInicio_oferta(String data_inicio){
		this.dataInicio_oferta = data_inicio;
	}
	public String getDataFinal_oferta() {
		return dataFinal_oferta;
	}
	public void setDataFinal_oferta(String data_final){
		this.dataFinal_oferta = data_final;
	}
	public void setDatas_oferta(String dataFinal_oferta, String dataInicio_oferta) {
		this.dataInicio_oferta = dataInicio_oferta;
		
		
		Calendar inicio = Calendar.getInstance();
		Calendar fim = Calendar.getInstance();
		//Locale l = new Locale("pt", "BR");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			inicio.setTime(format.parse(dataInicio_oferta));
			fim.setTime(format.parse(dataFinal_oferta));
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalArgumentException("Data Inválida");
		}
		
		if(fim.before(inicio)){
			throw new IllegalArgumentException("Validade menor que data atual");
		}else{
			this.dataFinal_oferta = dataFinal_oferta;
		}
	}
	
	
	public String getPreco_oferta() {
		return preco_oferta;
	}
	public void setPreco_oferta(String preco_oferta) {
		
		if(preco_oferta.equals("")){
			throw new IllegalArgumentException("Preço não pode ser 0.00");
		}else{
			this.preco_oferta = preco_oferta;
		}
	}
	
	
	public LatLng getGeoPoint() {
		return geoPoint;
	}
	public void setGeoPoint(LatLng geoPoint) {
		this.geoPoint = geoPoint;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public static ArrayList<Oferta> buscarOfertasTitulo (String titulo)throws ParseException{
		
		final ArrayList<Oferta> listaOfertasLoja = new ArrayList<Oferta>();
		ArrayList<ParseObject> listaParse = new ArrayList<ParseObject>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Oferta");
		query.whereContains("tituloOferta", titulo);

			listaParse = (ArrayList<ParseObject>) query.find();
			
			for(ParseObject o : listaParse){
				
				Oferta x = new Oferta();
				
				x.setId(o.getObjectId());
				x.setTitulo_oferta(o.getString("tituloOferta"));
				x.setDataInicio_oferta(o.getString("dataInicioOferta"));
				x.setDataFinal_oferta(o.getString("dataFinalOferta"));
				x.setDescricao_oferta(o.getString("descricaoOferta"));
				x.setPreco_oferta(o.getString("precoOferta"));
				x.setNotaOferta(o.getInt("notaOferta"));
				ParseFile file = o.getParseFile("imagemOferta");
				x.setImagemOferta(x.converterBitmap(file));
				
				if(o.getString("statusOferta").equals(tipoStatusOferta.EM_MAPA.toString())){
					x.setStatusOferta(tipoStatusOferta.EM_MAPA);
				}else{
					x.setStatusOferta(tipoStatusOferta.EM_LISTA);
				}
				
				listaOfertasLoja.add(x);
			}	
		return listaOfertasLoja;
	}
	
	public ArrayList<Oferta> ofertasLoja (Oferta o){
		
		final ArrayList<Oferta> listaOfertasLoja = new ArrayList<Oferta>();
		ArrayList<ParseObject> listaParse = new ArrayList<ParseObject>();
		ParseObject po = new ParseObject("Oferta");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Oferta");
		query.whereEqualTo("objectId", o.getId());
		
		try {
			po = query.find().get(0);
			ParseQuery<ParseObject> queryB = ParseQuery.getQuery("Oferta");
			queryB.whereEqualTo("lojaOferta", po.getParseObject("lojaOferta"));
			listaParse = (ArrayList<ParseObject>) queryB.find();
			
			for(ParseObject parse : listaParse){
				
				Oferta x = new Oferta();
				
				x.setId(parse.getObjectId());
				x.setTitulo_oferta(parse.getString("tituloOferta"));
				x.setDataInicio_oferta(parse.getString("dataInicioOferta"));
				x.setDataFinal_oferta(parse.getString("dataFinalOferta"));
				x.setDescricao_oferta(parse.getString("descricaoOferta"));
				x.setPreco_oferta(parse.getString("precoOferta"));
				x.setNotaOferta(parse.getInt("notaOferta"));
				ParseFile file = parse.getParseFile("imagemOferta");
				x.setImagemOferta(x.converterBitmap(file));
				
				listaOfertasLoja.add(x);
			}
			
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return listaOfertasLoja;
		
	}
	
	public void excluiOferta(){
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Oferta");
		query.whereEqualTo("tituloOferta", this.getTitulo_oferta());
		ParseObject oParse = new ParseObject("Oferta");
		
		try {
			oParse = (ParseObject) query.find().get(0);
			oParse.delete();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getLojaOferta(Oferta o){
		
		ParseObject p = new ParseObject("Loja");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Oferta");
		query.whereEqualTo("objectId", o.getId());
		String IdLoja = "";
		
		try {
			p = (ParseObject) query.find().get(0).getParseObject("lojaOferta");
			IdLoja = p.getObjectId();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return IdLoja;
	}
	
	static void notificarClientes(String idLoja, String tituloOferta, String idOferta){
		
		ParsePush push = new ParsePush();
		//push.setChannel(idLoja);
		
		push.setQuery(ParseInstallation.getQuery().whereEqualTo("channels", idLoja));
		JSONObject data;
		try {
			data = new JSONObject();
			data.put("alert", tituloOferta);
			data.put("oferta", idOferta);
			push.setData(data);
			push.sendInBackground();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public void atualizaOfertaPrincipal(Oferta o, boolean marcar)throws ParseException{
		
		if (marcar) {
			ParseObject po = new ParseObject("Oferta");
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Oferta");
			query.whereEqualTo("tituloOferta", o.getTitulo_oferta());
			try {
				po = (ParseObject) query.find().get(0);
				po.put("statusOferta", tipoStatusOferta.EM_MAPA.toString());
				po.save();

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			return;
		}
		
	}
	
	
	public void limpaOfertaPrincipal(ParseObject l, boolean marcar)throws ParseException{
		
		if (marcar) {
			ArrayList<ParseObject> minhasOfertasParse = new ArrayList<ParseObject>();
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Oferta");
			query.whereEqualTo("lojaOferta", l);
			try {
				minhasOfertasParse = (ArrayList<ParseObject>) query.find();
				for (ParseObject po : minhasOfertasParse) {

					po.put("statusOferta", tipoStatusOferta.EM_LISTA.toString());
					po.save();
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			return;
		}
	}
	
	
	
public void atualizaOferta(final Oferta o)throws ParseException{	
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Oferta");
		ParseObject oferta = new ParseObject("Oferta");
		
		
		oferta = query.get(o.getId());
		if(oferta !=null){
			oferta.put("tituloOferta", o.getTitulo_oferta());
			oferta.put("dataInicioOferta",o.getDataInicio_oferta());
			oferta.put("dataFinalOferta", o.getDataFinal_oferta());
			oferta.put("precoOferta", o.getPreco_oferta());
			oferta.put("descricaoOferta", o.getDescricao_oferta());
			oferta.put("statusOferta", tipoStatusOferta.EM_LISTA.toString());
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			o.getImagemOferta().compress(Bitmap.CompressFormat.JPEG, 85, stream);
			byte[] imagemData = stream.toByteArray();
			
			try {
				oferta.save();
				if(imagemData != null){
					ParseFile file = new ParseFile("oferta.jpg",imagemData);
					file.save();
					oferta.put("imagemOferta", file);
					oferta.save();
				}
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	
	public void CadastraOferta(ParseObject loja) throws ParseException{
		ParseObject oferta = new ParseObject("Oferta");
		
		oferta.put("tituloOferta", this.titulo_oferta);
		oferta.put("dataInicioOferta", this.dataInicio_oferta);
		oferta.put("dataFinalOferta", this.dataFinal_oferta);
		oferta.put("precoOferta", this.preco_oferta);
		oferta.put("descricaoOferta", this.descricao_oferta);
		oferta.put("statusOferta", tipoStatusOferta.EM_LISTA.toString());
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		this.imagemOferta.compress(Bitmap.CompressFormat.JPEG, 85, stream);
		
		byte[] imagemData = stream.toByteArray();
		
		
		if(loja != null){
			oferta.put("lojaOferta", loja);
			oferta.put("geoPointOferta",loja.getParseGeoPoint("geoPointLoja"));
			if(imagemData != null){
				ParseFile file = new ParseFile("oferta.jpg",imagemData);
				file.save();
				oferta.put("imagemOferta", file);
				oferta.save();
				
				notificarClientes(loja.getObjectId(), this.titulo_oferta, oferta.getObjectId());
				
			}
			
			
		}
		
	}
	
	
	
	
	private Bitmap converterBitmap (ParseFile file){
		
		try {
			byte minhaImagemBytes[] = file.getData();
			Bitmap minhaImagem = BitmapFactory.decodeByteArray(minhaImagemBytes, 0, minhaImagemBytes.length);
			return minhaImagem;
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
		
		
	}
	
	public ArrayList<Oferta> getMinhasOfertas(ParseObject loja)throws ParseException{
		
		
		ArrayList<Oferta> minhasOfertas = new ArrayList<Oferta>();
		ArrayList<ParseObject> minhasOfertasParse = new ArrayList<ParseObject>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Oferta");
		query.whereEqualTo("lojaOferta", loja);
				
		try {
			minhasOfertasParse = (ArrayList<ParseObject>) query.find();
			for(ParseObject o : minhasOfertasParse){
				Oferta x = new Oferta();
				
				x.setId(o.getObjectId());
				x.setTitulo_oferta(o.getString("tituloOferta"));
				x.setDataInicio_oferta(o.getString("dataInicioOferta"));
				x.setDataFinal_oferta(o.getString("dataFinalOferta"));
				x.setDescricao_oferta(o.getString("descricaoOferta"));
				x.setPreco_oferta(o.getString("precoOferta"));
				x.setNotaOferta(o.getInt("notaOferta"));
				ParseFile file = o.getParseFile("imagemOferta");
				x.setImagemOferta(this.converterBitmap(file));
				
				if(o.getString("statusOferta").equals(tipoStatusOferta.EM_MAPA.toString())){
					x.setStatusOferta(tipoStatusOferta.EM_MAPA);
				}else{
					x.setStatusOferta(tipoStatusOferta.EM_LISTA);
				}
				
				
				minhasOfertas.add(x);
			}
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return minhasOfertas;
		
	}
	
	public static ArrayList<Oferta> getTodasOfertas(){
		
		
		ArrayList<Oferta> minhasOfertas = new ArrayList<Oferta>();
		ArrayList<ParseObject> minhasOfertasParse = new ArrayList<ParseObject>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Oferta");
		query.whereEqualTo("statusOferta", tipoStatusOferta.EM_MAPA.toString());
				
		try {
			minhasOfertasParse = (ArrayList<ParseObject>) query.find();
			for(ParseObject o : minhasOfertasParse){
				Oferta x = new Oferta();
				
				x.setId(o.getObjectId());
				x.setTitulo_oferta(o.getString("tituloOferta"));
				x.setDataInicio_oferta(o.getString("dataInicioOferta"));
				x.setDataFinal_oferta(o.getString("dataFinalOferta"));
				x.setDescricao_oferta(o.getString("descricaoOferta"));
				x.setPreco_oferta(o.getString("precoOferta"));
				x.setNotaOferta(o.getInt("notaOferta"));
				ParseFile file = o.getParseFile("imagemOferta");
				x.setGeoPoint(new LatLng(o.getParseGeoPoint("geoPointOferta").getLatitude(), o.getParseGeoPoint("geoPointOferta").getLongitude()));
				x.setImagemOferta(x.converterBitmap(file));
				
				minhasOfertas.add(x);
			}
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return minhasOfertas;
		
	}
	
	
	public static ArrayList<LatLng> getOfertasGeoPoint(){
		final ArrayList<LatLng> todasOfertas = new ArrayList<LatLng>();
		ArrayList<ParseObject> ofertasparse = new ArrayList<ParseObject>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Oferta");
		
		try {
			ofertasparse = (ArrayList<ParseObject>) query.find();
			for(ParseObject o : ofertasparse){
				LatLng geoPoint = new LatLng(o.getParseGeoPoint("geoPointOferta").getLatitude(), o.getParseGeoPoint("geoPointOferta").getLongitude());
				todasOfertas.add(geoPoint);
			}
			
			return todasOfertas;
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.titulo_oferta + this.id;
	}

	
	//-------------------------------------------------------------------------------------------------------
	//Métodos Parcelable
	
	
	public Oferta (Parcel p){
		
		String status = p.readString();
		if(status.equals(tipoStatusOferta.EM_LISTA)){
			this.statusOferta = tipoStatusOferta.EM_LISTA;
		}else{
			this.statusOferta = tipoStatusOferta.EM_MAPA;
		};
		
		this.titulo_oferta = p.readString();
		this.descricao_oferta = p.readString();
		this.dataInicio_oferta = p.readString();
		this.dataFinal_oferta = p.readString();
		this.preco_oferta = p.readString();
		this.id = p.readString();
		this.notaOferta = p.readInt();
		p.setDataPosition(0);
		this.imagemOferta = Bitmap.CREATOR.createFromParcel(p);
		
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		

		parcel.writeParcelable(this.imagemOferta, 0);
		parcel.writeString(String.valueOf(this.statusOferta));
		parcel.writeString(this.titulo_oferta);
		parcel.writeString(this.descricao_oferta);
		parcel.writeString(this.dataInicio_oferta);
		parcel.writeString(this.dataFinal_oferta);
		parcel.writeString(this.preco_oferta);
		parcel.writeString(this.id);
		parcel.writeInt(this.notaOferta);
		
	}
	
	public static Creator<Oferta> CREATOR = new Creator<Oferta>() {

	    @Override
	    public Oferta  createFromParcel(Parcel source) {
	        return new Oferta (source);
	    }

	    @Override
	    public Oferta  [] newArray(int size) {
	        return new Oferta  [size];
	    }

	  };
	
}
