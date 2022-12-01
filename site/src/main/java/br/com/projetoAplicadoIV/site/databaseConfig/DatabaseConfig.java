package br.com.projetoAplicadoIV.site.databaseConfig;

import org.hibernate.dialect.PostgreSQL9Dialect;

public class DatabaseConfig extends PostgreSQL9Dialect {
    @Override
    public String getQuerySequencesString() {
        // Takes care of ERROR: relation “information_schema.sequences” does not exist
        return null;
    }
}