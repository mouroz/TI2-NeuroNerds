package model;

import java.time.LocalDate;

public class Resposta {

	private int id_resposta;
	private int id_pergunta;
	private String conteudo;
	private LocalDate data_postagem;
	private int id_usuario;
	public int getId_resposta() {
		return id_resposta;
	}
	public void setId_resposta(int id_resposta) {
		this.id_resposta = id_resposta;
	}
	public int getId_pergunta() {
		return id_pergunta;
	}
	public void setId_pergunta(int id_pergunta) {
		this.id_pergunta = id_pergunta;
	}
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	public LocalDate getData_postagem() {
		return data_postagem;
	}
	public void setData_postagem(LocalDate data_postagem) {
		this.data_postagem = data_postagem;
	}
	public int getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}
	
	
}
