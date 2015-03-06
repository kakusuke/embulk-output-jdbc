package org.embulk.output.jdbc.setter;

import org.embulk.output.jdbc.BatchInsert;
import org.embulk.output.jdbc.JdbcColumn;
import org.embulk.spi.PageReader;
import org.embulk.spi.time.Timestamp;

import java.io.IOException;
import java.sql.SQLException;

public class FloatColumnSetter
        extends ColumnSetter
{
    public FloatColumnSetter(BatchInsert batch, PageReader pageReader,
                             JdbcColumn column)
    {
        super(batch, pageReader, column);
    }

    protected void booleanValue(boolean v) throws IOException, SQLException
    {
        batch.setFloat(v ? 1.0f : 0.0f);
    }

    protected void longValue(long v) throws IOException, SQLException
    {
        batch.setFloat(Long.valueOf(v).floatValue());
    }

    protected void doubleValue(double v) throws IOException, SQLException
    {
        batch.setFloat(Double.valueOf(v).floatValue());
    }

    protected void stringValue(String v) throws IOException, SQLException
    {
        float dv;
        try {
            dv = Float.parseFloat(v);
        } catch (NumberFormatException e) {
            nullValue();
            return;
        }
        batch.setFloat(dv);
    }

    protected void timestampValue(Timestamp v) throws IOException, SQLException
    {
        nullValue();
    }
}
