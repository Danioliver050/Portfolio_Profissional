
-- Triggers.
USE estoque_Dante;

DELIMITER $$

-- atualização automática da ultima movimentação de produtos
CREATE TRIGGER trg_atualiza_data_movimentacao
AFTER INSERT ON movimentacoes_Entrada_Saida
FOR EACH ROW
BEGIN
  UPDATE produtos
  SET data_ultima_movimentacao = NEW.data_movimentacao
  WHERE id = NEW.produto_id;
END$$

-- não permite que saia, do estoque, uma quantiade maior que a disponível
CREATE TRIGGER trg_verifica_estoque
BEFORE INSERT ON movimentacoes_Entrada_Saida
FOR EACH ROW
BEGIN
  DECLARE qtd_atual INT;

  IF NEW.tipo = 'SAIDA' THEN
    SELECT quantidade INTO qtd_atual FROM produtos WHERE id = NEW.produto_id;
    IF qtd_atual < NEW.quantidade THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Estoque insuficiente para realizar a saída.';
    END IF;
  END IF;
END$$

-- excluir um produto do estoque
CREATE TABLE IF NOT EXISTS log_alteracoes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  produto_id INT,
  acao VARCHAR(50),
  usuario VARCHAR(100),
  data_log DATETIME DEFAULT CURRENT_TIMESTAMP,
  detalhe TEXT
);

CREATE TRIGGER trg_log_delete_produto
AFTER DELETE ON produtos
FOR EACH ROW
BEGIN
  INSERT INTO log_alteracoes (produto_id, acao, usuario, detalhe)
  VALUES (OLD.id, 'EXCLUSAO', CURRENT_USER(), CONCAT('Produto "', OLD.nome, '" foi removido do estoque.'));
END$$

DELIMITER ;
