package dao;

import model.Produto;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    // Inserir produto
    public boolean inserir(Produto produto) {
        String sql = "INSERT INTO produtos (codigo_barras, nome, marca, lote, validade, preco, quantidade) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getCodigoBarras());
            stmt.setString(2, produto.getNome());
            stmt.setString(3, produto.getMarca());
            stmt.setString(4, produto.getLote());
            stmt.setDate(5, new java.sql.Date(produto.getValidade().getTime()));
            stmt.setDouble(6, produto.getPreco());
            stmt.setInt(7, produto.getQuantidade());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao inserir produto: " + e.getMessage());
        }
        return false;
    }

    // Atualizar produto
    public boolean atualizar(Produto produto) {
        String sql = "UPDATE produtos SET nome=?, marca=?, lote=?, validade=?, preco=?, quantidade=? WHERE codigo_barras=?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getMarca());
            stmt.setString(3, produto.getLote());
            stmt.setDate(4, new java.sql.Date(produto.getValidade().getTime()));
            stmt.setDouble(5, produto.getPreco());
            stmt.setInt(6, produto.getQuantidade());
            stmt.setString(7, produto.getCodigoBarras());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar o produto: " + e.getMessage());
        }
        return false;
    }

    // Remover produto
    public boolean remover(String codigoBarras) {
        String sql = "DELETE FROM produtos WHERE codigo_barras=?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoBarras);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao remover produto: " + e.getMessage());
        }
        return false;
    }

    // Buscar produto pelo código
    public Produto buscarPorCodigo(String codigoBarras) {
        String sql = "SELECT * FROM produtos WHERE codigo_barras=?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoBarras);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetParaProduto(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar produto: " + e.getMessage());
        }
        return null;
    }

    // Listar todos os produtos
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                produtos.add(mapResultSetParaProduto(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }

        return produtos;
    }

    // Produtos próximos do vencimento (ex: 10 dias)
    public List<Produto> produtosProxVencimento() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE validade BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 10 DAY)";

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                produtos.add(mapResultSetParaProduto(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar produtos próximos do vencimento: " + e.getMessage());
        }

        return produtos;
    }

    // Mapear ResultSet para objeto Produto
    private Produto mapResultSetParaProduto(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getInt("id"),
                rs.getString("codigo_barras"),
                rs.getString("nome"),
                rs.getString("marca"),
                rs.getString("lote"),
                rs.getDate("validade"),
                rs.getDouble("preco"),
                rs.getInt("quantidade"),
                rs.getTimestamp("data_cadastro"),
                rs.getTimestamp("data_ultima_movimentacao")
        );
    }
}
