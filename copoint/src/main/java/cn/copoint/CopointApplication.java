package cn.copoint;

import net.gjerull.etherpad.client.EPLiteClient;
import org.json.simple.JSONArray;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;

@SpringBootApplication
@EnableScheduling
public class CopointApplication  extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CopointApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(CopointApplication.class, args);
	}

}
