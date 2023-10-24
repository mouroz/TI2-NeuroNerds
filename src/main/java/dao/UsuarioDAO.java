package dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Usuario;

public class UsuarioDAO extends DAO {

    public UsuarioDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    public int fetchUsuarioId(Usuario usuario) {
        try {
            Statement st = conexao.createStatement();

            String sql = "SELECT id_usuario FROM usuario WHERE email = '" + usuario.getEmail() + "' LIMIT 1";

            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt("id_usuario");
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean insert(Usuario usuario) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql = "INSERT INTO \"BancoTI2\".\"usuario\" (\"senha\", \"email\", \"username\") "
                    + "VALUES ('" + usuario.getSenha() + "', '" + usuario.getEmail() 
                    + "', '" + usuario.getUsername() + "');";
            System.out.println(sql);

            st.executeUpdate(sql);
            st.close();
            status = true;

            System.out.println(usuario.toString());

        } catch (SQLException u) {
        	System.out.println("Não foi possível inserir: ");
        	System.out.println("Nome ou email já cadastrado");
        }
        return status;
    }

    
    public boolean updateEmail(Usuario usuario) {
    	 boolean status = false;
         try {
             Statement st = conexao.createStatement();
             String sql = "UPDATE usuario SET email = " + usuario.getEmail() + "'"
                     + " WHERE id_usuario = " + usuario.getId_usuario();
             System.out.println(sql);
             st.executeUpdate(sql);
             st.close();
             status = true;

         } catch (SQLException u) {
             throw new RuntimeException(u);
         }
         return status;
    }
    
    public boolean updateUsername(Usuario usuario) {
    	
    	boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql = "UPDATE usuario SET email = " + usuario.getEmail() + "'"
                    + " WHERE id_usuario = " + usuario.getId_usuario();
            System.out.println(sql);
            st.executeUpdate(sql);
            st.close();
            status = true;

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
    
    /*
    public boolean updateSenha(Usuario usuario) {
    	
   
    }*/
    
    public boolean update(Usuario usuario) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql = "UPDATE usuario SET senha = " + usuario.getSenha() + ", email = '"  
                    + usuario.getEmail() + "', username = '" + usuario.getUsername() + "'"
                    + " WHERE id_usuario = " + usuario.getId_usuario();
            System.out.println(sql);
            st.executeUpdate(sql);
            st.close();
            status = true;

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean delete(int id) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql = "DELETE FROM usuario WHERE id_usuario = " + id;
            System.out.println(sql);
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public List<Usuario> list(int id1, int id2) {
        List<Usuario> usuarios = new ArrayList<Usuario>();

        try {
            Statement st = conexao.createStatement();

            String sql = "SELECT * FROM usuario WHERE id_usuario BETWEEN " + id1 + " AND " + id2;
            System.out.println(sql);

            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setSenha(rs.getString("senha"));
                usuario.setEmail(rs.getString("email"));
                usuario.setUsername(rs.getString("username"));
                usuarios.add(usuario);
            }

            for (Usuario u : usuarios) {
                System.out.println(u.toString());
            }

            rs.close();
            st.close();

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return usuarios;
    }
}
