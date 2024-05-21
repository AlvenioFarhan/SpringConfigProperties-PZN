package pzn.spring.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import pzn.spring.config.converter.StringtoDateConverter;
import pzn.spring.config.properties.ApplicationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		ApplicationProperties.class
})
public class SpringConfigPropertiesPznApplication {

	@Bean
	public ConversionService conversionService(StringtoDateConverter stringtoDateConverter){
		ApplicationConversionService conversionService = new ApplicationConversionService();
		conversionService.addConverter(stringtoDateConverter);
		return conversionService;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringConfigPropertiesPznApplication.class, args);
	}

}
