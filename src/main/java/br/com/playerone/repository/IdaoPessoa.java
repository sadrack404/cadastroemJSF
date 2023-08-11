package br.com.playerone.repository;

import br.com.playerone.Pessoa;

public interface IdaoPessoa {
	
	public Pessoa buscarUsuario(String login, String senha);

}
