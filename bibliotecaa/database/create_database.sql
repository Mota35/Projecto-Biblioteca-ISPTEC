-- ============================================================================
-- SCRIPT DE CRIAÇÃO DA BASE DE DADOS - SISTEMA DE BIBLIOTECA ISPTEC
-- ============================================================================
-- Versão: 1.0
-- Data: Fevereiro 2026
-- Descrição: Script para criação da estrutura completa da base de dados MySQL
--            para o Sistema de Gestão de Biblioteca do ISPTEC
-- ============================================================================

-- Cria a base de dados se não existir
CREATE DATABASE IF NOT EXISTS biblioteca_isptec
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE biblioteca_isptec;

-- ============================================================================
-- TABELAS PRINCIPAIS
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Tabela: pessoa
-- Descrição: Tabela base para armazenar dados comuns de todas as pessoas
--            (Padrão de herança - Strategy Pattern)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pessoa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil ENUM('ADMIN', 'USUARIO', 'BIBLIOTECARIO') NOT NULL,
    tipo_pessoa ENUM('MEMBRO', 'ESTUDANTE', 'BIBLIOTECARIO') NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_perfil (perfil),
    INDEX idx_tipo_pessoa (tipo_pessoa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Tabela: membro
-- Descrição: Armazena dados específicos de membros da biblioteca
-- Regras: Máximo 3 empréstimos ativos, multa máxima permitida 1000 KZ
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS membro (
    id INT PRIMARY KEY,
    matricula VARCHAR(50) NOT NULL UNIQUE,
    bloqueado BOOLEAN DEFAULT FALSE,
    multa_pendente DECIMAL(10,2) DEFAULT 0.00,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_matricula (matricula),
    INDEX idx_bloqueado (bloqueado),
    FOREIGN KEY (id) REFERENCES pessoa(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Tabela: estudante
-- Descrição: Armazena dados específicos de estudantes (herda de membro)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS estudante (
    id INT PRIMARY KEY,
    curso VARCHAR(255) NOT NULL,
    ano_letivo INT NOT NULL DEFAULT 1,
    INDEX idx_curso (curso),
    INDEX idx_ano_letivo (ano_letivo),
    FOREIGN KEY (id) REFERENCES membro(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Tabela: bibliotecario
-- Descrição: Armazena dados de funcionários/administradores da biblioteca
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bibliotecario (
    id INT PRIMARY KEY,
    funcionario_id VARCHAR(50) NOT NULL UNIQUE,
    departamento VARCHAR(255) DEFAULT 'Biblioteca Central',
    cargo VARCHAR(100) DEFAULT 'Bibliotecário',
    data_contratacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_funcionario_id (funcionario_id),
    FOREIGN KEY (id) REFERENCES pessoa(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Tabela: autor
-- Descrição: Armazena informações sobre autores de livros
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS autor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    nacionalidade VARCHAR(100),
    biografia TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_nome (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Tabela: categoria
-- Descrição: Armazena categorias de classificação de livros
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_nome (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Tabela: livro
-- Descrição: Armazena informações sobre livros do acervo
-- Regras: Controle de quantidade total vs disponível
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS livro (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    editora VARCHAR(255),
    ano_publicacao INT,
    quantidade_total INT NOT NULL DEFAULT 1,
    quantidade_disponivel INT NOT NULL DEFAULT 1,
    estado ENUM('DISPONIVEL', 'EMPRESTADO', 'RESERVADO', 'INDISPONIVEL') DEFAULT 'DISPONIVEL',
    descricao TEXT,
    localizacao VARCHAR(100) COMMENT 'Prateleira/Corredor',
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_titulo (titulo),
    INDEX idx_isbn (isbn),
    INDEX idx_estado (estado),
    INDEX idx_editora (editora),
    CONSTRAINT chk_quantidade CHECK (quantidade_disponivel >= 0 AND quantidade_disponivel <= quantidade_total)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Tabela: emprestimo
-- Descrição: Registra empréstimos de livros para membros
-- Regras: Prazo padrão 14 dias, máximo 2 renovações, multa diária 50 KZ
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS emprestimo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    livro_id INT NOT NULL,
    membro_id INT NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_devolucao_prevista DATE NOT NULL,
    data_devolucao_real DATE,
    numero_renovacoes INT DEFAULT 0,
    estado ENUM('ATIVO', 'DEVOLVIDO', 'ATRASADO') DEFAULT 'ATIVO',
    valor_multa DECIMAL(10,2) DEFAULT 0.00,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_livro (livro_id),
    INDEX idx_membro (membro_id),
    INDEX idx_estado (estado),
    INDEX idx_data_emprestimo (data_emprestimo),
    INDEX idx_data_devolucao_prevista (data_devolucao_prevista),
    FOREIGN KEY (livro_id) REFERENCES livro(id) ON DELETE RESTRICT,
    FOREIGN KEY (membro_id) REFERENCES membro(id) ON DELETE RESTRICT,
    CONSTRAINT chk_renovacoes CHECK (numero_renovacoes >= 0 AND numero_renovacoes <= 2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Tabela: reserva
-- Descrição: Registra reservas de livros por membros
-- Regras: Apenas livros indisponíveis, atendimento por ordem de data, expira em 3 dias
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS reserva (
    id INT AUTO_INCREMENT PRIMARY KEY,
    livro_id INT NOT NULL,
    membro_id INT NOT NULL,
    data_reserva DATE NOT NULL,
    data_expiracao DATE NOT NULL,
    ativa BOOLEAN DEFAULT TRUE,
    confirmada BOOLEAN DEFAULT FALSE,
    notificado BOOLEAN DEFAULT FALSE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_livro (livro_id),
    INDEX idx_membro (membro_id),
    INDEX idx_ativa (ativa),
    INDEX idx_data_reserva (data_reserva),
    FOREIGN KEY (livro_id) REFERENCES livro(id) ON DELETE CASCADE,
    FOREIGN KEY (membro_id) REFERENCES membro(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- TABELAS DE RELACIONAMENTO (MUITOS-PARA-MUITOS)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Tabela: livro_autor
-- Descrição: Relacionamento entre livros e autores (N:N)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS livro_autor (
    livro_id INT NOT NULL,
    autor_id INT NOT NULL,
    PRIMARY KEY (livro_id, autor_id),
    FOREIGN KEY (livro_id) REFERENCES livro(id) ON DELETE CASCADE,
    FOREIGN KEY (autor_id) REFERENCES autor(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Tabela: livro_categoria
-- Descrição: Relacionamento entre livros e categorias (N:N)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS livro_categoria (
    livro_id INT NOT NULL,
    categoria_id INT NOT NULL,
    PRIMARY KEY (livro_id, categoria_id),
    FOREIGN KEY (livro_id) REFERENCES livro(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- TABELAS AUXILIARES
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Tabela: historico_acao
-- Descrição: Log de ações realizadas no sistema (auditoria)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS historico_acao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    acao VARCHAR(100) NOT NULL,
    tabela_afetada VARCHAR(50),
    registro_id INT,
    descricao TEXT,
    data_acao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_usuario (usuario_id),
    INDEX idx_acao (acao),
    INDEX idx_data (data_acao),
    FOREIGN KEY (usuario_id) REFERENCES pessoa(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Tabela: notificacao
-- Descrição: Armazena notificações para usuários
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS notificacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    membro_id INT NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    mensagem TEXT NOT NULL,
    tipo ENUM('INFO', 'ALERTA', 'URGENTE') DEFAULT 'INFO',
    lida BOOLEAN DEFAULT FALSE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_membro (membro_id),
    INDEX idx_lida (lida),
    INDEX idx_tipo (tipo),
    FOREIGN KEY (membro_id) REFERENCES membro(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- VIEWS (VISÕES)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- View: view_emprestimos_ativos
-- Descrição: Lista todos os empréstimos ativos com informações completas
-- ----------------------------------------------------------------------------
CREATE OR REPLACE VIEW view_emprestimos_ativos AS
SELECT
    e.id AS emprestimo_id,
    e.data_emprestimo,
    e.data_devolucao_prevista,
    e.numero_renovacoes,
    e.estado,
    l.id AS livro_id,
    l.titulo AS livro_titulo,
    l.isbn,
    m.id AS membro_id,
    p.nome AS membro_nome,
    m.matricula AS membro_matricula,
    DATEDIFF(CURRENT_DATE, e.data_devolucao_prevista) AS dias_atraso,
    CASE
        WHEN e.estado = 'ATIVO' AND CURRENT_DATE > e.data_devolucao_prevista
        THEN DATEDIFF(CURRENT_DATE, e.data_devolucao_prevista) * 50.00
        ELSE 0.00
    END AS multa_calculada
FROM emprestimo e
INNER JOIN livro l ON e.livro_id = l.id
INNER JOIN membro m ON e.membro_id = m.id
INNER JOIN pessoa p ON m.id = p.id
WHERE e.estado IN ('ATIVO', 'ATRASADO');

-- ----------------------------------------------------------------------------
-- View: view_reservas_ativas
-- Descrição: Lista todas as reservas ativas ordenadas por data
-- ----------------------------------------------------------------------------
CREATE OR REPLACE VIEW view_reservas_ativas AS
SELECT
    r.id AS reserva_id,
    r.data_reserva,
    r.data_expiracao,
    r.confirmada,
    r.notificado,
    l.id AS livro_id,
    l.titulo AS livro_titulo,
    l.isbn,
    m.id AS membro_id,
    p.nome AS membro_nome,
    m.matricula AS membro_matricula,
    DATEDIFF(r.data_expiracao, CURRENT_DATE) AS dias_restantes
FROM reserva r
INNER JOIN livro l ON r.livro_id = l.id
INNER JOIN membro m ON r.membro_id = m.id
INNER JOIN pessoa p ON m.id = p.id
WHERE r.ativa = TRUE
ORDER BY r.data_reserva ASC;

-- ----------------------------------------------------------------------------
-- View: view_livros_disponiveis
-- Descrição: Lista livros disponíveis com informações de autores e categorias
-- ----------------------------------------------------------------------------
CREATE OR REPLACE VIEW view_livros_disponiveis AS
SELECT
    l.id,
    l.titulo,
    l.isbn,
    l.editora,
    l.ano_publicacao,
    l.quantidade_total,
    l.quantidade_disponivel,
    l.estado,
    l.localizacao,
    GROUP_CONCAT(DISTINCT a.nome ORDER BY a.nome SEPARATOR ', ') AS autores,
    GROUP_CONCAT(DISTINCT c.nome ORDER BY c.nome SEPARATOR ', ') AS categorias
FROM livro l
LEFT JOIN livro_autor la ON l.id = la.livro_id
LEFT JOIN autor a ON la.autor_id = a.id
LEFT JOIN livro_categoria lc ON l.id = lc.livro_id
LEFT JOIN categoria c ON lc.categoria_id = c.id
WHERE l.quantidade_disponivel > 0
GROUP BY l.id;

-- ----------------------------------------------------------------------------
-- View: view_membros_info
-- Descrição: Informações completas de membros com estatísticas de empréstimos
-- ----------------------------------------------------------------------------
CREATE OR REPLACE VIEW view_membros_info AS
SELECT
    p.id,
    p.nome,
    p.email,
    p.perfil,
    p.tipo_pessoa,
    m.matricula,
    m.bloqueado,
    m.multa_pendente,
    e.curso,
    e.ano_letivo,
    COUNT(DISTINCT emp.id) AS total_emprestimos,
    COUNT(DISTINCT CASE WHEN emp.estado = 'ATIVO' THEN emp.id END) AS emprestimos_ativos,
    COUNT(DISTINCT res.id) AS total_reservas_ativas
FROM pessoa p
INNER JOIN membro m ON p.id = m.id
LEFT JOIN estudante e ON m.id = e.id
LEFT JOIN emprestimo emp ON m.id = emp.membro_id
LEFT JOIN reserva res ON m.id = res.membro_id AND res.ativa = TRUE
GROUP BY p.id, p.nome, p.email, p.perfil, p.tipo_pessoa, m.matricula,
         m.bloqueado, m.multa_pendente, e.curso, e.ano_letivo;

-- ============================================================================
-- STORED PROCEDURES (PROCEDIMENTOS ARMAZENADOS)
-- ============================================================================

DELIMITER //

-- ----------------------------------------------------------------------------
-- Procedure: sp_realizar_emprestimo
-- Descrição: Realiza um empréstimo de livro para um membro
-- Parâmetros: livro_id, membro_id
-- Retorno: Status da operação
-- ----------------------------------------------------------------------------
CREATE PROCEDURE sp_realizar_emprestimo(
    IN p_livro_id INT,
    IN p_membro_id INT,
    OUT p_status VARCHAR(100),
    OUT p_emprestimo_id INT
)
BEGIN
    DECLARE v_qtd_disponivel INT;
    DECLARE v_membro_bloqueado BOOLEAN;
    DECLARE v_emprestimos_ativos INT;
    DECLARE v_multa DECIMAL(10,2);

    -- Verifica disponibilidade do livro
    SELECT quantidade_disponivel INTO v_qtd_disponivel
    FROM livro WHERE id = p_livro_id;

    IF v_qtd_disponivel IS NULL THEN
        SET p_status = 'ERRO: Livro não encontrado';
        SET p_emprestimo_id = NULL;

    ELSEIF v_qtd_disponivel <= 0 THEN
        SET p_status = 'ERRO: Livro não disponível';
        SET p_emprestimo_id = NULL;

    ELSE
        -- Verifica situação do membro
        SELECT bloqueado, multa_pendente INTO v_membro_bloqueado, v_multa
        FROM membro WHERE id = p_membro_id;

        IF v_membro_bloqueado THEN
            SET p_status = 'ERRO: Membro bloqueado';
            SET p_emprestimo_id = NULL;

        ELSEIF v_multa > 1000.00 THEN
            SET p_status = 'ERRO: Multa pendente excede o limite';
            SET p_emprestimo_id = NULL;

        ELSE
            -- Conta empréstimos ativos do membro
            SELECT COUNT(*) INTO v_emprestimos_ativos
            FROM emprestimo
            WHERE membro_id = p_membro_id AND estado = 'ATIVO';

            IF v_emprestimos_ativos >= 3 THEN
                SET p_status = 'ERRO: Membro já possui 3 empréstimos ativos';
                SET p_emprestimo_id = NULL;

            ELSE
                -- Realiza o empréstimo
                START TRANSACTION;

                INSERT INTO emprestimo (livro_id, membro_id, data_emprestimo, data_devolucao_prevista, estado)
                VALUES (p_livro_id, p_membro_id, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 14 DAY), 'ATIVO');

                SET p_emprestimo_id = LAST_INSERT_ID();

                -- Atualiza quantidade disponível do livro
                UPDATE livro
                SET quantidade_disponivel = quantidade_disponivel - 1
                WHERE id = p_livro_id;

                -- Atualiza estado do livro se necessário
                UPDATE livro
                SET estado = CASE
                    WHEN quantidade_disponivel = 0 THEN 'EMPRESTADO'
                    ELSE 'DISPONIVEL'
                END
                WHERE id = p_livro_id;

                COMMIT;

                SET p_status = 'SUCESSO: Empréstimo realizado';
            END IF;
        END IF;
    END IF;
END //

-- ----------------------------------------------------------------------------
-- Procedure: sp_devolver_livro
-- Descrição: Registra a devolução de um livro emprestado
-- Parâmetros: emprestimo_id
-- ----------------------------------------------------------------------------
CREATE PROCEDURE sp_devolver_livro(
    IN p_emprestimo_id INT,
    OUT p_status VARCHAR(100),
    OUT p_multa DECIMAL(10,2)
)
BEGIN
    DECLARE v_livro_id INT;
    DECLARE v_membro_id INT;
    DECLARE v_data_prevista DATE;
    DECLARE v_dias_atraso INT;
    DECLARE v_estado VARCHAR(20);

    -- Busca informações do empréstimo
    SELECT livro_id, membro_id, data_devolucao_prevista, estado
    INTO v_livro_id, v_membro_id, v_data_prevista, v_estado
    FROM emprestimo WHERE id = p_emprestimo_id;

    IF v_livro_id IS NULL THEN
        SET p_status = 'ERRO: Empréstimo não encontrado';
        SET p_multa = 0.00;

    ELSEIF v_estado = 'DEVOLVIDO' THEN
        SET p_status = 'ERRO: Livro já foi devolvido';
        SET p_multa = 0.00;

    ELSE
        -- Calcula multa se houver atraso
        SET v_dias_atraso = GREATEST(0, DATEDIFF(CURRENT_DATE, v_data_prevista));
        SET p_multa = v_dias_atraso * 50.00;

        START TRANSACTION;

        -- Atualiza o empréstimo
        UPDATE emprestimo
        SET data_devolucao_real = CURRENT_DATE,
            estado = 'DEVOLVIDO',
            valor_multa = p_multa
        WHERE id = p_emprestimo_id;

        -- Devolve o livro (incrementa quantidade disponível)
        UPDATE livro
        SET quantidade_disponivel = quantidade_disponivel + 1,
            estado = 'DISPONIVEL'
        WHERE id = v_livro_id;

        -- Adiciona multa ao membro se houver atraso
        IF p_multa > 0 THEN
            UPDATE membro
            SET multa_pendente = multa_pendente + p_multa
            WHERE id = v_membro_id;
        END IF;

        COMMIT;

        IF p_multa > 0 THEN
            SET p_status = CONCAT('SUCESSO: Livro devolvido com ', v_dias_atraso, ' dias de atraso');
        ELSE
            SET p_status = 'SUCESSO: Livro devolvido no prazo';
        END IF;
    END IF;
END //

-- ----------------------------------------------------------------------------
-- Procedure: sp_renovar_emprestimo
-- Descrição: Renova um empréstimo (estende prazo de devolução)
-- Parâmetros: emprestimo_id
-- ----------------------------------------------------------------------------
CREATE PROCEDURE sp_renovar_emprestimo(
    IN p_emprestimo_id INT,
    OUT p_status VARCHAR(100)
)
BEGIN
    DECLARE v_livro_id INT;
    DECLARE v_numero_renovacoes INT;
    DECLARE v_estado VARCHAR(20);
    DECLARE v_tem_reserva BOOLEAN;

    -- Busca informações do empréstimo
    SELECT livro_id, numero_renovacoes, estado
    INTO v_livro_id, v_numero_renovacoes, v_estado
    FROM emprestimo WHERE id = p_emprestimo_id;

    IF v_livro_id IS NULL THEN
        SET p_status = 'ERRO: Empréstimo não encontrado';

    ELSEIF v_estado != 'ATIVO' THEN
        SET p_status = 'ERRO: Apenas empréstimos ativos podem ser renovados';

    ELSEIF v_numero_renovacoes >= 2 THEN
        SET p_status = 'ERRO: Limite de renovações atingido (máximo 2)';

    ELSE
        -- Verifica se há reserva ativa para o livro
        SELECT EXISTS(
            SELECT 1 FROM reserva
            WHERE livro_id = v_livro_id AND ativa = TRUE
        ) INTO v_tem_reserva;

        IF v_tem_reserva THEN
            SET p_status = 'ERRO: Livro possui reserva ativa, não pode ser renovado';

        ELSE
            -- Renova o empréstimo
            UPDATE emprestimo
            SET data_devolucao_prevista = DATE_ADD(data_devolucao_prevista, INTERVAL 14 DAY),
                numero_renovacoes = numero_renovacoes + 1
            WHERE id = p_emprestimo_id;

            SET p_status = 'SUCESSO: Empréstimo renovado por mais 14 dias';
        END IF;
    END IF;
END //

-- ----------------------------------------------------------------------------
-- Procedure: sp_criar_reserva
-- Descrição: Cria uma reserva de livro para um membro
-- Parâmetros: livro_id, membro_id
-- ----------------------------------------------------------------------------
CREATE PROCEDURE sp_criar_reserva(
    IN p_livro_id INT,
    IN p_membro_id INT,
    OUT p_status VARCHAR(100),
    OUT p_reserva_id INT
)
BEGIN
    DECLARE v_qtd_disponivel INT;
    DECLARE v_reserva_existente BOOLEAN;

    -- Verifica disponibilidade do livro
    SELECT quantidade_disponivel INTO v_qtd_disponivel
    FROM livro WHERE id = p_livro_id;

    IF v_qtd_disponivel IS NULL THEN
        SET p_status = 'ERRO: Livro não encontrado';
        SET p_reserva_id = NULL;

    ELSEIF v_qtd_disponivel > 0 THEN
        SET p_status = 'ERRO: Livro disponível, empreste diretamente';
        SET p_reserva_id = NULL;

    ELSE
        -- Verifica se membro já tem reserva ativa para este livro
        SELECT EXISTS(
            SELECT 1 FROM reserva
            WHERE livro_id = p_livro_id
              AND membro_id = p_membro_id
              AND ativa = TRUE
        ) INTO v_reserva_existente;

        IF v_reserva_existente THEN
            SET p_status = 'ERRO: Membro já possui reserva ativa para este livro';
            SET p_reserva_id = NULL;

        ELSE
            -- Cria a reserva
            INSERT INTO reserva (livro_id, membro_id, data_reserva, data_expiracao, ativa)
            VALUES (p_livro_id, p_membro_id, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), TRUE);

            SET p_reserva_id = LAST_INSERT_ID();
            SET p_status = 'SUCESSO: Reserva criada';
        END IF;
    END IF;
END //

DELIMITER ;

-- ============================================================================
-- TRIGGERS (GATILHOS)
-- ============================================================================

DELIMITER //

-- ----------------------------------------------------------------------------
-- Trigger: trg_atualizar_estado_emprestimo
-- Descrição: Atualiza automaticamente o estado de empréstimos atrasados
-- ----------------------------------------------------------------------------
CREATE TRIGGER trg_atualizar_estado_emprestimo
BEFORE UPDATE ON emprestimo
FOR EACH ROW
BEGIN
    IF NEW.estado = 'ATIVO' AND NEW.data_devolucao_prevista < CURRENT_DATE THEN
        SET NEW.estado = 'ATRASADO';
    END IF;
END //

-- ----------------------------------------------------------------------------
-- Trigger: trg_verificar_reserva_expirada
-- Descrição: Marca reservas como inativas se expiraram
-- ----------------------------------------------------------------------------
CREATE TRIGGER trg_verificar_reserva_expirada
BEFORE UPDATE ON reserva
FOR EACH ROW
BEGIN
    IF NEW.ativa = TRUE AND NEW.data_expiracao < CURRENT_DATE AND NEW.confirmada = FALSE THEN
        SET NEW.ativa = FALSE;
    END IF;
END //

-- ----------------------------------------------------------------------------
-- Trigger: trg_log_emprestimo
-- Descrição: Registra log ao criar empréstimo
-- ----------------------------------------------------------------------------
CREATE TRIGGER trg_log_emprestimo
AFTER INSERT ON emprestimo
FOR EACH ROW
BEGIN
    INSERT INTO historico_acao (usuario_id, acao, tabela_afetada, registro_id, descricao)
    VALUES (NEW.membro_id, 'CRIAR_EMPRESTIMO', 'emprestimo', NEW.id,
            CONCAT('Empréstimo do livro ID ', NEW.livro_id, ' para membro ID ', NEW.membro_id));
END //

-- ----------------------------------------------------------------------------
-- Trigger: trg_notificar_devolucao_proxima
-- Descrição: Cria notificação quando faltam 2 dias para devolução
-- ----------------------------------------------------------------------------
CREATE TRIGGER trg_notificar_devolucao_proxima
AFTER INSERT ON emprestimo
FOR EACH ROW
BEGIN
    DECLARE v_livro_titulo VARCHAR(255);

    SELECT titulo INTO v_livro_titulo FROM livro WHERE id = NEW.livro_id;

    -- Poderia ser implementado um evento agendado para verificar diariamente
    -- Por enquanto, apenas demonstração da estrutura
END //

DELIMITER ;

-- ============================================================================
-- EVENTS (EVENTOS AGENDADOS)
-- ============================================================================

-- Ativa o agendador de eventos
SET GLOBAL event_scheduler = ON;

DELIMITER //

-- ----------------------------------------------------------------------------
-- Event: evt_verificar_emprestimos_atrasados
-- Descrição: Verifica e atualiza empréstimos atrasados diariamente
-- ----------------------------------------------------------------------------
CREATE EVENT IF NOT EXISTS evt_verificar_emprestimos_atrasados
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_TIMESTAMP
DO
BEGIN
    UPDATE emprestimo
    SET estado = 'ATRASADO'
    WHERE estado = 'ATIVO'
      AND data_devolucao_prevista < CURRENT_DATE;
END //

-- ----------------------------------------------------------------------------
-- Event: evt_expirar_reservas
-- Descrição: Marca reservas expiradas como inativas diariamente
-- ----------------------------------------------------------------------------
CREATE EVENT IF NOT EXISTS evt_expirar_reservas
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_TIMESTAMP
DO
BEGIN
    UPDATE reserva
    SET ativa = FALSE
    WHERE ativa = TRUE
      AND confirmada = FALSE
      AND data_expiracao < CURRENT_DATE;
END //

DELIMITER ;

-- ============================================================================
-- DADOS INICIAIS (SEED DATA)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Inserir categorias padrão
-- ----------------------------------------------------------------------------
INSERT INTO categoria (nome, descricao) VALUES
('Ficção', 'Obras de ficção literária'),
('Romance', 'Romances e literatura romântica'),
('Ciência', 'Livros científicos e acadêmicos'),
('Tecnologia', 'Livros sobre tecnologia e computação'),
('História', 'Obras sobre história e eventos históricos'),
('Biografia', 'Biografias e autobiografias'),
('Educação', 'Livros educacionais e didáticos'),
('Filosofia', 'Obras filosóficas'),
('Psicologia', 'Livros sobre psicologia'),
('Negócios', 'Administração e negócios')
ON DUPLICATE KEY UPDATE nome = nome;

-- ----------------------------------------------------------------------------
-- Inserir administrador padrão
-- ----------------------------------------------------------------------------
-- Senha padrão: 'admin123' (deve ser alterada em produção)
-- Nota: Em produção, usar hash bcrypt ou similar
INSERT INTO pessoa (nome, email, senha, perfil, tipo_pessoa) VALUES
('Administrador', 'admin@biblioteca.isptec.ao', 'admin123', 'ADMIN', 'BIBLIOTECARIO')
ON DUPLICATE KEY UPDATE email = email;

INSERT INTO bibliotecario (id, funcionario_id, departamento, cargo)
SELECT id, 'BIB001', 'Biblioteca Central', 'Administrador'
FROM pessoa WHERE email = 'admin@biblioteca.isptec.ao'
ON DUPLICATE KEY UPDATE funcionario_id = funcionario_id;

-- ============================================================================
-- ÍNDICES ADICIONAIS PARA OTIMIZAÇÃO
-- ============================================================================

-- Índices compostos para consultas frequentes
CREATE INDEX idx_emprestimo_membro_estado ON emprestimo(membro_id, estado);
CREATE INDEX idx_emprestimo_livro_estado ON emprestimo(livro_id, estado);
CREATE INDEX idx_reserva_livro_ativa ON reserva(livro_id, ativa);
CREATE INDEX idx_reserva_membro_ativa ON reserva(membro_id, ativa);
CREATE INDEX idx_livro_estado_disponivel ON livro(estado, quantidade_disponivel);

-- ============================================================================
-- PERMISSÕES E SEGURANÇA
-- ============================================================================

-- Nota: Criar usuários específicos em produção
-- Exemplo:
-- CREATE USER 'biblioteca_app'@'localhost' IDENTIFIED BY 'senha_forte';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON biblioteca_isptec.* TO 'biblioteca_app'@'localhost';
-- FLUSH PRIVILEGES;

-- ============================================================================
-- VERIFICAÇÃO DA ESTRUTURA
-- ============================================================================

-- Listar todas as tabelas criadas
SELECT TABLE_NAME, TABLE_TYPE, ENGINE, TABLE_ROWS
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'biblioteca_isptec'
ORDER BY TABLE_NAME;

-- Verificar views criadas
SELECT TABLE_NAME
FROM information_schema.VIEWS
WHERE TABLE_SCHEMA = 'biblioteca_isptec';

-- ============================================================================
-- FIM DO SCRIPT
-- ============================================================================
-- Script criado para o Sistema de Gestão de Biblioteca ISPTEC
-- Para questões ou suporte, contate o administrador do sistema
-- ============================================================================

