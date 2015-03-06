package org.embulk.output.jdbc.setter;

import org.embulk.output.jdbc.BatchInsert;
import org.embulk.output.jdbc.JdbcColumn;
import org.embulk.spi.PageReader;
import org.embulk.spi.time.Timestamp;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class BigDecimalColumnSetter
        extends ColumnSetter
{
    public BigDecimalColumnSetter(BatchInsert batch, PageReader pageReader,
                                  JdbcColumn column)
    {
        super(batch, pageReader, column);
    }

    protected void booleanValue(boolean v) throws IOException, SQLException
    {
        batch.setBigDecimal(v ? BigDecimal.ONE : BigDecimal.ZERO);
    }

    protected void longValue(long v) throws IOException, SQLException
    {
        batch.setBigDecimal(BigDecimal.valueOf(v));
    }

    protected void doubleValue(double v) throws IOException, SQLException
    {
        batch.setBigDecimal(BigDecimal.valueOf(v));
    }

    protected void stringValue(String v) throws IOException, SQLException
    {
        BigDecimal lv;
        try {
            lv = new BigDecimal(v);
        } catch (NumberFormatException e) {
            nullValue();
            return;
        }
        batch.setBigDecimal(lv);
    }

    protected void timestampValue(Timestamp v) throws IOException, SQLException
    {
        nullValue();
    }
}
