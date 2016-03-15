package br.com.hector.ofertaki;
import android.content.Context;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;



public class Lojista{

	private String email;
	private String senha;
	ParseUser usuario = new ParseUser();
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email){
		if(!Validacoes.isEmail(email)){
			throw new IllegalArgumentException("Email Inválido");
		}else{
			this.email = email;
		}
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha, String confirmacaoSenha)throws RuntimeException {
		if(senha.equals("") || !senha.equals(confirmacaoSenha)){
			throw new IllegalArgumentException("Senhas Inválidas");
		}else if (senha.length() < 6){
			throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
		}else{
			this.senha = senha;
		}
	}
	
	
	public ParseUser getUsuarioLogado(){
		ParseUser usuarioLogado = ParseUser.getCurrentUser();
		return usuarioLogado;
	}
	
	public ParseUser CadastrarUsuario () throws ParseException{
		//usuario.setObjectId(email);
		usuario.setUsername(email);
		usuario.setPassword(senha);
		usuario.signUp();
		
		return usuario;
	}
	
	
	public ParseObject minhaLoja (Context context){
		
		ParseUser usuarioLogado = ParseUser.getCurrentUser();
		ParseObject minhaLoja = new ParseObject("Loja");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Loja");
		query.whereEqualTo("parent", usuarioLogado);
		try {
			minhaLoja = (ParseObject) query.find().get(0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IndexOutOfBoundsException e){
			Toast.makeText(context, "Você precisa estar Logado para cadastrar", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
		
		
		return minhaLoja;
		
	}
	
}
