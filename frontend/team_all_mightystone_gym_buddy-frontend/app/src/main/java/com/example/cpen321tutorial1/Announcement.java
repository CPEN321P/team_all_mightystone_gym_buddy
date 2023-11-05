package com.example.cpen321tutorial1;

public class Announcement {

    private String _id,
            header,
            body;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toString() {
        return String.format("_id:%d,header:%d,body:%d", _id, header, body);
    }
}
