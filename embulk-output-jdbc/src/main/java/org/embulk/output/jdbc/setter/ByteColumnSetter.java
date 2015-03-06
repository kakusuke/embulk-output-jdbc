package org.embulk.output.jdbc.setter;

import com.google.common.math.DoubleMath;
import org.embulk.output.jdbc.BatchInsert;
import org.embulk.output.jdbc.JdbcColumn;
import org.embulk.spi.PageReader;
import org.embulk.spi.time.Timestamp;

import java.io.IOException;
import java.math.RoundingMode;
import java.sql.SQLException;

public class ByteColumnSetter
        extends ColumnSetter
{
    public ByteColumnSetter(BatchInsert batch, PageReader pageReader,
                            JdbcColumn column)
    {
        super(batch, pageReader, column);
    }

    protected void booleanValue(boolean v) throws IOException, SQLException
    {
        batch.setByte((byte) (v ? 1 : 0));
    }

    protected void longValue(long v) throws IOException, SQLException
    {
        batch.setByte(Long.valueOf(v).byteValue());
    }

    protected void doubleValue(double v) throws IOException, SQLException
    {
        byte lv;
        try {
            // TODO configurable rounding mode
            lv = (byte) DoubleMath.roundToInt(v, RoundingMode.HALF_UP);
        } catch (ArithmeticException ex) {
            // NaN / Infinite / -Infinite
            nullValue();
            return;
        }
        batch.setByte(lv);
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
        batch.setByte((byte) lv);
    }

    protected void timestampValue(Timestamp v) throws IOException, SQLException
    {
        nullValue();
    }
}
