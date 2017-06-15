package com.asking.pad.app.ui.downbook.download.DownLoadListener;

import com.asking.pad.app.entity.BookInfo;
import com.asking.pad.app.ui.downbook.db.DbHelper;
import com.asking.pad.app.ui.downbook.download.DownState;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 自定义进度的body
 * @author wzg
 */
public class DownloadResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private BookInfo mBookInfo;
    private BufferedSource bufferedSource;

    public DownloadResponseBody(ResponseBody responseBody, BookInfo mBookInfo) {
        this.responseBody = responseBody;
        this.mBookInfo = mBookInfo;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (null != mBookInfo) {

                    long read = totalBytesRead ;
                    long count = responseBody.contentLength();
                    boolean done = bytesRead == -1;

                    if(mBookInfo.getCountLength()>count){
                        read=mBookInfo.getCountLength()-count+read;
                    }else{
                        mBookInfo.setCountLength(count);
                    }
                    mBookInfo.setReadLength(read);
                }
                mBookInfo.setDownState(DownState.DOWN);
                DbHelper.getInstance().updateBookInfo(mBookInfo);
                EventBus.getDefault().post(mBookInfo);
                return bytesRead;
            }
        };

    }
}

