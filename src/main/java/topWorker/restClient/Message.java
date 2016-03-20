package topWorker.restClient;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by echomil on 19.03.16.
 */
public class Message implements Serializable{
    private Date start;
    private Date end;

    public Message(){

    }

    public Message(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
