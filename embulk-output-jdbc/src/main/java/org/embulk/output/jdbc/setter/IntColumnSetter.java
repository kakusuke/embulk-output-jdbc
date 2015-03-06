package org.embulk.output.jdbc.setter;

import com.google.common.math.DoubleMath;
import org.embulk.output.jdbc.BatchInsert;
import org.embulk.output.jdbc.JdbcColumn;
import org.embulk.spi.PageReader;
import org.embulk.spi.time.Timestamp;

import java.io.IOException;
import java.math.RoundingMode;
import java.sql.SQLException;

public class IntColumnSetter
        extends ColumnSetter
{
    public IntColumnSetter(BatchInsert batch, PageReader pageReader,
                           JdbcColumn column)
    {
        super(batch, pageReader, column);
    }

    protected void booleanValue(boolean v) throws IOException, SQLException
    {
        batch.setInt(v ? 1 : 0);
    }

    protected void longValue(long v) throws IOException, SQLException
    {
        batch.setInt(Long.valueOf(v).intValue());
    }

    protected void doubleValue(double v) throws IOException, SQLException
    {
        int lv;
        try {
            // TODO configurable rounding mode
            lv = DoubleMath.roundToInt(v, RoundingMode.HALF_UP);
        } catch (ArithmeticException ex) {
            // NaN / Infinite / -Infinite
            nullValue();
            return;
        }
        batch.setInt(lv);
    }

    protected void stringValue(String v) throws IOException, SQLException
    {
        int lv;
        try {
            lv = Integer.parseInt(v);
        } catch (NumberFormatException e) {
            nullValue();
            return;
        }
        batch.setInt(lv);
    }

    protected void timestampValue(Timestamp v) throws IOException, SQLException
    {
        nullValue();
    }
}
