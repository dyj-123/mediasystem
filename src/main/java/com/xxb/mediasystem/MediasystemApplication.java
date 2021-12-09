package com.xxb.mediasystem;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@SpringBootApplication
@Import({FdfsClientConfig.class})
public class MediasystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediasystemApplication.class, args);
    }

}
