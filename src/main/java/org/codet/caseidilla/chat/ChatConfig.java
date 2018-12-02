package org.codet.caseidilla.chat;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackageClasses = ChatConfig.class)
@EnableJpaRepositories(basePackageClasses = ChatConfig.class)
public class ChatConfig {
}
