package xpfei.mylibrary.utils.imageutil;

import java.util.List;

public interface OnCompressListener {


    /**
     * Fired when a compression returns successfully, override to handle in your own code
     */
    void onSuccess(List<String> file);

    /**
     * Fired when a compression fails to complete, override to handle in your own code
     */
    void onError(Throwable e);
}
