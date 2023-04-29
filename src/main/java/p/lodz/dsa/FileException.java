package p.lodz.dsa;

import java.io.IOException;

class FileException extends IOException {
    public FileException(String msg){
        super(msg);
    }

    public FileException(String msg, Throwable e){
        super(msg, e);
    }

    public FileException(Throwable e){
        super(e);
    }
}