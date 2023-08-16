//package br.com.playerone;
//
//import java.util.List;
//
//import javax.faces.model.SelectItem;
//
//import org.junit.Test;
//
//import br.com.playerone.dao.DaoGeneric;
//import br.com.playerone.repository.IdaoPessoa;
//import br.com.playerone.repository.IdaoPessoaImpl;
//
//public class Teste {
//
//	@Test
//	public void testePessoa() {
//		DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
//		Pessoa p = new Pessoa();
//		p.setAtivo(true);
//		p.setNome("Gabriel");
//		p.setLogin("sad");
//		p.setSenha("123");
//		p.setPerfilUsuario("ADMINISTRADOR");
//
//		//daoGeneric.salvar(p);
//	}
//
//	@Test
//	public void testeEstados() {
//		IdaoPessoa daPessoa = new IdaoPessoaImpl();
//
//		List<SelectItem> listaEstados = daPessoa.listaEstados();
//
//		System.out.println(listaEstados);
//	}
//
//}