package dao;

import model.Usuario;
import util.Conexao;
import java.sql.*;

public class UsuarioDAO {

    public Usuario autenticar(String usuario, String senha) {
        Usuario user = null;
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND senha = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("usuario"),
                            rs.getString("senha"),
                            rs.getString("cargo")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
