package pzn.spring.config.configurationproperties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import pzn.spring.config.converter.StringtoDateConverter;
import pzn.spring.config.properties.ApplicationProperties;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;

@SpringBootTest(classes = ConfigurationPropertiesTest.TestApplication.class)
public class ConfigurationPropertiesTest {

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private ConversionService conversionService;

    @Test
    void testConversionService() {
        Assertions.assertTrue(conversionService.canConvert(String.class, Duration.class));
        Assertions.assertTrue(conversionService.canConvert(String.class, Date.class));

        Duration result = conversionService.convert("10s", Duration.class);
        Assertions.assertEquals(Duration.ofSeconds(10), result);
    }

    @Test
    void testConfigurationProperties() {
        Assertions.assertEquals("SpringConfigProperties-PZN", properties.getName());
        Assertions.assertEquals(1, properties.getVersion());
        Assertions.assertEquals(false, properties.getProductionMode());
    }

    @Test
    void testDatabaseProperties() {
        Assertions.assertEquals("alvenio", properties.getDatabase().getUsername());
        Assertions.assertEquals("rahasia", properties.getDatabase().getPassword());
        Assertions.assertEquals("learn", properties.getDatabase().getDatabase());
        Assertions.assertEquals("jdbc:test", properties.getDatabase().getUrl());
    }

    @Test
    void testCollection() {
        Assertions.assertEquals(Arrays.asList("products", "customers", "categories"), properties.getDatabase().getWhitelistTables());
        Assertions.assertEquals(100, properties.getDatabase().getMaxTablesSize().get("products"));
        Assertions.assertEquals(100, properties.getDatabase().getMaxTablesSize().get("customers"));
        Assertions.assertEquals(100, properties.getDatabase().getMaxTablesSize().get("categories"));
    }

    @Test
    void testEmbededCollection() {

        Assertions.assertEquals("default", properties.getDefaultRole().get(0).getId());
        Assertions.assertEquals("Default Role", properties.getDefaultRole().get(0).getName());
        Assertions.assertEquals("guest", properties.getDefaultRole().get(1).getId());
        Assertions.assertEquals("Guest Role", properties.getDefaultRole().get(1).getName());

        Assertions.assertEquals("admin", properties.getRoles().get("admin").getId());
        Assertions.assertEquals("Admin Role", properties.getRoles().get("admin").getName());
        Assertions.assertEquals("finance", properties.getRoles().get("finance").getId());
        Assertions.assertEquals("Finance Role", properties.getRoles().get("finance").getName());
    }

    @Test
    void testDuration() {
        Assertions.assertEquals(Duration.ofSeconds(10), properties.getDefaultTimeOut());
    }

    @Test
    void testCustomConverter() {
        Date expireDate = properties.getExpireDate();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Assertions.assertEquals("2024-05-21", dateFormat.format(expireDate));
    }

    @SpringBootApplication
    @EnableConfigurationProperties({
            ApplicationProperties.class
    })
    @Import(StringtoDateConverter.class)
    public static class TestApplication {

        @Bean
        public ConversionService conversionService(StringtoDateConverter stringtoDateConverter) {
            ApplicationConversionService conversionService = new ApplicationConversionService();
            conversionService.addConverter(stringtoDateConverter);
            return conversionService;
        }

    }
}
