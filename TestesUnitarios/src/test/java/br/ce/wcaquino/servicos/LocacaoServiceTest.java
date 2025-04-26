package br.ce.wcaquino.servicos;



import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private LocacaoService service = new LocacaoService();
	
	private static Long contador = 0L;
	
	@Before
	public void verificandoAntes() {
//		System.out.println("Como essa instancia precisava iniciar em todos os teste, colocamos ela com essa notacao para nao precisar"
//				+ "e ele ainda sera rodado em todas as funcoes de teste");
		service = new LocacaoService();
		
		System.out.println(contador++);
	}
	
	@BeforeClass
	public static void setupGeral() {
	    System.out.println("Executa só uma vez, antes de tudo");
	}

	
	@Test
	public void t1_testeLocacao() throws Exception {
		//cenario
		System.out.println(contador++);
		Usuario usuario = new Usuario("Usuario 1");
		
		//List <Filme> filme = new Filme("Filme 1", 1, 5.0);
		
		List <Filme> filme = Arrays.asList(
				new Filme("Filme 1", 1, 5.0),
				new Filme("Filme 2", 2, 15.0),
				new Filme("Filme 3", 1, 25.0)
				);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filme);
			
		//verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
	}
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void t2_testLocacao_filmeSemEstoque() throws Exception{
		//cenario
		System.out.println(contador++);
		Usuario usuario = new Usuario("Usuario 1");
		// Filme filme = new Filme("Filme 2", 0, 4.0);
		List <Filme> filme = Arrays.asList(
				new Filme("Filme 1", 1, 5.0),
				new Filme("Filme 2", 2, 15.0),
				new Filme("Filme 3", 1, 25.0)
				);
		
		//acao
		service.alugarFilme(usuario, filme);
	}
	
	@Test
	public void t3_testLocacao_usuarioVazio() throws FilmeSemEstoqueException{
		//cenario
		System.out.println(contador++);
		//Filme filme = new Filme("Filme 2", 1, 4.0);
		List <Filme> filme = Arrays.asList(
				new Filme("Filme 1", 1, 5.0),
				new Filme("Filme 2", 2, 15.0),
				new Filme("Filme 3", 1, 25.0)
				);
		
		//acao
		try {
			service.alugarFilme(null, filme);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}
	

	@Test
	public void t4_testLocacao_FilmeVazio() throws FilmeSemEstoqueException, LocadoraException{
		//cenario
		System.out.println(contador++);
		Usuario usuario = new Usuario("Usuario 1");
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		//acao
		service.alugarFilme(usuario, null);
	}
}
