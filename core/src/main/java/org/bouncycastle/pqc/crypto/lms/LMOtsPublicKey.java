package org.bouncycastle.pqc.crypto.lms;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.bouncycastle.util.Encodable;

public class LMOtsPublicKey
    implements Encodable
{
    private final LmOtsParameter parameter;
    private final byte[] I;
    private final int q;
    private final byte[] K;


    public LMOtsPublicKey(LmOtsParameter parameter, byte[] i, int q, byte[] k)
    {
        this.parameter = parameter;
        this.I = i;
        this.q = 0;
        this.K = k;
    }

    public static LMOtsPublicKey getInstance(Object src)
        throws Exception
    {
        if (src instanceof LMOtsPublicKey)
        {
            return (LMOtsPublicKey)src;
        }
        else if (src instanceof DataInputStream)
        {
            LmOtsParameter parameter = LmOtsParameters.getOtsParameter(((DataInputStream)src).readInt());
            byte[] I = new byte[16];
            ((DataInputStream)src).readFully(I);
            int q = ((DataInputStream)src).readInt();

            byte[] K = new byte[parameter.getN()];
            ((DataInputStream)src).readFully(K);

            return new LMOtsPublicKey(parameter, I, q, K);

        }
        else if (src instanceof byte[])
        {
            return getInstance(new DataInputStream(new ByteArrayInputStream((byte[])src)));
        }
        else if (src instanceof InputStream)
        {
            return getInstance(new DataInputStream((InputStream)src));
        }

        throw new IllegalArgumentException("cannot parse " + src);
    }

    public LmOtsParameter getParameter()
    {
        return parameter;
    }

    public byte[] getI()
    {
        return I;
    }

    public int getQ()
    {
        return q;
    }

    public byte[] getK()
    {
        return K;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        LMOtsPublicKey that = (LMOtsPublicKey)o;

        if (q != that.q)
        {
            return false;
        }
        if (parameter != null ? !parameter.equals(that.parameter) : that.parameter != null)
        {
            return false;
        }
        if (!Arrays.equals(I, that.I))
        {
            return false;
        }
        return Arrays.equals(K, that.K);
    }

    @Override
    public int hashCode()
    {
        int result = parameter != null ? parameter.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(I);
        result = 31 * result + q;
        result = 31 * result + Arrays.hashCode(K);
        return result;
    }


    @Override
    public byte[] getEncoded()
        throws IOException
    {
        return Composer.compose()
            .u32str(parameter.getType())
            .bytes(I)
            .u32str(q)
            .bytes(K).build();
    }
}
