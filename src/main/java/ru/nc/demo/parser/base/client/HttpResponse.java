package ru.nc.demo.parser.base.client;

import org.apache.http.StatusLine;

public class HttpResponse {
    private StatusLine statusLine;
    private byte[] entityData;

    public HttpResponse(StatusLine statusLine, byte[] entityData) {
        this.entityData = entityData;
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }
    public byte[] getEntityData() {
        return entityData;
    }
}
