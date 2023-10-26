package model;


public class Usuario {
	private String senha;
    private int id_usuario;
    private String email;
    private String username;

    public Usuario(){
        this.senha      = "";
        this.id_usuario = -1;
        this.email      = "";
        this.username   = "";
    }

    public Usuario(String senha, String email, String username) {
        this.senha = senha;
        this.email = email;
        this.username = username;
    }

    public String getSenha() {return senha;}
    public int getId_usuario() {return id_usuario;}
    public String getEmail() {return email;}
    public String getUsername() {return username;}
    
    public void setEmail(String email) {this.email = email;}
    public void setUsername(String username) {this.username = username;}
    public void setSenha(String senha) {this.senha = senha;}
    @Override
    public String toString(){
        return "usuario [email=" + email + ", senha=" + senha + ", idUsuario=" + id_usuario + ", username=" + username + "]";
    }

	
}
