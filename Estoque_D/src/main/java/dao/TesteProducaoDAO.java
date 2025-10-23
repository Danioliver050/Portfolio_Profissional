package dao;

import dao.ProdutoDAO;
import model.Produto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TesteProducaoDAO {

    public static void main(String[] args) {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // 1️⃣ Criar produto de teste
            Date validade = sdf.parse("2026-12-31");
            Produto p = new Produto(0, "1234567890", "Perfume Teste", "MarcaTeste", "L001",
                    validade, 99.90, 10, null, null);

            // Inserir produto
            if (produtoDAO.inserir(p)) {
                System.out.println("Produto inserido com sucesso: " + p);
            } else {
                System.out.println("Falha ao inserir produto.");
            }

            // 2️⃣ Atualizar produto
            p.setNome("Perfume Teste Atualizado");
            p.setPreco(119.90);
            if (produtoDAO.atualizar(p)) {
                System.out.println("Produto atualizado com sucesso: " + p);
            } else {
                System.out.println("Falha ao atualizar produto.");
            }

            // 3️⃣ Buscar produto por código
            Produto produtoBuscado = produtoDAO.buscarPorCodigo("1234567890");
            if (produtoBuscado != null) {
                System.out.println("Produto encontrado: " + produtoBuscado);
            } else {
                System.out.println("Produto não encontrado.");
            }

            // 4️⃣ Listar todos os produtos
            List<Produto> produtos = produtoDAO.listarTodos();
            System.out.println("Lista de todos os produtos:");
            for (Produto prod : produtos) {
                System.out.println(prod);
            }

            // 5️⃣ Remover produto
            if (produtoDAO.remover("1234567890")) {
                System.out.println("Produto removido com sucesso.");
            } else {
                System.out.println("Falha ao remover produto.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
