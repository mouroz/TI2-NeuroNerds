package model;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Pergunta {

	private int id_pergunta;
	private String titulo;
	private String conteudo;
	private LocalDate data_postagem;
	private int id_usuario;
	private String nome_usuario;
	
	public String getNome_usuario() {
		return nome_usuario;
	}

	public void setNome_usuario(String nome_usuario) {
		this.nome_usuario = nome_usuario;
	}

	public Pergunta() {
		
	}
	
	public Pergunta(int id_pergunta, String titulo, String conteudo, LocalDate data_postagem, int id_usuario){
		this.id_pergunta = id_pergunta;
		this.titulo = titulo;
		this.conteudo = conteudo;
		this.data_postagem = data_postagem;
		this.id_usuario = id_usuario;
	}
	
	
	public int getId_pergunta() {return id_pergunta;}
	public String getTitulo() {return titulo;}
	
	public void setId_pergunta(int id_pergunta) {this.id_pergunta = id_pergunta;}
	public void setTitulo(String titulo) {this.titulo = titulo;}
	public LocalDate getData_postagem() {return data_postagem;}
	public void setData_postagem(LocalDate data_postagem) {this.data_postagem = data_postagem;
	}
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	public int getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	
}
