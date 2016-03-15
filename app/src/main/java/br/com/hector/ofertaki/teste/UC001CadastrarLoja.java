package br.com.hector.ofertaki.teste;

import junit.framework.TestCase;

import org.junit.Test;

import com.parse.ParseException;

import br.com.hector.ofertaki.Loja;
import br.com.hector.ofertaki.Lojista;
import br.com.hector.ofertaki.Mapa;

public class UC001CadastrarLoja extends TestCase {
	
	private Loja loja;
	private Lojista lojista;
	
	protected void setUp() throws Exception {
		loja = new Loja();
		lojista = new Lojista();
	}
	
	@Test
	public void CT001CadastrarLojaComSucesso(){
		
		try {
			
			lojista.setEmail("emailteste@teste.com.br");
			lojista.setSenha("12345", "12345");
			
			loja.setNomeLoja("TestShop");
			loja.setCategoriaLoja("Tecnologia");
			loja.setCnpj("90908250000117");
			loja.setEnderecoLoja("Rua Frei João, 55");
		//	loja.setGeoPointLoja(new Mapa().geoPonto(loja.getEnderecoLoja()));
			loja.setTelefone("11966354817");
			
			assertNotNull(loja.CadastraLoja(lojista.CadastrarUsuario()));
			
		} catch (RuntimeException e) {
			
			e.printStackTrace();
		} catch (ParseException e) {
			
			e.printStackTrace();
		}	
	}
	
	protected void tearDown() throws Exception {
		
	}
}
