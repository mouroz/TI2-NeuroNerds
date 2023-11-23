package dao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Usuario;

public class UsuarioDAO extends DAO{
	public static final String cId = "id";
	public static final String cNome = "nome";
	public static final String cUsername = "username";
	public static final String cSenha = "senha";
	public static final String cEmail = "email";
    
    
    /*
     * Methods for returning a single Usuario
     * Used mostly with unique keys (username, email, id)
     */
	
    public static Usuario getUsuarioByUniqueCol(String sql, String value) {
    	Usuario usuario = null;
    	
    	try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
	         pstmt.setString(1, value);
	      
	         try (ResultSet resultSet = pstmt.executeQuery()) {
	             if (resultSet.next()) {
	            	 int id = resultSet.getInt(cId);
	                 String nome = resultSet.getString(cNome);
	                 String username = resultSet.getString(cUsername);
	                 String email = resultSet.getString(cEmail);
	                 String senha = resultSet.getString(cSenha);
	                 
	                 usuario = new Usuario(id, nome,username,email,senha);
	             } else {
	            	 log("Usuário não está cadastrado");
	             }
	         }
	         
	     } catch (SQLException e) { e.printStackTrace(); }
    	
    	return usuario;
    }
    
    public static Usuario getUsuarioById(String id) {
    	String  sql = "SELECT * FROM bancoti2.usuario WHERE id = ?";
    	return getUsuarioByUniqueCol(sql, id);
    }
    
    public static Usuario getUsuarioByEmail(String email) {
    	String  sql = "SELECT * FROM bancoti2.usuario WHERE email = ?";
    	return getUsuarioByUniqueCol(sql, email);
    }
    
    public static Usuario getUsuarioByUsername(String username) {
    	String  sql = "SELECT * FROM bancoti2.usuario WHERE username = ?";
    	return getUsuarioByUniqueCol(sql, username);
    }
    
    
    /*
     * Methods for updating the user data
     * Returns false if operation failed
     */
    
    public static boolean updateUser(Usuario usuario, String id) {
    	String sql = "UPDATE bancoti2.usuario" 
    			+ " SET nome = ?, username = ?,  email = ?,  senha =  ?"
    			+ " WHERE id = ?";
    	
    	try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
    		pstmt.setString(1, usuario.getNome());
    		pstmt.setString(2, usuario.getUsername());
    		pstmt.setString(3, usuario.getEmail());
    		pstmt.setString(4, usuario.getSenha());
    		pstmt.executeUpdate();
    		
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return false;
    	}
    	
    	return true;
    }
    
    public static boolean updateCol(String sql, String value) {
    	try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
    		pstmt.setString(1, value);
    		pstmt.executeUpdate();
    		
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return false;
    	}
    	
    	return true;
    }
    
    public static boolean updateNome(String nome) {
    	return updateCol("UPDATE bancoti2.usuario SET nome = ? WHERE id = ?", nome);
    }
    
    public static boolean updateUsername(String username) {
    	return updateCol("UPDATE bancoti2.usuario SET username = ? WHERE id = ?", username);
    }
    
    public static boolean updateEmail(String email) {
    	return updateCol("UPDATE bancoti2.usuario SET email = ? WHERE id = ?", email);
    }
    
    public static boolean updateSenha(String senha) {
    	return updateCol("UPDATE bancoti2.usuario SET senha = ? WHERE id = ?", senha);
    }
    
    
    
    /* Usuario post. Used to insert new Usuario value to table
     * Returns false if failed. getSQLState may be used to notify of duplicate key violation
     */
    public static boolean postUsuario(Usuario usuario) {
    	String sql = "INSERT INTO bancoti2.usuario (nome, username, email, senha) VALUES (?, ?, ?, ?)";
    	
    	try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
    	     pstmt.setString(1, usuario.getNome());
             pstmt.setString(2, usuario.getUsername());
             pstmt.setString(3, usuario.getEmail());
             pstmt.setString(4, usuario.getSenha());
             pstmt.executeUpdate();

         } catch (SQLException u) {
             u.printStackTrace();
             return false;
         }
    	
         return true;
    }
    
    
    
    /* Delete user from database
     * Return false if failed
     */
    static public boolean delete(int id) {
        String sql = "DELETE FROM bancoti2.usuario WHERE id = ?";

        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    
    
    /* Authenticates via email and password
     * Returns true if authentication is a success
     */
    public static boolean autenticaUsuario(String email, String senha) {    
        boolean status = false;
        // Use o nome completo da tabela, incluindo o esquema
        String sql = "SELECT username FROM bancoti2.usuario WHERE email = ? AND senha = ?";
        logPStatement(sql);
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, senha);
            
            ResultSet resultSet = pstmt.executeQuery();
            // Se resultSet tem pelo menos uma linha, o usuário foi autenticado
            if (resultSet.next()) {
                status = true;
                log("Usuario esta cadastrado");    
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }
 
    
    
    /* Get list of Usuario amidst two id values
     * List comes out empty if operation ailed
     */
    public static List<Usuario> getUsuarioList(int idMin, int idMax) {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        String sql = String.format("SELECT * FROM bancoti2.usuario WHERE id BETWEEN ? AND ?");
        logPStatement(sql);
        
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, idMin);
            pstmt.setInt(2, idMax);

            ResultSet resultSet = pstmt.executeQuery();
            
            while(resultSet.next()) {
            	int id = resultSet.getInt(cId);
                String nome = resultSet.getString(cNome);
                String username = resultSet.getString(cUsername);
                String email = resultSet.getString(cEmail);
                String senha = resultSet.getString(cSenha);

                usuarios.add(new Usuario(id, nome, username, email, senha));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }


    static public void logPStatement(String s){logPS_DAO("(UsuarioDAO) -> ", s);  }
    static public void log(String s) {System.out.println("(UsuarioDAO) -> " + s); }
    
}
