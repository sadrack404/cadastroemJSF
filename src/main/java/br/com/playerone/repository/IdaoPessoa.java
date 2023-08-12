package br.com.playerone.repository;

import java.util.List;

import javax.faces.model.SelectItem;

import br.com.playerone.Pessoa;

public interface IdaoPessoa {

	
	public Pessoa buscarUsuario(String login, String senha);

	public List<SelectItem> listaEstados ();
	
}
