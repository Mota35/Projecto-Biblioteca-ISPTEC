package isptec.biblioteca.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gerenciador de conexões com o banco de dados usando HikariCP.
 * Implementa o padrão Singleton para garantir uma única instância do pool.
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private final HikariDataSource dataSource;
    private final Properties properties;

    /**
     * Construtor privado - Singleton.
     * Inicializa o pool de conexões.
     */
    private DatabaseManager() {
        this.properties = loadProperties();
        this.dataSource = setupDataSource();
    }

    /**
     * Retorna a instância única do DatabaseManager.
     *
     * @return instância do DatabaseManager
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Carrega as propriedades do arquivo database.properties.
     *
     * @return Properties com as configurações
     */
    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input == null) {
                System.err.println("Arquivo database.properties não encontrado!");
                // Configurações padrão
                props.setProperty("db.url", "jdbc:mysql://localhost:3306/biblioteca_isptec");
                props.setProperty("db.username", "root");
                props.setProperty("db.password", "");
                props.setProperty("db.pool.maximumPoolSize", "10");
                props.setProperty("db.pool.minimumIdle", "3");
                props.setProperty("db.pool.connectionTimeout", "30000");
                return props;
            }
            props.load(input);
        } catch (IOException e) {
            System.err.println("Erro ao carregar database.properties: " + e.getMessage());
        }
        return props;
    }

    /**
     * Configura e inicializa o HikariCP DataSource.
     *
     * @return HikariDataSource configurado
     */
    private HikariDataSource setupDataSource() {
        HikariConfig config = new HikariConfig();

        // Configurações de conexão
        config.setJdbcUrl(properties.getProperty("db.url"));
        config.setUsername(properties.getProperty("db.username"));
        config.setPassword(properties.getProperty("db.password"));

        // Configurações do pool
        config.setMaximumPoolSize(
            Integer.parseInt(properties.getProperty("db.pool.maximumPoolSize", "10"))
        );
        config.setMinimumIdle(
            Integer.parseInt(properties.getProperty("db.pool.minimumIdle", "3"))
        );
        config.setConnectionTimeout(
            Long.parseLong(properties.getProperty("db.pool.connectionTimeout", "30000"))
        );
        config.setIdleTimeout(
            Long.parseLong(properties.getProperty("db.pool.idleTimeout", "600000"))
        );
        config.setMaxLifetime(
            Long.parseLong(properties.getProperty("db.pool.maxLifetime", "1800000"))
        );

        // Configurações adicionais
        config.setPoolName("BibliotecaPool");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        return new HikariDataSource(config);
    }

    /**
     * Obtém uma conexão do pool.
     *
     * @return Connection do banco de dados
     * @throws SQLException se não conseguir obter conexão
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Testa a conexão com o banco de dados.
     *
     * @return true se conectou com sucesso, false caso contrário
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Erro ao testar conexão: " + e.getMessage());
            return false;
        }
    }

    /**
     * Fecha o pool de conexões.
     * Deve ser chamado ao encerrar a aplicação.
     */
    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Pool de conexões fechado.");
        }
    }

    /**
     * Retorna informações sobre o estado do pool.
     *
     * @return String com estatísticas do pool
     */
    public String getPoolStats() {
        if (dataSource != null) {
            return String.format(
                "Pool Stats - Total: %d, Ativas: %d, Idle: %d, Aguardando: %d",
                dataSource.getHikariPoolMXBean().getTotalConnections(),
                dataSource.getHikariPoolMXBean().getActiveConnections(),
                dataSource.getHikariPoolMXBean().getIdleConnections(),
                dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection()
            );
        }
        return "Pool não inicializado";
    }

    /**
     * Retorna a URL do banco de dados.
     *
     * @return URL de conexão
     */
    public String getDatabaseUrl() {
        return properties.getProperty("db.url");
    }
}

