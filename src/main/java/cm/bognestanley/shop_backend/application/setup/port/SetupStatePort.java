package cm.bognestanley.shop_backend.application.setup.port;

/**
 * Owns the singleton setup state. The lock prevents two first-admin requests
 * from both succeeding when they arrive concurrently.
 */
public interface SetupStatePort {

    boolean isSetupCompleted();

    boolean lockAndIsSetupCompleted();

    void markSetupCompleted();
}
