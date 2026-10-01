package cm.bognestanley.shop_backend.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cm.bognestanley.shop_backend.application.user.usecase.SeedAdminUserUsecase;

@Component
public class AdminUserInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminUserInitializer.class);

    private final AdminSeedProperties adminSeedProperties;
    private final SeedAdminUserUsecase seedAdminUserUsecase;

    public AdminUserInitializer(AdminSeedProperties adminSeedProperties, SeedAdminUserUsecase seedAdminUserUsecase) {
        this.adminSeedProperties = adminSeedProperties;
        this.seedAdminUserUsecase = seedAdminUserUsecase;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!adminSeedProperties.isEnabled()) {
            return;
        }

        if (!StringUtils.hasText(adminSeedProperties.getEmail())
                || !StringUtils.hasText(adminSeedProperties.getPassword())
                || !StringUtils.hasText(adminSeedProperties.getFirstName())
                || !StringUtils.hasText(adminSeedProperties.getLastName())) {
            throw new IllegalStateException("Admin seed is enabled but its configuration is incomplete");
        }

        boolean created = seedAdminUserUsecase.execute(
                adminSeedProperties.getEmail(),
                adminSeedProperties.getFirstName(),
                adminSeedProperties.getLastName(),
                adminSeedProperties.getPassword());

        if (created) {
            log.info("Default admin user created: {}", adminSeedProperties.getEmail());
        }
    }
}
