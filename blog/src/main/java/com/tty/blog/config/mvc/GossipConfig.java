package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.gossip.Gossip;
import com.tty.blog.interceptor.gossip.GossipComment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class GossipConfig implements WebMvcConfigurer {

    private static final String GOSSIPPATH = "/api/gossip";
    private static final String GOSSIPCOMMENTPATH = GOSSIPPATH + "/comment";
    @Bean
    public Gossip gossip() {
        return new Gossip();
    }
    @Bean
    public GossipComment gossipComment() {
        return new GossipComment();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.gossip())
                .addPathPatterns(
                        GOSSIPPATH + "/add",
                        GOSSIPPATH + "/like",
                        GOSSIPPATH + "/delete"
                )
                .excludePathPatterns(GOSSIPPATH + "/list");
        registry.addInterceptor(this.gossipComment())
                .addPathPatterns(
                        GOSSIPCOMMENTPATH + "/add",
                        GOSSIPCOMMENTPATH + "/delete",
                        GOSSIPCOMMENTPATH + "/like"
                );
    }
}
