package model;

public class Questao {

	private int id;
	private int neuro_div;
	private String enunciado;
	private int dificuldade;
	private int habilidade;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNeuro_div() {
		return neuro_div;
	}
	public void setNeuro_div(int neuro_div) {
		this.neuro_div = neuro_div;
	}
	public String getEnunciado() {
		return enunciado;
	}
	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}
	public int getDificuldade() {
		return dificuldade;
	}
	public void setDificuldade(int dificuldade) {
		this.dificuldade = dificuldade;
	}
	public int getHabilidade() {
		return habilidade;
	}
	public void setHabilidade(int habilidade) {
		this.habilidade = habilidade;
	}
}
