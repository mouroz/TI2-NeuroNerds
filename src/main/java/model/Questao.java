package model;

public class Questao {

	private int id;
	private int neuro_div;
	private String enunciado;
	private int dificuldade;
	private int habilidade;
	
	//GETTERS
	public int getId() {return id;}
	public int getNeuro_div() {return neuro_div;}
	public String getEnunciado() {return enunciado;}
	public int getDificuldade() {return dificuldade;}
	public int getHabilidade() {return habilidade;}
	
	//SETTERS
	public void setId(int id) {this.id = id;}
	public void setNeuro_div(int neuro_div) {this.neuro_div = neuro_div;}
	public void setEnunciado(String enunciado) {this.enunciado = enunciado;}
	public void setDificuldade(int dificuldade) {this.dificuldade = dificuldade;}
	public void setHabilidade(int habilidade) {this.habilidade = habilidade;}
}
