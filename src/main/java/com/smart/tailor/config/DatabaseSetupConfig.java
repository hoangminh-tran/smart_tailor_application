package com.smart.tailor.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DatabaseSetupConfig {

    @Bean
    public CommandLineRunner runScript(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {

                if (!isSchemaAlreadyInitialized(connection)) {
//                    statement.execute("USE railway");
                    statement.execute("USE smart_tailor_be");
                    // Drop the function if it exists
                    statement.execute("DROP FUNCTION IF EXISTS GenerateCustomKeyString");

                    // Create the function
                    String functionSql = "CREATE FUNCTION GenerateCustomKeyString() " +
                            "RETURNS CHAR(14) " +
                            "DETERMINISTIC " +
                            "BEGIN " +
                            "    DECLARE chars CHAR(62) DEFAULT 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890'; " +
                            "    DECLARE customKeyString CHAR(14); " +
                            "    DECLARE i INT DEFAULT 1; " +
                            "    DECLARE randomChar CHAR(1); " +
                            "    DECLARE HH CHAR(2); " +
                            "    DECLARE mm CHAR(2); " +
                            "    DECLARE ss CHAR(2); " +
                            "    DECLARE microseconds CHAR(6); " +
                            "    DECLARE randIndex1 INT; " +
                            "    DECLARE randIndex2 INT; " +
                            "    DECLARE randChar1 CHAR(1); " +
                            "    DECLARE randChar2 CHAR(1); " +
                            "    SET customKeyString = ''; " +
                            "    SET HH = DATE_FORMAT(CURRENT_TIMESTAMP, '%H'); " +
                            "    SET mm = DATE_FORMAT(CURRENT_TIMESTAMP, '%i'); " +
                            "    SET ss = DATE_FORMAT(CURRENT_TIMESTAMP, '%s'); " +
                            "    WHILE i <= 6 DO " +
                            "        SET randomChar = SUBSTRING(chars, FLOOR(1 + (RAND() * 62)), 1); " +
                            "        SET customKeyString = CONCAT(customKeyString, randomChar); " +
                            "        SET i = i + 1; " +
                            "    END WHILE; " +
                            "    SET microseconds = RIGHT(DATE_FORMAT(CURRENT_TIMESTAMP, '%f'), 6); " +
                            "    SET randIndex1 = FLOOR(1 + (RAND() * 6)); " +
                            "    SET randIndex2 = FLOOR(1 + (RAND() * 6)); " +
                            "    WHILE randIndex2 = randIndex1 DO " +
                            "        SET randIndex2 = FLOOR(1 + (RAND() * 6)); " +
                            "    END WHILE; " +
                            "    SET randChar1 = SUBSTRING(microseconds, randIndex1, 1); " +
                            "    SET randChar2 = SUBSTRING(microseconds, randIndex2, 1); " +
                            "    SET customKeyString = CONCAT(SUBSTRING(customKeyString, 1, 2), " +
                            "                                HH, " +
                            "                                SUBSTRING(customKeyString, 3, 2), " +
                            "                                mm, " +
                            "                                SUBSTRING(customKeyString, 5, 2), " +
                            "                                ss, " +
                            "                                randChar1, " +
                            "                                randChar2); " +
                            "    RETURN customKeyString; " +
                            "END";

                    statement.execute(functionSql);

                    ScriptUtils.executeSqlScript(connection, new ClassPathResource("smartTailorScript.sql"));

                    System.out.println("Script executed successfully");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error executing script");
            }
        };
    }

    private boolean isSchemaAlreadyInitialized(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // Kiểm tra sự tồn tại của bảng schema_version
            ResultSet resultSet = statement.executeQuery(
                    "SELECT COUNT(*) " +
                            "FROM smart_tailor_be.roles");
//                    "SELECT COUNT(*) " +
//                            "FROM railway.roles");
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            } else {
                return false;
            }
        }
    }
}