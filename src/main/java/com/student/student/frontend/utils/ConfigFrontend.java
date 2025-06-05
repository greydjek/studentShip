package com.student.student.frontend.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "front")
public class ConfigFrontend {
    private Duration duration;
    private Grid grid;
    private Header header;
    private Size size;
    private Quantity quantity;
    private Comment comment;

    @Getter
    @Setter
    public static class Duration {
        private int intShow;
    }

    @Getter
    @Setter
    public static class Grid {
        private String height;
        private String heightSize;
    }

    @Getter
    @Setter
    public static class Header {
        private String width;
    }

    @Getter
    @Setter
    public static class Size {
        private String phone;
    }

    @Getter
    @Setter
    public static class Quantity {
        private int column;
    }

    @Getter
    @Setter
    public static class Comment {
        private String heightSize;
        private String weightSize;
    }
}

