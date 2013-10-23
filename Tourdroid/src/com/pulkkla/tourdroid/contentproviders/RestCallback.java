package com.pulkkla.tourdroid.contentproviders;

import java.util.List;

import com.pulkkla.tourdroid.Spot;

public interface RestCallback {
    public void preExecute();
    public void postExecute(List<Spot> response);
    public String inExecute();
    public void cancelExecute();
}
