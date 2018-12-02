package org.codet.caseidilla.user;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackageClasses = UserConfig.class)
@EnableJpaRepositories(basePackageClasses = UserConfig.class)
public class UserConfig {
}
