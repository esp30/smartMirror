package com.esp30.smartMirror.data;

public class Photo {

    public String b64Encoded;

    public byte[] byteArrayImage;

    public Photo(){}

    public Photo(String b64Encoded) {
        this.b64Encoded = b64Encoded;
    }

    public String getB64Encoded() {
        return b64Encoded;
    }

    public void setB64Encoded(String b64Encoded) {
        this.b64Encoded = b64Encoded;
    }

    public byte[] getByteArrayImage() {
        return byteArrayImage;
    }

    public void setByteArrayImage(byte[] byteArrayImage) {
        this.byteArrayImage = byteArrayImage;
    }
}
