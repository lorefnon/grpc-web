package io.grpcweb;

import java.util.ArrayList;

public class GrpcWebConfiguration {
    private int grpcPortNum = 9090;
    private int grpcWebPortNum = 9191;
    private int grpcProxyTimeoutMS = 500;
    private ArrayList<String> includedHttpHeaders = new ArrayList<>();
    private ArrayList<String> excludedHttpHeaders = new ArrayList<>();

    public int getGrpcPortNum() {
        return grpcPortNum;
    }

    public void setGrpcPortNum(int grpcPortNum) {
        this.grpcPortNum = grpcPortNum;
    }

    public int getGrpcWebPortNum() {
        return grpcWebPortNum;
    }

    public void setGrpcWebPortNum(int grpcWebPortNum) {
        this.grpcWebPortNum = grpcWebPortNum;
    }

    public int getGrpcProxyTimeoutMS() {
        return grpcProxyTimeoutMS;
    }

    public void setGrpcProxyTimeoutMS(int grpcProxyTimeoutMS) {
        this.grpcProxyTimeoutMS = grpcProxyTimeoutMS;
    }

    public void addIncludedHttpHeader(String header) {
        includedHttpHeaders.add(header.toLowerCase());
    }

    public void addExcludedHttpHeader(String header) {
        excludedHttpHeaders.add(header.toLowerCase());
    }

    public ArrayList<String> getIncludedHttpHeaders() {
        return includedHttpHeaders;
    }

    public ArrayList<String> getExcludedHttpHeaders() {
        return excludedHttpHeaders;
    }
}
