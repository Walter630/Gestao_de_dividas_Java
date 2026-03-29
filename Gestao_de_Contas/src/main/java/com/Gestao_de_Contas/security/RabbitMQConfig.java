package com.Gestao_de_Contas.security;

import ch.qos.logback.classic.pattern.MessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE    = "debt.notification.queue";
    public static final String EXCHANGE = "debt.notification.exchange";
    public static final String ROUTING  = "debt.notification";

    @Bean
    public Queue queue() { return new Queue(QUEUE, true); }

    @Bean
    public DirectExchange exchange() { return new DirectExchange(EXCHANGE); }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING);
    }

    @Bean
    public MessageConverter converteJackson() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converteJackson());
        return rabbitTemplate;
    }
}