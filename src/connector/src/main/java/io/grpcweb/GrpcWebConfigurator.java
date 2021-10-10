package io.grpcweb;

public class GrpcWebConfigurator {
    public static JettyWebserverForGrpcwebTraffic configure(GrpcWebConfiguration config) {
        GrpcWebGuiceModule.init(config);
        return GrpcWebGuiceModule.getInjector().getInstance(JettyWebserverForGrpcwebTraffic.class);
    }
}
