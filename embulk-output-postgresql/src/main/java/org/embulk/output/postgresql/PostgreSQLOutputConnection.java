package org.embulk.output.postgresql;

import org.embulk.output.jdbc.JdbcOutputConnection;
import org.embulk.output.jdbc.JdbcSchema;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgreSQLOutputConnection
        extends JdbcOutputConnection
{
    public PostgreSQLOutputConnection(Connection connection, String schemaName, boolean autoCommit)
            throws SQLException
    {
        super(connection, schemaName);
        connection.setAutoCommit(autoCommit);
    }

    public String buildCopySql(String toTable, JdbcSchema toTableSchema)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("COPY ");
        quoteIdentifierString(sb, toTable);
        sb.append(" (");
        for (int i=0; i < toTableSchema.getCount(); i++) {
            if(i != 0) { sb.append(", "); }
            quoteIdentifierString(sb, toTableSchema.getColumnName(i));
        }
        sb.append(") ");
        sb.append("FROM STDIN");

        return sb.toString();
    }

    public CopyManager newCopyManager() throws SQLException
    {
        return new CopyManager((BaseConnection) connection);
    }

    @Override
    protected String convertTypeName(String typeName)
    {
        switch(typeName) {
        case "CLOB":
            return "TEXT";
        case "BLOB":
            return "BYTEA";
        default:
            return typeName;
        }
    }

    @Override
    protected String buildTruncateSql(String table)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("TRUNCATE ");
        quoteIdentifierString(sb, table);

        return sb.toString();
    }
}
