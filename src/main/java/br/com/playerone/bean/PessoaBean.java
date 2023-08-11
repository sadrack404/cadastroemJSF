package br.com.playerone.bean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

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
		return null;
	}

	public String limpar() {
		pessoa = new Pessoa();
		carregarUsuarios();
		return null;
	}

	public String salvar() {
		System.err.println(pessoa.toString());
		pessoa = daogeneric.atualizar(pessoa);
		carregarUsuarios();
		mostrarMsg("Cadastrado com sucesso!");
		return "";
	}

	public String remover() {
		daogeneric.removerPorId(pessoa);
		pessoa = new Pessoa();
		carregarUsuarios();
		return "";
	}

	public void pesquisarCep(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream inputStream = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

			String cep = "";
			StringBuilder cepBuilder = new StringBuilder();

			while ((cep = reader.readLine()) != null) {
				cepBuilder.append(cep);
			}

			Pessoa gson = new Gson().fromJson(cepBuilder.toString(), Pessoa.class);

			pessoa.setCep(gson.getCep());
			pessoa.setLogradouro(gson.getLogradouro());
			pessoa.setComplemento(gson.getComplemento());
			pessoa.setBairro(gson.getBairro());
			pessoa.setLocalidade(gson.getLocalidade());
			pessoa.setUf(gson.getUf());
			pessoa.setIbge(gson.getIbge());
			pessoa.setGia(gson.getGia());
			pessoa.setDdd(gson.getDdd());
			pessoa.setSiafi(gson.getSiafi());

		} catch (Exception e) {
			System.out.println(e.getMessage());
			mostrarMsg("Erro ao pesquisar CEP!");
		}
	}

	@PostConstruct
	public void carregarUsuarios() {
		pessoas = daogeneric.getListEntity(pessoa);
	}

	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage(msg);

		context.addMessage(null, facesMessage);
	}

	public String logar() {

		carregarUsuarios();

		Pessoa usuario = daoPessoa.buscarUsuario(pessoa.getLogin(), pessoa.getSenha());

		if (usuario != null) { // encontrou usuario
			// Adiciona o usuário na sessão
			FacesContext context = FacesContext.getCurrentInstance();// <- busca informação do jsf
			ExternalContext externalContext = context.getExternalContext();

			HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
			HttpSession session = req.getSession();

			session.setAttribute("usuarioLogado", usuario);

			return "principal.jsf";
		}

		return "index.jsf";
	}
	
	public String deslogar() {
		// Adiciona o usuário na sessão
		FacesContext context = FacesContext.getCurrentInstance();// <- busca informação do jsf
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuarioLogado");

		//Controla a sessão do usuário!
		HttpServletRequest requestSession = (HttpServletRequest) context.getCurrentInstance().getExternalContext().getRequest();
		
		requestSession.getSession().invalidate();
		
		return "index.jsf";
	}

	public boolean permitirAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();// <- busca informação do jsf
		ExternalContext externalContext = context.getExternalContext();

		HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
		HttpSession session = req.getSession();

		Pessoa usuario = (Pessoa) session.getAttribute("usuarioLogado");

		return usuario.getPerfilUsuario().equals(acesso);
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

}
