package br.com.playerone;

import org.junit.Test;

import br.com.playerone.dao.DaoGeneric;

public class Teste {
	
	@Test
	public void testePessoa() {
		DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
		Pessoa p = new Pessoa();
		p.setAtivo(true);
		p.setNome("Gabriel");
		p.setLogin("sad");
		p.setSenha("123");
		p.setPerfilUsuario("ADMINISTRADOR");
		
		daoGeneric.salvar(p);
	}
}
