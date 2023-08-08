package br.com.playerone.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.playerone.Pessoa;
import br.com.playerone.dao.DaoGeneric;
import br.com.playerone.repository.IdaoPessoa;
import br.com.playerone.repository.IdaoPessoaImpl;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {
	
	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daogeneric = new DaoGeneric<Pessoa>();
	private IdaoPessoa daoPessoa = new IdaoPessoaImpl();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();

	public String novo() {
		pessoa = new Pessoa();
		carregarUsuarios();
		return "";
	}

	public String atualizar() {
		pessoa = daogeneric.atualizar(pessoa);
		return "";
	}

	public String remover() {
		daogeneric.removerPorId(pessoa);
		pessoa = new Pessoa();
		carregarUsuarios();
		return "";
	}
	
	@PostConstruct
	public void carregarUsuarios() {
		pessoas = daogeneric.getListEntity(pessoa);
	}
	
	public String logar2() {
		Pessoa p = daoPessoa.buscarUsuario(pessoa.getLogin(), pessoa.getSenha());
		System.err.println(p);
		return "index.jsf";
	}
	
	public String logar() {
		
		carregarUsuarios();
		
		Pessoa usuario = daoPessoa.buscarUsuario(pessoa.getLogin(), pessoa.getSenha());
		if (usuario != null ) { // encontrou usuario
			//Adiciona o usuário na sessão
			FacesContext context = FacesContext.getCurrentInstance();// <- busca informação do jsf
			ExternalContext externalContext = context.getExternalContext();
			
			HttpServletRequest req = (HttpServletRequest) externalContext.getRequest(); 
			HttpSession session =  req.getSession();
			
			session.setAttribute("usuarioLogado", usuario);
			
			return "paginaum.jsf";
		}
		
		return "index.jsf";
	}
	

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DaoGeneric<Pessoa> getDaogeneric() {
		return daogeneric;
	}

	public void setDaogeneric(DaoGeneric<Pessoa> daogeneric) {
		this.daogeneric = daogeneric;
	}

	public IdaoPessoa getDaoPessoa() {
		return daoPessoa;
	}

	public void setDaoPessoa(IdaoPessoa daoPessoa) {
		this.daoPessoa = daoPessoa;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}
	
}
