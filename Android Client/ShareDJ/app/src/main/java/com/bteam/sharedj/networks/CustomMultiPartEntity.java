package com.bteam.sharedj.networks;

/**
 * Created by nhungpro on 2/25/2017.
 */



import com.bteam.sharedj.listeners.iProgressListener;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;


@SuppressWarnings("deprecation")
public class CustomMultiPartEntity extends MultipartEntity {

    private iProgressListener mListerner;

    public CustomMultiPartEntity(iProgressListener listener) {
        super();
        this.mListerner = listener;
    }

    public CustomMultiPartEntity(final HttpMultipartMode mode, iProgressListener listener) {
        super(mode);
        this.mListerner = listener;
    }

    public CustomMultiPartEntity(HttpMultipartMode mode, String boundary, Charset charset, iProgressListener listener) {
        super(mode, boundary, charset);
        this.mListerner = listener;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream, this.mListerner));
    }

    public static class CountingOutputStream extends FilterOutputStream {

        private iProgressListener listener;
        private long transferred;

        public CountingOutputStream(final OutputStream out, iProgressListener listener) {
            super(out);
            this.listener = listener;
            this.transferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            this.transferred += len;
            this.listener.transferred(this.transferred);
        }

        public void write(int b) throws IOException {
            out.write(b);
            this.transferred++;
            this.listener.transferred(this.transferred);
        }
    }

}