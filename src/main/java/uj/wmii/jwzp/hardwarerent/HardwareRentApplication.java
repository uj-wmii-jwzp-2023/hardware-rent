package uj.wmii.jwzp.hardwarerent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import uj.wmii.jwzp.hardwarerent.config.RsaKeyProperties;

@EnableConfigurationProperties({RsaKeyProperties.class})
@SpringBootApplication
@EnableWebMvc
public class HardwareRentApplication {

	public static void main(String[] args) {
		SpringApplication.run(HardwareRentApplication.class, args);
	}

}
