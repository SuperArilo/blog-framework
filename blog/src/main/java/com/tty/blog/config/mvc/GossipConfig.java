package com.tty.blog.config.mvc;

import com.tty.blog.interceptor.gossip.Gossip;
import com.tty.blog.interceptor.gossip.GossipComment;
import com.tty.system.config.BasePathProperties;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class GossipConfig implements WebMvcConfigurer {

    @Resource
    private BasePathProperties basePathProperties;

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
                        this.basePathProperties.basePath + "/gossip/add",
                        this.basePathProperties.basePath + "/gossip/like",
                        this.basePathProperties.basePath + "/gossip/delete"
                )
                .excludePathPatterns(this.basePathProperties.basePath + "/gossip/list");
        registry.addInterceptor(this.gossipComment())
                .addPathPatterns(
                        this.basePathProperties.basePath + "/gossip/comment/add",
                        this.basePathProperties.basePath + "/gossip/comment/delete",
                        this.basePathProperties.basePath + "/gossip/comment/like"
                );
    }
}
