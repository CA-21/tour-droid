package com.pulkkla.tourdroid.contentproviders;

public interface RestCallback {
    public void preExecute();
    public void postExecute(String response);
    public String inExecute();
    public void cancelExecute();
}
